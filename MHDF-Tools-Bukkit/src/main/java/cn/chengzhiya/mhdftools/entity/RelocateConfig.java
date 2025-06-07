package cn.chengzhiya.mhdftools.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public final class RelocateConfig {
    private final boolean relocatable;
    private final boolean relocatableGroupId;
    private final String[] relocator;

    public RelocateConfig(boolean relocatable, boolean relocatableGroupId, String... relocator) {
        this.relocatable = relocatable;
        this.relocatableGroupId = relocatableGroupId;
        this.relocator = Arrays.stream(relocator)
                .map(s -> s.replace("{}", "."))
                .toArray(String[]::new);
    }

    public RelocateConfig(boolean relocatable) {
        this(relocatable, true);
    }
}
