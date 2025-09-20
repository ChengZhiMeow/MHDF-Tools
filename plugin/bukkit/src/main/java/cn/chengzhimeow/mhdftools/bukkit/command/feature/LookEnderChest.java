package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.chat.LookEnderChestMenu;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
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
                ConfigSetting.getSettingInstance().getData().getStringList("chatSettings.showEnderChest.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.lookenderchest.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String data = Main.instance.getCacheManager().get("showEnderChest", args[0]);
        if (data == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.lookenderchest.noData"));
            return;
        }

        new LookEnderChestMenu(sender, JSONObject.parseObject(data)).openMenu();
        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.lookenderchest.message"));
    }
}
