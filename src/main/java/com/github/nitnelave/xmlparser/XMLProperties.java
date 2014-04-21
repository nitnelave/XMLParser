package com.github.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * Annotation to describe XML node properties.
 * <p>
 * This annotation allows to define the properties of an XML node,
 * e.g. the "name" and "age" properties of the tag <br />
 * &lt;Person age="3" name="Arthur" /&gt;
 * </p>
 * <p>
 * This annotation is just a placeholder to allow to define several
 * {@link XMLProperty} for a {@link XMLNode}.
 * The syntax is as such:
 * <pre>
 *     &#64;XMLProperties({&#64;XMLProperty( ... ),
 *                     &#64;XMLProperty( ... )})
 *     public class Foo { ... }
 * </pre>
 * </p>
 *
 * @author nitnelave
 * @see XMLNode
 * @see XMLProperty
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XMLProperties
{
    /**
     * The list of properties of the tag.
     *
     * @return The list of properties.
     */
    XMLProperty[] value();
}
