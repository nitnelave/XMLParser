package com.github.nitnelave.xmlparser;

import org.xml.sax.SAXException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nitnelave
 */
final class Reflect
{
    /**
     * Create a new instance of the class.
     * @param clazz The class to instantiate
     * @return The new instance
     * @throws SAXException if the instantiation failed
     */
    public static Object newInstance(Class<?> clazz)
    throws SAXException
    {
        try
        {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored)
        {
            throw new SAXException("Could not instantiate class " + clazz.getSimpleName());
        }
    }

    /**
     * Call the method "set" + name of the object o with a new instance of valueClass built with the value.
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

    /**
     * Call the given method on the target with the single argument arg.
     * Fails silently if there is a problem.
     * @param target The target of the call
     * @param methodName The method's name
     * @param arg The argument
     */
    public static void call(Object target, String methodName, Object arg)
    {
        try
        {
            Method method = target.getClass().getMethod(methodName, arg.getClass());
            method.invoke(target, arg);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored)
        {
        }
    }

    public static boolean hasMethod(Class<?> c, String methodName, Class<?>... args)
    {
        try
        {
            c.getMethod(methodName, args);
            return true;
        } catch (NoSuchMethodException e)
        {
            return false;
        }
    }

    public static boolean hasConstructor(Class<?> valueType, Class<?>... params)
    {
        try
        {
            valueType.getConstructor(params);
            return true;
        } catch (NoSuchMethodException e)
        {
            return false;
        }
    }

    public static Method getMethod(Class<? extends Object> aClass, String method, Class<?>... clazz)
    {
        try
        {
            Method m = aClass.getMethod(method, clazz);
            return m;
        } catch (NoSuchMethodException e)
        {
            return null;
        }
    }
}
