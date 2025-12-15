package cn.chengzhimeow.mhdftools.bukkit.module.timeaction.task;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import cn.chengzhimeow.mhdftools.bukkit.module.timeaction.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.timeaction.config.ConfigSetting;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class TimeAction extends Task {
    private final Map<String, Integer> delays = new ConcurrentHashMap<>();

    public TimeAction() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                20
        );
    }

    private int timeStringToTime(String time) {
        String[] data = time.split(":");
        int hour = Integer.parseInt(data[0]) * 3600;
        int minute = Integer.parseInt(data[1]) * 60;
        int second = Integer.parseInt(data[2]);
        return hour + minute + second;
    }

    @Override
    public void run() {
        ConfigurationSection list = ConfigSetting.getInstance().getData().getConfigurationSection("list");
        if (list == null) return;

        for (String key : list.getKeys(false)) {
            ConfigurationSection action = list.getConfigurationSection(key);
            if (action == null) continue;

            String type = action.getString("type");
            if (type == null) continue;

            String time = action.getString("time");
            if (time == null) continue;

            switch (type) {
                case "定时操作" -> {
                    int delay = this.delays.getOrDefault(key, 0);

                    if (delay >= this.timeStringToTime(time)) {
                        this.delays.put(key, delay + 1);
                        continue;
                    }

                    List<ConditionAction> conditionActions = ConditionActionManager.getInstance().getConditionActionListFromConfig(action, "action");
                    ConditionActionManager.getInstance().actionWithCondition(null, conditionActions);
                    this.delays.remove(key);
                }
                case "定点操作" -> {
                    LocalTime localTime = LocalTime.now();
                    String[] data = time.split(":");
                    int hour = Integer.parseInt(data[0]);
                    int minute = Integer.parseInt(data[1]);
                    int second = Integer.parseInt(data[2]);
                    if (localTime.getHour() != hour) continue;
                    if (localTime.getMinute() != minute) continue;
                    if (localTime.getSecond() != second) continue;

                    List<ConditionAction> conditionActions = ConditionActionManager.getInstance().getConditionActionListFromConfig(action, "action");
                    ConditionActionManager.getInstance().actionWithCondition(null, conditionActions);
                }
            }
        }
    }
}
