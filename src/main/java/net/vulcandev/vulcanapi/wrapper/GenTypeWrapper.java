package net.vulcandev.vulcanapi.wrapper;

import net.vulcandev.genblocks.managers.BucketManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanGenBlocks GenType to provide a clean API without exposing internal types
 */
public class GenTypeWrapper {

    public enum Type {
        VERTICAL,
        HORIZONTAL,
        BUCKET,
        INFINITEBLOCK
    }

    private final Type type;

    public GenTypeWrapper(@NotNull Type type) {
        this.type = type;
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @Nullable
    public static GenTypeWrapper fromVulcanGenType(@Nullable BucketManager.Type genType) {
        if (genType == null) return null;
        try {
            Type wrapperType = Type.valueOf(genType.name());
            return new GenTypeWrapper(wrapperType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public BucketManager.Type toVulcanGenType() {
        return BucketManager.Type.valueOf(type.name());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GenTypeWrapper that = (GenTypeWrapper) obj;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return type.name();
    }
}