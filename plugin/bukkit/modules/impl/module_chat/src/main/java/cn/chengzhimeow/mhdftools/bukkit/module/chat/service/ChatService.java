package cn.chengzhimeow.mhdftools.bukkit.module.chat.service;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatService {
    private static final Random RANDOM = new Random();

    /*
     * 按发送者权限清理聊天文本中的颜色符号和 MiniMessage 标签。
     */
    public static String sanitize(CommandSender sender, String message) {
        if (!sender.hasPermission("mhdftools.chat.color")) {
            message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (!sender.hasPermission("mhdftools.chat.minimessage")) {
            message = Pattern.compile("</?[a-zA-Z0-9_:-]+[^>]*>").matcher(message).replaceAll("");
        }
        return message;
    }

    /*
     * 根据替换词配置处理原始消息，并把命中的内容替换到组件中。
     */
    public static TextComponent applyReplaceWord(CommandSender sender, TextComponent component, String rawMessage) {
        ConfigSetting.Config.ReplaceWord config = ConfigSetting.getInstance().getConfig().replaceWord();
        if (!config.enable()) return component;

        for (ConfigSetting.Config.ReplaceWord.Replace replace : config.replaceList()) {
            if (replace.bypassEnable() && sender.hasPermission(replace.bypassPermission())) continue;

            for (String word : replace.word()) {
                String value = findValue(rawMessage, word, replace.regex());
                if (value == null) continue;

                if (replace.type().equalsIgnoreCase("line")) {
                    if (!replace.lineList().isEmpty()) {
                        component = ColorUtil.color(replace.lineList().get(RANDOM.nextInt(replace.lineList().size())));
                    }
                    continue;
                }

                if (!replace.replaceWord().isBlank()) {
                    component = component.replace(value, ColorUtil.color(replace.replaceWord()).replaceByMiniMessage("{value}", value));
                }
            }
        }
        return component;
    }

    /*
     * 处理展示手持物品的占位词，并把物品缓存数据写入消息缓存列表。
     */
    public static TextComponent applyShowItem(Player player, TextComponent component, List<byte[]> cacheDataList) {
        ConfigSetting.Config.Show config = ConfigSetting.getInstance().getConfig().showItem();
        if (!config.enable()) return component;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return component;

        int removeCache = Math.max(config.removeCache(), 1);
        DisplayCache.CacheEntry entry = ModuleMain.instance.getDisplayCache().putItem(item, removeCache);
        cacheDataList.add(DisplayCache.encode(entry, removeCache));

        ItemMeta meta = item.getItemMeta();
        Component displayName = meta != null && meta.hasDisplayName() ? meta.displayName() : Component.translatable(item.translationKey());
        TextComponent replacement = ColorUtil.color(config.format().replace("{uuid}", entry.id()))
                .replace("{name}", displayName)
                .replace("{amount}", String.valueOf(item.getAmount()));

        for (String word : config.word()) {
            component = component.replace(word, replacement.hoverEvent(item.asHoverEvent()));
        }
        return component;
    }

    /*
     * 按 at 列表高亮消息中的 at 文本。
     */
    public static TextComponent applyAt(TextComponent component, String rawMessage, Set<String> atList) {
        ConfigSetting.Config.At config = ConfigSetting.getInstance().getConfig().at();
        TextComponent format = LangSetting.getInstance().getConfig().chat().at().format();
        for (String target : atList) {
            String name = target.equals(AtService.AT_ALL) ? LangSetting.getInstance().getConfig().chat().at().all().toMiniMessageString() : target;
            Matcher matcher = Pattern.compile(config.patternFormat().replace("{at}", Pattern.quote(target))).matcher(rawMessage);
            if (matcher.find()) {
                component = component.replace(matcher.group(), format.replace("{target}", name));
            }
        }
        return component;
    }

    /*
     * 套用聊天格式配置，并替换玩家名和消息组件。
     */
    public static TextComponent formatMessage(Player player, MHDFToolsPlayer mhdfPlayer, TextComponent message) {
        ConfigSetting.Config.Format config = ConfigSetting.getInstance().getConfig().format();
        String format = config.enable() ? config.select(player).format() : "<{player}> {message}";
        String parsed = PlaceholderCompatibilityRegistry.getInstance().parseString(PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API, player, format);
        return ColorUtil.color(parsed)
                .replace("{player}", mhdfPlayer.getDisplayName())
                .replace("{message}", message);
    }

    /*
     * 处理可展示容器的占位词，并把容器内容缓存到 DisplayCache。
     */
    public static TextComponent applyShowableContainer(MHDFToolsPlayer player, TextComponent component, List<byte[]> cacheDataList, ConfigSetting.Config.Show config, DisplayCache.Type type, ItemStack[] contents) {
        if (!config.enable()) return component;

        int removeCache = Math.max(config.removeCache(), 1);
        DisplayCache.CacheEntry entry = ModuleMain.instance.getDisplayCache().putInventory(type, contents, removeCache);
        cacheDataList.add(DisplayCache.encode(entry, removeCache));
        TextComponent replacement = ColorUtil.color(config.format()
                .replace("{uuid}", entry.id())
                .replace("{player}", player.getDisplayName()));

        for (String word : config.word()) {
            component = component.replace(word, replacement);
        }
        return component;
    }

    /*
     * 根据配置选择普通包含匹配或正则匹配，返回命中的原始文本。
     */
    private static String findValue(String message, String word, boolean regex) {
        if (regex) {
            Matcher matcher = Pattern.compile(word).matcher(message);
            return matcher.find() ? matcher.group() : null;
        }
        return message.contains(word) ? word : null;
    }

    private ChatService() {
    }
}
