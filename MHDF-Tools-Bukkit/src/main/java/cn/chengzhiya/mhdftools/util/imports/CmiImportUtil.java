package cn.chengzhiya.mhdftools.util.imports;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.data.EconomyData;
import cn.chengzhiya.mhdftools.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.entity.database.data.cmi.CmiUserData;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.manager.database.impl.CmiDatabaseManager;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.plugin.CmiConfigUtil;
import cn.chengzhiya.mhdftools.util.database.EconomyDataUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import cn.chengzhiya.mhdftools.util.database.WarpDataUtil;
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
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.start")
                    .replace("{plugin}", "Cmi")
            );
            CmiDatabaseManager cmiDatabaseManager = new CmiDatabaseManager();
            cmiDatabaseManager.connect();
            cmiDatabaseManager.initDao();

            // 导入家数据
            {
                if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (CmiUserData cmiUserData : cmiDatabaseManager.getCmiUserDataList()) {
                        String[] homeList = cmiUserData.getHomes()
                                .replaceAll("\\$-0%%", ":")
                                .split(";");
                        for (String home : homeList) {
                            int split = home.indexOf(":");
                            String name = home.substring(0, split);
                            String location = home.substring(split);

                            HomeData homeData = new HomeData();
                            homeData.setHome(name);
                            homeData.setPlayer(cmiUserData.getPlayerUuid());
                            homeData.setLocation(cmiLocationToBungeeCordLocation(location.split(":")));

                            HomeDataUtil.updateHomeData(homeData);
                        }
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "家系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入传送点数据
            {
                if (ConfigUtil.getConfig().getBoolean("warpSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "传送点系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (String name : CmiConfigUtil.getWarpConfig().getKeys(false)) {
                        String location = CmiConfigUtil.getWarpConfig().getString(name + ".Location");
                        if (location == null) {
                            continue;
                        }

                        WarpData warpData = new WarpData();
                        warpData.setWarp(name);
                        warpData.setLocation(cmiLocationToBungeeCordLocation(location.split(";")));

                        WarpDataUtil.updateWarpData(warpData);
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "传送点系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            // 导入经济数据
            {
                if (ConfigUtil.getConfig().getBoolean("homeSettings.enable")) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.start")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "经济系统")
                    );
                    Long startTime = System.currentTimeMillis();

                    for (CmiUserData cmiUserData : cmiDatabaseManager.getCmiUserDataList()) {
                        EconomyData economyData = new EconomyData();
                        economyData.setPlayer(cmiUserData.getPlayerUuid());
                        economyData.setBigDecimal(cmiUserData.getBalance());

                        EconomyDataUtil.updateEconomyData(economyData);
                    }

                    Long endTime = System.currentTimeMillis();
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.import.done")
                            .replace("{plugin}", "Cmi")
                            .replace("{name}", "经济系统")
                            .replace("{time}", String.valueOf(endTime - startTime))
                    );
                }
            }

            cmiDatabaseManager.close();
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.mhdftools.subCommands.import.message.done")
                    .replace("{plugin}", "Cmi")
            );
        });
    }
}
