package com.github.nitnelave.xmlparser.printer;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLNodeType;
import com.github.nitnelave.xmlparser.XMLProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nitnelave
 */
@XMLNode(type = XMLNodeType.ROOT)
@XMLProperty(name = "Id", key = "id", valueType = Integer.class, required = true)
public class Person
{
    private int id = 0;
    private Name name = new Name();
    private List<Child> children = new ArrayList<>();

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setName(Name name)
    {
        this.name = name;
    }

    public void addChild(Child child)
    {
        children.add(child);
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("<Person id=\"").append(id).append("\">").append(System.lineSeparator())
         .append("    ").append(name.toString()).append(System.lineSeparator());
        for (Child c : children)
            b.append(c.toString());
        b.append("</Person>").append(System.lineSeparator());
        return b.toString();
    }
}
