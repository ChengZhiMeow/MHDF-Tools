package cn.chengzhimeow.mhdftools.bukkit.module.motd.listener;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
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
                List.of("enable"),
                PacketListenerPriority.LOWEST
        );
    }

    @Override
    public void onPacketSend(@NotNull PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.RESPONSE) return;

        WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
        JsonObject data = packet.getComponent();

        // 版本信息
        ConfigurationSection version = ConfigSetting.getInstance().getData().getConfigurationSection("version");
        if (version != null && version.getBoolean("enable")) {
            JsonObject versionData = new JsonObject();
            String name = this.applyPlaceholder(version.getString("name", ""));
            versionData.addProperty("name", ColorUtil.color(name).toLegacyString());
            versionData.addProperty("protocol", 5835);
            data.add("version", versionData);
        }

        // 玩家列表
        ConfigurationSection players = ConfigSetting.getInstance().getData().getConfigurationSection("players");
        if (players != null && players.getBoolean("enable")) {
            JsonObject playersData = data.getAsJsonObject("players");

            // 假在线人数
            ConfigurationSection fakeOnline = players.getConfigurationSection("fake_online");
            if (fakeOnline != null && fakeOnline.getBoolean("enable")) {
                int online = (int) CalculateUtil.calculate(this.applyPlaceholder(fakeOnline.getString("amount")));
                playersData.addProperty("online", online);
            }

            // 假最大人数
            ConfigurationSection fakeMax = players.getConfigurationSection("fake_max");
            if (fakeMax != null && fakeMax.getBoolean("enable")) {
                int max = (int) CalculateUtil.calculate(this.applyPlaceholder(fakeMax.getString("amount")));
                playersData.addProperty("max", max);
            }

            // 假玩家列表
            ConfigurationSection fakeSample = players.getConfigurationSection("fakeSample");
            if (fakeSample != null && fakeSample.getBoolean("enable")) {
                JsonArray sample = new JsonArray();
                for (String string : fakeSample.getStringList("text")) {
                    JsonObject sampleData = new JsonObject();
                    sampleData.addProperty("name", ColorUtil.color(this.applyPlaceholder(string)).toLegacyString());
                    sampleData.addProperty("id", String.valueOf(UUID.randomUUID()));

                    sample.add(sampleData);
                }
                playersData.add("sample", sample);
            }

            data.add("players", playersData);
        }

        // MOTD内容
        // noinspection rawtypes
        List<List> descriptionList = ConfigSetting.getInstance().getData().getList("description", List.class);
        {
            // noinspection unchecked
            List<String> description = (List<String>) descriptionList.get(new Random().nextInt(descriptionList.size()));
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
