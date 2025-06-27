package cn.chengzhiya.mhdftools.redismessagelistener;

public interface RedisMessageListener {
    /**
     * 当收到redis消息的时候
     *
     * @param message 消息
     */
    void onMessage(String message);
}
