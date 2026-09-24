import com.sun.source.doctree.DeprecatedTree;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.EndElementTree;
import com.sun.source.doctree.EntityTree;
import com.sun.source.doctree.LinkTree;
import com.sun.source.doctree.ParamTree;
import com.sun.source.doctree.ReferenceTree;
import com.sun.source.doctree.ReturnTree;
import com.sun.source.doctree.SeeTree;
import com.sun.source.doctree.SinceTree;
import com.sun.source.doctree.StartElementTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.ThrowsTree;
import com.sun.source.doctree.ValueTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Build-time only, never shipped: writes META-INF/vulcan/api-schema.json for the website's API reference.
public final class ApiSchemaGenerator {

    private static final String ROOT_PACKAGE = "net.vulcandev.vulcanapi";
    private static final String VULCAN_EVENT = ROOT_PACKAGE + ".event.VulcanEvent";
    private static final int FORMAT = 1;
    private static final Pattern IDENTIFIER =
            Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*(?:\\.[A-Za-z_$][A-Za-z0-9_$]*)*");

    private final Map<String, TypeDecl> declared = new HashMap<>();
    private final Set<String> published = new HashSet<>();
    private final Map<String, List<Object>> packageDocs = new TreeMap<>();
    private DocTrees docTrees;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("usage: ApiSchemaGenerator <sourceRoot> <outputFile> <readme>");
            System.exit(2);
        }
        new ApiSchemaGenerator().run(Paths.get(args[0]), Paths.get(args[1]), Paths.get(args[2]));
    }

    private void run(Path sourceRoot, Path output, Path readme) throws IOException {
        List<File> files;
        try (Stream<Path> walk = Files.walk(sourceRoot)) {
            files = walk.filter(p -> p.toString().endsWith(".java"))
                    .sorted()
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("A JDK is required to generate the API schema");
        }
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8);
        JavacTask task = (JavacTask) compiler.getTask(
                null, fileManager, diagnostics, Arrays.asList("-proc:none"), null,
                fileManager.getJavaFileObjectsFromFiles(files));
        List<CompilationUnitTree> units = new ArrayList<>();
        for (CompilationUnitTree unit : task.parse()) {
            units.add(unit);
        }
        boolean failed = false;
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                System.err.println(diagnostic);
                failed = true;
            }
        }
        if (failed) {
            throw new IllegalStateException("VulcanAPI sources did not parse");
        }
        docTrees = DocTrees.instance(task);

        for (CompilationUnitTree unit : units) {
            register(unit);
        }
        for (TypeDecl decl : declared.values()) {
            if (decl.isPublished()) {
                published.add(decl.fqn);
            }
        }

        Map<String, Object> schema = build(readme);
        Files.createDirectories(output.toAbsolutePath().getParent());
        StringBuilder json = new StringBuilder(1 << 18);
        Json.write(schema, json);
        Files.write(output, json.toString().getBytes(StandardCharsets.UTF_8));

        @SuppressWarnings("unchecked")
        Map<String, Object> stats = (Map<String, Object>) schema.get("stats");
        System.out.println(String.format(Locale.ROOT,
                "api-schema: %s types, %s members, %s%% documented -> %s",
                stats.get("types"), stats.get("members"), stats.get("documentedPercent"), output));
    }

    // ------------------------------------------------------------------ discovery

    private void register(CompilationUnitTree unit) {
        String pkg = unit.getPackageName() == null ? "" : unit.getPackageName().toString();
        if (!pkg.equals(ROOT_PACKAGE) && !pkg.startsWith(ROOT_PACKAGE + ".")) {
            return;
        }
        Scope scope = new Scope(pkg);
        for (ImportTree imp : unit.getImports()) {
            if (imp.isStatic()) {
                continue;
            }
            String name = imp.getQualifiedIdentifier().toString();
            if (name.endsWith(".*")) {
                scope.starImports.add(name.substring(0, name.length() - 2));
            } else {
                scope.singleImports.put(name.substring(name.lastIndexOf('.') + 1), name);
            }
        }
        TreePath unitPath = new TreePath(unit);
        if (unit.getSourceFile().getName().endsWith("package-info.java") && unit.getPackage() != null) {
            DocCommentTree doc = docTrees.getDocCommentTree(new TreePath(unitPath, unit.getPackage()));
            if (doc != null) {
                packageDocs.put(pkg, new DocConverter(scope, null).blocks(doc.getFullBody()));
            }
        }
        for (Tree tree : unit.getTypeDecls()) {
            if (tree instanceof ClassTree) {
                registerType((ClassTree) tree, new TreePath(unitPath, tree), scope, null);
            }
        }
    }

    private void registerType(ClassTree tree, TreePath path, Scope scope, TypeDecl enclosing) {
        String fqn = enclosing == null
                ? qualify(scope.pkg, tree.getSimpleName().toString())
                : enclosing.fqn + "." + tree.getSimpleName();
        TypeDecl decl = new TypeDecl(fqn, tree, path, scope, enclosing);
        declared.put(fqn, decl);
        if (enclosing != null) {
            enclosing.nested.put(tree.getSimpleName().toString(), fqn);
        }
        for (Tree member : tree.getMembers()) {
            if (member instanceof ClassTree) {
                registerType((ClassTree) member, new TreePath(path, member), scope, decl);
            }
        }
    }

    // ------------------------------------------------------------------ schema

    private Map<String, Object> build(Path readme) throws IOException {
        List<String> order = new ArrayList<>(published);
        Collections.sort(order);

        List<Object> types = new ArrayList<>();
        Map<String, Map<String, Object>> modules = new TreeMap<>();
        int memberCount = 0;
        int documented = 0;
        int typeDocumented = 0;
        for (String fqn : order) {
            TypeDecl decl = declared.get(fqn);
            TypeBuilder builder = new TypeBuilder(decl);
            Map<String, Object> type = builder.build();
            types.add(type);
            memberCount += builder.members;
            documented += builder.documentedMembers;
            if (type.containsKey("doc")) {
                typeDocumented++;
            }

            String moduleId = decl.module();
            Map<String, Object> module = modules.get(moduleId);
            if (module == null) {
                module = new LinkedHashMap<>();
                module.put("id", moduleId);
                module.put("title", moduleTitle(moduleId));
                module.put("packages", new TreeMap<String, List<String>>());
                modules.put(moduleId, module);
            }
            @SuppressWarnings("unchecked")
            Map<String, List<String>> packages = (Map<String, List<String>>) module.get("packages");
            List<String> packageTypes = packages.get(decl.scope.pkg);
            if (packageTypes == null) {
                packageTypes = new ArrayList<>();
                packages.put(decl.scope.pkg, packageTypes);
            }
            packageTypes.add(fqn);
        }

        List<Object> moduleList = new ArrayList<>();
        for (Map<String, Object> module : modules.values()) {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> packages = (Map<String, List<String>>) module.remove("packages");
            List<Object> packageList = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : packages.entrySet()) {
                Map<String, Object> pkg = new LinkedHashMap<>();
                pkg.put("name", entry.getKey());
                putIfPresent(pkg, "doc", packageDocs.get(entry.getKey()));
                pkg.put("types", entry.getValue());
                packageList.add(pkg);
            }
            module.put("packages", packageList);
            moduleList.add(module);
        }

        int total = order.size() + memberCount;
        int covered = typeDocumented + documented;
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("types", order.size());
        stats.put("members", memberCount);
        stats.put("documentedTypes", typeDocumented);
        stats.put("documentedMembers", documented);
        stats.put("documentedPercent", total == 0 ? 0 : (int) Math.round(covered * 100.0 / total));

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("format", FORMAT);
        schema.put("name", "VulcanAPI");
        schema.put("rootPackage", ROOT_PACKAGE);
        schema.put("overview", Files.exists(readme)
                ? new MarkdownConverter(simpleNameIndex()).convert(
                        new String(Files.readAllBytes(readme), StandardCharsets.UTF_8))
                : new ArrayList<Object>());
        schema.put("modules", moduleList);
        schema.put("types", types);
        schema.put("stats", stats);
        return schema;
    }

    private Map<String, String> simpleNameIndex() {
        Map<String, String> index = new HashMap<>();
        Set<String> ambiguous = new HashSet<>();
        for (String fqn : published) {
            String simple = fqn.substring(fqn.lastIndexOf('.') + 1);
            if (index.containsKey(simple)) {
                ambiguous.add(simple);
            }
            index.put(simple, fqn);
            index.put(fqn, fqn);
        }
        for (String simple : ambiguous) {
            index.remove(simple);
        }
        return index;
    }

    private String moduleTitle(String moduleId) {
        if (moduleId.isEmpty()) {
            return "Core";
        }
        String prefix = ROOT_PACKAGE + "." + moduleId + ".";
        List<String> sorted = new ArrayList<>(published);
        Collections.sort(sorted);
        for (String fqn : sorted) {
            if (fqn.startsWith(prefix) && fqn.indexOf('.', prefix.length()) < 0 && fqn.endsWith("API")
                    && fqn.length() > prefix.length() + 3) {
                return fqn.substring(prefix.length(), fqn.length() - 3);
            }
        }
        return Character.toUpperCase(moduleId.charAt(0)) + moduleId.substring(1);
    }

    private final class TypeBuilder {
        private final TypeDecl decl;
        private final DocConverter docs;
        private final Set<String> usedIds = new HashSet<>();
        int members;
        int documentedMembers;

        TypeBuilder(TypeDecl decl) {
            this.decl = decl;
            this.docs = new DocConverter(decl.scope, decl);
        }

        Map<String, Object> build() {
            ClassTree tree = decl.tree;
            Map<String, Object> type = new LinkedHashMap<>();
            type.put("name", tree.getSimpleName().toString());
            type.put("qualifiedName", decl.fqn);
            type.put("package", decl.scope.pkg);
            type.put("module", decl.module());
            type.put("kind", kind(tree));
            putIfPresent(type, "enclosing", decl.enclosing == null ? null : decl.enclosing.fqn);
            putIfPresent(type, "modifiers", modifiers(tree.getModifiers()));
            putIfPresent(type, "typeParameters", typeParameters(tree.getTypeParameters()));

            List<Object> supertypes = new ArrayList<>();
            if (tree.getExtendsClause() != null) {
                type.put("extends", Collections.singletonList(typeRef(tree.getExtendsClause().toString())));
            }
            for (Tree implemented : tree.getImplementsClause()) {
                supertypes.add(typeRef(implemented.toString()));
            }
            putIfPresent(type, tree.getKind() == Tree.Kind.INTERFACE ? "extends" : "implements", supertypes);

            DocCommentTree comment = docTrees.getDocCommentTree(decl.path);
            Map<String, Object> doc = docs.doc(comment);
            putIfPresent(type, "doc", doc);
            if (isDeprecated(tree.getModifiers(), comment)) {
                type.put("deprecated", true);
            }

            String system = eventSystem(decl.fqn, new HashSet<String>());
            if (system != null) {
                Map<String, Object> event = new LinkedHashMap<>();
                event.put("system", system);
                event.put("cancellable", isCancellable(decl.fqn, new HashSet<String>()));
                type.put("event", event);
            }

            List<Object> constants = new ArrayList<>();
            List<Object> constructors = new ArrayList<>();
            List<Object> methods = new ArrayList<>();
            List<Object> fields = new ArrayList<>();
            List<String> nested = new ArrayList<>();
            Map<String, Set<Integer>> explicitMethods = new HashMap<>();

            for (Tree member : tree.getMembers()) {
                if (member instanceof MethodTree) {
                    MethodTree method = (MethodTree) member;
                    String name = method.getName().toString().toLowerCase(Locale.ROOT);
                    Set<Integer> arities = explicitMethods.get(name);
                    if (arities == null) {
                        arities = new HashSet<>();
                        explicitMethods.put(name, arities);
                    }
                    arities.add(method.getParameters().size());
                }
            }

            for (Tree member : tree.getMembers()) {
                if (member instanceof ClassTree) {
                    String fqn = decl.fqn + "." + ((ClassTree) member).getSimpleName();
                    if (published.contains(fqn)) {
                        nested.add(fqn);
                    }
                } else if (member instanceof VariableTree) {
                    VariableTree field = (VariableTree) member;
                    if (tree.getKind() == Tree.Kind.ENUM && field.getInitializer() instanceof NewClassTree) {
                        constants.add(constant(field));
                    } else if (visible(field.getModifiers())) {
                        fields.add(field(field));
                    }
                } else if (member instanceof MethodTree) {
                    MethodTree method = (MethodTree) member;
                    boolean constructor = method.getName().contentEquals("<init>");
                    if (constructor && tree.getKind() == Tree.Kind.ENUM) {
                        continue;
                    }
                    if (visible(method.getModifiers())) {
                        (constructor ? constructors : methods).add(method(method, constructor));
                    }
                }
            }
            lombok(constructors, methods, explicitMethods);

            putIfPresent(type, "constants", constants);
            putIfPresent(type, "constructors", constructors);
            putIfPresent(type, "methods", methods);
            putIfPresent(type, "fields", fields);
            putIfPresent(type, "nested", nested);
            return type;
        }

        private boolean visible(ModifiersTree modifiers) {
            if (isInternal(modifiers)) {
                return false;
            }
            Set<Modifier> flags = modifiers.getFlags();
            if (decl.isInterfaceLike()) {
                return !flags.contains(Modifier.PRIVATE);
            }
            return flags.contains(Modifier.PUBLIC) || flags.contains(Modifier.PROTECTED);
        }

        private Map<String, Object> constant(VariableTree field) {
            Map<String, Object> constant = new LinkedHashMap<>();
            String name = field.getName().toString();
            constant.put("id", uniqueId(name));
            constant.put("name", name);
            DocCommentTree comment = comment(field);
            count(putIfPresent(constant, "doc", docs.doc(comment)));
            if (isDeprecated(field.getModifiers(), comment)) {
                constant.put("deprecated", true);
            }
            return constant;
        }

        private Map<String, Object> field(VariableTree field) {
            Map<String, Object> result = new LinkedHashMap<>();
            String name = field.getName().toString();
            String type = field.getType().toString();
            result.put("id", uniqueId(name));
            result.put("name", name);
            result.put("kind", "field");
            putIfPresent(result, "modifiers", modifiers(field.getModifiers()));
            result.put("type", typeRef(type));
            result.put("signature", type + " " + name);
            nullability(result, field.getModifiers());
            DocCommentTree comment = comment(field);
            count(putIfPresent(result, "doc", docs.doc(comment)));
            if (isDeprecated(field.getModifiers(), comment)) {
                result.put("deprecated", true);
            }
            return result;
        }

        private Map<String, Object> method(MethodTree method, boolean constructor) {
            Map<String, Object> result = new LinkedHashMap<>();
            String name = constructor ? decl.tree.getSimpleName().toString() : method.getName().toString();
            List<Object> params = new ArrayList<>();
            List<String> paramTypes = new ArrayList<>();
            List<String> paramText = new ArrayList<>();
            for (VariableTree param : method.getParameters()) {
                String type = param.getType().toString();
                boolean varargs = param.toString().contains("...");
                if (varargs && type.endsWith("[]")) {
                    type = type.substring(0, type.length() - 2) + "...";
                }
                Map<String, Object> p = new LinkedHashMap<>();
                p.put("name", param.getName().toString());
                p.put("type", typeRef(type));
                nullability(p, param.getModifiers());
                params.add(p);
                paramTypes.add(type);
                paramText.add(type + " " + param.getName());
            }

            result.put("id", uniqueId((constructor ? "new" : name) + "(" + idTypes(paramTypes) + ")"));
            result.put("name", name);
            String kind = constructor ? "constructor" : decl.tree.getKind() == Tree.Kind.ANNOTATION_TYPE ? "element" : "method";
            result.put("kind", kind);
            putIfPresent(result, "modifiers", modifiers(method.getModifiers()));
            String typeParams = typeParameters(method.getTypeParameters());
            putIfPresent(result, "typeParameters", typeParams);
            String returns = constructor ? null : method.getReturnType().toString();
            if (returns != null) {
                result.put("returns", typeRef(returns));
            }
            putIfPresent(result, "params", params);
            List<Object> thrown = new ArrayList<>();
            for (ExpressionTree t : method.getThrows()) {
                thrown.add(typeRef(t.toString()));
            }
            putIfPresent(result, "throws", thrown);
            String defaultValue = method.getDefaultValue() == null ? null : method.getDefaultValue().toString();
            putIfPresent(result, "default", defaultValue);
            nullability(result, method.getModifiers());

            StringBuilder signature = new StringBuilder();
            if (typeParams != null) {
                signature.append(typeParams).append(' ');
            }
            if (returns != null) {
                signature.append(returns).append(' ');
            }
            signature.append(name).append('(').append(String.join(", ", paramText)).append(')');
            if (defaultValue != null) {
                signature.append(" default ").append(defaultValue);
            }
            result.put("signature", signature.toString());

            DocCommentTree comment = comment(method);
            count(putIfPresent(result, "doc", docs.doc(comment)));
            if (isDeprecated(method.getModifiers(), comment)) {
                result.put("deprecated", true);
            }
            return result;
        }

        // Mirrors the members Lombok adds at compile time, which do not exist in the source.
        private void lombok(List<Object> constructors, List<Object> methods, Map<String, Set<Integer>> explicit) {
            ClassTree tree = decl.tree;
            if (decl.isInterfaceLike()) {
                return;
            }
            String classGetter = lombokAccess(tree.getModifiers(), "Getter");
            String classSetter = lombokAccess(tree.getModifiers(), "Setter");

            List<VariableTree> fields = new ArrayList<>();
            for (Tree member : tree.getMembers()) {
                if (member instanceof VariableTree) {
                    VariableTree field = (VariableTree) member;
                    if (tree.getKind() == Tree.Kind.ENUM && field.getInitializer() instanceof NewClassTree) {
                        continue;
                    }
                    if (!field.getName().toString().startsWith("$")) {
                        fields.add(field);
                    }
                }
            }

            for (VariableTree field : fields) {
                Set<Modifier> flags = field.getModifiers().getFlags();
                boolean isStatic = flags.contains(Modifier.STATIC);
                boolean isFinal = flags.contains(Modifier.FINAL);
                if (isInternal(field.getModifiers())) {
                    continue;
                }
                String getter = lombokAccess(field.getModifiers(), "Getter");
                if (getter == null && !isStatic) {
                    getter = classGetter;
                }
                String setter = lombokAccess(field.getModifiers(), "Setter");
                if (setter == null && !isStatic && !isFinal) {
                    setter = classSetter;
                }
                if (isFinal) {
                    setter = null;
                }

                String name = field.getName().toString();
                String type = field.getType().toString();
                boolean primitiveBoolean = type.equals("boolean");
                String base = primitiveBoolean && name.length() > 2 && name.startsWith("is")
                        && Character.isUpperCase(name.charAt(2)) ? name.substring(2) : name;
                DocCommentTree comment = comment(field);
                Map<String, Object> doc = docs.doc(comment);

                if (published(getter)) {
                    String getterName = primitiveBoolean
                            ? (base.equals(name) ? "is" + capitalize(name) : name)
                            : "get" + capitalize(name);
                    if (!exists(explicit, getterName, 0)) {
                        Map<String, Object> m = generated(getterName, getterName + "()", getter, isStatic);
                        m.put("returns", typeRef(type));
                        m.put("signature", type + " " + getterName + "()");
                        nullability(m, field.getModifiers());
                        count(putIfPresent(m, "doc", doc));
                        methods.add(m);
                    }
                }
                if (published(setter)) {
                    String setterName = "set" + capitalize(base);
                    if (!exists(explicit, setterName, 1)) {
                        Map<String, Object> m = generated(setterName, setterName + "(" + idTypes(Collections.singletonList(type)) + ")", setter, isStatic);
                        m.put("returns", typeRef("void"));
                        Map<String, Object> p = new LinkedHashMap<>();
                        p.put("name", name);
                        p.put("type", typeRef(type));
                        m.put("params", Collections.singletonList(p));
                        m.put("signature", "void " + setterName + "(" + type + " " + name + ")");
                        count(putIfPresent(m, "doc", doc));
                        methods.add(m);
                    }
                }
            }

            constructor(constructors, methods, "AllArgsConstructor", fields, false);
            constructor(constructors, methods, "RequiredArgsConstructor", fields, true);
        }

        private void constructor(List<Object> constructors, List<Object> methods, String annotation,
                                 List<VariableTree> fields, boolean requiredOnly) {
            AnnotationTree found = lombokAnnotation(decl.tree.getModifiers(), annotation);
            if (found == null) {
                return;
            }
            Map<String, String> args = arguments(found);
            if (internalOn(args.get("onConstructor_")) || internalOn(args.get("onConstructor"))) {
                return;
            }
            String access = level(args.containsKey("access") ? args.get("access") : "PUBLIC");
            String staticName = args.get("staticName");
            List<VariableTree> selected = new ArrayList<>();
            for (VariableTree field : fields) {
                Set<Modifier> flags = field.getModifiers().getFlags();
                if (flags.contains(Modifier.STATIC)) {
                    continue;
                }
                boolean initialized = field.getInitializer() != null;
                boolean isFinal = flags.contains(Modifier.FINAL);
                if (requiredOnly) {
                    boolean nonNull = lombokAnnotation(field.getModifiers(), "NonNull") != null;
                    if (!initialized && (isFinal || nonNull)) {
                        selected.add(field);
                    }
                } else if (!(isFinal && initialized)) {
                    selected.add(field);
                }
            }
            List<Object> params = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<String> text = new ArrayList<>();
            for (VariableTree field : selected) {
                Map<String, Object> p = new LinkedHashMap<>();
                p.put("name", field.getName().toString());
                p.put("type", typeRef(field.getType().toString()));
                params.add(p);
                types.add(field.getType().toString());
                text.add(field.getType() + " " + field.getName());
            }
            String simple = decl.tree.getSimpleName().toString();
            if (staticName != null) {
                String factory = staticName.replace("\"", "");
                Map<String, Object> m = generated(factory, factory + "(" + idTypes(types) + ")", access, true);
                m.put("returns", typeRef(simple));
                putIfPresent(m, "params", params);
                m.put("signature", simple + " " + factory + "(" + String.join(", ", text) + ")");
                if (published(access)) {
                    methods.add(m);
                }
                return;
            }
            if (!published(access)) {
                return;
            }
            Map<String, Object> m = generated(simple, "new(" + idTypes(types) + ")", access, false);
            m.put("kind", "constructor");
            putIfPresent(m, "params", params);
            m.put("signature", simple + "(" + String.join(", ", text) + ")");
            constructors.add(m);
        }

        private Map<String, Object> generated(String name, String id, String access, boolean isStatic) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", uniqueId(id));
            m.put("name", name);
            m.put("kind", "method");
            List<String> modifiers = new ArrayList<>();
            if ("PROTECTED".equals(access)) {
                modifiers.add("protected");
            }
            if (isStatic) {
                modifiers.add("static");
            }
            putIfPresent(m, "modifiers", modifiers);
            m.put("origin", "lombok");
            return m;
        }

        private boolean published(String access) {
            return "PUBLIC".equals(access) || "PROTECTED".equals(access);
        }

        private boolean exists(Map<String, Set<Integer>> explicit, String name, int arity) {
            Set<Integer> arities = explicit.get(name.toLowerCase(Locale.ROOT));
            return arities != null && arities.contains(arity);
        }

        private void count(boolean documented) {
            members++;
            if (documented) {
                documentedMembers++;
            }
        }

        private String uniqueId(String id) {
            String candidate = id;
            int n = 2;
            while (!usedIds.add(candidate)) {
                candidate = id + "-" + n++;
            }
            return candidate;
        }

        private DocCommentTree comment(Tree member) {
            return docTrees.getDocCommentTree(new TreePath(decl.path, member));
        }

        private Map<String, Object> typeRef(String text) {
            Map<String, Object> ref = new LinkedHashMap<>();
            ref.put("text", text);
            Map<String, Object> refs = new TreeMap<>();
            Matcher matcher = IDENTIFIER.matcher(text);
            while (matcher.find()) {
                String token = matcher.group();
                String fqn = decl.resolve(token);
                if (fqn != null && published.contains(fqn)) {
                    refs.put(token, fqn);
                }
            }
            putIfPresent(ref, "refs", refs);
            return ref;
        }

        private String lombokAccess(ModifiersTree modifiers, String annotation) {
            AnnotationTree found = lombokAnnotation(modifiers, annotation);
            if (found == null) {
                return null;
            }
            Map<String, String> args = arguments(found);
            String value = args.containsKey("value") ? args.get("value") : args.get("access");
            return level(value == null ? "PUBLIC" : value);
        }

        private AnnotationTree lombokAnnotation(ModifiersTree modifiers, String simple) {
            for (AnnotationTree annotation : modifiers.getAnnotations()) {
                String name = annotation.getAnnotationType().toString();
                if (name.equals("lombok." + simple)) {
                    return annotation;
                }
                if (name.equals(simple) && ("lombok." + simple).equals(decl.scope.resolveImport(simple))) {
                    return annotation;
                }
            }
            return null;
        }
    }

    private static Map<String, String> arguments(AnnotationTree annotation) {
        Map<String, String> args = new HashMap<>();
        for (ExpressionTree argument : annotation.getArguments()) {
            if (argument instanceof AssignmentTree) {
                AssignmentTree assignment = (AssignmentTree) argument;
                args.put(assignment.getVariable().toString(), assignment.getExpression().toString());
            } else {
                args.put("value", argument.toString());
            }
        }
        return args;
    }

    private static String level(String expression) {
        String value = expression.substring(expression.lastIndexOf('.') + 1).trim();
        return value.toUpperCase(Locale.ROOT);
    }

    private static String idTypes(List<String> types) {
        List<String> simple = new ArrayList<>();
        for (String type : types) {
            String erased = stripGenerics(type);
            StringBuilder out = new StringBuilder();
            for (String part : erased.split("\\s+")) {
                out.append(part);
            }
            String value = out.toString();
            int dot = value.replace("...", "").replace("[]", "").lastIndexOf('.');
            if (dot >= 0) {
                value = value.substring(dot + 1);
            }
            simple.add(value);
        }
        return String.join(",", simple);
    }

    private static String stripGenerics(String type) {
        StringBuilder out = new StringBuilder();
        int depth = 0;
        for (char c : type.toCharArray()) {
            if (c == '<') {
                depth++;
            } else if (c == '>') {
                depth--;
            } else if (depth == 0) {
                out.append(c);
            }
        }
        return out.toString().trim();
    }

    private static String kind(ClassTree tree) {
        switch (tree.getKind()) {
            case INTERFACE:
                return "interface";
            case ENUM:
                return "enum";
            case ANNOTATION_TYPE:
                return "annotation";
            default:
                return "class";
        }
    }

    private static List<String> modifiers(ModifiersTree modifiers) {
        List<String> out = new ArrayList<>();
        for (Modifier modifier : modifiers.getFlags()) {
            if (modifier != Modifier.PUBLIC) {
                out.add(modifier.toString());
            }
        }
        Collections.sort(out);
        return out;
    }

    private static String typeParameters(List<? extends TypeParameterTree> parameters) {
        if (parameters.isEmpty()) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for (TypeParameterTree parameter : parameters) {
            out.add(parameter.toString());
        }
        return "<" + String.join(", ", out) + ">";
    }

    private static void nullability(Map<String, Object> target, ModifiersTree modifiers) {
        for (AnnotationTree annotation : modifiers.getAnnotations()) {
            String name = simpleName(annotation.getAnnotationType().toString());
            if (name.equals("Nullable")) {
                target.put("nullable", true);
            } else if (name.equals("NotNull") || name.equals("NonNull") || name.equals("Nonnull")) {
                target.put("notNull", true);
            }
        }
    }

    private static boolean isInternal(ModifiersTree modifiers) {
        for (AnnotationTree annotation : modifiers.getAnnotations()) {
            String name = annotation.getAnnotationType().toString();
            if (name.equals("ApiStatus.Internal") || name.endsWith(".ApiStatus.Internal")) {
                return true;
            }
        }
        return false;
    }

    private static boolean internalOn(String expression) {
        return expression != null && expression.contains("ApiStatus.Internal");
    }

    private static boolean isDeprecated(ModifiersTree modifiers, DocCommentTree comment) {
        for (AnnotationTree annotation : modifiers.getAnnotations()) {
            if (simpleName(annotation.getAnnotationType().toString()).equals("Deprecated")) {
                return true;
            }
        }
        if (comment != null) {
            for (DocTree tag : comment.getBlockTags()) {
                if (tag.getKind() == DocTree.Kind.DEPRECATED) {
                    return true;
                }
            }
        }
        return false;
    }

    private String eventSystem(String fqn, Set<String> seen) {
        TypeDecl decl = declared.get(fqn);
        if (decl == null || !seen.add(fqn) || decl.tree.getExtendsClause() == null
                || decl.tree.getKind() != Tree.Kind.CLASS) {
            return null;
        }
        String parent = decl.resolve(stripGenerics(decl.tree.getExtendsClause().toString()));
        if (parent == null) {
            return null;
        }
        if (parent.equals(VULCAN_EVENT)) {
            return "vulcan";
        }
        if (parent.startsWith("org.bukkit.event.")) {
            return "bukkit";
        }
        return eventSystem(parent, seen);
    }

    private boolean isCancellable(String fqn, Set<String> seen) {
        TypeDecl decl = declared.get(fqn);
        if (decl == null || !seen.add(fqn)) {
            return false;
        }
        List<Tree> supertypes = new ArrayList<Tree>(decl.tree.getImplementsClause());
        if (decl.tree.getExtendsClause() != null) {
            supertypes.add(decl.tree.getExtendsClause());
        }
        for (Tree supertype : supertypes) {
            String name = stripGenerics(supertype.toString());
            String resolved = decl.resolve(name);
            if (simpleName(name).equals("Cancellable")) {
                return true;
            }
            if (resolved != null && isCancellable(resolved, seen)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------ model

    private static final class Scope {
        final String pkg;
        final Map<String, String> singleImports = new HashMap<>();
        final List<String> starImports = new ArrayList<>();

        Scope(String pkg) {
            this.pkg = pkg;
        }

        String resolveImport(String simple) {
            String single = singleImports.get(simple);
            if (single != null) {
                return single;
            }
            for (String star : starImports) {
                if (star.equals("lombok")) {
                    return "lombok." + simple;
                }
            }
            return null;
        }
    }

    private final class TypeDecl {
        final String fqn;
        final ClassTree tree;
        final TreePath path;
        final Scope scope;
        final TypeDecl enclosing;
        final Map<String, String> nested = new HashMap<>();

        TypeDecl(String fqn, ClassTree tree, TreePath path, Scope scope, TypeDecl enclosing) {
            this.fqn = fqn;
            this.tree = tree;
            this.path = path;
            this.scope = scope;
            this.enclosing = enclosing;
        }

        boolean isInterfaceLike() {
            return tree.getKind() == Tree.Kind.INTERFACE || tree.getKind() == Tree.Kind.ANNOTATION_TYPE;
        }

        boolean isPublished() {
            if (isInternal(tree.getModifiers())) {
                return false;
            }
            Set<Modifier> flags = tree.getModifiers().getFlags();
            if (enclosing == null) {
                return flags.contains(Modifier.PUBLIC);
            }
            if (!enclosing.isPublished()) {
                return false;
            }
            if (enclosing.isInterfaceLike()) {
                return !flags.contains(Modifier.PRIVATE);
            }
            return flags.contains(Modifier.PUBLIC) || flags.contains(Modifier.PROTECTED);
        }

        String module() {
            if (scope.pkg.equals(ROOT_PACKAGE)) {
                return "";
            }
            String rest = scope.pkg.substring(ROOT_PACKAGE.length() + 1);
            int dot = rest.indexOf('.');
            return dot < 0 ? rest : rest.substring(0, dot);
        }

        String resolve(String name) {
            int dot = name.indexOf('.');
            if (dot > 0) {
                String head = resolveSimple(name.substring(0, dot));
                return head != null ? head + name.substring(dot) : name;
            }
            return resolveSimple(name);
        }

        private String resolveSimple(String name) {
            for (TypeDecl type = this; type != null; type = type.enclosing) {
                if (type.tree.getSimpleName().contentEquals(name)) {
                    return type.fqn;
                }
                String nestedType = type.nested.get(name);
                if (nestedType != null) {
                    return nestedType;
                }
            }
            String imported = scope.singleImports.get(name);
            if (imported != null) {
                return imported;
            }
            String local = qualify(scope.pkg, name);
            if (declared.containsKey(local)) {
                return local;
            }
            for (String star : scope.starImports) {
                String candidate = star + "." + name;
                if (declared.containsKey(candidate)) {
                    return candidate;
                }
            }
            return null;
        }
    }

    // ------------------------------------------------------------------ javadoc

    /** Turns javadoc into the schema's small document model: no HTML survives into the output. */
    private final class DocConverter {
        private final Scope scope;
        private final TypeDecl context;

        DocConverter(Scope scope, TypeDecl context) {
            this.scope = scope;
            this.context = context;
        }

        Map<String, Object> doc(DocCommentTree comment) {
            Map<String, Object> doc = new LinkedHashMap<>();
            if (comment == null) {
                return doc;
            }
            putIfPresent(doc, "summary", inline(comment.getFirstSentence()));
            List<Object> body = blocks(comment.getFullBody());
            if (body.size() > 1 || (body.size() == 1 && !sameAsSummary(body.get(0), doc.get("summary")))) {
                doc.put("body", body);
            }
            Map<String, Object> params = new LinkedHashMap<>();
            List<Object> thrown = new ArrayList<>();
            List<Object> see = new ArrayList<>();
            for (DocTree tag : comment.getBlockTags()) {
                switch (tag.getKind()) {
                    case PARAM: {
                        ParamTree param = (ParamTree) tag;
                        String name = param.getName().getName().toString();
                        params.put(param.isTypeParameter() ? "<" + name + ">" : name, inline(param.getDescription()));
                        break;
                    }
                    case RETURN:
                        putIfPresent(doc, "returns", inline(((ReturnTree) tag).getDescription()));
                        break;
                    case THROWS:
                    case EXCEPTION: {
                        ThrowsTree throwsTree = (ThrowsTree) tag;
                        Map<String, Object> entry = new LinkedHashMap<>();
                        entry.put("type", throwsTree.getExceptionName().getSignature());
                        putIfPresent(entry, "doc", inline(throwsTree.getDescription()));
                        thrown.add(entry);
                        break;
                    }
                    case DEPRECATED:
                        doc.put("deprecated", inline(((DeprecatedTree) tag).getBody()));
                        break;
                    case SINCE:
                        putIfPresent(doc, "since", plain(inline(((SinceTree) tag).getBody())));
                        break;
                    case SEE: {
                        List<? extends DocTree> reference = ((SeeTree) tag).getReference();
                        if (!reference.isEmpty() && reference.get(0) instanceof ReferenceTree) {
                            List<Object> label = inline(reference.subList(1, reference.size()));
                            see.add(Collections.singletonList(
                                    link(((ReferenceTree) reference.get(0)).getSignature(), plain(label), false)));
                        } else {
                            List<Object> text = inline(reference);
                            if (!text.isEmpty()) {
                                see.add(text);
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            putIfPresent(doc, "params", params);
            putIfPresent(doc, "throws", thrown);
            putIfPresent(doc, "see", see);
            return doc;
        }

        private boolean sameAsSummary(Object block, Object summary) {
            if (!(block instanceof Map) || summary == null) {
                return false;
            }
            Map<?, ?> map = (Map<?, ?>) block;
            return "paragraph".equals(map.get("type")) && summary.equals(map.get("content"));
        }

        List<Object> blocks(List<? extends DocTree> trees) {
            BlockWriter writer = new BlockWriter();
            visit(trees, writer);
            return writer.finish();
        }

        List<Object> inline(List<? extends DocTree> trees) {
            List<Object> out = new ArrayList<>();
            for (Object block : blocks(trees)) {
                Map<?, ?> map = (Map<?, ?>) block;
                Object content = map.get("content");
                if (content instanceof List) {
                    if (!out.isEmpty()) {
                        out.add(text(" "));
                    }
                    out.addAll((List<?>) content);
                } else if (map.get("items") instanceof List) {
                    for (Object item : (List<?>) map.get("items")) {
                        if (!out.isEmpty()) {
                            out.add(text(" "));
                        }
                        out.addAll((List<?>) item);
                    }
                } else if (map.get("text") != null) {
                    out.add(node("code", "text", map.get("text")));
                }
            }
            return normalize(out);
        }

        private void visit(List<? extends DocTree> trees, BlockWriter out) {
            for (DocTree tree : trees) {
                switch (tree.getKind()) {
                    case TEXT:
                    case ERRONEOUS:
                        out.text(((TextTree) tree).getBody());
                        break;
                    case CODE:
                        out.code(((com.sun.source.doctree.LiteralTree) tree).getBody().getBody());
                        break;
                    case LITERAL:
                        out.literal(((com.sun.source.doctree.LiteralTree) tree).getBody().getBody());
                        break;
                    case LINK:
                    case LINK_PLAIN: {
                        LinkTree link = (LinkTree) tree;
                        String label = plain(inline(link.getLabel()));
                        out.inline(link(link.getReference().getSignature(), label, tree.getKind() == DocTree.Kind.LINK));
                        break;
                    }
                    case VALUE: {
                        ReferenceTree reference = ((ValueTree) tree).getReference();
                        if (reference != null) {
                            out.code(reference.getSignature().replace('#', '.'));
                        }
                        break;
                    }
                    case START_ELEMENT:
                        out.startElement(((StartElementTree) tree).getName().toString().toLowerCase(Locale.ROOT));
                        break;
                    case END_ELEMENT:
                        out.endElement(((EndElementTree) tree).getName().toString().toLowerCase(Locale.ROOT));
                        break;
                    case ENTITY:
                        out.text(entity(((EntityTree) tree).getName().toString()));
                        break;
                    case INHERIT_DOC:
                        break;
                    default:
                        out.text(tree.toString());
                        break;
                }
            }
        }

        private Map<String, Object> link(String signature, String label, boolean code) {
            int hash = signature.indexOf('#');
            String typePart = hash < 0 ? signature : signature.substring(0, hash);
            String member = hash < 0 ? null : signature.substring(hash + 1);
            String memberName = member == null ? null : member.contains("(") ? member.substring(0, member.indexOf('(')) : member;
            String fqn;
            if (typePart.isEmpty()) {
                fqn = context == null ? null : context.fqn;
            } else if (context != null) {
                fqn = context.resolve(stripGenerics(typePart));
            } else {
                String imported = scope.singleImports.get(typePart);
                fqn = imported != null ? imported : published.contains(typePart) ? typePart : qualify(scope.pkg, typePart);
            }
            if (label == null || label.isEmpty()) {
                String typeLabel = typePart.isEmpty() ? "" : simpleName(typePart);
                label = memberName == null ? typeLabel : typeLabel.isEmpty() ? memberName : typeLabel + "." + memberName;
            }
            if (fqn == null || !published.contains(fqn)) {
                return code ? node("code", "text", label) : text(label);
            }
            Map<String, Object> link = new LinkedHashMap<>();
            link.put("type", "link");
            link.put("label", label);
            link.put("ref", fqn);
            putIfPresent(link, "member", memberName);
            if (code) {
                link.put("code", true);
            }
            return link;
        }
    }

    private static final class BlockWriter {
        private final List<Object> blocks = new ArrayList<>();
        private List<Object> paragraph = new ArrayList<>();
        private List<Object> items;
        private List<Object> item;
        private boolean ordered;
        private StringBuilder pre;
        private int strong;
        private int emphasis;
        private int code;

        void text(String body) {
            if (pre != null) {
                pre.append(body);
                return;
            }
            String[] parts = body.split("\\n[ \\t]*\\n", -1);
            for (int i = 0; i < parts.length; i++) {
                if (i > 0 && item == null) {
                    flushParagraph();
                }
                String collapsed = parts[i].replaceAll("\\s+", " ");
                if (collapsed.isEmpty()) {
                    continue;
                }
                if (code > 0) {
                    inline(node("code", "text", collapsed));
                } else if (strong > 0) {
                    inline(node("strong", "text", collapsed));
                } else if (emphasis > 0) {
                    inline(node("em", "text", collapsed));
                } else {
                    inline(ApiSchemaGenerator.text(collapsed));
                }
            }
        }

        void code(String body) {
            if (pre != null) {
                pre.append(body);
            } else {
                inline(node("code", "text", body.replaceAll("\\s+", " ").trim()));
            }
        }

        void literal(String body) {
            if (pre != null) {
                pre.append(body);
            } else {
                text(body);
            }
        }

        void inline(Map<String, Object> node) {
            if (pre != null) {
                Object label = node.containsKey("label") ? node.get("label") : node.get("text");
                pre.append(label);
                return;
            }
            (item != null ? item : paragraph).add(node);
        }

        void startElement(String name) {
            switch (name) {
                case "p":
                    if (pre == null && item == null) {
                        flushParagraph();
                    }
                    break;
                case "pre":
                    flushParagraph();
                    pre = new StringBuilder();
                    break;
                case "ul":
                case "ol":
                    flushParagraph();
                    finishItem();
                    items = new ArrayList<>();
                    ordered = name.equals("ol");
                    break;
                case "li":
                    if (items == null) {
                        flushParagraph();
                        items = new ArrayList<>();
                    }
                    finishItem();
                    item = new ArrayList<>();
                    break;
                case "b":
                case "strong":
                    strong++;
                    break;
                case "i":
                case "em":
                    emphasis++;
                    break;
                case "code":
                case "tt":
                    code++;
                    break;
                case "br":
                    text(" ");
                    break;
                default:
                    break;
            }
        }

        void endElement(String name) {
            switch (name) {
                case "p":
                    if (pre == null && item == null) {
                        flushParagraph();
                    }
                    break;
                case "pre":
                    if (pre != null) {
                        String body = dedent(pre.toString());
                        pre = null;
                        if (!body.isEmpty()) {
                            Map<String, Object> block = new LinkedHashMap<>();
                            block.put("type", "code");
                            block.put("language", "java");
                            block.put("text", body);
                            blocks.add(block);
                        }
                    }
                    break;
                case "ul":
                case "ol":
                    endList();
                    break;
                case "li":
                    finishItem();
                    break;
                case "b":
                case "strong":
                    strong = Math.max(0, strong - 1);
                    break;
                case "i":
                case "em":
                    emphasis = Math.max(0, emphasis - 1);
                    break;
                case "code":
                case "tt":
                    code = Math.max(0, code - 1);
                    break;
                default:
                    break;
            }
        }

        private void finishItem() {
            if (item != null) {
                List<Object> normalized = normalize(item);
                if (!normalized.isEmpty() && items != null) {
                    items.add(normalized);
                }
                item = null;
            }
        }

        private void endList() {
            finishItem();
            if (items != null && !items.isEmpty()) {
                Map<String, Object> block = new LinkedHashMap<>();
                block.put("type", "list");
                if (ordered) {
                    block.put("ordered", true);
                }
                block.put("items", items);
                blocks.add(block);
            }
            items = null;
        }

        private void flushParagraph() {
            List<Object> normalized = normalize(paragraph);
            if (!normalized.isEmpty()) {
                blocks.add(paragraph(normalized));
            }
            paragraph = new ArrayList<>();
        }

        List<Object> finish() {
            if (pre != null) {
                endElement("pre");
            }
            endList();
            flushParagraph();
            return blocks;
        }
    }

    // ------------------------------------------------------------------ README

    /** A deliberately small markdown subset: headings, paragraphs, fenced code, tables, lists. */
    private static final class MarkdownConverter {
        private static final Pattern INLINE = Pattern.compile(
                "`([^`]+)`|\\*\\*(.+?)\\*\\*|\\[([^\\]]+)\\]\\(([^)\\s]+)\\)|(https?://[^\\s)<>]+)");
        private static final Pattern HEADING = Pattern.compile("^(#{1,6})\\s+(.*)$");
        private static final Pattern BULLET = Pattern.compile("^\\s*(?:[-*]|(\\d+)\\.)\\s+(.*)$");
        private static final Pattern TABLE_RULE = Pattern.compile("^\\|?\\s*:?-{3,}.*$");

        private final Map<String, String> types;

        MarkdownConverter(Map<String, String> types) {
            this.types = types;
        }

        List<Object> convert(String markdown) {
            String[] lines = markdown.replace("\r\n", "\n").split("\n", -1);
            List<Object> blocks = new ArrayList<>();
            List<String> paragraph = new ArrayList<>();
            boolean skippedTitle = false;
            int i = 0;
            while (i < lines.length) {
                String line = lines[i];
                String trimmed = line.trim();
                if (trimmed.startsWith("```")) {
                    flush(paragraph, blocks);
                    String language = trimmed.substring(3).trim();
                    StringBuilder code = new StringBuilder();
                    i++;
                    while (i < lines.length && !lines[i].trim().startsWith("```")) {
                        code.append(lines[i]).append('\n');
                        i++;
                    }
                    i++;
                    Map<String, Object> block = new LinkedHashMap<>();
                    block.put("type", "code");
                    putIfPresent(block, "language", language.isEmpty() ? null : language);
                    block.put("text", dedent(code.toString()));
                    blocks.add(block);
                    continue;
                }
                Matcher heading = HEADING.matcher(trimmed);
                if (heading.matches()) {
                    flush(paragraph, blocks);
                    int level = heading.group(1).length();
                    if (level == 1 && !skippedTitle && blocks.isEmpty()) {
                        skippedTitle = true;
                    } else {
                        Map<String, Object> block = new LinkedHashMap<>();
                        block.put("type", "heading");
                        block.put("level", level);
                        block.put("content", inline(heading.group(2)));
                        blocks.add(block);
                    }
                    i++;
                    continue;
                }
                if (trimmed.startsWith("|") && i + 1 < lines.length && TABLE_RULE.matcher(lines[i + 1].trim()).matches()) {
                    flush(paragraph, blocks);
                    Map<String, Object> block = new LinkedHashMap<>();
                    block.put("type", "table");
                    block.put("header", cells(trimmed));
                    List<Object> rows = new ArrayList<>();
                    i += 2;
                    while (i < lines.length && lines[i].trim().startsWith("|")) {
                        rows.add(cells(lines[i].trim()));
                        i++;
                    }
                    block.put("rows", rows);
                    blocks.add(block);
                    continue;
                }
                Matcher bullet = BULLET.matcher(line);
                if (bullet.matches()) {
                    flush(paragraph, blocks);
                    boolean ordered = bullet.group(1) != null;
                    List<Object> items = new ArrayList<>();
                    while (i < lines.length) {
                        Matcher next = BULLET.matcher(lines[i]);
                        if (next.matches()) {
                            StringBuilder text = new StringBuilder(next.group(2));
                            i++;
                            while (i < lines.length && !lines[i].trim().isEmpty()
                                    && !BULLET.matcher(lines[i]).matches() && Character.isWhitespace(lines[i].charAt(0))) {
                                text.append(' ').append(lines[i].trim());
                                i++;
                            }
                            items.add(inline(text.toString()));
                        } else {
                            break;
                        }
                    }
                    Map<String, Object> block = new LinkedHashMap<>();
                    block.put("type", "list");
                    if (ordered) {
                        block.put("ordered", true);
                    }
                    block.put("items", items);
                    blocks.add(block);
                    continue;
                }
                if (trimmed.isEmpty()) {
                    flush(paragraph, blocks);
                } else {
                    paragraph.add(trimmed);
                }
                i++;
            }
            flush(paragraph, blocks);
            return blocks;
        }

        private List<Object> cells(String row) {
            String body = row.trim();
            if (body.startsWith("|")) {
                body = body.substring(1);
            }
            if (body.endsWith("|")) {
                body = body.substring(0, body.length() - 1);
            }
            List<Object> cells = new ArrayList<>();
            for (String cell : body.split("\\|", -1)) {
                cells.add(inline(cell.trim()));
            }
            return cells;
        }

        private void flush(List<String> paragraph, List<Object> blocks) {
            if (!paragraph.isEmpty()) {
                List<Object> content = inline(String.join(" ", paragraph));
                if (!content.isEmpty()) {
                    blocks.add(ApiSchemaGenerator.paragraph(content));
                }
                paragraph.clear();
            }
        }

        private List<Object> inline(String text) {
            List<Object> out = new ArrayList<>();
            Matcher matcher = INLINE.matcher(text);
            int last = 0;
            while (matcher.find()) {
                int end = matcher.end();
                if (matcher.group(5) != null) {
                    String url = matcher.group(5);
                    int trim = url.length();
                    while (trim > 0 && ".,;:!?".indexOf(url.charAt(trim - 1)) >= 0) {
                        trim--;
                    }
                    end = matcher.start() + trim;
                    out.add(ApiSchemaGenerator.text(text.substring(last, matcher.start())));
                    out.add(external(url.substring(0, trim), url.substring(0, trim)));
                    out.add(ApiSchemaGenerator.text(text.substring(end, matcher.end())));
                    last = matcher.end();
                    continue;
                }
                out.add(ApiSchemaGenerator.text(text.substring(last, matcher.start())));
                if (matcher.group(1) != null) {
                    String code = matcher.group(1);
                    String fqn = types.get(code);
                    if (fqn != null) {
                        Map<String, Object> link = new LinkedHashMap<>();
                        link.put("type", "link");
                        link.put("label", code);
                        link.put("ref", fqn);
                        link.put("code", true);
                        out.add(link);
                    } else {
                        out.add(node("code", "text", code));
                    }
                } else if (matcher.group(2) != null) {
                    out.add(node("strong", "text", matcher.group(2)));
                } else {
                    out.add(external(matcher.group(3), matcher.group(4)));
                }
                last = end;
            }
            out.add(ApiSchemaGenerator.text(text.substring(last)));
            return normalize(out);
        }

        private static Map<String, Object> external(String label, String href) {
            if (!href.startsWith("https://") && !href.startsWith("http://")) {
                return ApiSchemaGenerator.text(label);
            }
            Map<String, Object> link = new LinkedHashMap<>();
            link.put("type", "link");
            link.put("label", label);
            link.put("href", href);
            return link;
        }
    }

    // ------------------------------------------------------------------ helpers

    private static Map<String, Object> text(String text) {
        return node("text", "text", text);
    }

    private static Map<String, Object> node(String type, String key, Object value) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("type", type);
        node.put(key, value);
        return node;
    }

    private static Map<String, Object> paragraph(List<Object> content) {
        Map<String, Object> block = new LinkedHashMap<>();
        block.put("type", "paragraph");
        block.put("content", content);
        return block;
    }

    private static List<Object> normalize(List<Object> nodes) {
        List<Object> merged = new ArrayList<>();
        for (Object o : nodes) {
            @SuppressWarnings("unchecked")
            Map<String, Object> node = (Map<String, Object>) o;
            if ("text".equals(node.get("type")) && !merged.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> previous = (Map<String, Object>) merged.get(merged.size() - 1);
                if ("text".equals(previous.get("type"))) {
                    previous.put("text", ((String) previous.get("text") + node.get("text")).replaceAll("\\s+", " "));
                    continue;
                }
            }
            merged.add(new LinkedHashMap<>(node));
        }
        if (!merged.isEmpty()) {
            trimEdge(merged, 0, true);
        }
        if (!merged.isEmpty()) {
            trimEdge(merged, merged.size() - 1, false);
        }
        List<Object> out = new ArrayList<>();
        for (Object o : merged) {
            Map<?, ?> node = (Map<?, ?>) o;
            Object text = node.get("text");
            if (!"text".equals(node.get("type")) || (text != null && !((String) text).isEmpty())) {
                out.add(node);
            }
        }
        return out;
    }

    private static void trimEdge(List<Object> nodes, int index, boolean leading) {
        @SuppressWarnings("unchecked")
        Map<String, Object> node = (Map<String, Object>) nodes.get(index);
        Object text = node.get("text");
        if (text instanceof String) {
            String s = (String) text;
            node.put("text", leading ? s.replaceAll("^\\s+", "") : s.replaceAll("\\s+$", ""));
        }
    }

    private static String plain(List<Object> nodes) {
        StringBuilder out = new StringBuilder();
        for (Object o : nodes) {
            Map<?, ?> node = (Map<?, ?>) o;
            Object value = node.containsKey("label") ? node.get("label") : node.get("text");
            if (value != null) {
                out.append(value);
            }
        }
        return out.toString().trim();
    }

    private static String dedent(String code) {
        List<String> lines = new ArrayList<>(Arrays.asList(code.replace("\r\n", "\n").split("\n", -1)));
        while (!lines.isEmpty() && lines.get(0).trim().isEmpty()) {
            lines.remove(0);
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        int indent = Integer.MAX_VALUE;
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                int n = 0;
                while (n < line.length() && Character.isWhitespace(line.charAt(n))) {
                    n++;
                }
                indent = Math.min(indent, n);
            }
        }
        List<String> out = new ArrayList<>();
        for (String line : lines) {
            String stripped = line.length() >= indent ? line.substring(indent) : line.trim();
            out.add(stripped.replaceAll("\\s+$", ""));
        }
        return String.join("\n", out);
    }

    private static String entity(String name) {
        switch (name) {
            case "amp":
                return "&";
            case "lt":
                return "<";
            case "gt":
                return ">";
            case "quot":
                return "\"";
            case "apos":
                return "'";
            case "nbsp":
                return " ";
            case "mdash":
                return "—";
            case "ndash":
                return "–";
            default:
                if (name.startsWith("#x") || name.startsWith("#X")) {
                    return new String(Character.toChars(Integer.parseInt(name.substring(2), 16)));
                }
                if (name.startsWith("#")) {
                    return new String(Character.toChars(Integer.parseInt(name.substring(1))));
                }
                return "&" + name + ";";
        }
    }

    private static String qualify(String pkg, String name) {
        return pkg.isEmpty() ? name : pkg + "." + name;
    }

    private static String simpleName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private static String capitalize(String name) {
        return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static boolean putIfPresent(Map<String, Object> target, String key, Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof List && ((List<?>) value).isEmpty()) {
            return false;
        }
        if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) {
            return false;
        }
        target.put(key, value);
        return true;
    }

    private static final class Json {
        static void write(Object value, StringBuilder out) {
            if (value == null) {
                out.append("null");
            } else if (value instanceof String) {
                string((String) value, out);
            } else if (value instanceof Number || value instanceof Boolean) {
                out.append(value);
            } else if (value instanceof Map) {
                out.append('{');
                boolean first = true;
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    if (!first) {
                        out.append(',');
                    }
                    first = false;
                    string(String.valueOf(entry.getKey()), out);
                    out.append(':');
                    write(entry.getValue(), out);
                }
                out.append('}');
            } else if (value instanceof List) {
                out.append('[');
                boolean first = true;
                for (Object item : (List<?>) value) {
                    if (!first) {
                        out.append(',');
                    }
                    first = false;
                    write(item, out);
                }
                out.append(']');
            } else {
                throw new IllegalArgumentException("Unsupported JSON value: " + value.getClass());
            }
        }

        private static void string(String value, StringBuilder out) {
            out.append('"');
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '"':
                        out.append("\\\"");
                        break;
                    case '\\':
                        out.append("\\\\");
                        break;
                    case '\n':
                        out.append("\\n");
                        break;
                    case '\r':
                        out.append("\\r");
                        break;
                    case '\t':
                        out.append("\\t");
                        break;
                    default:
                        if (c < 0x20 || c == 0x2028 || c == 0x2029) {
                            out.append(String.format(Locale.ROOT, "\\u%04x", (int) c));
                        } else {
                            out.append(c);
                        }
                }
            }
            out.append('"');
        }
    }
}
