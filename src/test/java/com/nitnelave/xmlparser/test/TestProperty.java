package com.nitnelave.xmlparser.test;

import com.nitnelave.xmlparser.XMLNode;
import com.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode
@XMLProperty(name = "Name", key = "name", valueType = Integer.class, required = true)
public class TestProperty
{
    public void setName(Integer name)
    {
    }
}
