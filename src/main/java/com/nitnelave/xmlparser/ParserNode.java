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
    private final boolean isSingleNode;
    private final Collection<ParserProperty> properties = new ArrayList<>();
    private final Collection<Object> handlerList = new ArrayList<>();

    public ParserNode(XMLParser xmlParser, Class<?> clazz)
    throws XMLStructureException
    {
        XMLNode node = clazz.getAnnotation(XMLNode.class);
        if (node == null)
            throw new XMLStructureException("Trying to register class " + clazz.getName()
                                            + " that does not have the XMLNode annotation");
        name = node.name().trim();
        this.clazz = clazz;
        superClazz = node.parentNode();
        valueClazz = node.contentType();
        isSingleNode = node.single();
        hasContent = (!valueClazz.equals(None.class));
        if (hasContent)
        {
            if (!Reflect.hasStringConstructor(valueClazz))
                throw new XMLStructureException("XMLNode " + name + ": content type "
                                                + valueClazz.getName()
                                                + " must implement constructor "
                                                + valueClazz.getName() + "(String s)");
            if (!Reflect.hasMethod(clazz, "setContent", valueClazz))
                throw new XMLStructureException("XMLNode " + clazz.getName() +
                                                " is missing the method setContent("
                                                + valueClazz.getName() + ')');
        }
        constructProperties();
        if (superClazz.equals(DefaultNode.class))
            xmlParser.setDefaultNode(this);
        else if (name.isEmpty())
            throw new XMLStructureException("Nodes must define non-empty names, except for the default one.");
        else if (superClazz.equals(RootNode.class))
            xmlParser.setRootNode(this);
    }

    private void constructProperties()
    throws XMLStructureException
    {
        XMLProperties prop = clazz.getAnnotation(XMLProperties.class);
        if (prop != null)
            for (XMLProperty p : prop.value())
                registerProperty(p);
        XMLProperty sProp = clazz.getAnnotation(XMLProperty.class);
        if (sProp != null)
            registerProperty(sProp);
    }

    private void registerProperty(XMLProperty property)
    throws XMLStructureException
    {
        if (!Reflect.hasMethod(clazz, "set" + property.name(), property.valueType()))
            throw new XMLStructureException("XMLNode " + clazz.getName()
                                            + " is missing the method set" + property.name()
                                            + '(' + property.valueType().getName() + ')');
        properties.add(new ParserProperty(property));
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
            Reflect.call(o, "handle", last);
    }

    public void addListener(Object handler)
    {
        handlerList.add(handler);
    }

    public void registerParent(Object child, Object parent)
    {
        Reflect.call(parent, (isSingleNode ? "set" : "add") + getName(), child);
    }
}
