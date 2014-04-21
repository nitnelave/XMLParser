package com.github.nitnelave.xmlparser.printer;

/**
 * @author nitnelave
 */
public class XMLPrinter
{
    private StringBuilder xml = new StringBuilder();

    public void handle(Person person)
    {
        xml.append(person.toString());
    }

    public String getXML()
    {
        return xml.toString();
    }
}

