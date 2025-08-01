package cn.chengzhiya.mhdftools.util.feature;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public final class IpUtil {
    /**
     * 获取指定IP的归属地
     *
     * @param ip IP
     * @return 归属地
     */
    public static String getIpLocation(String ip) {
        if (ip == null) return "IP不存在!";
        if (ip.startsWith("127.0.")) return "回环地址";

        try {
            URL url = new URL("https://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JSONObject json = JSON.parseObject(reader.readLine());
                JSONObject data = (JSONObject) json.getJSONArray("data").get(0);
                return data.getString("location");
            }
        } catch (IOException e) {
            return "接口调用出现问题";
        }
    }
}
