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
    private final boolean hasParent;
    private final boolean isSingleNode;
    private final XMLNodeType type;
    private final Collection<ParserProperty> properties = new ArrayList<>();
    private final Collection<Object> handlerList = new ArrayList<>();
    private Collection<Object> beginHandlers = new ArrayList<>();

    public ParserNode(XMLParser xmlParser, Class<?> clazz)
    throws XMLStructureException
    {
        XMLNode node = clazz.getAnnotation(XMLNode.class);
        if (node == null)
            throw new XMLStructureException("Trying to register class " + clazz.getName()
                                            + " that does not have the XMLNode annotation");
        String tmpName = node.name().trim();
        if (tmpName.isEmpty())
            tmpName = clazz.getSimpleName();
        name = tmpName;
        this.clazz = clazz;
        superClazz = node.parentNode();
        valueClazz = node.contentType();
        isSingleNode = node.single();
        type = node.type();
        hasContent = (!valueClazz.equals(None.class));
        hasParent = (!superClazz.equals(None.class));
        if (isRoot() && hasParent())
            throw new XMLStructureException(name + ": Trying to register a root node with a parent node");
        if (type == XMLNodeType.DEFAULT && hasParent())
            throw new XMLStructureException(name + ": Trying to register a default node with a parent node");
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
        if (type == XMLNodeType.DEFAULT)
            xmlParser.setDefaultNode(this);
        else if (name.isEmpty())
            throw new XMLStructureException("Nodes must define non-empty names, except for the default one.");
        constructProperties();
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
        if (type == XMLNodeType.DEFAULT && property.required())
            throw new XMLStructureException("Default node cannot have required properties.");
        if (!(type == XMLNodeType.DEFAULT)
            && !Reflect.hasMethod(clazz, "set" + property.name(), property.valueType()))
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
        if (Reflect.hasMethod(clazz, "handle", getClazz()))
            handlerList.add(handler);
        if (Reflect.hasMethod(clazz, "handleBegin", getClazz()))
            beginHandlers.add(handler);
    }

    public void registerParent(Object child, Object parent)
    {
        Reflect.call(parent, (isSingleNode ? "set" : "add") + getName(), child);
    }

    public Iterable<Object> getBeginHandlers()
    {
        return beginHandlers;
    }

    public boolean isRoot()
    {
        return type == XMLNodeType.ROOT;
    }

    public boolean hasParent()
    {
        return !superClazz.equals(None.class);
    }
}
