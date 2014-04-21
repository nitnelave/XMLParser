package com.nitnelave.xmlparser.test;

import com.nitnelave.xmlparser.XMLNode;
import com.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode
@XMLProperty(name = "Prop", key = "prop", valueType = TestPropertyNoConstructor.class)
public class TestPropertyNoConstructor
{
    public void setProp(TestPropertyNoConstructor testPropertyNoConstructor)
    {
    }
}
