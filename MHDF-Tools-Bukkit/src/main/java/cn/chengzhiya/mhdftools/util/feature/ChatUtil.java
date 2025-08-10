package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.Base64Util;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import com.alibaba.fastjson2.JSONObject;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
     * @param player           玩家实例
     * @param messageComponent 文本实例
     * @param message          文本
     * @return 处理后的文本
     */
    public static TextComponent applyReplaceWord(CommandSender player, TextComponent messageComponent, String message) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("chatSettings.replaceWord");
        if (config == null || !config.getBoolean("enable")) return messageComponent;

        for (YamlConfiguration replace : Main.instance.getConfigManager().getConfigManager().getYamlConfigurationList("chatSettings.replaceWord.replace")) {
            String type = replace.getString("type");
            if (type == null) return messageComponent;
            if (replace.getBoolean("bypass.enable") && player.hasPermission(replace.getString("bypass.permission", "")))
                continue;

            boolean regex = replace.getBoolean("regex");
            for (String s : replace.getStringList("word")) {
                if (!message.contains(s)) continue;
                switch (type) {
                    case "line" -> {
                        List<String> lineList = replace.getStringList("lineList");
                        if (!lineList.isEmpty())
                            messageComponent = ColorUtil.color(lineList.get(new Random().nextInt(lineList.size())));
                    }
                    case "word" -> {
                        String word = replace.getString("replaceWord");
                        if (word != null) messageComponent = messageComponent.replace(s, word);
                    }
                }
                if (regex) {
                    Matcher matcher = Pattern.compile(s).matcher(message);
                    if (matcher.find()) messageComponent = messageComponent.replace("{value}", matcher.group());
                }
            }
        }
        return messageComponent;
    }

    /**
     * 获取处理展示物品后的文本
     *
     * @param player           玩家实例
     * @param messageComponent 文本实例
     * @return 处理后的文本
     */
    public static TextComponent applyShowItem(Player player, TextComponent messageComponent) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("chatSettings.showItem");
        if (config == null || !config.getBoolean("enable")) return messageComponent;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return messageComponent;

        String format = config.getString("format");
        if (format == null) return messageComponent;

        UUID uuid = UUID.randomUUID();
        Main.instance.getCacheManager().put("showItem", uuid.toString(), Base64Util.encode(item.serializeAsBytes()));
        MHDFScheduler.getAsyncScheduler().runTaskLater(Main.instance, () -> Main.instance.getCacheManager().remove("showItem", uuid.toString()), 20L * config.getInt("removeCache"));

        ItemMeta meta = item.getItemMeta();
        Component displayName = PluginUtil.isNativeSupportAdventureApi() && meta.hasCustomName() ? meta.customName() : ColorUtil.color(Main.instance.getMinecraftLangManager().getItemName(item));
        TextComponent formatComponent = ColorUtil.color(format)
                .replace("{uuid}", uuid.toString())
                .replace("{name}", displayName)
                .replace("{amount}", String.valueOf(item.getAmount()));

        for (String s : config.getStringList("word")) {
            messageComponent = messageComponent.replace(s, formatComponent.hoverEvent(item.asHoverEvent()));
        }
        return messageComponent;
    }

    /**
     * 获取处理展示物背包的文本
     *
     * @param player           玩家实例
     * @param messageComponent 文本实例
     * @return 处理后的文本
     */
    public static TextComponent applyShowInventory(Player player, TextComponent messageComponent) {
        return ChatUtil.applyShowableContainer(player, messageComponent, "showInventory", player.getInventory().getContents());
    }

    /**
     * 获取处理展示物末影箱的文本
     *
     * @param player           玩家实例
     * @param messageComponent 文本实例
     * @return 处理后的文本
     */
    public static TextComponent applyShowEnderChest(Player player, TextComponent messageComponent) {
        return ChatUtil.applyShowableContainer(player, messageComponent, "showEnderChest", player.getEnderChest().getContents());
    }

    /**
     * 获取处理AT后的文本
     *
     * @param messageComponent 文本实例
     * @param message          文本
     * @param atList           被AT的玩家列表
     * @return 处理后的文本
     */
    public static TextComponent applyAt(TextComponent messageComponent, String message, Set<String> atList) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("chatSettings.at");
        if (config == null) return messageComponent;

        String patternFormat = config.getString("patternFormat");
        if (patternFormat == null) return messageComponent;

        TextComponent format = Main.instance.getConfigManager().getLangManager().i18n("chat.at.format");
        for (String at : atList) {
            Matcher matcher = Pattern.compile(patternFormat.replace("{at}", at)).matcher(message);
            if (matcher.find()) {
                messageComponent = messageComponent.replace(matcher.group(), format.replace("{name}", at));
            }
        }

        if (atList.contains(AtUtil.getAtAll())) {
            TextComponent allFormat = format.replace("{name}", Main.instance.getConfigManager().getLangManager().i18n("chat.at.all"));
            for (String at : config.getStringList("allMessage")) {
                Matcher matcher = Pattern.compile(patternFormat.replace("{at}", at)).matcher(message);
                if (matcher.find()) {
                    messageComponent = messageComponent.replace(matcher.group(), allFormat);
                }
            }
        }
        return messageComponent;
    }

    /**
     * 获取处理格式后的文本
     *
     * @param player  玩家实例
     * @param message 文本
     * @return 处理后的文本
     */
    public static TextComponent formatMessage(Player player, TextComponent message) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("chatSettings.format");
        String group = GroupUtil.getGroup(player, config, "mhdftools.group.chatformat.");
        String format = (config == null || !config.getBoolean("enable")) ? "<{player}> {message}" : config.getString(group + ".format");

        return ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, format))
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{message}", message);
    }

    /**
     * 处理展示容器后的文本
     *
     * @param player           玩家实例
     * @param messageComponent 文本实例
     * @param configKey        配置名称
     * @param contents         物品数组
     * @return 处理后的文本
     */
    private static TextComponent applyShowableContainer(Player player, TextComponent messageComponent, String configKey, ItemStack[] contents) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("chatSettings." + configKey);
        if (config == null || !config.getBoolean("enable")) return messageComponent;

        String format = config.getString("format");
        if (format == null) return messageComponent;

        JSONObject inventoryData = new JSONObject();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && !item.getType().isAir()) {
                inventoryData.put(String.valueOf(i), Base64Util.encode(item.serializeAsBytes()));
            }
        }

        if (inventoryData.isEmpty()) return messageComponent;

        UUID uuid = UUID.randomUUID();
        Main.instance.getCacheManager().put(configKey, uuid.toString(), inventoryData.toString());
        MHDFScheduler.getAsyncScheduler().runTaskLater(Main.instance, () -> Main.instance.getCacheManager().remove(configKey, uuid.toString()), 20L * config.getInt("removeCache"));

        TextComponent formatComponent = ColorUtil.color(format
                .replace("{uuid}", uuid.toString())
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName()));

        for (String s : config.getStringList("word")) {
            messageComponent = messageComponent.replace(s, formatComponent);
        }
        return messageComponent;
    }
}
