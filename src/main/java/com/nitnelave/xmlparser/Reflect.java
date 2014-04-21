package com.nitnelave.xmlparser;

import org.xml.sax.SAXException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nitnelave
 */
final class Reflect
{
    public static Object newInstance(Class<?> clazz)
    throws SAXException
    {
        try
        {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored)
        {
            throw new SAXException("Could not instantiate class " + clazz.getName());
        }
    }

    /**
     * Call the method set + name of the object o with a new instance of valueClass built with the value.
     * Fails silently if there is a problem.
     * @param o The target
     * @param name The name of the setter, without "set"
     * @param valueClass The class of the value to build
     * @param value The String content of the value to set
     */
    public static void setFromString(Object o, String name, Class<?> valueClass, String value)
    {
        assert value != null;
        try
        {
            Constructor<?> con = valueClass.getConstructor(String.class);
            call(o, "set" + name, con.newInstance(value));
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException ignored)
        {
        }
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
