package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.langutil.LangAPI;
import cn.chengzhiya.mhdftools.Main;
import lombok.Getter;
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
        this.langAPI = new LangAPI(Main.instance, file);
        this.langAPI.getLangManager("zh_cn").downloadLang();
        this.langAPI.getLangManager("zh_cn").reloadLang();
    }

    /**
     * 获取物品名称
     *
     * @param item 物品实例
     * @return 物品名称
     */
    public String getItemName(ItemStack item) {
        return getLangAPI().getLangManager().getItemName(item);
    }

    /**
     * 获取群系名称
     *
     * @param biome 群系实例
     * @return 群系名称
     */
    public String getBiomeName(Biome biome) {
        return getLangAPI().getLangManager().getBiomeName(biome);
    }
}
