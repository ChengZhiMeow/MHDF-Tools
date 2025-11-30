package cn.chengzhimeow.mhdftools.feature;

import org.reflections.Reflections;

import java.util.Set;

public final class ClassScanner {
    private static final Reflections reflections = new Reflections("cn.chengzhimeow.mhdftools");

    public static <T> Set<Class<? extends T>> scanSubTypeOf(Class<T> clazz) {
        return ClassScanner.reflections.getSubTypesOf(clazz);
    }
}
