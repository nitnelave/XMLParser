package com.github.nitnelave.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author nitnelave
 */
class ParserProperty
{
    private final String name;
    private final String key;
    private final boolean isOptional;
    private final StringSetter setter;

    public ParserProperty(ParserNode parserNode, XMLProperty property)
    throws XMLStructureException
    {
        name = property.name();
        key = property.key();
        Class<?> valueType = property.valueType();
        setter = new StringSetter(parserNode.getClazz(), name, valueType);
        isOptional = !property.required();
    }

    public String getName()
    {
        return name;
    }

    public String getKey()
    {
        return key;
    }

    public boolean isOptional()
    {
        return isOptional;
    }

    public void set(Object instance, Attributes attributes)
    throws SAXException
    {
        String v = attributes.getValue(getKey());
        if (v == null)
        {
            if (!isOptional())
                throw new SAXException(String.format("XMLNode %s is missing required property %s",
                                                     instance.getClass().getSimpleName(), getName()));
        }
        else
            setter.set(instance, v);
    }
}
