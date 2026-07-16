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

final class Hat extends Command {
    public Hat() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().hat().description(),
                "mhdftools.commands.hat",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().hat().usage())
                    .replace("{command}", label));
            return;
        }

        ItemStack oldHelmet = sender.getInventory().getHelmet();
        ItemStack handItem = sender.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().hat().noItem());
            return;
        }

        sender.getInventory().setItemInMainHand(oldHelmet);
        sender.getInventory().setHelmet(handItem);

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().hat().message());
    }
}
