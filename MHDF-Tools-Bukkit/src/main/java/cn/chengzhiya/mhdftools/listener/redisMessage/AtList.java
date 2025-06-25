package cn.chengzhiya.mhdftools.listener.redisMessage;

import cn.chengzhiya.mhdftools.listener.AbstractRedisMessageListener;
import cn.chengzhiya.mhdftools.util.feature.AtUtil;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AtList extends AbstractRedisMessageListener {
    public AtList() {
        super(
                List.of("chatSettings.enable", "chatSettings.at.enable"),
                "atList"
        );
    }

    @Override
    public void onMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);

        Set<String> atList = new HashSet<>(data.getList("atList", String.class));
        String by = data.getString("by");
        AtUtil.atList(atList, by);
    }
}
