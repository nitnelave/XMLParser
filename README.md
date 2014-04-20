XMLParser
=========

XML Parser is (as the name implies) a XML Parsing library, for Java (v 7 and
up), based on SAX XML parsing.

This projects aims at keeping the advantages of SAX parsing (namely the low
memory footprint and the on-the-fly parsing) while greatly simplifying the
interface, through the use of annotations and reflection.

The advantage of SAX parsers is that the document does not need to be entirely
known at the time of the parsing; the document is parsed as it is read, which
allows for streamable documents, such as messages in a network communication.
The other side of the coin is that to use a SAX parser, many small-scale
actions are required, such as buffering the text inside a node.

This package provides a overlay around the SAX parser, to take care of the
finer details, storing the nodes and their properties directly in classes using
reflection, and telling a listener when each node is done parsing. A simple
interface using annotation to reflect the document structure, along with
natural method names will allow a simple parsing in no time.

Resources:
* [Project homepage](https://github.com/nitnelave/XMLParser)
* [Bug tracker](https://github.com/nitnelave/XMLParser)
* [Javadoc](http://nitnelave.github.io/XMLParser)

Usage
-----

The project documentation provides a complete description of the interface
needed, as well as an example in the package documentation.

Using Maven:

Add the project as a dependency in your pom.xml:
<dependency>
  <groupId>com.nitnelave.xmlparser</groupId>
  <artifactId>xmlparser</artifactId>
  <version>/* insert the latest version number here */</version>
</dependency>

Manually:

Download the jar file, and add it as external library.

Authors
-------

See [AUTHORS.md](https://github.com/nitnelave/XMLParser/AUTHORS.md) for a list
of authors.

License
-------

XMLParser is licensed under the terms of the Lesser GNU Public License version
3.0
