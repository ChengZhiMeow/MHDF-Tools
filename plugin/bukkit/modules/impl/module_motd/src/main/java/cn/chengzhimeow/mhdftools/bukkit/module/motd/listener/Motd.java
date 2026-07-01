package cn.chengzhimeow.mhdftools.bukkit.module.motd.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.math.CalculateUtil;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.PacketListener;
import cn.chengzhimeow.mhdftools.bukkit.module.motd.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.motd.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public final class Motd extends PacketListener {
    public Motd() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                PacketListenerPriority.LOWEST
        );
    }

    @Override
    public void onPacketSend(@NotNull PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.RESPONSE) return;

        WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
        JsonObject data = packet.getComponent();
        ConfigSetting.Config config = ConfigSetting.getInstance().getConfig();

        ConfigSetting.Config.Version version = config.version();
        if (version.enable()) {
            JsonObject versionData = new JsonObject();
            String name = this.applyPlaceholder(version.name());
            versionData.addProperty("name", ColorUtil.color(name).toLegacyString());
            versionData.addProperty("protocol", 5835);
            data.add("version", versionData);
        }

        ConfigSetting.Config.Players players = config.players();
        if (players.enable()) {
            JsonObject playersData = data.getAsJsonObject("players");

            ConfigSetting.Config.Players.FakeAmount fakeOnline = players.fakeOnline();
            if (fakeOnline.enable()) {
                int online = (int) CalculateUtil.calculate(this.applyPlaceholder(fakeOnline.amount()));
                playersData.addProperty("online", online);
            }

            ConfigSetting.Config.Players.FakeAmount fakeMax = players.fakeMax();
            if (fakeMax.enable()) {
                int max = (int) CalculateUtil.calculate(this.applyPlaceholder(fakeMax.amount()));
                playersData.addProperty("max", max);
            }

            ConfigSetting.Config.Players.FakeSample fakeSample = players.fakeSample();
            if (fakeSample.enable()) {
                JsonArray sample = new JsonArray();
                for (String string : fakeSample.text()) {
                    JsonObject sampleData = new JsonObject();
                    sampleData.addProperty("name", ColorUtil.color(this.applyPlaceholder(string)).toLegacyString());
                    sampleData.addProperty("id", String.valueOf(UUID.randomUUID()));

                    sample.add(sampleData);
                }
                playersData.add("sample", sample);
            }

            data.add("players", playersData);
        }

        List<List<String>> descriptionList = config.description();
        {
            List<String> description = descriptionList.get(new Random().nextInt(descriptionList.size()));
            JsonArray descriptionData = new JsonArray();

            JsonObject nextLine = new JsonObject();
            nextLine.addProperty("text", "\n");
            nextLine.addProperty("color", "white");

            for (int i = 0; i < description.size(); i++) {
                TextComponent component = ColorUtil.color(description.get(i));
                JsonElement element = component.toJsonElement();
                if (element instanceof JsonObject jsonObject)
                    descriptionData.add(jsonObject);
                else if (element instanceof JsonArray jsonArray)
                    descriptionData.addAll(jsonArray);

                if (i >= description.size() - 1) descriptionData.add(nextLine);
            }

            data.add("description", descriptionData);
        }

        packet.setComponentJson(data.toString());
        event.setLastUsedWrapper(packet);
        event.markForReEncode(true);
    }

    private String applyPlaceholder(String text) {
        return Objects.requireNonNull(PlaceholderCompatibilityRegistry.getInstance().parseString(PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API, null, text))
                .replace("{online}", String.valueOf(BungeeCordManager.getInstance().getBukkitPlayerList().size()))
                .replace("{max}", String.valueOf(Bukkit.getMaxPlayers()));
    }
}
