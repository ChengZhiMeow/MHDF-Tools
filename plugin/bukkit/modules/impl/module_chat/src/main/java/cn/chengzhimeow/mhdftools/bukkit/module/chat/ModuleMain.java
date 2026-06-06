package cn.chengzhimeow.mhdftools.bukkit.module.chat;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.ChatDelayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.LastChatCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.ReplyTargetCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.message.ChatBroadcastMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.message.ChatPrivateMessage;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ModuleMain extends Module {
    public static ModuleMain instance;
    private final DisplayCache displayCache = new DisplayCache();
    private final ChatDelayCache chatDelayCache = new ChatDelayCache();
    private final LastChatCache lastChatCache = new LastChatCache();
    private final ReplyTargetCache replyTargetCache = new ReplyTargetCache();

    public ModuleMain() {
        super("chat");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        this.displayCache.init();
        this.chatDelayCache.init();
        this.lastChatCache.init();
        this.replyTargetCache.init();

        MHDFToolsBukkit.getInstance().getRedisManager().register(ChatBroadcastMessage.ID, ChatBroadcastMessage.CODEC);
        MHDFToolsBukkit.getInstance().getRedisManager().register(ChatPrivateMessage.ID, ChatPrivateMessage.CODEC);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
