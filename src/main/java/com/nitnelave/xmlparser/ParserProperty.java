package com.nitnelave.xmlparser;

/**
 * @author nitnelave
 */
class ParserProperty
{
    private final String name;
    private final String key;
    private final Class<?> valueType;
    private final boolean isOptional;

    public ParserProperty(XMLProperty property)
    throws XMLStructureException
    {
        name = property.name();
        key = property.key();
        valueType = property.valueType();
        isOptional = !property.required();
        if (!Reflect.hasConstructor(valueType, String.class))
        {
            throw new XMLStructureException(
                    "Property " + name + ": value type " + valueType.getSimpleName() + " must implement constructor "
                    + valueType.getSimpleName() + "(String)"
            );
        }
    }

    public String getName()
    {
        return name;
    }

    public String getKey()
    {
        return key;
    }

    public Class<?> getValueType()
    {
        return valueType;
    }

    public boolean isOptional()
    {
        return isOptional;
    }
}
