package cn.chengzhimeow.mhdftools.text;

import cn.chengzhimeow.mhdftools.message.ColorUtil;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.AbstractComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class TextComponent extends AbstractComponent implements net.kyori.adventure.text.TextComponent {
    private String content;

    public TextComponent(@NotNull List<? extends ComponentLike> children, @NotNull Style style, @NotNull String content) {
        super(children, style);
        this.content = content;
    }

    public TextComponent(@NotNull List<? extends ComponentLike> children, @NotNull Style style) {
        this(children, style, "");
    }

    public TextComponent(String content) {
        this(Collections.emptyList(), Style.empty(), content);
    }

    public TextComponent() {
        this("");
    }

    public TextComponent(Component component) {
        this(component.children(), component.style());
        if (component instanceof net.kyori.adventure.text.TextComponent textComponent) {
            this.content = textComponent.content();
        }
    }

    @Override
    public @NotNull TextComponent content(@NotNull String content) {
        this.content = content;
        return this;
    }

    @Override
    public @NotNull String content() {
        return this.content;
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new TextComponentBuilder(this);
    }

    @Override
    public @NotNull TextComponent children(@NotNull List<? extends ComponentLike> children) {
        return new TextComponent(children, this.style, this.content);
    }

    @Override
    public @NotNull TextComponent style(@NotNull Style style) {
        return new TextComponent(this.children, style, this.content);
    }

    /**
     * 替换文本实例中的指定字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的字符串
     * @return 替换完成后的文本实例
     */
    public TextComponent replace(String target, String replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .build();

        return new TextComponent(this.toBuilder().build().replaceText(replacementConfig));
    }

    /**
     * 替换文本实例中的指定字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的文本实例
     * @return 替换完成后的文本实例
     */
    public TextComponent replace(String target, Component replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .build();

        return new TextComponent(this.toBuilder().build().replaceText(replacementConfig));
    }

    /**
     * 替换文本实例中的第一个指定字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的文本实例
     * @return 替换完成后的文本实例
     */
    public TextComponent replaceFirst(String target, String replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .times(1)
                .build();

        return new TextComponent(this.toBuilder().build().replaceText(replacementConfig));
    }

    /**
     * 替换文本实例中的第一个指定字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的文本
     * @return 替换完成后的文本实例
     */
    public TextComponent replaceFirst(String target, Component replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .times(1)
                .build();

        return new TextComponent(this.toBuilder().build().replaceText(replacementConfig));
    }

    /**
     * 替换miniMessage字符串中的字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的文本实例
     * @return 替换完成后的文本实例
     */
    public TextComponent replaceByMiniMessage(String target, String replacement) {
        return ColorUtil.color(this.toMiniMessageString().replace(target, replacement));
    }

    /**
     * 转换为miniMessage字符串
     *
     * @return miniMessage字符串
     */
    public String toMiniMessageString() {
        return MiniMessage.miniMessage().serialize(this);
    }

    /**
     * 转换为json实例
     *
     * @return json实例
     */
    public JsonElement toJsonElement() {
        return GsonComponentSerializer.gson().serializeToTree(this);
    }

    /**
     * 转换为json字符符串
     *
     * @return json字符符串
     */
    public String toJsonString() {
        return JSONComponentSerializer.json().serialize(this);
    }

    /**
     * 转换为旧版格式字符串
     *
     * @return 旧版格式字符串
     */
    public String toLegacyString() {
        return LegacyComponentSerializer.legacySection().serialize(this);
    }

    @Override
    public String toString() {
        return this.content();
    }
}
