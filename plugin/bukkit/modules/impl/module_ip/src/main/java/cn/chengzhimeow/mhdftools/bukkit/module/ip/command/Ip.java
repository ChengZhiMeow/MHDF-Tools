package cn.chengzhimeow.mhdftools.bukkit.module.ip.command;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

final class Ip extends Command {
    public Ip() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "查询IP信息",
                "mhdftools.commands.ip",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().ip().usage())
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        String ip = Objects.requireNonNull(player.getAddress()).getHostString();
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().ip().message()
                .replace("{player}", player.getName())
                .replace("{ip}", ip)
                .replace("{location}", this.getIpLocation(ip)));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        return super.tabCompleter(sender, label, args);
    }

    /**
     * 获取指定IP的归属地
     *
     * @param ip IP
     * @return 归属地
     */
    private String getIpLocation(String ip) {
        if (ip == null) return "未知";
        if (ip.startsWith("127.")) return "回环地址";

        try {
            URI uri = URI.create("https://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = uri.toURL().openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JSONObject json = JSON.parseObject(reader.readLine());
                JSONObject data = (JSONObject) json.getJSONArray("data").getFirst();
                return data.getString("location");
            }
        } catch (IOException e) {
            return "未知";
        }
    }
}
