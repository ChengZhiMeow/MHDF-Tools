package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public final class TimeAction extends AbstractTask {
    private final ConcurrentHashMap<String, Integer> delayHashMap = new ConcurrentHashMap<>();

    public TimeAction() {
        super(
                List.of("timeActionSettings.enable"),
                20L
        );
    }

    /**
     * 获取指定时间文本的时间数值
     *
     * @param time 时间文本
     * @return 时间数值
     */
    public static int getDelayTime(String time) {
        String[] data = time.split(":");
        int hour = Integer.parseInt(data[0]) * 3600;
        int minute = Integer.parseInt(data[1]) * 60;
        int second = Integer.parseInt(data[2]);
        return hour + minute + second;
    }

    @Override
    public void run() {
        ConfigurationSection actionList = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("timeActionSettings.actionList");
        if (actionList == null) {
            return;
        }

        for (String key : actionList.getKeys(false)) {
            ConfigurationSection action = actionList.getConfigurationSection(key);
            if (action == null) {
                continue;
            }

            String type = action.getString("type");
            if (type == null) {
                return;
            }

            String time = action.getString("time");
            if (time == null) {
                return;
            }

            switch (type) {
                case "定时操作" -> {
                    int delay = delayHashMap.getOrDefault(key, 0);

                    if (delay >= getDelayTime(time)) {
                        ActionUtil.runActionList(Bukkit.getConsoleSender(), action.getStringList("action"));
                        delayHashMap.remove(key);
                        return;
                    }

                    delayHashMap.put(key, delay + 1);
                }
                case "定点操作" -> {
                    String[] data = time.split(":");
                    int hour = Integer.parseInt(data[0]);
                    int minute = Integer.parseInt(data[1]);
                    int second = Integer.parseInt(data[2]);

                    LocalTime localTime = LocalTime.now();
                    if (localTime.getHour() == hour && localTime.getMinute() == minute && localTime.getSecond() == second) {
                        ActionUtil.runActionList(Bukkit.getConsoleSender(), action.getStringList("action"));
                    }
                }
            }
        }
    }
}
