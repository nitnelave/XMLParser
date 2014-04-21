package com.github.nitnelave.xmlparser.printer;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLNodeType;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode(type = XMLNodeType.DEFAULT, contentType = String.class)
@XMLProperty(name = "Id", key = "id", valueType = Integer.class)
public class DefaultNode
{
    private String name = null;
    private String content = null;
    private Integer id = null;

    public void setName(String name)
    {
        this.name = name;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append('<').append(name);
        if (id != null)
            b.append(" id=\"").append(id).append('"');
        if (!content.isEmpty())
            b.append('>').append(content).append("</").append(name).append('>');
        else
            b.append(" />");
        return b.toString();
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}
