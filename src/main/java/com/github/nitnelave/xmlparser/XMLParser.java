package com.github.nitnelave.xmlparser;

import org.reflections.Reflections;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Generic XML SAX parser.
 * <p>
 * This class handles the registration of {@link XMLNode}s and listeners,
 * and takes care of the actual parsing.
 * </p>
 *
 * @author nitnelave
 * @see XMLProperty
 * @see XMLNode
 */
public class XMLParser
{
    private ParserNode defaultNode = null;
    private final Collection<ParserNode> nodeList = new ArrayList<>();
    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    /**
     * Register classes that describe XML Nodes. <br />
     * If the classes do not have the &#64;XMLNode annotation, or there is a required method missing,
     * an XMLStructureException will be raised.
     *
     * @param nodes
     *         The list of classes to be registered as xml nodes
     * @throws XMLStructureException
     *         if there is an architecture problem.
     * @see XMLStructureException
     */
    public void registerNodes(Class<?>... nodes)
            throws XMLStructureException
    {
        for (Class<?> clazz : nodes)
            nodeList.add(new ParserNode(this, clazz));
    }

    /**
     * Register all classes describing XML nodes in the given package. <br />
     * This method searches in the classpath for the given package, then checks
     * every class inside, and if the &#64;XMLNode annotation is present, the
     * class is registered. As the whole package is searched, this method is
     * slower than listing the classes.
     *
     * @param packageName
     *         The package name.
     * @throws XMLStructureException
     *         if there is an architecture problem
     * @see com.github.nitnelave.xmlparser.XMLStructureException
     */
    public void registerPackage(String packageName)
            throws XMLStructureException
    {
        Reflections reflections = new Reflections(packageName);
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(XMLNode.class))
            nodeList.add(new ParserNode(this, clazz));
    }

    /**
     * Register a listener. The class will be inspected for compatible methods.<br />
     * Make sure to register all the node before registering handlers;
     *
     * @param handler
     *         the handler class, with the appropriate methods
     * @throws XMLStructureException
     *         if there is no root node defined
     */
    public void registerListener(Object handler)
            throws XMLStructureException
    {
        for (ParserNode parserNode : nodeList)
            parserNode.addListener(handler);
    }

    /**
     * Start parsing, taking the input from the stream given.
     *
     * @param in
     *         The stream to read from
     * @throws SAXException
     *         parsing error, either from SAX (see SAX documentation) or from a problem in the parsed file structure.
     * @throws ParserConfigurationException
     *         see SAXParser documentation
     * @throws IOException
     *         if there is a IO error
     */
    public void parse(InputStream in)
            throws SAXException, IOException, ParserConfigurationException
    {
        PARSER_FACTORY.newSAXParser().parse(in, new Handler(this));
    }


    protected void setDefaultNode(ParserNode defaultNode)
    throws XMLStructureException
    {
        if (this.defaultNode != null)
            throw new XMLStructureException("Two default nodes defined: "
                                            + defaultNode.getClazz().getName() + " and "
                                            + this.defaultNode.getClazz().getName());
        this.defaultNode = defaultNode;
    }

    protected ParserNode getNode(String qName)
    {
        for (ParserNode node : nodeList)
            if (qName.equalsIgnoreCase(node.getName()))
                return node;
        return defaultNode;
    }

    protected ParserNode getNodeForClass(Class<?> c)
    {
        for (ParserNode n : nodeList)
            if (c.equals(n.getClazz()))
                return n;
        return defaultNode;
    }
}
