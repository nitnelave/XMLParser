package com.github.nitnelave.xmlparser.test;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperty;

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
