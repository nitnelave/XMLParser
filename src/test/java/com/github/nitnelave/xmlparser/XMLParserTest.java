package com.github.nitnelave.xmlparser;

import com.github.nitnelave.xmlparser.printer.DefaultNode;
import com.github.nitnelave.xmlparser.printer.Person;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author nitnelave
 */
public class XMLParserTest
{
    private XMLParser parser = null;

    @Before
    public void setUp()
    {
        parser = new XMLParser();
    }

    @Test
    public void testGetNode1()
    {
        assertNull(parser.getNode("toto"));
    }

    @Test
    public void testGetNode2()
    throws XMLStructureException
    {
        parser.registerNodes(Person.class);
        assertNull(parser.getNode("toto"));
    }

    @Test
    public void testGetNode3()
    throws XMLStructureException
    {
        parser.registerNodes(Person.class);
        ParserNode node = parser.getNode("Person");
        assertEquals("Person", node.getName());
    }

    @Test
    public void testGetNodeForClass1()
    throws Exception
    {
        assertNull(parser.getNodeForClass(String.class));
    }

    @Test
    public void testGetNodeForClass2()
    throws Exception
    {
        parser.registerNodes(Person.class);
        assertNull(parser.getNodeForClass(String.class));
    }

    @Test
    public void testGetNodeForClass3()
    throws Exception
    {
        parser.registerNodes(Person.class);
        ParserNode node = parser.getNodeForClass(Person.class);
        assertEquals("Person", node.getName());
    }

    @Test
    public void testSetDefaultNode()
    throws Exception
    {
        ParserNode defaultNode = new ParserNode(parser, DefaultNode.class);
        ParserNode node = parser.getNodeForClass(Person.class);
        assertEquals(defaultNode, node);
    }

    @Test
    public void testRegisterNodes()
    throws XMLStructureException
    {
        parser.registerNodes(Person.class);
        assertEquals("Person", parser.getNode("Person").getName());
    }

    @Test
    public void testRegisterPackage()
    throws XMLStructureException
    {
        parser.registerPackage("com.github.nitnelave.xmlparser.printer");
        assertEquals("Person", parser.getNode("Person").getName());
    }
}
