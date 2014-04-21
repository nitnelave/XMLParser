package com.nitnelave.xmlparser.test;

import com.nitnelave.xmlparser.XMLNode;
import com.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode(parentNode = TestRoot.class, single = true, contentType = String.class, resetContent = true)
@XMLProperty(name = "Name", key = "name")
public class TestChild
{
    public void setContent(String s)
    {
    }

    public void setName(String s)
    {
    }
}
