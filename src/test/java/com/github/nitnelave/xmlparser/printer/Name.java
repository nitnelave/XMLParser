package com.github.nitnelave.xmlparser.printer;

import com.github.nitnelave.xmlparser.XMLNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nitnelave
 */
@XMLNode(single = true, contentType = String.class, resetContent = true, parentNodes = {Child.class, Person.class})
public class Name
{
    List<Object> content = new ArrayList<>();

    public void setContent(String text)
    {
        content.add(text);
    }

    public void addA(Link l)
    {
        content.add(l);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<name");
        if (content.size() == 1 && content.get(0).toString().isEmpty())
            builder.append(" />");
        else
        {
            builder.append('>');
            for (Object o : content)
                builder.append(o.toString());
            builder.append("</name>");
        }
        return builder.toString();
    }
}
