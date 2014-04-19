package com.nitnelave.xmlparser;

/**
 * <p>
 *     Exception thrown when a class does not implement every method expected,
 *     or the XML structure is otherwise compromised.
 * </p>
 *
 * <p>
 *     The details of the exception are in the message.
 * </p>
 *
 * @author nitnelave
 */
public class XMLStructureException extends Exception
{
    private final String message;

    /**
     * Create an exception with the given message.
     * @param s
     */
    public XMLStructureException(String s)
    {
        super();
        message = s;
    }

    /**
     * Get the details of the exception.
     * @return A string with the message.
     */
    @Override
    public String getMessage()
    {
        return message;
    }
}
