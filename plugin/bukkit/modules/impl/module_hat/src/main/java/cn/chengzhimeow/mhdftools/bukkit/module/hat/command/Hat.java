package cn.chengzhimeow.mhdftools.bukkit.module.hat.command;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.hat.ModuleMain;
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
                ModuleMain.instance.getModuleConfigSetting().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            sender.sendMessage(ModuleMain.instance.getModuleLangSetting().i18n("commands.hat.noItem"));
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        sender.sendMessage(ModuleMain.instance.getModuleLangSetting().i18n("commands.hat.message"));
    }
}
