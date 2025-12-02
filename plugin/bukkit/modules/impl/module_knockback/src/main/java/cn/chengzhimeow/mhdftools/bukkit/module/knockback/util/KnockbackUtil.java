package cn.chengzhimeow.mhdftools.bukkit.module.knockback.util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public final class KnockbackUtil {
    private static final Random random = new Random();

    /**
     * 击退指定玩家实例
     *
     * @param player 玩家实例
     * @param type   击退类型
     * @param vector 击退向量
     * @return 击退类型是否存在
     */
    public static boolean knockbackPlayer(Player player, String type, Vector vector) {
        switch (type) {
            case "normal" -> player.setVelocity(vector);
            case "random" -> {
                double theta = KnockbackUtil.random.nextDouble() * 2 * Math.PI;
                double phi = KnockbackUtil.random.nextDouble() * Math.PI;
                double randX = Math.sin(phi) * Math.cos(theta);
                double randY = Math.sin(phi) * Math.sin(theta);
                double randZ = Math.cos(phi);
                player.setVelocity(new Vector(randX, randY, randZ).multiply(vector.length()));
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}
