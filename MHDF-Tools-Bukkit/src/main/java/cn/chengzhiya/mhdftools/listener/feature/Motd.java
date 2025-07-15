package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
import cn.chengzhiya.mhdftools.util.feature.MotdUtil;
import cn.chengzhiya.mhdftools.util.math.MathUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Motd extends AbstractPacketListener {
    public Motd() {
        super(
                List.of("motdSettings.enable"),
                PacketListenerPriority.NORMAL
        );
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.RESPONSE) {
            return;
        }

        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("motdSettings");
        if (config == null) {
            return;
        }

        WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
        JsonObject data = packet.getComponent();

        ConfigurationSection version = config.getConfigurationSection("version");
        if (version != null && version.getBoolean("enable")) {
            JsonObject versionData = new JsonObject();

            String name = applyPlaceholder(version.getString("name", ""));
            versionData.addProperty("name", ColorUtil.color(name).toLegacyString());
            versionData.addProperty("protocol", 5835);

            data.add("version", versionData);
        }

        ConfigurationSection players = config.getConfigurationSection("players");
        if (players != null && players.getBoolean("enable")) {
            JsonObject playersData = new JsonObject();

            ConfigurationSection fakePlayers = players.getConfigurationSection("fakePlayers");
            int online = getAmount(fakePlayers, Main.instance.getBungeeCordManager().getBukkitPlayerList().size());

            ConfigurationSection fakeMax = players.getConfigurationSection("fakeMax");
            int max = getAmount(fakeMax, Bukkit.getMaxPlayers());

            JsonArray sample = new JsonArray();

            ConfigurationSection fakeSample = players.getConfigurationSection("fakeSample");
            if (fakeSample != null && fakeSample.getBoolean("enable")) {
                for (String string : fakeSample.getStringList("text")) {
                    JsonObject sampleData = new JsonObject();
                    sampleData.addProperty("name", ColorUtil.color(applyPlaceholder(string)).toLegacyString());
                    sampleData.addProperty("id", String.valueOf(UUID.randomUUID()));

                    sample.add(sampleData);
                }
            }

            playersData.addProperty("online", online);
            playersData.addProperty("max", max);
            playersData.add("sample", sample);

            data.add("players", playersData);
        }

        List<YamlConfiguration> descriptionList = Main.instance.getConfigManager().getConfigManager().getYamlConfigurationList("motdSettings.description");
        {
            YamlConfiguration description = descriptionList.get(new Random().nextInt(descriptionList.size()));
            JsonArray descriptionData = new JsonArray();

            String line1 = applyPlaceholder(description.getString("line1", ""));
            descriptionData.addAll(MotdUtil.getMessageJsonArray(ColorUtil.color(line1)));

            JsonObject nextLine = new JsonObject();
            nextLine.addProperty("text", "\n");
            nextLine.addProperty("color", "white");

            descriptionData.add(nextLine);

            String line2 = applyPlaceholder(description.getString("line2", ""));
            descriptionData.addAll(MotdUtil.getMessageJsonArray(ColorUtil.color(line2)));

            data.add("description", descriptionData);
        }

        packet.setComponentJson(data.toString());

        event.setLastUsedWrapper(packet);
        event.markForReEncode(true);
    }

    private int getAmount(ConfigurationSection config, int defaultAmount) {
        if (config != null && config.getBoolean("enable")) {
            String amount = config.getString("amount");
            if (amount != null) {
                return (int) MathUtil.calculate(applyPlaceholder(amount));
            }
        }
        return defaultAmount;
    }

    private String applyPlaceholder(String text) {
        return Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(null, text)
                .replace("{online}", String.valueOf(Main.instance.getBungeeCordManager().getBukkitPlayerList().size()))
                .replace("{max}", String.valueOf(Bukkit.getMaxPlayers()));
    }
}
