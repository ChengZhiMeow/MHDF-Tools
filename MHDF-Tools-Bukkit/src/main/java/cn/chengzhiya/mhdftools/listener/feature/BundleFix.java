package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectBundleItem;

import java.util.List;

public final class BundleFix extends AbstractPacketListener {
    public BundleFix() {
        super(
                List.of("bundleFixSettings.enable"),
                PacketListenerPriority.LOW
        );
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.SELECT_BUNDLE_ITEM) {
            return;
        }

        if (Main.instance.getPluginHookManager().getPacketEventsHook()
                .getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_21)
        ) {
            return;
        }

        WrapperPlayClientSelectBundleItem wrapper = new WrapperPlayClientSelectBundleItem(event);
        if (wrapper.getSelectedItemIndex() >= -1) {
            return;
        }

        event.setCancelled(true);
    }
}
