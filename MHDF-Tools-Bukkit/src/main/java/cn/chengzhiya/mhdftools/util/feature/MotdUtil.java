package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.text.TextComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class MotdUtil {
    /**
     * 将字符串实例转换为json数组实例
     *
     * @param message 字符串实例
     * @return json数组实例
     */
    public static JsonArray getMessageJsonArray(TextComponent message) {
        JsonArray jsonArray = new JsonArray();

        for (Component component : message.children()) {
            TextComponent textComponent = new TextComponent(component);

            String content = textComponent.content();
            if (!content.isEmpty()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("text", content);

                TextColor color = textComponent.color();
                if (color != null) {
                    jsonObject.addProperty("color", color.asHexString());
                } else {
                    jsonObject.addProperty("color", "white");
                }

                for (TextDecoration decoration : textComponent.decorations().keySet()) {
                    TextDecoration.State status = textComponent.decorations().get(decoration);
                    if (status == TextDecoration.State.TRUE) {
                        jsonObject.addProperty(decoration.name(), true);
                    }
                }

                jsonArray.add(jsonObject);
            }


            if (!textComponent.children().isEmpty()) {
                jsonArray.addAll(MotdUtil.getMessageJsonArray(textComponent));
            }
        }

        return jsonArray;
    }
}
