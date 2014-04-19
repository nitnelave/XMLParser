package com.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * <p>
 * Annotation to describe an XML node structure.
 * For the properties, see {@link XMLProperties}.
 * </p>
 *
 * @author nitnelave
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
