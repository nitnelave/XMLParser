package com.github.nitnelave.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nitnelave
 */
class Handler extends DefaultHandler
{
    private final XMLParser xmlParser;
    private final List<StackElement> stack = new ArrayList<>();

    public Handler(XMLParser xmlParser)
    {
        super();
        this.xmlParser = xmlParser;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
    {
        ParserNode node = xmlParser.getNode(qName);
        if (node == null)
            throw new SAXException("Unknown XML tag: " + qName);
        if (node.isRoot() && !stack.isEmpty())
            throw new SAXException("Root node " + node.getName()
                                   + " as child of another node: " + peek().getClass().getName());
        if (!stack.isEmpty())
            updateContent(getLastElement());
        Class<?> superClazz = node.getSuperClazz();
        if (node.hasParent()
            && !superClazz.equals(peek().getClass()))
            throw new SAXException("Invalid XML architecture: " + node.getName() + " as child of a " +
                                   xmlParser.getNodeForClass(peek().getClass()).getName() + ". " +
                                   xmlParser.getNodeForClass(superClazz).getName() + " expected.");
        Object instance = Reflect.newInstance(node.getClazz());
        if (node.isDefault())
            Reflect.call(instance, "setName", qName);
        push(new StackElement(node, instance));
        getProperties(node, attributes);
        node.call(peek(), true);
    }

    private void updateContent(StackElement lastElement)
    {
        ParserNode node = lastElement.xmlNode;
        if (node.hasContent() && node.shouldUpdateContent())
        {
            Object elem = lastElement.node;
            Reflect.setFromString(elem, "Content", node.getValueClazz(), lastElement.getContent());
            if (node.shouldResetContent())
                lastElement.builder.setLength(0);
        }
    }

    private void getProperties(ParserNode node, Attributes attributes)
    throws SAXException
    {
        for (ParserProperty property : node.getProperties())
        {
            String v = attributes.getValue(property.getKey());
            if (v == null)
            {
                if (!property.isOptional())
                    throw new SAXException(String.format("XMLNode %s is missing required property %s",
                                                         node.getName(), property.getName()));
            }
            else
                Reflect.setFromString(peek(), property.getName(), property.getValueType(), v);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException
    {
        getLastElement().append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException
    {
        StackElement element = getLastElement();
        Object last = element.node;
        ParserNode lastNode = element.xmlNode;
        if (lastNode.hasContent())
            Reflect.setFromString(last, "Content", lastNode.getValueClazz(), getLastElement().getContent());
        pop();


        if (!stack.isEmpty())
            lastNode.registerParent(last, peek());
        lastNode.call(last, false);
    }

    private void push(StackElement o)
    {
        stack.add(o);
    }

    private Object peek()
    {
        return getLastElement().node;
    }

    private StackElement getLastElement()
    {
        return stack.get(stack.size() - 1);
    }

    private void pop()
    {
        stack.remove(stack.size() - 1);
    }

    private class StackElement
    {
        private final StringBuilder builder;
        private final Object node;
        private final ParserNode xmlNode;

        private StackElement(ParserNode xmlNode, Object node)
        {
            this.xmlNode = xmlNode;
            builder = xmlNode.hasContent() ? new StringBuilder() : null;
            this.node = node;
        }

        public void append(String content)
        {
            if (builder != null)
                builder.append(content);
        }

        public String getContent()
        {
            assert builder != null;
            return builder.toString();
        }
    }
}
