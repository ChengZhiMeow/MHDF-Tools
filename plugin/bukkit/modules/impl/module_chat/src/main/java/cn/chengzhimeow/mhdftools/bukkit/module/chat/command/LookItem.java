package cn.chengzhimeow.mhdftools.bukkit.module.chat.command;

import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.cache.DisplayCache;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class LookItem extends Command {
    public LookItem() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable() && ConfigSetting.getInstance().getConfig().showItem().enable(),
                "展示物品",
                "mhdftools.commands.lookitem",
                true,
                ConfigSetting.getInstance().getConfig().showItem().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().lookItem().usage())
                    .replace("{command}", label));
            return;
        }

        DisplayCache.CacheEntry entry = ModuleMain.instance.getDisplayCache().get(args[0]);
        if (entry == null || entry.type() != DisplayCache.Type.ITEM) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().lookItem().noData());
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 9, ColorUtil.color(LangSetting.getInstance().getConfig().commands().lookItem().title()));
        ItemStack item = DisplayCache.readItem(entry);
        if (item == null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().lookItem().noData());
            return;
        }

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, item);
        }
        sender.openInventory(inventory);
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().lookItem().message());
    }
}
