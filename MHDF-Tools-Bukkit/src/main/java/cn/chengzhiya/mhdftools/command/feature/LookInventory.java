package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.menu.feature.chat.LookInventoryMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class LookInventory extends AbstractCommand {
    public LookInventory() {
        super(
                List.of("chatSettings.enable", "chatSettings.showInventory.enable"),
                "展示背包",
                "mhdftools.commands.lookinventory",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("chatSettings.showInventory.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.lookinventory.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String data = Main.instance.getCacheManager().get("showInventory", args[0]);
        if (data == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.lookinventory.noData"));
            return;
        }

        new LookInventoryMenu(sender, JSONObject.parseObject(data)).openMenu();
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.lookinventory.message"));
    }
}
