package com.nitnelave.xmlparser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nitnelave
 */
final class Reflect
{
    public static Object newInstance(Class<?> clazz)

    {
        try
        {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored)
        {
        }
        return null;
    }

    public static void setString(Object o, String name, Class<?> valueClass, String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException();
        }
        Object instance = null;
        try
        {
            Constructor<?> con = valueClass.getConstructor(String.class);
            instance = con.newInstance(value);
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException ignored)
        {
        }
        set(o, name, valueClass, instance);
    }

    private static void set(Object o, String name, Class<?> valueClass, Object value)
    {
        Class<?> clazz = o.getClass();
        try
        {
            Method m = clazz.getMethod("set" + name, valueClass);
            m.invoke(o, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored)
        {
        }
    }

    public static void call(Object handler, String methodName, Object o)
    {
        Class<?> clazz = o.getClass();
        Class<?> handlerClazz = handler.getClass();
        try
        {
            Method method = handlerClazz.getMethod(methodName, clazz);
            method.invoke(handler, o);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored)
        {
        }
    }

    public static boolean hasMethod(Class<?> c, String methodName, Class<?> arg)
    {
        try
        {
            c.getMethod(methodName, arg);
            return true;
        } catch (NoSuchMethodException e)
        {
            return false;
        }
    }

    public static boolean hasStringConstructor(Class<?> valueType)
    {
        try
        {
            valueType.getConstructor(String.class);
            return true;
        } catch (NoSuchMethodException e)
        {
            return false;
        }
    }
}
