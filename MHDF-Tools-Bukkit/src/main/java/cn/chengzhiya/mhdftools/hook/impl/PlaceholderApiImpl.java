package cn.chengzhiya.mhdftools.hook.impl;

import cn.chengzhiya.mhdftools.placeholder.Placeholder;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class PlaceholderApiImpl extends PlaceholderExpansion {
    private final List<Placeholder> placeholderList = new ArrayList<>();

    @SneakyThrows
    public PlaceholderApiImpl() {
        Reflections reflections = new Reflections(Placeholder.class.getPackageName());

        for (Class<? extends Placeholder> clazz : reflections.getSubTypesOf(Placeholder.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends Placeholder> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Placeholder placeholder = constructor.newInstance();

                if (placeholder.isEnable()) {
                    this.placeholderList.add(placeholder);
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
        for (Placeholder placeholder : this.placeholderList) {
            String result = placeholder.onPlaceholder(player, params);
            if (result == null) continue;

            return result;
        }
        return null;
    }
}
