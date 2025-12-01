package cn.chengzhimeow.mhdftools.bukkit.util.imports;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.entity.database.data.huskhomes.HuskHomesHomeData;
import cn.chengzhimeow.mhdftools.bukkit.entity.database.data.huskhomes.HuskHomesPositionData;
import cn.chengzhimeow.mhdftools.bukkit.entity.database.data.huskhomes.HuskHomesPositionInfoData;
import cn.chengzhimeow.mhdftools.bukkit.entity.database.data.huskhomes.HuskHomesWarpData;
import cn.chengzhimeow.mhdftools.bukkit.manager.database.HuskHomesDatabaseManager;
import org.bukkit.command.CommandSender;

public final class HuskHomesImportUtil {
    /**
     * 导入HuskHomes的数据
     *
     * @param sender 命令执行者
     */
    public static void importHuskHomesData(CommandSender sender) {
        CCScheduler.getInstance().getAsyncScheduler().runTask(Main.instance, () -> {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.message.start")
                    .replace("{plugin}", "HuskHomes")
            );
            HuskHomesDatabaseManager databaseManager = new HuskHomesDatabaseManager();
            databaseManager.connect();
            databaseManager.initTable();

            // 导入家数据
            {
                if (ConfigSetting.getInstance().getData().getBoolean("homeSettings.enable")) {
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (HuskHomesHomeData homeData : databaseManager.getHomeDataManager().getList()) {
                        HuskHomesPositionInfoData positionInfoData = databaseManager.getPositionInfoDataManager().getById(homeData.getPositionInfoId());
                        HuskHomesPositionData positionData = databaseManager.getPositionDataManager().getById(positionInfoData.getPositionId());

                        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(homeData.getOwner());
                        player.setHome(positionInfoData.getName(), positionData.toBungeeCordLocation());
                    }

                    Long endTime = System.currentTimeMillis();
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "家系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入传送点数据
            {
                if (ConfigSetting.getInstance().getData().getBoolean("warpSettings.enable")) {
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "传送点系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (HuskHomesWarpData warpData : databaseManager.getWarpDataManager().getList()) {
                        HuskHomesPositionInfoData positionInfoData = databaseManager.getPositionInfoDataManager().getById(warpData.getPositionInfoId());
                        HuskHomesPositionData positionData = databaseManager.getPositionDataManager().getById(positionInfoData.getPositionId());

                        WarpData data = new WarpData(positionInfoData.getName());
                        data.setLocation(positionData.toBungeeCordLocation());
                        MHDFToolsAPI.getInstance().getWarpDataManager().update(data);
                    }

                    Long endTime = System.currentTimeMillis();
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "传送点系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            databaseManager.close();
            sender.sendMessage(LangSetting.getInstance().i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "HuskHomes")
            );
        });
    }
}
