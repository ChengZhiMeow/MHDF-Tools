package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Hat extends Command {
    public Hat() {
        super(
                List.of("hatSettings.enable"),
                "帽子",
                "mhdftools.commands.hat",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("hatSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.hat.noItem"));
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.hat.message"));
    }
}
