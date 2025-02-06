package nearclipadjust.core;

//import net.minecraft.client.renderer.culling.ClippingHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Field getField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object getFieldValue(Object instance, String fieldName) {
        try {
            Field field = getField(instance, fieldName);
            return field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            Field field = getField(instance, fieldName);
            field.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Method Method(Object instance, String methodName, Class<?>[] parameterTypes) {
        try {
            Method method = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object realizeMethod(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object invokeMethod(Object instance, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = Method(instance, methodName, parameterTypes);
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}