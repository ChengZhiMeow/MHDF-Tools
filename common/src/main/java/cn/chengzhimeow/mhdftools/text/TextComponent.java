package cn.chengzhimeow.mhdftools.text;

import cn.chengzhimeow.mhdftools.message.ColorUtil;
import com.google.gson.JsonElement;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.util.ARGBLike;
import net.kyori.adventure.util.IntFunction2;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.Examiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public final class TextComponent implements net.kyori.adventure.text.TextComponent {
    private final net.kyori.adventure.text.TextComponent source;

    public TextComponent() {
        this("");
    }

    public TextComponent(String content) {
        this(Component.text(content));
    }

    public TextComponent(Component component) {
        this(Component.text().append(component).build());
    }

    public TextComponent(net.kyori.adventure.text.TextComponent source) {
        this.source = source;
    }

    public Component copy() {
        return new TextComponent(source.toBuilder().build());
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

        return new TextComponent(this.copy().replaceText(replacementConfig));
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

        return new TextComponent(this.copy().replaceText(replacementConfig));
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

        return new TextComponent(this.copy().replaceText(replacementConfig));
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

        return new TextComponent(this.copy().replaceText(replacementConfig));
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
    public @NotNull String content() {
        return source.content();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent content(@NotNull String content) {
        return source.content(content);
    }

    @Override
    public @NotNull String examinableName() {
        return source.examinableName();
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return source.examinableProperties();
    }

    @Override
    public <R> @NotNull R examine(@NotNull Examiner<R> examiner) {
        return source.examine(examiner);
    }

    @Override
    public @NotNull Builder toBuilder() {
        return source.toBuilder();
    }

    @Override
    public @NotNull @Unmodifiable List<Component> children() {
        return source.children();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent asComponent() {
        return source.asComponent();
    }

    @Override
    public @NotNull HoverEvent<Component> asHoverEvent() {
        return source.asHoverEvent();
    }

    @Override
    public @NotNull HoverEvent<Component> asHoverEvent(@NotNull UnaryOperator<Component> op) {
        return source.asHoverEvent(op);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent children(@NotNull List<? extends ComponentLike> children) {
        return source.children(children);
    }

    @Override
    public boolean contains(@NotNull Component that) {
        return source.contains(that);
    }

    @Override
    public boolean contains(@NotNull Component that, @NotNull BiPredicate<? super Component, ? super Component> equals) {
        return source.contains(that, equals);
    }

    @Override
    public void detectCycle(@NotNull Component that) {
        source.detectCycle(that);
    }

    @Override
    public @NotNull Style style() {
        return source.style();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent style(@NotNull Style style) {
        return source.style(style);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent style(@NotNull Consumer<Style.Builder> style) {
        return source.style(style);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent style(Style.@NotNull Builder style) {
        return source.style(style);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent style(@NotNull Consumer<Style.Builder> consumer, Style.Merge.@NotNull Strategy strategy) {
        return source.style(consumer, strategy);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent mergeStyle(@NotNull Component that) {
        return source.mergeStyle(that);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent mergeStyle(@NotNull Component that, Style.@NotNull Merge @NotNull ... merges) {
        return source.mergeStyle(that, merges);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent append(@NotNull Component component) {
        return source.append(component);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent append(@NotNull ComponentLike like) {
        return source.append(like);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent append(@NotNull ComponentBuilder<?, ?> builder) {
        return source.append(builder);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent append(@NotNull List<? extends ComponentLike> components) {
        return source.append(components);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent append(@NotNull ComponentLike @NotNull ... components) {
        return source.append(components);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent appendNewline() {
        return source.appendNewline();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent appendSpace() {
        return source.appendSpace();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent applyFallbackStyle(@NotNull StyleBuilderApplicable @NotNull ... style) {
        return source.applyFallbackStyle(style);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent applyFallbackStyle(@NotNull Style style) {
        return source.applyFallbackStyle(style);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
        return source.mergeStyle(that, merges);
    }

    @Override
    public @Nullable Key font() {
        return source.font();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent color(@Nullable TextColor color) {
        return source.color(color);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent colorIfAbsent(@Nullable TextColor color) {
        return source.colorIfAbsent(color);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent shadowColor(@Nullable ARGBLike argb) {
        return source.shadowColor(argb);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent shadowColorIfAbsent(@Nullable ARGBLike argb) {
        return source.shadowColorIfAbsent(argb);
    }

    @Override
    public boolean hasDecoration(@NotNull TextDecoration decoration) {
        return source.hasDecoration(decoration);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent decorate(@NotNull TextDecoration decoration) {
        return source.decorate(decoration);
    }

    @Override
    public @NotNull Component decorate(@NotNull TextDecoration @NotNull ... decorations) {
        return source.decorate(decorations);
    }

    @Override
    public TextDecoration.@NotNull State decoration(@NotNull TextDecoration decoration) {
        return source.decoration(decoration);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent decoration(@NotNull TextDecoration decoration, boolean flag) {
        return source.decoration(decoration, flag);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent decoration(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
        return source.decoration(decoration, state);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent decorationIfAbsent(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
        return source.decorationIfAbsent(decoration, state);
    }

    @Override
    public @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
        return source.decorations();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
        return source.decorations(decorations);
    }

    @Override
    public @NotNull Component decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
        return source.decorations(decorations, flag);
    }

    @Override
    public @Nullable ClickEvent clickEvent() {
        return source.clickEvent();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent clickEvent(@Nullable ClickEvent event) {
        return source.clickEvent(event);
    }

    @Override
    public @Nullable HoverEvent<?> hoverEvent() {
        return source.hoverEvent();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent hoverEvent(@Nullable HoverEventSource<?> event) {
        return source.hoverEvent(event);
    }

    @Override
    public @Nullable String insertion() {
        return source.insertion();
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent insertion(@Nullable String insertion) {
        return source.insertion(insertion);
    }

    @Override
    public boolean hasStyling() {
        return source.hasStyling();
    }

    @Override
    public @NotNull Component replaceText(@NotNull Consumer<TextReplacementConfig.Builder> configurer) {
        return source.replaceText(configurer);
    }

    @Override
    public @NotNull Component replaceText(@NotNull TextReplacementConfig config) {
        return source.replaceText(config);
    }

    @Override
    public @NotNull Component compact() {
        return source.compact();
    }

    @Override
    public @NotNull Component compact(@Nullable Style parentStyle) {
        return source.compact(parentStyle);
    }

    @Override
    public @NotNull Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag @Nullable ... flags) {
        return source.iterable(type, flags);
    }

    @Override
    public @NotNull Iterable<Component> iterable(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
        return source.iterable(type, flags);
    }

    @Override
    public @NotNull Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag @Nullable ... flags) {
        return source.iterator(type, flags);
    }

    @Override
    public @NotNull Iterator<Component> iterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
        return source.iterator(type, flags);
    }

    @Override
    public @NotNull Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull ComponentIteratorFlag @Nullable ... flags) {
        return source.spliterator(type, flags);
    }

    @Override
    public @NotNull Spliterator<Component> spliterator(@NotNull ComponentIteratorType type, @NotNull Set<ComponentIteratorFlag> flags) {
        return source.spliterator(type, flags);
    }

    @Override
    public @NotNull Component replaceText(@NotNull String search, @Nullable ComponentLike replacement) {
        return source.replaceText(search, replacement);
    }

    @Override
    public @NotNull Component replaceText(@NotNull Pattern pattern, @NotNull Function<Builder, @Nullable ComponentLike> replacement) {
        return source.replaceText(pattern, replacement);
    }

    @Override
    public @NotNull Component replaceFirstText(@NotNull String search, @Nullable ComponentLike replacement) {
        return source.replaceFirstText(search, replacement);
    }

    @Override
    public @NotNull Component replaceFirstText(@NotNull Pattern pattern, @NotNull Function<Builder, @Nullable ComponentLike> replacement) {
        return source.replaceFirstText(pattern, replacement);
    }

    @Override
    public @NotNull Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, int numberOfReplacements) {
        return source.replaceText(search, replacement, numberOfReplacements);
    }

    @Override
    public @NotNull Component replaceText(@NotNull Pattern pattern, @NotNull Function<Builder, @Nullable ComponentLike> replacement, int numberOfReplacements) {
        return source.replaceText(pattern, replacement, numberOfReplacements);
    }

    @Override
    public @NotNull Component replaceText(@NotNull String search, @Nullable ComponentLike replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
        return source.replaceText(search, replacement, fn);
    }

    @Override
    public @NotNull Component replaceText(@NotNull Pattern pattern, @NotNull Function<Builder, @Nullable ComponentLike> replacement, @NotNull IntFunction2<PatternReplacementResult> fn) {
        return source.replaceText(pattern, replacement, fn);
    }

    @Override
    public void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
        source.componentBuilderApply(component);
    }

    @Override
    public net.kyori.adventure.text.@NotNull TextComponent font(@Nullable Key key) {
        return source.font(key);
    }

    @Override
    public @Nullable TextColor color() {
        return source.color();
    }

    @Override
    public @Nullable ShadowColor shadowColor() {
        return source.shadowColor();
    }
}
