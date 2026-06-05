package cn.chengzhimeow.mhdftools.bukkit.module.motd.config;

import cn.chengzhimeow.mhdftools.bukkit.module.motd.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.List;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();
    @Getter private Config config;

    private ConfigSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(
                super.getData().getBoolean("enable"),
                new Config.Version(
                        super.getData().getBoolean("version.enable"),
                        super.getData().getString("version.name", "")
                ),
                new Config.Players(
                        super.getData().getBoolean("players.enable"),
                        new Config.Players.FakeAmount(
                                super.getData().getBoolean("players.fake_online.enable"),
                                super.getData().getString("players.fake_online.amount")
                        ),
                        new Config.Players.FakeAmount(
                                super.getData().getBoolean("players.fake_max.enable"),
                                super.getData().getString("players.fake_max.amount")
                        ),
                        new Config.Players.FakeSample(
                                super.getData().getBoolean("players.fake_sample.enable"),
                                super.getData().getStringList("players.fake_sample.text")
                        )
                ),
                super.getData().getList("description", List.class)
        );
    }

    public record Config(
            boolean enable,
            Version version,
            Players players,
            List<List> description
    ) {
        public record Version(
                boolean enable,
                String name
        ) {
        }

        public record Players(
                boolean enable,
                FakeAmount fakeOnline,
                FakeAmount fakeMax,
                FakeSample fakeSample
        ) {
            public record FakeAmount(
                    boolean enable,
                    String amount
            ) {
            }

            public record FakeSample(
                    boolean enable,
                    List<String> text
            ) {
            }
        }
    }
}
