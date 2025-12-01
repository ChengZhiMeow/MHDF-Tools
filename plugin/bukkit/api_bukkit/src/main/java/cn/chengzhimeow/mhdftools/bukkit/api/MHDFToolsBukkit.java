package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.bukkit.api.manager.BungeeCordManager;
import lombok.Getter;
import lombok.Setter;

public final class MHDFToolsBukkit {
    @Getter(lazy = true)
    private static final MHDFToolsBukkit instance = new MHDFToolsBukkit();

    @Getter
    @Setter
    private BungeeCordManager bungeeCordManager;

    private MHDFToolsBukkit() {}
}
