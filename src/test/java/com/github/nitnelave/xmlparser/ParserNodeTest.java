package com.github.nitnelave.xmlparser;

import com.github.nitnelave.xmlparser.test.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author nitnelave
 */
public class ParserNodeTest
{

    private XMLParser parser = null;

    @Before
    public void setUp()
    {
        parser = new XMLParser();
    }

    @Test
    public void testConstructor1()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestRoot.class);
        } catch (XMLStructureException e)
        {
            Assert.fail();
        }
    }

    @Test
    public void testConstructor2()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestNoXML.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong error message: ",
                                "Trying to register class TestNoXML " +
                                "that does not have the XMLNode annotation", e.getMessage()
                               );
        }
    }

    @Test
    public void testConstructor3()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestRootParent.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong error message: ",
                                "TestRootParent: " +
                                "Trying to register a root node with a parent node",
                                e.getMessage()
                               );
        }
    }

    @Test
    public void testConstructor4()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestDefaultParent.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong error message: ",
                                "TestDefaultParent: " +
                                "Trying to register a default node with a parent node",
                                e.getMessage()
                               );
        }
    }

    @Test
    public void testConstructor5()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestContentNoConstructor.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong error message: ",
                                "XMLNode TestContentNoConstructor: content type " +
                                "TestContentNoConstructor " +
                                "must implement constructor " +
                                "TestContentNoConstructor(String)",
                                e.getMessage()
                               );
        }
    }


    @Test
    public void testConstructor6()
    {
        try
        {
            //noinspection ResultOfObjectAllocationIgnored
            new ParserNode(parser, TestContentNoSetter.class);
            Assert.fail();
        } catch (XMLStructureException e)
        {
            Assert.assertEquals("Wrong error message: ",
                                "XMLNode: TestContentNoSetter is missing setter setContent(TestContentNoSetter)",
                                e.getMessage()
                               );
        }
    }

    @Test
    public void testGetName1()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong node name: ", "Root", new ParserNode(parser, TestRoot.class).getName());
    }

    @Test
    public void testGetName2()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong node name: ", "TestNoName", new ParserNode(parser, TestNoName.class).getName());

    }

    @Test
    public void testGetClazz()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong class: ", "TestNoName", new ParserNode(parser, TestNoName.class)
                .getClazz().getSimpleName());
    }

    @Test
    public void testIsSuperClazz1()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestRoot.class).isSuperClazz(TestRoot.class));
    }

    @Test
    public void testIsSuperClazz2()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestChild.class).isSuperClazz(TestRoot.class));
    }

    @Test
    public void testGetValueClazz1()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong class: ", "String", new ParserNode(parser, TestChild.class)
                .getValueClazz().getSimpleName());
    }

    @Test
    public void testGetValueClazz2()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong class: ", "None", new ParserNode(parser, TestRoot.class)
                .getValueClazz().getSimpleName());
    }

    @Test
    public void testHasContent1()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestChild.class).hasContent());
    }

    @Test
    public void testHasContent2()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestRoot.class).hasContent());
    }

    @Test
    public void testGetProperties1()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong number of properties: ", 0,
                            new ParserNode(parser, TestRoot.class).getProperties().size());
    }


    @Test
    public void testGetProperties2()
    throws XMLStructureException
    {
        Assert.assertEquals("Wrong number of properties: ", 1,
                            new ParserNode(parser, TestChild.class).getProperties().size());
    }

    @Test
    public void testCall1()
    throws XMLStructureException
    {
        ParserNode root = new ParserNode(parser, TestRoot.class);
        TestHandler handler = new TestHandler();
        root.addListener(handler);
        root.call(new TestRoot(), false);
        root.call(new TestRoot(), false);
        Assert.assertEquals("Wrong number of calls: ", 2, handler.getCallCount());
        Assert.assertEquals("Wrong number of calls to begin: ", 0, handler.getCallBeginCount());
    }

    @Test
    public void testCall2()
    throws XMLStructureException
    {
        ParserNode root = new ParserNode(parser, TestRoot.class);
        TestHandler handler = new TestHandler();
        root.addListener(handler);
        root.call(new TestRoot(), true);
        root.call(new TestRoot(), true);
        Assert.assertEquals("Wrong number of calls: ", 0, handler.getCallCount());
        Assert.assertEquals("Wrong number of calls to begin: ", 2, handler.getCallBeginCount());
    }

    @Test
    public void testIsRoot1()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestRoot.class).isRoot());
    }

    @Test
    public void testIsRoot2()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestChild.class).isRoot());
    }

    @Test
    public void testHasParent1()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestRoot.class).hasParent());
    }

    @Test
    public void testHasParent2()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestChild.class).hasParent());
    }

    @Test
    public void testShouldUpdateContent1()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestRoot.class).shouldUpdateContent());
    }

    @Test
    public void testShouldUpdateContent2()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestChild.class).shouldUpdateContent());
    }

    @Test
    public void testShouldResetContent1()
    throws XMLStructureException
    {
        Assert.assertTrue(new ParserNode(parser, TestChild.class).shouldResetContent());

    }

    @Test
    public void testShouldResetContent2()
    throws XMLStructureException
    {
        Assert.assertFalse(new ParserNode(parser, TestRoot.class).shouldResetContent());

    }
}
