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
import cn.chengzhiya.mhdftools.manager.database.impl.HuskHomesDatabaseManager;
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
            HuskHomesDatabaseManager huskHomesManager = new HuskHomesDatabaseManager();
            huskHomesManager.connect();
            huskHomesManager.initDao();

            // 导入家数据
            {
                if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "HuskHomes")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (HuskHomesHomeData huskHomesHomeData : huskHomesManager.getHuskHomesHomeDataList()) {
                        HuskHomesPositionInfoData huskHomesPositionInfoData = huskHomesManager.getHuskHomesPositionInfoData(huskHomesHomeData.getPositionInfoId());
                        HuskHomesPositionData huskHomesPositionData = huskHomesManager.getHuskHomesPositionData(huskHomesPositionInfoData.getPositionId());

                        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(huskHomesHomeData.getOwner());
                        player.setHome(huskHomesPositionInfoData.getName(), huskHomesPositionData.toBungeeCordLocation());
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

                    for (HuskHomesWarpData huskHomesWarpData : huskHomesManager.getHuskHomesWarpDataList()) {
                        HuskHomesPositionInfoData huskHomesPositionInfoData = huskHomesManager.getHuskHomesPositionInfoData(huskHomesWarpData.getPositionInfoId());
                        HuskHomesPositionData huskHomesPositionData = huskHomesManager.getHuskHomesPositionData(huskHomesPositionInfoData.getPositionId());

                        WarpData data = new WarpData(huskHomesPositionInfoData.getName());
                        data.setLocation(huskHomesPositionData.toBungeeCordLocation());
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

            huskHomesManager.close();
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "HuskHomes")
            );
        });
    }
}
