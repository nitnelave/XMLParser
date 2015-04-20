package com.github.nitnelave.xmlparser.test;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode
@XMLProperty(name="Prop1", key="prop1", valueType = Integer.class, required = true)
@XMLProperty(name="Prop2", key="prop2", valueType = Integer.class, required = true)
public class TestProperties
{
    public void setProp1(Integer prop1)
    {}

    public void setProp2(Integer prop2)
    {}
}
