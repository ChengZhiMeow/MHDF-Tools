package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.menu.feature.chat.LookEnderChestMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class LookEnderChest extends Command {
    public LookEnderChest() {
        super(
                List.of("chatSettings.enable", "chatSettings.showEnderChest.enable"),
                "展示末影箱",
                "mhdftools.commands.lookenderchest",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("chatSettings.showEnderChest.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.lookenderchest.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String data = Main.instance.getCacheManager().get("showEnderChest", args[0]);
        if (data == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.lookenderchest.noData"));
            return;
        }

        new LookEnderChestMenu(sender, JSONObject.parseObject(data)).openMenu();
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.lookenderchest.message"));
    }
}
