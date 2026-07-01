package cn.chengzhimeow.mhdftools.bukkit.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Placeholder;
import lombok.Getter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class PlaceholderRegisterManager {
    @Getter(lazy = true)
    private static final PlaceholderRegisterManager instance = new PlaceholderRegisterManager();

    private final Map<String, Placeholder> placeholders = new ConcurrentHashMap<>();
    private PlaceholderExpansion expansion;
    private boolean registered;

    private PlaceholderRegisterManager() {
    }

    @SneakyThrows
    public void registerPlaceholders(Module module) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return;

        Reflections reflections = new Reflections(module.getClass().getPackageName());

        Set<Class<? extends Placeholder>> placeholderClassSet = reflections.getSubTypesOf(Placeholder.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends Placeholder> clazz : placeholderClassSet) {
            Constructor<? extends Placeholder> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Placeholder placeholder = constructor.newInstance();

            if (!placeholder.isEnable()) continue;

            this.placeholders.put(placeholder.getPlaceholder().toLowerCase(), placeholder);
        }

        if (this.registered) return;
        this.expansion = new Expansion();
        this.expansion.register();
        this.registered = true;
    }

    public void unregister() {
        if (!this.registered) return;

        this.expansion.unregister();
        this.expansion = null;
        this.placeholders.clear();
        this.registered = false;
    }

    private @Nullable String placeholder(@Nullable OfflinePlayer player, @NotNull String params) {
        String[] args = params.split("_");
        for (int i = args.length; i > 0; i--) {
            String key = String.join("_", Arrays.copyOfRange(args, 0, i)).toLowerCase();
            Placeholder placeholder = this.placeholders.get(key);
            if (placeholder == null) continue;

            return placeholder.placeholder(player, Arrays.copyOfRange(args, i, args.length));
        }

        return null;
    }

    private final class Expansion extends PlaceholderExpansion {
        @Override
        public @NotNull String getIdentifier() {
            return "mhdftools";
        }

        @Override
        public @NotNull String getAuthor() {
            return Main.instance.getDescription().getAuthors().toString();
        }

        @Override
        public @NotNull String getVersion() {
            return Main.instance.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
            return PlaceholderRegisterManager.this.placeholder(player, params);
        }
    }
}
