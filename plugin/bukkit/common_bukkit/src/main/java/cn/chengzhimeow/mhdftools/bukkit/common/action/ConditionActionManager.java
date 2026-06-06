package cn.chengzhimeow.mhdftools.bukkit.common.action;

import cn.chengzhimeow.ccaction.action.ActionBuilder;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConditionActionManager {
    @Getter(lazy = true)
    private static final ConditionActionManager instance = new ConditionActionManager();

    private ConditionActionManager() {
    }

    /**
     * 检查条件操作构造实例的参数合法性
     *
     * @param conditionAction 条件操作构造实例
     * @return 错误列表
     */
    public List<String> check(ConditionAction conditionAction) {
        List<String> errors = new ArrayList<>();
        errors.addAll(ConditionManager.getInstance().check(conditionAction.conditions()));
        errors.addAll(ActionManager.getInstance().check(conditionAction.actions()));
        return errors;
    }

    /**
     * 检查条件操作构造实例列表的参数合法性
     *
     * @param conditionActions 条件操作构造实例列表
     * @return 错误列表
     */
    public List<String> check(List<ConditionAction> conditionActions) {
        List<String> errors = new ArrayList<>();
        for (ConditionAction conditionAction : conditionActions) {
            errors.addAll(this.check(conditionAction));
        }
        return errors;
    }

    /**
     * 检查条件并执行操作
     *
     * @param player          玩家实例
     * @param conditionAction 条件操作实例
     * @param params          参数
     */
    public void actionWithCondition(Player player, ConditionAction conditionAction, Map<String, Object> params) {
        if (!ConditionManager.getInstance().condition(player, conditionAction.conditions(), params)) return;
        for (ActionBuilder.Builder action : conditionAction.actions()) {
            ActionManager.getInstance().action(player, action, new HashMap<>());
        }
    }

    /**
     * 获取条件操作构造实例
     *
     * @param data 配置实例
     * @return 条件操作构造实例
     */
    public ConditionAction getConditionActionFromConfig(ConfigurationSection data) {
        return new ConditionAction(
                ConditionManager.getInstance().getConditionListFromConfig(data.getConfigurationSectionList("conditions")),
                ActionManager.getInstance().getActionListFromConfig(data.getConfigurationSectionList("actions"))
        );
    }

    /**
     * 获取条件操作构造实例列表
     *
     * @param sections 配置实例列表
     * @return 条件操作构造实例列表
     */
    public List<ConditionAction> getConditionActionListFromConfig(List<ConfigurationSection> sections) {
        List<ConditionAction> list = new ArrayList<>();
        for (ConfigurationSection section : sections) {
            list.add(this.getConditionActionFromConfig(section));
        }
        return list;
    }

    /**
     * 获取条件操作构造实例列表
     *
     * @param parent 配置实例
     * @param path   配置位置
     * @return 条件操作构造实例列表
     */
    public List<ConditionAction> getConditionActionListFromConfig(ConfigurationSection parent, String path) {
        return this.getConditionActionListFromConfig(parent.getConfigurationSectionList(path));
    }
}
