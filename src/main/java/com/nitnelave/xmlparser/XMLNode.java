package com.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * <p>
 * Annotation to describe an XML node structure.
 * For the properties, see {@link XMLProperties}.
 * </p>
 * There are 3 special types of nodes:
 * <ul>
 * <li>Root nodes, defined by {@code parentNode = RootNode.class}.
 * Root nodes cannot have parents, and will raise an exception if read as an inner node.</li>
 * <li>Generic child nodes, defined by {@code parentNode = None.class}.
 * These can have parents of any type.</li>
 * <li>Default node, defined by {@code parentNode = DefaultNode.class}. It must be unique.
 * If an unknown XML tag is recognized, a default node is created.
 * Its name attribute will be ignored, but if the class provides a {@code setName(String name)} method,
 * it will be called with the name of the tag.
 * The default node can have properties, but they cannot be required.</li>
 * </ul>
 * Otherwise, a node can only be parsed as a direct child of its {@code parentNode}, or it will raise an exception.
 * <p>
 * Upon reading the opening tag, the handleBegin(MyNode n) will be called for every applicable registered listener.
 * At this point, the node only has information about its properties.
 * </p>
 * <p>
 * Upon reading the closing tag, the handle(MyNode n) will be called for every applicable registered listener.
 * At this point, the node has all information available: properties, content, and sub-nodes.
 * </p>
 * <p>
 *     If a node is missing a property marked required, any corresponding XML tag missing the property
 * will raise an exception upon parsing.
 * </p>
 * <H4>Constructors</H4>
 * <ul>
 * <li>The XML nodes will be created with the empty constructor. Make sure to provide a meaningful one.</li>
 * <li>The classes for the node content and the properties must have a constructor taking a {@code String} as unique
 * parameter. For a simple number, use {@code Integer.class}.</li>
 * </ul>
 *
 * @author nitnelave
 * @see com.nitnelave.xmlparser.XMLParser
 * @see com.nitnelave.xmlparser.XMLProperty
 * @see com.nitnelave.xmlparser.XMLProperties
 * @see com.nitnelave.xmlparser.XMLStructureException
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XMLNode
{
    /**
     * The name of the XML tag to recognize, and the name of the parent method to set the child.
     * As xml is case insensitive, it is recommended to type this field in CamelCase. <br />
     * I.e. if the field is Node, then the method addNode(Node n) of the parent node will be called if found.
     * <p/>
     * NB: The prefix of the method can be changed with the {@link #single() single} field.
     *
     * @return The name of the XML tag
     */
    public String name();

    /**
     * The class of the parent node in the XML structure. <br />
     * If the class is {@link RootNode}, then the node is marked as root, and likewise for {@link DefaultNode}.
     *
     * @return The parent node's class
     */
    public Class<?> parentNode();

    /**
     * The class of the (optional) content. <br />
     * This defines what object will be constructed from the content inside the XML tag.
     * If the contentType is not {@link None}, then the class must define a method setContent(String s).
     *
     * @return The content class.
     */
    public Class<?> contentType() default None.class;

    /**
     * The arity of the node. <br />
     * Determines the name of the method called when adding the node to its parent node. <br />
     * If it is false (default), the name of the method is "add" + {@link #name() name}; <br />
     * if true, it is "set" + {@link #name() name};
     *
     * @return True if it is a single node.
     */
    public boolean single() default false;
}
