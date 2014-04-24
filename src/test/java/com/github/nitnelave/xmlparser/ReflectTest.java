package com.github.nitnelave.xmlparser;

import com.github.nitnelave.xmlparser.test.TestRoot;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author nitnelave
 */
public class ReflectTest
{
    @Test
    public void testNewInstance1()
    throws Exception
    {
        Assert.assertEquals("", Reflect.newInstance(String.class));
    }

    @Test
    public void testNewInstance2()
    throws Exception
    {
        try
        {
            Reflect.newInstance(None.class);
        } catch (SAXException e)
        {
            Assert.assertEquals("Could not instantiate class None", e.getMessage());
        }
    }

    @Test
    public void testCall()
    {
        try
        {
            Reflect.call(new TestRoot(), "nonExistentMethod", new TestRoot());
        } catch(Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void testHasMethod1()
    {
        Assert.assertTrue(Reflect.hasMethod(Reflect.class, "call", Object.class, String.class, Object.class));
    }

    @Test
    public void testHasMethod2()
    {
        Assert.assertFalse(Reflect.hasMethod(Reflect.class, "toto"));
    }

    @Test
    public void testHasConstructor1()
    {
        Assert.assertTrue(Reflect.hasConstructor(Integer.class, String.class));
    }

    @Test
    public void testHasConstructor2()
    {
        Assert.assertFalse(Reflect.hasConstructor(Integer.class, Reflect.class));
    }
}
