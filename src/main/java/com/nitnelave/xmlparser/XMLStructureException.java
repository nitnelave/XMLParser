package com.nitnelave.xmlparser;

/**
 * Exception raised when registering invalid XML nodes.
 * <p>
 * Exception thrown when a class does not implement every method expected,
 * or the XML structure is otherwise compromised.
 * </p>
 * <p/>
 * <p>
 * The details of the exception are in the message.
 * </p>
 *
 * @author nitnelave
 */
public class XMLStructureException extends Exception
{
    private final String message;

    /**
     * Create an exception with the given message.
     *
     * @param message
     *         The exception message.
     */
    public XMLStructureException(String message)
    {
        super();
        this.message = message;
    }

    /**
     * Get the details of the exception.
     *
     * @return A string with the message.
     */
    @Override
    public String getMessage()
    {
        return message;
    }
}
