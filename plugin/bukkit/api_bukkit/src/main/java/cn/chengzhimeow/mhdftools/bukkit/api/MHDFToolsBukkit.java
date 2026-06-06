package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.bukkit.api.manager.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MHDFToolsBukkit extends JavaPlugin {
    @Getter
    @Setter
    private static MHDFToolsBukkit instance;

    public abstract ItemManager getItemManager();
}
