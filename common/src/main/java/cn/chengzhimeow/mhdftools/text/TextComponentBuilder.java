package cn.chengzhimeow.mhdftools.text;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("UnusedReturnValue")
public final class TextComponentBuilder implements net.kyori.adventure.text.TextComponent.Builder {
    private final List<Component> children;
    private String content = "";
    private @Nullable Style style;
    private @Nullable Style.Builder styleBuilder;

    public TextComponentBuilder() {
        this.children = new ArrayList<>();
    }

    public TextComponentBuilder(Component component) {
        this.children = List.of(component);
    }

    public TextComponentBuilder(TextComponent component) {
        this.children = List.of(component);
        this.content = component.content();
    }

    @Override
    public @NotNull String content() {
        return this.content;
    }

    @Override
    public @NotNull List<Component> children() {
        return this.children;
    }

    public @NotNull Style.Builder styleBuilder() {
        if (this.styleBuilder == null) {
            if (this.style != null) {
                this.styleBuilder = this.style.toBuilder();
                this.style = null;
            } else {
                this.styleBuilder = Style.style();
            }
        }

        return this.styleBuilder;
    }

    public @NotNull Style style() {
        if (this.styleBuilder != null) return this.styleBuilder.build();
        return Objects.requireNonNullElseGet(this.style, Style::empty);
    }

    @Override
    public @NotNull TextComponentBuilder content(@NotNull String content) {
        this.content = content;
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder append(@NotNull Component component) {
        this.children.add(component);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder appendNewline() {
        this.append(Component.newline());
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder append(@NotNull Component @NotNull ... components) {
        Arrays.stream(components).forEach(this::append);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder append(@NotNull ComponentLike componentLike) {
        Component component = componentLike.asComponent();
        if (component != Component.empty()) {
            this.append(component);
        }
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder append(@NotNull ComponentLike @NotNull ... components) {
        Arrays.stream(components).forEach(this::append);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder append(@NotNull Iterable<? extends ComponentLike> components) {
        components.forEach(this::append);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder applyDeep(@NotNull Consumer<? super ComponentBuilder<?, ?>> action) {
        this.apply(action);
        if (this.children == Collections.<Component>emptyList()) return this;

        ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            Component component = it.next();
            if (!(component instanceof BuildableComponent<?, ?> builder)) {
                continue;
            }

            ComponentBuilder<?, ?> childBuilder = builder.toBuilder();
            childBuilder.applyDeep(action);
            it.set(childBuilder.build());
        }
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder mapChildren(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.<Component>emptyList()) return this;

        ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            Component component = it.next();
            if (!(component instanceof BuildableComponent<?, ?> builder)) {
                continue;
            }

            BuildableComponent<?, ?> child = function.apply(builder);
            if (component == child) {
                continue;
            }
            it.set(child);
        }
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder mapChildrenDeep(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.<Component>emptyList()) return this;

        ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            Component component = it.next();
            if (!(component instanceof BuildableComponent<?, ?> builder)) {
                continue;
            }

            BuildableComponent<?, ?> child = function.apply(builder);
            if (!child.children().isEmpty()) {
                ComponentBuilder<?, ?> childBuilder = builder.toBuilder();
                childBuilder.mapChildrenDeep(function);
                it.set(childBuilder.build());
            } else {
                if (component == child) {
                    continue;
                }
                it.set(child);
            }
        }
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder style(@NotNull Style style) {
        this.style = style;
        this.styleBuilder = null;
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder style(@NotNull Consumer<Style.Builder> consumer) {
        consumer.accept(this.styleBuilder());
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder font(@Nullable Key font) {
        this.styleBuilder().font(font);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder color(@Nullable TextColor color) {
        this.styleBuilder().color(color);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder colorIfAbsent(@Nullable TextColor color) {
        this.styleBuilder().colorIfAbsent(color);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder shadowColor(@Nullable ARGBLike argb) {
        this.styleBuilder().shadowColor(argb);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder shadowColorIfAbsent(@Nullable ARGBLike argb) {
        this.styleBuilder().shadowColorIfAbsent(argb);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder decoration(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
        this.styleBuilder().decoration(decoration, state);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder decorationIfAbsent(@NotNull TextDecoration decoration, TextDecoration.@NotNull State state) {
        this.styleBuilder().decorationIfAbsent(decoration, state);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder clickEvent(@Nullable ClickEvent event) {
        this.styleBuilder().clickEvent(event);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder hoverEvent(@Nullable HoverEventSource<?> source) {
        this.styleBuilder().hoverEvent(source);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder insertion(@Nullable String insertion) {
        this.styleBuilder().insertion(insertion);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
        this.styleBuilder().merge(requireNonNull(that, "component").style(), merges);
        return this;
    }

    @Override
    public @NotNull TextComponentBuilder resetStyle() {
        this.style = null;
        this.styleBuilder = null;
        return this;
    }

    @Override
    public @NotNull TextComponent build() {
        if (this.isEmpty()) {
            return new TextComponent();
        }
        return new TextComponent(this.children, this.style(), this.content);
    }

    private boolean hasStyle() {
        return this.styleBuilder != null || this.style != null;
    }

    private boolean isEmpty() {
        return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
    }
}
