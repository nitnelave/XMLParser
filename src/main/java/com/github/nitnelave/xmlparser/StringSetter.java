package com.github.nitnelave.xmlparser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nitnelave
 */
class StringSetter
{
    private final Method setter;
    private final Constructor constructor;

    public StringSetter(Class<?> clazz, String methodName, Class<?> valueClazz)
    throws XMLStructureException
    {
        try
        {
            setter = clazz.getMethod("set" + methodName, valueClazz);
        } catch (NoSuchMethodException e)
        {
            throw new XMLStructureException("XMLNode: " + clazz.getSimpleName() + " is missing setter set" + methodName
                                            + '(' + valueClazz.getSimpleName() + ')');
        }
        try
        {
            constructor = valueClazz.getConstructor(String.class);
        } catch (NoSuchMethodException e)
        {
            throw new XMLStructureException(
                    "Value type " + valueClazz.getSimpleName() + " must implement constructor "
                    + valueClazz.getSimpleName() + "(String)"
            );
        }
    }

    public void set(Object target, String content)
    {
        try
        {
            setter.invoke(target, constructor.newInstance(content));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ignored)
        {
        }
    }
}
