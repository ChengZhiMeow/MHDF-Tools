package cn.chengzhimeow.mhdftools.bukkit.feature;

import cn.chengzhimeow.mhdftools.bukkit.compatibility.packetevents.PacketEventsManager;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.PacketListener;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class PacketListenerRegisterManager {
    @Getter(lazy = true)
    private static final PacketListenerRegisterManager instance = new PacketListenerRegisterManager();

    private PacketListenerRegisterManager() {
    }

    @SneakyThrows
    public void registerPacketListeners(Module module) {
        Reflections reflections = new Reflections(module.getClass().getPackageName());

        Set<Class<? extends PacketListener>> packetListenerClassSet = reflections.getSubTypesOf(PacketListener.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends PacketListener> clazz : packetListenerClassSet) {
            Constructor<? extends PacketListener> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            PacketListener listener = constructor.newInstance();

            if (!listener.isEnable()) continue;

            PacketEventsManager.getInstance().registerListener(listener, listener.getPriority());
        }
    }
}
