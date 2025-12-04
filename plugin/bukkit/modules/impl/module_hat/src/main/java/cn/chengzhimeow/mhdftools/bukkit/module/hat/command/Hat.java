package cn.chengzhimeow.mhdftools.bukkit.module.hat.command;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.hat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.hat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.hat.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Hat extends Command {
    public Hat() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                "帽子",
                "mhdftools.commands.hat",
                true,
                ConfigSetting.getInstance().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.hat.usage"))
                    .replace("{command}", label));
            return;
        }

        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.hat.no_item"));
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        sender.sendMessage(LangSetting.getInstance().i18n("commands.hat.message"));
    }
}