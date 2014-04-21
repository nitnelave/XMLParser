package com.nitnelave.xmlparser;

import com.nitnelave.xmlparser.test.TestChild;
import com.nitnelave.xmlparser.test.TestProperty;
import com.nitnelave.xmlparser.test.TestPropertyNoConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author nitnelave
 */
@SuppressWarnings("ResultOfObjectAllocationIgnored")
public class ParserPropertyTest
{
    private XMLParser parser;

    @Before
    public void setUp()
    {
        parser = new XMLParser();
    }

    private ParserProperty getProperty(Class<?> clazz)
    throws XMLStructureException
    {
        return new ParserNode(parser, clazz).getProperties().get(0);
    }

    @Test
    public void testConstructor1()
    {
        try
        {
            new ParserNode(parser, TestPropertyNoConstructor.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong exception message: ",
                                "Property Prop: value type TestPropertyNoConstructor must implement " +
                                "constructor TestPropertyNoConstructor(String)", e.getMessage()
                               );
        }
    }

    @Test
    public void testGetName()
    throws Exception
    {
        ParserProperty p = getProperty(TestChild.class);
        Assert.assertEquals("Wrong name: ", "Name", p.getName());
    }

    @Test
    public void testGetKey()
    throws Exception
    {
        ParserProperty p = getProperty(TestChild.class);
        Assert.assertEquals("Wrong key: ", "name", p.getKey());
    }

    @Test
    public void testGetValueType1()
    throws Exception
    {
        ParserProperty p = getProperty(TestChild.class);
        Assert.assertEquals("Wrong value type: ", "String", p.getValueType().getSimpleName());
    }

    @Test
    public void testGetValueType2()
    throws Exception
    {
        ParserProperty p = getProperty(TestProperty.class);
        Assert.assertEquals("Wrong value type: ", "Integer", p.getValueType().getSimpleName());
    }

    @Test
    public void testIsOptional1()
    throws Exception
    {
        ParserProperty p = getProperty(TestChild.class);
        Assert.assertTrue(p.isOptional());
    }

    @Test
    public void testIsOptional2()
    throws Exception
    {
        ParserProperty p = getProperty(TestProperty.class);
        Assert.assertFalse(p.isOptional());
    }
}
