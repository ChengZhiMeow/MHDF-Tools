package cn.chengzhiya.mhdftools.util.action;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.menu.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public final class RequirementUtil {
    /**
     * 检测指定玩家是否满足指定条件条件配置下的所有条件
     *
     * @param player 玩家实例
     * @param config 条件配置实例
     * @return 不满足条件时的操作(如果为空则为全部满足)
     */
    public static List<String> checkRequirements(Player player, ConfigurationSection config) {
        if (config == null) return new ArrayList<>();

        List<String> keys = new ArrayList<>(config.getKeys(false));
        for (String key : keys) {
            ConfigurationSection requirement = config.getConfigurationSection(key);
            if (requirement == null) {
                continue;
            }

            String type = requirement.getString("type");
            if (type == null) {
                continue;
            }

            switch (type) {
                case ">" -> {
                    String input = requirement.getString("input");
                    int output = requirement.getInt("output");
                    if (input == null) {
                        continue;
                    }

                    int input_value = Integer.parseInt(
                            Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, input)
                    );

                    if (input_value > output) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case ">=" -> {
                    String input = requirement.getString("input");
                    int output = requirement.getInt("output");
                    if (input == null) {
                        continue;
                    }

                    int input_value = Integer.parseInt(
                            Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, input)
                    );

                    if (input_value >= output) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case "==" -> {
                    String input = requirement.getString("input");
                    int output = requirement.getInt("output");
                    if (input == null) {
                        continue;
                    }

                    int input_value = Integer.parseInt(
                            Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, input)
                    );

                    if (input_value == output) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case "<" -> {
                    String input = requirement.getString("input");
                    int output = requirement.getInt("output");
                    if (input == null) {
                        continue;
                    }

                    int input_value = Integer.parseInt(
                            Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, input)
                    );

                    if (input_value < output) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case "<=" -> {
                    String input = requirement.getString("input");
                    int output = requirement.getInt("output");
                    if (input == null) {
                        continue;
                    }

                    int input_value = Integer.parseInt(
                            Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, input)
                    );

                    if (input_value <= output) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case "permission" -> {
                    String permission = requirement.getString("permission");
                    if (permission == null) {
                        continue;
                    }

                    if (player.hasPermission(permission)) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
                case "hasItem" -> {
                    ConfigurationSection item = requirement.getConfigurationSection("item");
                    if (item == null) {
                        continue;
                    }

                    ItemStack itemStack = ItemStackUtil.getItemStackBuilder(player, item).build();
                    if (player.getInventory().contains(itemStack)) {
                        continue;
                    }

                    return requirement.getStringList("denyActions");
                }
            }
        }

        return new ArrayList<>();
    }
}
