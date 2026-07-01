package cn.chengzhimeow.mhdftools.bukkit.common.action.cast;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.exception.CastException;
import cn.chengzhimeow.ccaction.manager.CastManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;

public final class ComponentCastManagerImpl implements CastManager {
    public Object cast(CCAction ccAction, Object value, Class<?> type) throws CastException {
        if (value instanceof String s) {
            return ColorUtil.color(s);
        } else {
            throw new CastException(value, type);
        }
    }
}
