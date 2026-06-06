package cn.chengzhimeow.mhdftools.bukkit.module.bugfix.listener.crash;

import cn.chengzhimeow.mhdftools.bukkit.compatibility.packetevents.PacketEventsManager;
import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectBundleItem;

final class BundleCrash extends PacketListener {
    public BundleCrash() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().crash().bundle().enable(),
                PacketListenerPriority.LOWEST
        );
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.SELECT_BUNDLE_ITEM) return;

        if (PacketEventsManager.getInstance().getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_21)) return;
        WrapperPlayClientSelectBundleItem wrapper = new WrapperPlayClientSelectBundleItem(event);
        if (wrapper.getSelectedItemIndex() >= -1) return;

        event.setCancelled(true);
    }
}
