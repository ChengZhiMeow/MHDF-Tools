package cn.chengzhimeow.mhdftools.bukkit.common.condition;

import cn.chengzhimeow.cccondition.CCCondition;
import cn.chengzhimeow.cccondition.condition.AbstractCondition;
import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.cccondition.exception.ConditionIllegalArgumentException;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.array.ArrayUtil;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.exception.StackTraceUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConditionManager {
    @Getter(lazy = true)
    private static final ConditionManager instance = new ConditionManager();
    private final CCCondition ccCondition;

    private ConditionManager() {
        this.ccCondition = new CCCondition(MHDFToolsBukkit.getInstance());
    }

    /**
     * 检查条件构造实例的参数合法性
     *
     * @param condition 条件构造实例
     * @return 错误列表
     */
    public List<String> check(ConditionBuilder.Builder condition) {
        List<String> errors = new ArrayList<>();
        try {
            condition.build().check();
        } catch (ConditionIllegalArgumentException e) {
            for (ConditionIllegalArgumentException.ErrorKey error : e.errorKeys) {
                errors.add(switch (error.caused()) {
                    case NO_CAST_IMPLEMENTATION ->
                            "&c找不到参数" + ArrayUtil.join(error.key().keys(), 0, ",") + "的转换类实现!";
                    case CAST_ERROR ->
                            "&c转换参数" + ArrayUtil.join(error.key().keys(), 0, ",") + "时,遇到了一些问题!\n" + StackTraceUtil.stackTraceToString(error.e());
                    case NOT_FOUND ->
                            "&c在参数列表中找不到参数" + ArrayUtil.join(error.key().keys(), 0, ",") + ",参数数据: " + condition.getParams();
                });
            }
        }
        return errors;
    }

    /**
     * 检查条件构造实例列表的参数合法性
     *
     * @param conditions 条件构造实例列表
     * @return 错误列表
     */
    public List<String> check(List<ConditionBuilder.Builder> conditions) {
        List<String> errors = new ArrayList<>();
        for (ConditionBuilder.Builder condition : conditions) {
            errors.addAll(this.check(condition));
        }
        return errors;
    }

    /**
     * 检查条件
     *
     * @param player    玩家实例
     * @param condition 条件构造实例
     */
    public boolean condition(Player player, ConditionBuilder.Builder condition) {
        ConditionBuilder.Builder builder = condition.clone();
        builder.getParams().put("user", player);
        builder.getParams().put("player", player);
        builder.getParams().put("placeholder_owner", player);

        AbstractCondition buildAction = builder.build();
        buildAction.init();
        return buildAction.checkCondition();
    }

    /**
     * 检查条件
     *
     * @param player     玩家实例
     * @param conditions 条件构造实例列表
     */
    public boolean condition(Player player, List<ConditionBuilder.Builder> conditions) {
        for (ConditionBuilder.Builder condition : conditions) {
            if (!this.condition(player, condition)) return false;
        }
        return true;
    }

    /**
     * 获取条件构造实例
     *
     * @param data 配置实例
     * @return 条件构造实例
     */
    public ConditionBuilder.Builder getConditionFromConfig(ConfigurationSection data) {
        String type = data.getString("type");

        Map<String, Object> params = new HashMap<>();
        for (String key : data.getKeys(true)) {
            if (key.equals("type")) continue;
            Object value = data.get(key);

            list_handle:
            if (value instanceof List<?> list) {
                if (list.isEmpty()) break list_handle;
                if (!(list.getFirst() instanceof ConfigurationSection)) break list_handle;

                List<ConditionBuilder.Builder> subConditions = new ArrayList<>();
                for (Object o : list) {
                    ConfigurationSection section = (ConfigurationSection) o;
                    subConditions.add(this.getConditionFromConfig(section));
                }
                value = subConditions;
            }

            params.put(key, value);
        }

        return this.ccCondition.getCondition(type, params);
    }

    /**
     * 获取条件构造实例列表
     *
     * @param sections 配置实例列表
     * @return 条件构造实例列表
     */
    public List<ConditionBuilder.Builder> getConditionListFromConfig(List<ConfigurationSection> sections) {
        List<ConditionBuilder.Builder> builders = new ArrayList<>();

        for (ConfigurationSection section : sections) {
            builders.add(this.getConditionFromConfig(section));
        }

        return builders;
    }

    /**
     * 获取条件构造实例列表
     *
     * @param parent 配置实例
     * @param path   配置位置
     * @return 条件构造实例列表
     */
    public List<ConditionBuilder.Builder> getConditionListFromConfig(ConfigurationSection parent, String path) {
        return this.getConditionListFromConfig(parent.getConfigurationSectionList(path));
    }
}
