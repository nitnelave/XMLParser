package com.nitnelave.xmlparser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Generic XML SAX parser. This class aims at simplifying greatly the use of SAX parsers,
 * through heavy use of reflection. It allows to easily create local AST or to handle the nodes as they come.
 * </p>
 * <p/>
 * <p>
 * The XML file structure is defined by annotations (see {@link XMLNode} and {@link XMLProperty})
 * on the node classes, that are then registered in the parser.<br/>
 * Listeners are simply classes that define the right methods (handles).
 * </p>
 * <p/>
 * <p>
 * <h3>Example use</h3>
 * We want to parse the following type of XML file:
 * <br/>
 * <p/>
 * &lt;Person age="3" name="John"&gt;<br/>
 * &lt;Child&gt;Eric&lt;/Child&gt;<br/>
 * &lt;Child&gt;Paul&lt;/Child&gt;<br/>
 * &lt;/Person&gt;<br/>
 * </p>
 * <p/>
 * <p>
 * For this, we create the Person class, as such:
 * <pre>
 *     // Note: Here, Person is the root node, as it has None as parentNode
 *     &#64;XMLNode(name = "Person", parentNode = com.nitnelave.xmlparser.None)
 *     &#64;XMLProperties({&#64;XMLProperty(name = "Age", key = "age", valueType = Integer.class),
 *                      &#64;XMLProperty(name = "Name", key = "name", valueType = String.class)})
 *     public class Person
 *     {
 *         Collection&lt;Child&gt; children;
 *           ...
 *         public void setAge(Integer age) { ... }
 *         public void setName(String name) { ... }
 *           ...
 *         public void addChild(Child c) { children.add(c); }
 *     }
 * </pre>
 * And the Child class:
 * <pre>
 *     &#64;XMLNode(name = "Child", parentNode = Person.class, contentType = String.class)
 *     public class Child
 *     {
 *         String name;
 *           ...
 *         public void setContent(String content) { name = content; }
 *     }
 * </pre>
 * The listener for the events:
 * <pre>
 *     public class Handler
 *     {
 *         public void handleBegin(Person p) { // got opening &lt;Person&gt; }
 *         public void handleEnd(Person p) { // got closing &lt;/Person&gt; }
 *         public void handle(Child c) { // got a whole &lt;Child&gt;&lt;/Child&gt; subtree }
 *           ...
 *     }
 * </pre>
 * Then we register the nodes, the listener, and we start the parsing:
 * <pre>
 *     XMLParser parser = new XMLParser();
 *     parser.registerNodes(Person.class, Child.class);
 *     parser.registerListener(new Handler());
 *     parser.parse(inputstream);
 * </pre>
 * </p>
 * <p/>
 * <p>
 * And that's it! You can add more nodes, more properties, a more complex hierarchy, but it's as simple as that.
 * <h3>Error handling</h3>
 * </p>
 * There are 2 special nodes:
 * <ul>
 * <li>the root node, defined by a parentNode = None, that is required before registering the
 * listeners;</li>
 * <li>the default node, defined by a name = "" and parentNode = None. If no node are recognized,
 * a default one is created.</li>
 * </ul>
 * </p>
 * <h3>Restrictions</h3>
 * <p>
 * <ul>
 * <li>The xml nodes will be created with the empty constructor. Make sure to provide a meaningful one.</li>
 * <li>The listeners must be registered after all nodes are registered, other wise the handle methods of the nodes
 * added later will be ignored.</li>
 * <li>In case of a problem in the XML structure, an {@link XMLStructureException} will be
 * raised, with a description of the problem in the message.</li>
 * <li>The classes for the node content and the properties must have a constructor taking a String as unique
 * parameter. For a simple number, use Integer.class.</li>
 * </ul>
 *
 * @author nitnelave
 * @see XMLProperty
 * @see XMLNode
 * @see XMLStructureException
 */
public class XMLParser
{
    private final Collection<Object> beginHandlers = new ArrayList<>();
    private final Collection<Object> endHandlers = new ArrayList<>();
    private ParserNode defaultNode = null;
    private ParserNode rootNode = null;
    private final Collection<ParserNode> nodeList = new ArrayList<>();
    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    /**
     * Register classes that describe XML Nodes.<br />
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
        if (rootNode == null)
            throw new XMLStructureException("No root node defined for the XML");
        Class<?> clazz = handler.getClass();
        if (Reflect.hasMethod(clazz, "handleBegin", rootNode.getClazz()))
            beginHandlers.add(handler);
        if (Reflect.hasMethod(clazz, "handleEnd", rootNode.getClazz()))
            endHandlers.add(handler);
        for (ParserNode parserNode : nodeList)
            if (Reflect.hasMethod(clazz, "handle", parserNode.getClazz()))
                parserNode.addListener(handler);
    }

    /**
     * Start parsing, taking the input from the stream given.
     *
     * @param in
     *         The stream to read from
     * @throws SAXException
     *         parsing error; see SAXParser documentation
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


    protected void setRootNode(ParserNode parserNode)
    throws XMLStructureException
    {
        if (rootNode != null)
            throw new XMLStructureException("Two root nodes defined: "
                                            + parserNode.getClazz().getName() + " and "
                                            + rootNode.getClazz().getName());
        rootNode = parserNode;
    }

    protected Iterable<Object> getBeginHandlers()
    {
        return beginHandlers;
    }

    protected Iterable<Object> getEndHandlers()
    {
        return endHandlers;
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
        assert false;
        return null;
    }

    protected boolean isRoot(ParserNode n)
    {
        return n != null && n.equals(rootNode);
    }
}
