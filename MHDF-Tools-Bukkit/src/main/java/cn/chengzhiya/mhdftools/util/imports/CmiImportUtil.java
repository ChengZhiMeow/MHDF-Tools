package cn.chengzhiya.mhdftools.util.imports;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.entity.database.data.cmi.CmiUserData;
import cn.chengzhiya.mhdftools.manager.database.CmiDatabaseManager;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.plugin.CmiConfigUtil;
import org.bukkit.command.CommandSender;

public final class CmiImportUtil {
    /**
     * 将CMI位置数据转换为群组位置实例
     *
     * @param data CMI位置数据
     * @return 群组位置实例
     */
    private static BungeeCordLocation cmiLocationToBungeeCordLocation(String[] data) {
        return new BungeeCordLocation(
                data[0],
                Double.parseDouble(data[1]),
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3]),
                Float.parseFloat(data[4]),
                Float.parseFloat(data[5])
        );
    }

    /**
     * 导入Cmi的数据
     *
     * @param sender 命令执行者
     */
    public static void importCmiData(CommandSender sender) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.message.start")
                    .replace("{plugin}", "Cmi")
            );
            CmiDatabaseManager databaseManager = new CmiDatabaseManager();
            databaseManager.connect();
            databaseManager.initTable();

            // 导入家数据
            {
                if (Main.instance.getConfigManager().getConfigManager().getData().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (CmiUserData cmiUserData : databaseManager.getUserDataManager().getList()) {
                        String[] homeList = cmiUserData.getHomes()
                                .replaceAll("\\$-0%%", ":")
                                .split(";");
                        for (String home : homeList) {
                            int split = home.indexOf(":");
                            String name = home.substring(0, split);
                            String location = home.substring(split);

                            MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(cmiUserData.getPlayerUuid());
                            player.setHome(name, cmiLocationToBungeeCordLocation(location.split(":")));
                        }
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入传送点数据
            {
                if (Main.instance.getConfigManager().getConfigManager().getData().getBoolean("warpSettings.enable")) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "传送点系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (String name : CmiConfigUtil.getWarpConfig().getKeys(false)) {
                        String location = CmiConfigUtil.getWarpConfig().getString(name + ".Location");
                        if (location == null) {
                            continue;
                        }

                        WarpData data = new WarpData(name);
                        data.setLocation(cmiLocationToBungeeCordLocation(location.split(";")));
                        MHDFToolsAPIHelper.getInstance().getWarpDataManager().update(data);
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "传送点系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入经济数据
            {
                if (Main.instance.getConfigManager().getConfigManager().getData().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "经济系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (CmiUserData cmiUserData : databaseManager.getUserDataManager().getList()) {
                        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(cmiUserData.getPlayerUuid());
                        player.setMoney(cmiUserData.getBalance());
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "经济系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            databaseManager.close();
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "Cmi")
            );
        });
    }
}
