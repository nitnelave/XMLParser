package com.github.nitnelave.xmlparser.printer;

import com.github.nitnelave.xmlparser.XMLNode;
import com.github.nitnelave.xmlparser.XMLProperties;
import com.github.nitnelave.xmlparser.XMLProperty;

/**
 * @author nitnelave
 */
@XMLNode(parentNodes = Person.class)
@XMLProperties({@XMLProperty(name = "Id", key = "id", valueType = Integer.class, required = true),
                @XMLProperty(name = "Age", key = "age", valueType = Integer.class)})
public class Child
{
    private Name name = null;
    private int id = 0;
    private Integer age = null;

    public void setName(Name name)
    {
        this.name = name;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("    <Child id=\"").append(id).append('"');
        if (age != null)
            b.append(" age=\"").append(age).append('"');
        if (name != null)
            b.append('>').append(System.lineSeparator()).append("        ")
             .append(name.toString()).append(System.lineSeparator())
             .append("    </Child>").append(System.lineSeparator());
        else
            b.append(" />").append(System.lineSeparator());
        return b.toString();
    }
}
