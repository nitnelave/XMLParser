/**
 * This package aims at simplifying greatly the use of SAX parsers,
 * through heavy use of reflection. It allows to easily create local AST or to handle the nodes as they come.
 * </p>
 * <p>
 *     The advantage of SAX parsers is that the document does not need to be entirely known
 *     at the time of the parsing; the document is parsed as it is read, which allows for
 *     streamable documents, such as messages in a network communication.
 *     The other side of the coin is that to use a SAX parser, many small-scale actions are
 *     required, such as buffering the text inside a node.
 * </p>
 * <p>
 *     This package provides a overlay around the SAX parser, to take care of the finer details,
 *     storing the nodes and their properties directly in classes using reflection, and telling
 *     a listener when each node is done parsing. A simple interface using annotation to reflect
 *     the document structure, along with natural method names will allow a simple parsing in no
 *     time.
 * </p>
 * <p/>
 * <p>
 * The XML file structure is defined by annotations (see {@link com.nitnelave.xmlparser.XMLNode}
 * and {@link com.nitnelave.xmlparser.XMLProperty})
 * on the node classes, that are then registered in the parser.<br/>
 * Listeners are simply classes that define the right methods (handles).
 * </p>
 * <p>
 * For the listener's interface, 2 methods are possible:
 * <ul>
 *     <li>{@code handleBegin(MyNode n)} is called when the <b>opening tag</b> of the node is found.
 *     The node contains only information about its properties, not its content or sub-nodes.</li>
 *     <li>{@code handle(MyNode n)} is called when the <b>closing tag</b> of the node is found.
 *     All the information about the node is now known.</li>
 * </ul><br />
 * The methods for every node are all optional. It is entirely possible to decide to handle
 * one node in a listener, and another in another class, or to implement only {@code handle()} without
 * implementing {@code handleBegin()}.
 * </p>
 * <h3>Restrictions</h3>
 * <ul>
 * <li>The listeners must be registered after all nodes are registered, otherwise the handle methods of the nodes
 * added later will be ignored.</li>
 * <li>In case of a problem in the XML structure, an {@link com.nitnelave.xmlparser.XMLStructureException} will be
 * raised, with a description of the problem in the message.</li>
 * </ul>
 * <p>
 * <h3>Example use</h3>
 * We want to parse the following type of XML file:
 * <br/>
 * <p>
 * <pre>
 * {@literal
 * <Person age="3" name="John">
 *     <Child age="2">Eric</Child>
 *     <Child>Paul</Child>
 * </Person>
 * }</pre>
 * </p>
 * <p/>
 * For this, we create the Person class, as such:
 * <pre>
 *     &#64;XMLNode(name = "Person", type = XMLNodeType.ROOT)
 *     &#64;XMLProperties({&#64;XMLProperty(name = "Age", key = "age", valueType = Integer.class, required = true),
 *                     &#64;XMLProperty(name = "Name", key = "name", valueType = String.class, required = true)})
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
 *     &#64;XMLNode(parentNode = Person.class, contentType = String.class)
 *     &#64;XMLProperty(name = "Age", key = "age", valueType = Integer.class)
 *     public class Child
 *     {
 *         String name;
 *           ...
 *         public void setContent(String content) { name = content; }
 *         public void setAge(Integer age) { ... }
 *     }
 * </pre>
 * Let's add some error checking, in case of unrecognized nodes:
 * <pre>
 *     &#64;XMLNode(type = XMLNodeType.DEFAULT)
 *     public class UnknownNode
 *     {
 *         String name;
 *         public void setName(String name) { this.name = name; };
 *         public String getName() { return name; };
 *     }
 * </pre>
 * The listener for the events:
 * <pre>
 *     public class Listener
 *     {
 *         public void handleBegin(Person p) { // got opening &lt;Person&gt; }
 *         public void handle(Person p) { // got closing &lt;/Person&gt; }
 *         public void handle(Child c) { // got a whole &lt;Child&gt;&lt;/Child&gt; subtree }
 *         public void handle(UnknownNode n) { System.err.println("Unknown node: " + n.getName()); }
 *           ...
 *     }
 * </pre>
 * Then we register the nodes, the listener, and we start parsing:
 * <pre>
 *     XMLParser parser = new XMLParser();
 *     parser.registerNodes(Person.class, Child.class);
 *     parser.registerListener(new Handler());
 *     parser.parse(inputstream);
 * </pre>
 * <p/>
 * <p>
 * And that's it! You can add more nodes, more properties, a more complex hierarchy, but it's as simple as that.
 * </p>
 * @author nitnelave
 * @see com.nitnelave.xmlparser.XMLParser
 * @see com.nitnelave.xmlparser.XMLNode
 * @see com.nitnelave.xmlparser.XMLProperty
 */
package com.nitnelave.xmlparser;
