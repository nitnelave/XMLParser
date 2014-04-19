package com.nitnelave.xmlparser;

import java.lang.annotation.*;

/**
 * Created on 4/19/14
 *
 * This annotation describes a property of the XML tag. It must be encapsulated in an XMLProperties
 * to allow for several properties.
 *
 * @author nitnelave
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
     * @return The property name.
     */
    public String name();

    /**
     * The key to find the property in the XML. If the tag to parse is <br />
     * &lt;Node max_age="3" /&gt;<br />
     * the key would be "max_age".
     * @return The property key.
     */
    public String key();

    /**
     * The type of the data associated with the property. The class must define a constructor with a String as
     * only argument.
     * @return The data class.
     */
    public Class<?> valueType();
}
