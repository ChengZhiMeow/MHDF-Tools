package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.Base64Util;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtil {
    /**
     * 处理文本中的敏感词
     *
     * @param message 文本
     * @return 处理后的文本
     */
    public static String applyBlackWord(String message) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.replaceWord");
        if (config == null) {
            return message;
        }

        if (!config.getBoolean("enable")) {
            return message;
        }

        List<YamlConfiguration> replaceList = YamlUtil.getConfigurationSectionList(config, "replace");
        for (YamlConfiguration replace : replaceList) {
            String type = replace.getString("type");
            if (type == null) {
                return message;
            }

            for (String s : replace.getStringList("word")) {
                if (!message.contains(s)) {
                    continue;
                }

                switch (type) {
                    case "line" -> {
                        List<String> lineList = replace.getStringList("lineList");
                        if (lineList.isEmpty()) {
                            break;
                        }

                        message = lineList.get(new Random().nextInt(lineList.size()));
                    }
                    case "word" -> {
                        String word = replace.getString("replaceWord");
                        if (word == null) {
                            break;
                        }

                        message = message.replace(s, word);
                    }
                }
            }
        }

        return message;
    }

    /**
     * 获取处理展示物品后的文本
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static String applyShowItem(Player player, String message) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.showItem");
        if (config == null) {
            return message;
        }

        if (!config.getBoolean("enable")) {
            return message;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        Material itemType = item.getType();

        if (itemType.isAir() || itemType.isEmpty()) {
            return message;
        }

        UUID uuid = UUID.randomUUID();
        Main.instance.getCacheManager().put("showItem", uuid.toString(), Base64Util.encode(item.serializeAsBytes()));
        MHDFScheduler.getAsyncScheduler().runTaskLater(Main.instance, () ->
                Main.instance.getCacheManager().remove("showItem", uuid.toString()),
                20L * config.getInt("removeCache")
        );

        String format = config.getString("format");
        if (format == null) {
            return message;
        }
        format = format
                .replace("{uuid}", uuid.toString())
                .replace("{name}", Main.instance.getMinecraftLangManager().getItemName(item).replace("§", "&"))
                .replace("{amount}", String.valueOf(item.getAmount()));

        for (String s : config.getStringList("word")) {
            message = ColorUtil.color(message).replace(s,
                    ColorUtil.color(format).hoverEvent(item.asHoverEvent())
            ).toMiniMessageString();
        }

        return message;
    }

    /**
     * 获取处理AT后的文本
     *
     * @param message 文本
     * @param atList  被AT的玩家列表
     * @return 处理后的文本
     */
    public static String applyAt(String message, Set<String> atList) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.at");
        if (config == null) {
            return message;
        }

        String patternFormat = config.getString("patternFormat");
        if (patternFormat == null) {
            return message;
        }

        TextComponent messageComponent = ColorUtil.color(message);
        TextComponent format = LangUtil.i18n("chat.at.format");
        for (String at : atList) {
            Pattern pattern = Pattern.compile(patternFormat.replace("{at}", at));
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                messageComponent = messageComponent
                        .replace(matcher.group(), format.replace("{name}", at));
            }
        }

        if (atList.contains(AtUtil.getAtAll())) {
            for (String at : config.getStringList("allMessage")) {
                Pattern pattern = Pattern.compile(patternFormat.replace("{at}", at));
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    messageComponent = messageComponent
                            .replace(matcher.group(), format.replace("{name}", LangUtil.i18n("chat.at.all")));
                }
            }
        }

        return messageComponent.toMiniMessageString();
    }

    /**
     * 获取处理格式后的文本
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static String getFormatMessage(Player player, String message) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("chatSettings.format");
        String group = GroupUtil.getGroup(player, config, "mhdftools.group.chatformat.");

        String format;
        if (config == null || !config.getBoolean("enable")) {
            format = "<{player}> {message}";
        } else {
            format = config.getString(group + ".format");
        }

        return Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                .placeholder(player, format)
                .replace("{player}", NickUtil.getName(player))
                .replace("{message}", message);
    }
}
