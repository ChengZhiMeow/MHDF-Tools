package cn.chengzhiya.mhdftools.util.imports;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesHomeData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesPositionData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesPositionInfoData;
import cn.chengzhiya.mhdftools.entity.database.data.huskhomes.HuskHomesWarpData;
import cn.chengzhiya.mhdftools.manager.database.HuskHomesDatabaseManager;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.command.CommandSender;

public final class HuskHomesImportUtil {
    /**
     * 导入HuskHomes的数据
     *
     * @param sender 命令执行者
     */
    public static void importHuskHomesData(CommandSender sender) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.start")
                    .replace("{plugin}", "HuskHomes")
            );
            HuskHomesDatabaseManager databaseManager = new HuskHomesDatabaseManager();
            databaseManager.connect();
            databaseManager.initTable();

            // 导入家数据
            {
                if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (HuskHomesHomeData homeData : databaseManager.getHomeDataManager().getList()) {
                        HuskHomesPositionInfoData positionInfoData = databaseManager.getPositionInfoDataManager().getById(homeData.getPositionInfoId());
                        HuskHomesPositionData positionData = databaseManager.getPositionDataManager().getById(positionInfoData.getPositionId());

                        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(homeData.getOwner());
                        player.setHome(positionInfoData.getName(), positionData.toBungeeCordLocation());
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "家系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入传送点数据
            {
                if (ConfigUtil.getConfig().getBoolean("warpSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "传送点系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (HuskHomesWarpData warpData : databaseManager.getWarpDataManager().getList()) {
                        HuskHomesPositionInfoData positionInfoData = databaseManager.getPositionInfoDataManager().getById(warpData.getPositionInfoId());
                        HuskHomesPositionData positionData = databaseManager.getPositionDataManager().getById(positionInfoData.getPositionId());

                        WarpData data = new WarpData(positionInfoData.getName());
                        data.setLocation(positionData.toBungeeCordLocation());
                        MHDFToolsAPIHelper.getInstance().getWarpDataManager().update(data);
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "传送点系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            databaseManager.close();
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "HuskHomes")
            );
        });
    }
}
