package com.github.nitnelave.xmlparser.printer;

/**
 * @author nitnelave
 */
@SuppressWarnings("UnusedDeclaration")
public class XMLReport
{
    private StringBuilder report = new StringBuilder();
    private static final String SEP = System.lineSeparator();

    public void handleBegin(Person person)
    {
        report.append("Seen a new person").append(SEP);
    }

    public void handle(Person person)
    {
        report.append("Done seeing the person").append(SEP);
    }

    public void handleBegin(Name name)
    {
        report.append("Seen a new name").append(SEP);
    }

    public void handle(Name name)
    {
        report.append("Done seeing the name").append(SEP);
    }

    public void handleBegin(Child child)
    {
        report.append("Seen a new child").append(SEP);
    }

    public void handle(Child child)
    {
        report.append("Done seeing the child").append(SEP);
    }

    public void handleBegin(Link link)
    {
        report.append("Seen a new link").append(SEP);
    }

    public void handle(Link link)
    {
        report.append("Done seeing the link").append(SEP);
    }

    public void handle(DefaultNode node)
    {
        report.append("Seen unknown node: ").append(node.toString()).append(SEP);
    }

    public String getReport()
    {
        return report.toString();
    }
}
