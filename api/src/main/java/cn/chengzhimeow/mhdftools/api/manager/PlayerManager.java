package cn.chengzhimeow.mhdftools.api.manager;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerManager {
    /**
     * 获取指定玩家UUID的梦之工具玩家实例
     *
     * @param uuid 玩家UUID
     * @param name 玩家名称(用于创建默认值)
     * @return 梦之工具玩家实例
     */
    MHDFToolsPlayer getPlayer(@NotNull UUID uuid, @Nullable String name);

    /**
     * 获取指定玩家UUID的梦之工具玩家实例
     *
     * @param uuid 玩家UUID
     * @return 梦之工具玩家实例
     */
    default MHDFToolsPlayer getPlayer(@NotNull UUID uuid) {
        return this.getPlayer(uuid, null);
    }

    /**
     * 获取指定玩家名称的梦之工具玩家实例
     *
     * @param name 玩家名称
     * @return 梦之工具玩家实例
     */
    MHDFToolsPlayer getPlayer(String name);
}
