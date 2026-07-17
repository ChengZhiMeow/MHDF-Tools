package cn.chengzhimeow.mhdftools.bukkit.common.action;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.action.AbstractAction;
import cn.chengzhimeow.ccaction.action.ActionBuilder;
import cn.chengzhimeow.ccaction.exception.ActionIllegalArgumentException;
import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.array.ArrayUtil;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.action.cast.ComponentCastManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ext.ChatActionImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ext.NextPageActionImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ext.PrevPageActionImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.action.pre.StringPlaceholderPreProcess;
import cn.chengzhimeow.mhdftools.exception.StackTraceUtil;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ActionManager {
    @Getter(lazy = true)
    private static final ActionManager instance = new ActionManager();
    private final CCAction ccAction;

    private ActionManager() {
        this.ccAction = new CCAction(MHDFToolsBukkit.getInstance());

        this.ccAction.getPreProcessRegistry().register(String.class, new StringPlaceholderPreProcess());

        this.ccAction.getCastRegistry().register(Component.class, new ComponentCastManagerImpl());

        this.ccAction.getActionRegistry().register("chat", ChatActionImpl.class);
        this.ccAction.getActionRegistry().register("prev_page", PrevPageActionImpl.class);
        this.ccAction.getActionRegistry().register("next_page", NextPageActionImpl.class);
    }

    /**
     * 注册操作实现类
     *
     * @param id          操作ID
     * @param actionClass 操作实现类
     */
    public void register(String id, Class<? extends AbstractAction> actionClass) {
        this.ccAction.getActionRegistry().register(id, actionClass);
    }

    /**
     * 检查操作构造实例的参数合法性
     *
     * @param action 操作构造实例
     * @return 错误列表
     */
    public List<String> check(ActionBuilder.Builder action) {
        List<String> errors = new ArrayList<>();
        try {
            action.build().check();
        } catch (ActionIllegalArgumentException e) {
            for (ActionIllegalArgumentException.ErrorKey error : e.errorKeys) {
                errors.add(switch (error.caused()) {
                    case NO_CAST_IMPLEMENTATION ->
                            "&c找不到参数" + ArrayUtil.join(error.key().keys(), 0, ",") + "的转换类实现!";
                    case CAST_ERROR ->
                            "&c转换参数" + ArrayUtil.join(error.key().keys(), 0, ",") + "时,遇到了一些问题!\n" + StackTraceUtil.stackTraceToString(error.e());
                    case NOT_FOUND ->
                            "&c在参数列表中找不到参数" + ArrayUtil.join(error.key().keys(), 0, ",") + ",参数数据: " + action.getParams();
                });
            }
        }
        return errors;
    }

    /**
     * 检查操作构造实例列表的参数合法性
     *
     * @param actions 操作构造实例列表
     * @return 错误列表
     */
    public List<String> check(List<ActionBuilder.Builder> actions) {
        List<String> errors = new ArrayList<>();
        for (ActionBuilder.Builder action : actions) {
            errors.addAll(this.check(action));
        }
        return errors;
    }

    /**
     * 执行操作
     *
     * @param player 玩家实例
     * @param action 操作构造实例
     * @param params 参数
     */
    public void action(Player player, ActionBuilder.Builder action, Map<String, Object> params) {
        ActionBuilder.Builder builder = action.clone();
        builder.getParams().putAll(params);
        if (player != null) {
            builder.getParams().put("user", player);
            builder.getParams().put("player", player);
            builder.getParams().put("placeholder_owner", player);
        }

        AbstractAction buildAction = builder.build();
        buildAction.init();

        if (player != null)
            CCScheduler.getInstance().getEntityScheduler().runTask(MHDFToolsBukkit.getInstance(), player, buildAction::action);
        else CCScheduler.getInstance().getGlobalRegionScheduler().runTask(MHDFToolsBukkit.getInstance(), buildAction::action);
    }

    public ActionBuilder.Builder getActionFromConfig(ConfigurationSection data) {
        String type = data.getString("type");

        Map<String, Object> params = new HashMap<>();
        for (String key : data.getKeys(true)) {
            if (key.equals("type")) continue;
            Object value = data.get(key);

            list_handle:
            if (value instanceof List<?> list) {
                if (list.isEmpty()) break list_handle;
                if (!(list.getFirst() instanceof ConfigurationSection)) break list_handle;

                List<ActionBuilder.Builder> subActions = new ArrayList<>();
                for (Object o : list) {
                    ConfigurationSection section = (ConfigurationSection) o;
                    subActions.add(this.getActionFromConfig(section));
                }
                value = subActions;
            }

            params.put(key, value);
        }

        return this.ccAction.getAction(type, params);
    }

    /**
     * 获取操作构造实例列表
     *
     * @param sections 配置实例列表
     * @return 操作构造实例列表
     */
    public List<ActionBuilder.Builder> getActionListFromConfig(List<ConfigurationSection> sections) {
        List<ActionBuilder.Builder> builders = new ArrayList<>();

        for (ConfigurationSection section : sections) {
            builders.add(this.getActionFromConfig(section));
        }

        return builders;
    }

    /**
     * 获取操作构造实例列表
     *
     * @param parent 配置实例
     * @param path   配置位置
     * @return 操作构造实例列表
     */
    public List<ActionBuilder.Builder> getActionListFromConfig(ConfigurationSection parent, String path) {
        return this.getActionListFromConfig(parent.getConfigurationSectionList(path));
    }
}
