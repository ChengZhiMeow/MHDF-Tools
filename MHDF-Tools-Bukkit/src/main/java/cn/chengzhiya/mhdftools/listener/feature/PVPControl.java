package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PVPControl extends AbstractListener {
    public PVPControl() {
        super(
                List.of("pvpSettings.enable")
        );
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damaged) {

            Entity damagerEntity = event.getDamager();
            Player attacker = null;

            if (damagerEntity instanceof Player) {
                attacker = (Player) damagerEntity;
            } else if (damagerEntity instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Player) {
                    attacker = (Player) projectile.getShooter();
                }
            }

            if (attacker != null) {
                String value = Main.instance.getCacheManager().get("pvp", damaged.getName());
                String value2 = Main.instance.getCacheManager().get("pvp", attacker.getName());
                if (value != null) {
                    boolean damagedDisabled = value.contains("true");
                    if (damagedDisabled) {
                        event.setCancelled(true);
                    }
                }
                if (value2 != null) {
                    boolean attackerDisabled = value2.contains("true");
                    if (attackerDisabled) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}