package cn.chengzhimeow.mhdftools.bukkit.common.condition.cast;

import cn.chengzhimeow.cccondition.CCCondition;
import cn.chengzhimeow.cccondition.exception.CastException;
import cn.chengzhimeow.cccondition.manager.CastManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;

public final class ComponentCastManagerImpl implements CastManager {
    public Object cast(CCCondition ccCondition, Object value, Class<?> type) throws CastException {
        if (value instanceof String s) {
            return ColorUtil.color(s);
        } else {
            throw new CastException(value, type);
        }
    }
}
