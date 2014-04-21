package com.github.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * Annotation to describe a single XML node property.
 * <p>
 * This annotation describes a property of the XML tag. It must be encapsulated in an XMLProperties
 * to allow for several properties.
 * </p>
 * <p>The values of properties (see {@link #valueType()}) must implement a constructor that takes a String.
 * It will be called with the value of the property. If an exception is thrown during the construction
 * of the value, the method will be called with null as parameter.</p>
 * <p>
 * For example, the tag <br />
 * {@code <Person name="John" age="18" />}<br />
 * could be described by the annotation:
 * <pre>
 *     &#64;XMLNode
 *     &#64;XMLProperties({&#64;XMLProperty(name = "Age", key = "age", valueType = Integer.class),
 *                     &#64;XMLProperty(name = "Name", key = "name", valueType = String.class, required = true)})
 *     public class Person { ... }
 * </pre>
 * This means that Person is a root node (see {@link XMLNode}), and has 2 properties:
 * <ul>
 * <li>Age, referenced by the xml key "age", that is an integer.
 * Because of the name, the method called to register the property will be setAge(Integer age).</li>
 * <li>Name, referenced by the xml key "name", a String. This property is required,
 * and an exception will be raised if it is absent.</li>
 * </ul>
 * </p>
 *
 * @author nitnelave
 * @see XMLNode
 * @see XMLProperties
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XMLProperty
{
    /**
     * The name of the property.
     * Corresponds to the setter signature, i.e. if the name is "MaxAge" (note the CameCase),
     * the expected setter is <br />
     * <pre>public void setMaxAge(Integer age)</pre>
     *
     * @return The property name.
     */
    public String name();

    /**
     * The key to find the property in the XML. If the tag to parse is <br />
     * &lt;Node max_age="3" /&gt;<br />
     * the key would be "max_age".
     *
     * @return The property key.
     */
    public String key();

    /**
     * The type of the data associated with the property. The class must define a constructor with a String as
     * only argument.
     *
     * @return The data class.
     */
    public Class<?> valueType() default String.class;

    /**
     * Property required flag.
     * If true, then a node missing this property will raise an exception.
     *
     * @return Whether this property is required.
     */
    public boolean required() default false;
}
