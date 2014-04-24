package com.github.nitnelave.xmlparser;

import org.xml.sax.SAXException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author nitnelave
 */
class ParserNode
{
    private final String name;
    private final Class<?> clazz;
    private final HashSet<Class<?>> superClazz = new HashSet<>();
    private final Class<?> valueClazz;
    private final boolean hasContent;
    private final boolean updateContent;
    private final boolean resetContent;
    private final boolean isSingleNode;
    private final XMLNodeType type;
    private final List<ParserProperty> properties = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // The IDE does not detect access through ternary
    private final Collection<NodeHandler> handlerList = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Collection<NodeHandler> beginHandlers = new ArrayList<>();
    private final StringSetter setter;

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
        Collections.addAll(superClazz, node.parentNodes());
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
        setter = hasContent ? new StringSetter(clazz, "Content", valueClazz) : null;
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

    public boolean isSuperClazz(Class<?> c)
    {
        return superClazz.contains(c);
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
        for (NodeHandler h : begin ? beginHandlers : handlerList)
            h.call(last);
    }

    public void addListener(Object handler)
    {
        Method method = Reflect.getMethod(handler.getClass(), "handle", getClazz());
        if (method != null)
            handlerList.add(new NodeHandler(handler, method));
        method = Reflect.getMethod(handler.getClass(), "handleBegin", getClazz());
        if (method != null)
            beginHandlers.add(new NodeHandler(handler, method));
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
        return !superClazz.isEmpty();
    }

    public boolean shouldUpdateContent()
    {
        return updateContent;
    }

    public boolean shouldResetContent()
    {
        return resetContent;
    }

    public boolean isDefault()
    {
        return type == XMLNodeType.DEFAULT;
    }

    public void setContent(Object target, String content, boolean update)
    {
        if (!hasContent())
            return;
        if (update && !shouldUpdateContent())
            return;
        setter.set(target, content);
    }

    private class NodeHandler
    {
        private final Object handler;
        private final Method handle;

        public NodeHandler(Object handler, Method handle)
        {
            this.handler = handler;
            this.handle = handle;
        }

        public void call(Object target)
        {
            try
            {
                handle.invoke(handler, target);
            } catch (IllegalAccessException | InvocationTargetException ignored)
            {
            }
        }
    }
}
