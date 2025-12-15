package cn.chengzhimeow.mhdftools.bukkit.common.action.ext;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.action.AbstractAction;
import cn.chengzhimeow.ccaction.action.ArgumentKey;
import org.bukkit.entity.Player;

import java.util.Map;

public final class ChatActionImpl extends AbstractAction {
    @ArgumentKey(keys = {"player"}, disabledCheck = true)
    private Player player;
    @ArgumentKey(keys = {"message", "msg"})
    private String message;

    public ChatActionImpl(CCAction ccAction, Map<String, Object> params) {
        super(ccAction, params);
    }

    @Override
    protected void onAction() {
        this.player.chat(this.message);
    }
}
