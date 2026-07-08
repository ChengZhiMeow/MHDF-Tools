package cn.chengzhimeow.mhdftools.bukkit.module;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class Module {
    @Getter
    private static final List<Module> registerModuleList = new ArrayList<>();
    @Getter
    private static final Map<String, Command> registerCommandMap = new ConcurrentHashMap<>();

    private final String id;

    public Module(String id) {
        this.id = id;
        Module.registerModuleList.add(this);
    }

    public void onLoad() {
    }

    public boolean isEnable() {
        return true;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public abstract void reloadConfig();

    public abstract @NotNull AbstractYamlSetting<?> getConfig();
}
