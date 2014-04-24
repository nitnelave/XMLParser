package com.github.nitnelave.xmlparser;

import org.xml.sax.SAXException;

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

    public static Method getMethod(Class<?> aClass, String method, Class<?>... clazz)
    {
        try
        {
            return aClass.getMethod(method, clazz);
        } catch (NoSuchMethodException e)
        {
            return null;
        }
    }
}
