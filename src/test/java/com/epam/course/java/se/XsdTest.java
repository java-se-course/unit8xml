package com.epam.course.java.se;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XsdTest {
    @Test
    public void checkXml() throws SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema =
                schemaFactory.newSchema(getClass().getClassLoader().getResource("schema/notes.xsd"));
        final Validator validator = schema.newValidator();

        final Source xmlSource = new StreamSource(getClass().getClassLoader().getResource("xml/test_notes.xml").toExternalForm());

        try {
            validator.validate(xmlSource);
            System.out.println(xmlSource.getSystemId() + " is valid");
        } catch (IOException e) {
            System.out.println(xmlSource.getSystemId() + " is not valid");
            System.out.println("Reason: " + e.getLocalizedMessage());
        }
    }
}
