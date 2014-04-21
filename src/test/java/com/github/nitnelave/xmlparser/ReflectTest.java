package com.github.nitnelave.xmlparser;

import com.github.nitnelave.xmlparser.test.TestChild;
import com.github.nitnelave.xmlparser.test.TestProperty;
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
    public void testSetFromString1()
    {
        TestProperty property = new TestProperty();
        Assert.assertEquals(0, property.getName());
        Reflect.setFromString(property, "Name", Integer.class, "42");
        //noinspection MagicNumber
        Assert.assertEquals(42, property.getName());
    }

    @Test
    public void testSetFromString2()
    {
        TestChild child = new TestChild();
        Assert.assertEquals("", child.getName());
        Reflect.setFromString(child, "Name", Integer.class, "42");
        Assert.assertEquals("", child.getName());
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
