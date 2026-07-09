package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.vulcandev.genblocks.managers.BucketManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
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
    public String toString() {
        return type.name();
    }
}
