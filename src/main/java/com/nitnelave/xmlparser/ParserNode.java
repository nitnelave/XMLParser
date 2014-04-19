package com.nitnelave.xmlparser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nitnelave
 */
class ParserNode
{
    private final String name;
    private final Class<?> clazz;
    private final Class<?> superClazz;
    private final Class<?> valueClazz;
    private final boolean hasContent;
    private final Collection<ParserProperty> properties = new ArrayList<>();
    private final Collection<Object> handlerList = new ArrayList<>();

    public ParserNode(XMLParser xmlParser, Class<?> clazz)
    throws XMLStructureException
    {
        XMLNode node = clazz.getAnnotation(XMLNode.class);
        if (node == null)
        {
            throw new XMLStructureException(
                    "Trying to register class " + clazz.getName() + " that does not have the XMLNode annotation");
        }
        name = node.name();
        this.clazz = clazz;
        superClazz = node.parentNode();
        valueClazz = node.contentType();
        hasContent = (!valueClazz.equals(None.class));
        if (hasContent)
        {
            if (!Reflect.hasStringConstructor(valueClazz))
            {
                throw new XMLStructureException("XMLNode " + name + ": content type "
                                                + valueClazz.getName()
                                                + " must implement constructor "
                                                + valueClazz.getName() + "(String s)");
            }
            if (!Reflect.hasMethod(clazz, "setContent", valueClazz))
            {
                throw new XMLStructureException("XMLNode " + clazz.getName()
                                                + " is missing the method setContent("
                                                + valueClazz.getName() + ')');
            }
        }
        XMLProperties prop = clazz.getAnnotation(XMLProperties.class);
        if (prop != null)
        {
            for (XMLProperty p : prop.value())
            {
                properties.add(new ParserProperty(p));
                if (!Reflect.hasMethod(clazz, "set" + p.name(), p.valueType()))
                {
                    throw new XMLStructureException(
                            "XMLNode " + clazz.getName() + " is missing the method set" + p.name() + '(' + p.valueType()
                                                                                                            .getName()
                            + ')'
                    );
                }

            }
        }
        if (name.isEmpty())
        {
            if (superClazz.equals(None.class))
            {
                xmlParser.setDefaultNode(this);
            }
            else
            {
                throw new XMLStructureException("The default node must have XMLNode.NONE as parentNode");
            }
        }
        else if (superClazz.equals(None.class))
        {
            xmlParser.setRootNode(this);
        }
    }


    public String getName()
    {
        return name;
    }

    public Class<?> getClazz()
    {
        return clazz;
    }

    public Class<?> getSuperClazz()
    {
        return superClazz;
    }

    public Class<?> getValueClazz()
    {
        return valueClazz;
    }

    public boolean hasContent()
    {
        return hasContent;
    }

    public Iterable<ParserProperty> getProperties()
    {
        return properties;
    }

    public void call(Object last)
    {
        for (Object o : handlerList)
        {
            Reflect.call(o, "handle", last);
        }
    }

    public void addListener(Object handler)
    {
        handlerList.add(handler);
    }
}
