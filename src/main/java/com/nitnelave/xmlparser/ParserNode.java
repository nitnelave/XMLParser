package com.nitnelave.xmlparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private final boolean updateContent;
    private final boolean resetContent;
    private final boolean isSingleNode;
    private final XMLNodeType type;
    private final List<ParserProperty> properties = new ArrayList<>();
    private final Collection<Object> handlerList = new ArrayList<>();
    private Collection<Object> beginHandlers = new ArrayList<>();

    public ParserNode(XMLParser xmlParser, Class<?> clazz)
    throws XMLStructureException
    {
        XMLNode node = clazz.getAnnotation(XMLNode.class);
        if (node == null)
            throw new XMLStructureException("Trying to register class " + clazz.getSimpleName()
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
        updateContent = node.updateContent();
        resetContent = node.resetContent();
        hasContent = (!valueClazz.equals(None.class));
        if (isRoot() && hasParent())
            throw new XMLStructureException(name + ": Trying to register a root node with a parent node");
        if (type == XMLNodeType.DEFAULT && hasParent())
            throw new XMLStructureException(name + ": Trying to register a default node with a parent node");
        if (hasContent)
        {
            if (!Reflect.hasStringConstructor(valueClazz))
                throw new XMLStructureException("XMLNode " + name + ": content type "
                                                + valueClazz.getSimpleName()
                                                + " must implement constructor "
                                                + valueClazz.getSimpleName() + "(String)");
            if (!Reflect.hasMethod(clazz, "setContent", valueClazz))
                throw new XMLStructureException("XMLNode " + name +
                                                " is missing the method setContent("
                                                + valueClazz.getSimpleName() + ')');
        }
        if (type == XMLNodeType.DEFAULT)
            xmlParser.setDefaultNode(this);
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
            throw new XMLStructureException("XMLNode " + clazz.getSimpleName()
                                            + " is missing the method set" + property.name()
                                            + '(' + property.valueType().getSimpleName() + ')');
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

    public List<ParserProperty> getProperties()
    {
        return properties;
    }

    public void call(Object last, boolean begin)
    {
        for (Object o : begin ? beginHandlers : handlerList)
            Reflect.call(o, begin ? "handleBegin" : "handle", last);
    }

    public void addListener(Object handler)
    {
        if (Reflect.hasMethod(handler.getClass(), "handle", getClazz()))
            handlerList.add(handler);
        if (Reflect.hasMethod(handler.getClass(), "handleBegin", getClazz()))
            beginHandlers.add(handler);
    }

    public void registerParent(Object child, Object parent)
    {
        Reflect.call(parent, (isSingleNode ? "set" : "add") + getName(), child);
    }

    public boolean isRoot()
    {
        return type == XMLNodeType.ROOT;
    }

    public boolean hasParent()
    {
        return !superClazz.equals(None.class);
    }

    public boolean shouldUpdateContent()
    {
        return updateContent;
    }

    public boolean shouldResetContent()
    {
        return resetContent;
    }
}
