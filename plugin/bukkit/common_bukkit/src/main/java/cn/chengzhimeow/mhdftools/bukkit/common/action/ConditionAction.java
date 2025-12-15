package cn.chengzhimeow.mhdftools.bukkit.common.action;

import cn.chengzhimeow.ccaction.action.ActionBuilder;
import cn.chengzhimeow.cccondition.condition.ConditionBuilder;

import java.util.List;

public record ConditionAction(
        List<ConditionBuilder.Builder> conditions,
        List<ActionBuilder.Builder> actions
) {
}
