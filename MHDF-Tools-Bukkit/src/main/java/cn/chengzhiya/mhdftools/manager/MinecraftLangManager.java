package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.langutil.LangAPI;
import cn.chengzhiya.langutil.manager.lang.LangManager;
import cn.chengzhiya.mhdftools.Main;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public final class MinecraftLangManager {
    private final File file = new File(Main.instance.getDataFolder(), "minecraftLang");
    @Getter
    private LangAPI langAPI;

    /**
     * 初始化默认配置文件
     */
    public void init() {
        this.langAPI = new LangAPI(Main.instance, this.file);
        this.langAPI.getLangManager("zh_cn").downloadLang();
        this.langAPI.getLangManager("zh_cn").reloadLang();
    }

    /**
     * 获取物品名称
     *
     * @param item 物品实例
     * @return 物品名称
     */
    public Component getItemName(ItemStack item) {
        LangManager langManager = this.getLangAPI().getLangManager();
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().displayName();
        } else {
            if (!langManager.isLoaded()) langManager.reloadLang();
            return Component.text(langManager.getData().getString(LangAPI.instance.getItemManager().getKey(item)));
        }
    }

    /**
     * 获取群系名称
     *
     * @param biome 群系实例
     * @return 群系名称
     */
    public String getBiomeName(Biome biome) {
        return this.getLangAPI().getLangManager().getBiomeName(biome);
    }
}
