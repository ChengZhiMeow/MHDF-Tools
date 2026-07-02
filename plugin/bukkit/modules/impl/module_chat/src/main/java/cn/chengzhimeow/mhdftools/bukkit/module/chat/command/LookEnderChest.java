package cn.chengzhimeow.mhdftools.bukkit.module.chat.command;

import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.menu.LookEnderChestMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class LookEnderChest extends Command {
    public LookEnderChest() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable() && ConfigSetting.getInstance().getConfig().showEnderChest().enable(),
                "展示末影箱",
                "mhdftools.commands.lookenderchest",
                true,
                ConfigSetting.getInstance().getConfig().showEnderChest().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().lookEnderChest().usage())
                    .replace("{command}", label));
            return;
        }

        DisplayCache.CacheEntry entry = ModuleMain.instance.getDisplayCache().get(args[0]);
        if (entry == null || entry.type() != DisplayCache.Type.ENDER_CHEST) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().lookEnderChest().noData());
            return;
        }

        new LookEnderChestMenu(sender, DisplayCache.readSlottedItems(entry)).openInventory();
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().lookEnderChest().message());
    }
}
