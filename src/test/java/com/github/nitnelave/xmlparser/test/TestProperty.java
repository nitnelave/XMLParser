package com.github.nitnelave.xmlparser.test;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode
@XMLProperty(name = "Name", key = "name", valueType = Integer.class, required = true)
public class TestProperty
{
    private int name = 0;

    public void setName(Integer name)
    {
        this.name = name;
    }

    public int getName()
    {
        return name;
    }
}
