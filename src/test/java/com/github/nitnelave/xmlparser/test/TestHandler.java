package com.github.nitnelave.xmlparser.test;

/**
 * @author nitnelave
 */
public class TestHandler
{

    private int callCount = 0;
    private int callBeginCount = 0;

    public void handleBegin(TestRoot root)
    {
        ++callBeginCount;
    }

    public void handle(TestRoot root)
    {

        ++callCount;
    }

    public void handleBegin(TestChild child)
    {

        ++callBeginCount;
    }

    public void handle(TestChild child)
    {

        ++callCount;
    }

    public int getCallCount()
    {
        return callCount;
    }

    public int getCallBeginCount()
    {
        return callBeginCount;
    }
}
