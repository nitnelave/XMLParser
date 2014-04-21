package com.github.nitnelave.xmlparser;

import com.github.nitnelave.xmlparser.printer.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author nitnelave
 */
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class ParsingTest
{
    private XMLParser parser = null;
    private XMLPrinter printer = null;
    private XMLReport reporter = null;

    @Before
    public void setUp()
    throws XMLStructureException
    {
        parser = new XMLParser();
        parser.registerNodes(Child.class, DefaultNode.class, Link.class, Name.class, Person.class);
        printer = new XMLPrinter();
        reporter = new XMLReport();
        parser.registerListener(printer);
        parser.registerListener(reporter);
    }

    private void parseFile(String path)
    throws ParserConfigurationException, SAXException, IOException
    {
        FileInputStream in = new FileInputStream(path);
        parser.parse(in);
    }

    private String readFile(String path)
    throws IOException
    {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String s;
            //noinspection NestedAssignment
            while ((s = reader.readLine()) != null)
                builder.append(s).append(System.lineSeparator());
        }
        return builder.toString();
    }

    private void testFile(String input, String ref, String report)
    throws IOException, ParserConfigurationException, SAXException
    {
        parseFile(input);
        System.out.println(report);
        System.out.println(reporter.getReport());
        Assert.assertEquals("Printer failed: ", readFile(ref), printer.getXML());
        Assert.assertEquals("Report failed: ", readFile(report), reporter.getReport());
    }

    private void testFile(String file)
    throws ParserConfigurationException, SAXException, IOException
    {
        String f = "src/test/resources/" + file + '.';
        testFile(f + "xml", f + "xml", f + "report");
    }

    @Test
    public void testSimple()
    throws Exception
    {
        testFile("simple");
    }

    @Test
    public void testChildren()
    throws Exception
    {
        testFile("children");
    }

    @Test
    public void testContent()
    throws Exception
    {
        testFile("content");
    }

    @Test
    public void testMixedContent()
    throws Exception
    {
        testFile("mixed-content");
    }
}
