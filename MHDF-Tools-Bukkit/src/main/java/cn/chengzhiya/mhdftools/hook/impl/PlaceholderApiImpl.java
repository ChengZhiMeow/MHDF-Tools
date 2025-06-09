package cn.chengzhiya.mhdftools.hook.impl;

import cn.chengzhiya.mhdftools.placeholder.AbstractPlaceholder;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class PlaceholderApiImpl extends PlaceholderExpansion {
    private final List<AbstractPlaceholder> placeholderList = new ArrayList<>();

    @SneakyThrows
    public PlaceholderApiImpl() {
            Reflections reflections = new Reflections(AbstractPlaceholder.class.getPackageName());

            for (Class<? extends AbstractPlaceholder> clazz : reflections.getSubTypesOf(AbstractPlaceholder.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractPlaceholder abstractPlaceholder = clazz.getDeclaredConstructor().newInstance();
                    if (abstractPlaceholder.isEnable()) {
                        placeholderList.add(abstractPlaceholder);
                    }
                }
            }
        this.register();
    }

    public void unhook() {
        this.unregister();
    }

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mhdftools";
    }

    @Override
    public @NotNull String getAuthor() {
        return "白神遥桌上の橙汁";
    }

    @Override
    public @NotNull String getVersion() {
        return PluginUtil.getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        for (AbstractPlaceholder placeholder : placeholderList) {
            String result = placeholder.onPlaceholder(player, params);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
