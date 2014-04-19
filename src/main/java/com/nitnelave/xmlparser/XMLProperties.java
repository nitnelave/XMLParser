package com.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * Annotation to describe XML node properties.
 * <p>
 * This annotation allows to define the properties of an XML node, i.e. the name and age properties of the tag <br />
 * &lt;Person age="3" name="Arthur"&gt;
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
