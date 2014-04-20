package com.nitnelave.xmlparser;

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
        Class<?> superClazz = node.getSuperClazz();
        if (node.hasParent()
            && !superClazz.equals(peek().getClass()))
            throw new SAXException("Invalid XML architecture: " + node.getName() + " as child of a " +
                                   xmlParser.getNodeForClass(peek().getClass()).getName() + ". " +
                                   xmlParser.getNodeForClass(superClazz).getName() + " expected.");

        push(new StackElement(node, Reflect.newInstance(node.getClazz())));
        getProperties(node, attributes);
        for (Object o : node.getBeginHandlers())
            Reflect.call(o, "handleBegin", peek());

    }

    private void getProperties(ParserNode node, Attributes attributes)
    throws SAXException
    {
        for (ParserProperty property : node.getProperties())
        {
            String v = attributes.getValue(property.getPropertyName());
            if (v == null)
            {
                if (!property.isOptional())
                    throw new SAXException(String.format("XMLNode %s is missing required property %s",
                                                         node.getName(), property.getName()));
            }
            else
                Reflect.setString(peek(), property.getName(), property.getValueType(), v);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException
    {
        getLastElement().append(new String(ch, start, length).trim());
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException
    {
        Object last = peek();
        ParserNode lastNode = xmlParser.getNodeForClass(last.getClass());
        if (lastNode.hasContent())
            Reflect.setString(last, "Content", lastNode.getValueClazz(), getLastElement().getContent());
        pop();


        if (!stack.isEmpty())
            lastNode.registerParent(last, peek());
        lastNode.call(last);
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

        private StackElement(StringBuilder builder, Object node)
        {
            this.builder = builder;
            this.node = node;
        }

        public void append(String content)
        {
            if (builder != null)
                builder.append(content);
        }

        public String getContent()
        {
            if (builder == null)
                return "";
            return builder.toString();
        }
    }
}
