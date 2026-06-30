package cn.chengzhimeow.mhdftools.bukkit.module.spawn.config;

import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

public final class SpawnSetting extends AbstractYamlSetting<SpawnSetting.Config> {
    @Getter(lazy = true)
    private static final SpawnSetting instance = new SpawnSetting();
    @Getter private Config config;

    private SpawnSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/spawn.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(new BungeeCordLocation(
                super.getData().getString("server", "无"),
                super.getData().getString("world", "world"),
                super.getData().getDouble("x", 0.0),
                super.getData().getDouble("y", 60.0),
                super.getData().getDouble("z", 0.0),
                super.getData().getDouble("yaw", 0.0).floatValue(),
                super.getData().getDouble("pitch", 0.0).floatValue()
        ));
    }

    public void setLocation(BungeeCordLocation location) {
        super.getData().set("server", location.getServer());
        super.getData().set("world", location.getWorld());
        super.getData().set("x", location.getX());
        super.getData().set("y", location.getY());
        super.getData().set("z", location.getZ());
        super.getData().set("yaw", location.getYaw());
        super.getData().set("pitch", location.getPitch());
        super.save();
        this.reload();
    }

    public record Config(
            BungeeCordLocation location
    ) {
    }
}
