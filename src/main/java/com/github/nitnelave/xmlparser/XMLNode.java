package com.github.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * <p>
 * Annotation to describe an XML node structure.
 * For the properties, see {@link XMLProperties}.
 * </p>
 * There are 2 special types of nodes:
 * <ul>
 * <li>Root nodes, defined by {@code type = XMLNodeType.ROOT}.
 * Root nodes cannot have parents, and will raise an exception if read as an inner node.</li>
 * <li>Default node, defined by {@code type = XMLNodeType.DEFAULT}. It must be unique.
 * If an unknown XML tag is recognized, a default node is created.
 * Its name attribute will be ignored, but if the class provides a {@code setName(String name)} method,
 * it will be called with the name of the tag.
 * The default node can have properties, but they cannot be required.</li>
 * </ul>
 * By default, a node can have any other node as parent. If the parentNode is defined,
 * then it can only be parsed as a direct child of its {@code parentNode}, or it will raise an exception.
 * <p>
 * Upon reading the opening tag, the handleBegin(MyNode n) will be called for every applicable registered listener.
 * At this point, the node only has information about its properties.
 * </p>
 * <p>
 * Upon reading the closing tag, the handle(MyNode n) will be called for every applicable registered listener.
 * At this point, the node has all information available: properties, content, and sub-nodes.
 * </p>
 * <p>
 * If a node is missing a property marked required, any corresponding XML tag missing the property
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
 * @see XMLParser
 * @see XMLProperty
 * @see XMLProperties
 * @see XMLStructureException
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XMLNode
{
    /**
     * The name of the XML tag to recognize, and the name of the parent method to set the child.
     * As xml is case insensitive, it is recommended to type this field in CamelCase to have a nice method name.<br/>
     * E.g. if the field is Node, then the method addNode(Node n) of the parent node will be called if found.
     * <p/>
     * If the field is left empty, then the name will default to the class short name.
     * NB: The prefix of the method can be changed with the {@link #single() single} field.
     *
     * @return The name of the XML tag
     */
    public String name() default "";

    /**
     * The type of the node.
     * Either NORMAL (by default), ROOT or DEFAULT.
     *
     * @return The type of the node.
     */
    public XMLNodeType type() default XMLNodeType.NORMAL;

    /**
     * The class of the parent node in the XML structure. <br />
     * If the class is {@link None} (by default), then the node can have any node as parent.
     *
     * @return The parent node's class
     */
    public Class<?>[] parentNodes() default {};

    /**
     * The arity of the node. <br />
     * Determines the name of the method called when adding the node to its parent node. <br />
     * If it is false (default), the name of the method is "add" + {@link #name() name}; <br />
     * if true, it is "set" + {@link #name() name};
     *
     * @return True if it is a single node.
     */
    public boolean single() default false;

    /**
     * The class of the (optional) content. <br />
     * This defines what object will be constructed from the content inside the XML tag.
     * If the contentType is not {@link None}, then the class must define a method setContent(String s).
     *
     * @return The content class.
     */
    public Class<?> contentType() default None.class;

    /**
     * Whether to update the content when seeing a child.
     * Whenever a child of the node is seen, update the content. This allows to see the position of
     * inner child nodes inside the content.
     *
     * @return Whether to update the content when seeing a child.
     */
    public boolean updateContent() default true;

    /**
     * Whether to reset the content when seeing a child.
     * Whenever a child of the node is seen, reset the buffer (only applicable if updateContent = true).
     * If that's the case, then the same text will not be reported twice when updating the content.
     * <p/>
     * For example, for this file:
     * <pre>
     *     {@code
     *     <Container>
     *         The car is <color>red</color>. I like that car.
     *     </Container>
     *     }
     * </pre>
     * The content of Container is split by a child node. So calls to container will be:
     * <ul>
     * <li>resetContent = true:
     * <ul>
     * <li>container.setContent("The car is ");</li>
     * <li>container.setColor(Color red);</li>
     * <li>container.setContent(". I like that car");</li>
     * </ul></li>
     * <li>resetContent = false:
     * <ul>
     * <li>container.setContent("The car is ");</li>
     * <li>container.setColor(Color red);</li>
     * <li>container.setContent("The car is . I like that car");</li>
     * </ul></li>
     * </ul>
     *
     * @return Whether to update the content when seeing a child.
     */
    public boolean resetContent() default false;
}
