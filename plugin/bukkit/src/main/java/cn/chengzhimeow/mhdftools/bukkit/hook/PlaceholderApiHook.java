package cn.chengzhimeow.mhdftools.bukkit.hook;

import cn.chengzhimeow.mhdftools.bukkit.hook.impl.PlaceholderApiImpl;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@Getter
public final class PlaceholderApiHook extends Hook {
    private PlaceholderApiImpl api;

    /**
     * 初始化PlaceholderAPI的API
     */
    @Override
    public void hook() {
        this.api = new PlaceholderApiImpl();
        super.enable = true;
    }

    /**
     * 卸载PlaceholderAPI的API
     */
    @Override
    public void unhook() {
        if (!super.enable) return;
        super.enable = false;
        this.api = null;
    }

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        if (super.isEnable()) return this.getApi().placeholder(player, message);
        return message;
    }
}
