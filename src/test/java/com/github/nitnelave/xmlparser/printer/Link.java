package com.github.nitnelave.xmlparser.printer;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode(name = "A", parentNode = Name.class, contentType = String.class, updateContent = false)
@XMLProperty(name = "HRef", key = "href", required = true)
public class Link
{
    private String hRef = null;
    private String content = null;

    public void setHRef(String ref)
    {
        hRef = ref;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String toString()
    {
        return "<a href=\"" + hRef + "\">" + content + "</a>";
    }

}
