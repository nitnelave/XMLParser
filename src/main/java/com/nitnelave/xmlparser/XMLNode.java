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
     * The name of the xml tag to recognize, and the name of the parent method to set the child.
     * As xml is case insensitive, it is recommended to type this field in CamelCase. <br />
     * I.e. if the field is Node, then the method addNode(Node n) of the parent node will be called if found.
     *
     * @return The name of the xml tag
     */
    public String name();

    /**
     * The class of the parent node in the XML structure. If the class is {@link None}, then the node is marked as root.
     * <br />
     * If the name is also "", then the node is the default (error handling) one instead.
     *
     * @return The parent node's class
     */
    public Class<?> parentNode();

    /**
     * Optional content. If the contentType is not {@link None )}, then the class must define a
     * method setContent(String s).
     * @return The content class.
     */
    public Class<?> contentType() default None.class;
}
