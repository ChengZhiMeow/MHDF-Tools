package cn.chengzhiya.mhdftools.util.reflection;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    /**
     * 新建指定类实例的类对象
     *
     * @param clazz 类实例
     * @param args  传入参数
     * @return 类对象
     */
    @SneakyThrows
    public static <T> T newClass(Class<?> clazz, Object... args) {
        return (T) clazz.getConstructor().newInstance(args);
    }

    /**
     * 通过反射指定类实例获取指定方法的方法实例
     *
     * @param clazz      类实例
     * @param methodName 方法名称
     * @param accessible 强制访问
     * @param argsTypes  传参类型
     * @return 方法实例
     */
    @SneakyThrows
    public static Method getMethod(Class<?> clazz, String methodName, boolean accessible, Class<?>... argsTypes) {
        Method method = clazz.getDeclaredMethod(methodName, argsTypes);
        method.setAccessible(accessible);
        return method;
    }

    /**
     * 通过反射指定类实例获取指定方法的变量实例
     *
     * @param clazz      类实例
     * @param fieldName  变量名称
     * @param accessible 强制访问
     * @return 变量实例
     */
    @SneakyThrows
    public static Field getField(Class<?> clazz, String fieldName, boolean accessible) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(accessible);
        return field;
    }

    /**
     * 通过反射指定方法实例获取返回值
     *
     * @param method 方法实例
     * @param object 对象实例
     * @param args   传入参数
     * @return 返回值
     */
    @SneakyThrows
    public static <T> T invokeMethod(Method method, Object object, Object... args) {
        Object invokeObject = method.invoke(object, args);
        if (invokeObject == null) {
            return null;
        }
        return (T) invokeObject;
    }

    /**
     * 获取反射指定变量实例的返回值
     *
     * @param field  变量实例
     * @param object 对象实例
     */
    @SneakyThrows
    public static <T> T getFieldValue(Field field, Object object) {
        return (T) field.get(object);
    }

    /**
     * 通过反射指定变量实例的返回值
     *
     * @param field  变量实例
     * @param object 对象实例
     * @param value  修改的值
     */
    @SneakyThrows
    public static void setFieldValue(Field field, Object object, Object value) {
        field.set(object, value);
    }
}
