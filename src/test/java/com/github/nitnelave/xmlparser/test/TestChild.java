package com.github.nitnelave.xmlparser.test;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode(parentNodes = TestRoot.class, single = true, contentType = String.class, resetContent = true)
@XMLProperty(name = "Name", key = "name")
public class TestChild
{
    private String name = "";

    public void setContent(String s)
    {
    }

    public void setName(String s)
    {
        name = s;
    }

    public String getName()
    {
        return name;
    }
}
