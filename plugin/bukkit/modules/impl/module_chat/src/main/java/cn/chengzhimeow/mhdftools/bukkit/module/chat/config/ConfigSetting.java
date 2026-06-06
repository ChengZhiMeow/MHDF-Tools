package cn.chengzhimeow.mhdftools.bukkit.module.chat.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.*;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();

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

        List<Config.ReplaceWord.Replace> replaceList = new ArrayList<>();
        for (ConfigurationSection section : super.getData().getConfigurationSectionList("replace_word.replace")) {
            ConfigurationSection bypass = section.getConfigurationSection("bypass");
            replaceList.add(new Config.ReplaceWord.Replace(
                    section.getStringList("word"),
                    section.getString("type", "word"),
                    section.getBoolean("regex"),
                    bypass != null && bypass.getBoolean("enable"),
                    bypass == null ? "" : bypass.getString("permission", ""),
                    section.getString("replace_word", ""),
                    section.getStringList("line_list")
            ));
        }
        Config.ReplaceWord replaceWord = new Config.ReplaceWord(super.getData().getBoolean("replace_word.enable"), replaceList);

        Map<String, Config.Format.Group> groups = new LinkedHashMap<>();
        ConfigurationSection formatSection = super.getData().getConfigurationSection("format");
        if (formatSection != null) {
            for (String key : formatSection.getKeys(false)) {
                if (key.equals("enable")) continue;
                ConfigurationSection group = formatSection.getConfigurationSection(key);
                if (group == null) continue;
                groups.put(key, new Config.Format.Group(group.getInt("weight"), group.getString("format", "<{player}> {message}")));
            }
        }
        Config.Format format = new Config.Format(super.getData().getBoolean("format.enable"), groups);

        this.config = new Config(
                super.getData().getBoolean("enable"),
                new Config.Delay(
                        super.getData().getBoolean("delay.enable"),
                        super.getData().getInt("delay.delay")
                ),
                super.getData().getBoolean("spam.enable"),
                replaceWord,
                new Config.Show(
                        super.getData().getBoolean("show_item.enable"),
                        super.getData().getString("show_item.format", ""),
                        super.getData().getStringList("show_item.word"),
                        super.getData().getInt("show_item.remove_cache"),
                        super.getData().getStringList("show_item.commands")
                ),
                new Config.Show(
                        super.getData().getBoolean("show_inventory.enable"),
                        super.getData().getString("show_inventory.format", ""),
                        super.getData().getStringList("show_inventory.word"),
                        super.getData().getInt("show_inventory.remove_cache"),
                        super.getData().getStringList("show_inventory.commands")
                ),
                new Config.Show(
                        super.getData().getBoolean("show_ender_chest.enable"),
                        super.getData().getString("show_ender_chest.format", ""),
                        super.getData().getStringList("show_ender_chest.word"),
                        super.getData().getInt("show_ender_chest.remove_cache"),
                        super.getData().getStringList("show_ender_chest.commands")
                ),
                new Config.Msg(
                        super.getData().getBoolean("msg.enable"),
                        super.getData().getStringList("msg.commands"),
                        super.getData().getStringList("msg.reply_commands")
                ),
                new Config.At(
                        super.getData().getBoolean("at.enable"),
                        super.getData().getString("at.pattern_format", "@?{at}"),
                        super.getData().getStringList("at.all_message")
                ),
                format
        );
    }

    public record Config(
            boolean enable,
            Delay delay,
            boolean spam,
            ReplaceWord replaceWord,
            Show showItem,
            Show showInventory,
            Show showEnderChest,
            Msg msg,
            At at,
            Format format
    ) {
        public record Delay(boolean enable, int delay) {
        }

        public record ReplaceWord(boolean enable, List<Replace> replaceList) {
            public record Replace(
                    List<String> word,
                    String type,
                    boolean regex,
                    boolean bypassEnable,
                    String bypassPermission,
                    String replaceWord,
                    List<String> lineList
            ) {
            }
        }

        public record Show(boolean enable, String format, List<String> word, int removeCache, List<String> commands) {
        }

        public record Msg(boolean enable, List<String> commands, List<String> replyCommands) {
        }

        public record At(boolean enable, String patternFormat, List<String> allMessage) {
        }

        public record Format(boolean enable, Map<String, Group> groups) {
            public Group select(org.bukkit.entity.Player player) {
                return this.groups.entrySet().stream()
                        .filter(entry -> entry.getKey().equals("default") || player.hasPermission("mhdftools.group.chatformat." + entry.getKey()))
                        .map(Map.Entry::getValue)
                        .max(Comparator.comparingInt(Group::weight))
                        .orElse(new Group(0, "<{player}> {message}"));
            }

            public record Group(int weight, String format) {
            }
        }
    }
}
