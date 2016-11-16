package com.epam.course.java.se;

import lombok.Builder;
import lombok.Value;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DomTest {
    private static enum TagName {
        NOTES, NOTE, TO, FROM, HEADING, BODY, META, PRIORITY, PRIVATE
    }

    private static enum Priority {
        LOW, HIGH
    }

    @Value
    @Builder
    private static class Meta {
        Priority priority;
        boolean priv;
    }

    @Value
    @Builder
    private static class Note {
        Meta meta;
        int id;
        String to;
        String from;
        String heading;
        String body;
    }


    @Test
    public void test() throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        final InputStream resourceAsStream =
                getClass().getClassLoader().getResourceAsStream("xml/test_notes_stax.xml");
        final Document document = documentBuilder.parse(resourceAsStream);

        final Element root = document.getDocumentElement();

        final List<Note> notes = getNotes(root.getElementsByTagName("note"));

        for (Note note : notes) {
            System.out.println(note);
        }
    }

    private List<Note> getNotes(NodeList nodes) {
        final List<Note> notes = new ArrayList<>(nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            notes.add(getNote((Element) nodes.item(i)));
        }

        return notes;
    }

    private Note getNote(Element item) {
        return Note.builder()
                .id(Integer.parseInt(item.getAttribute("id")))
                .to(item.getElementsByTagName("to").item(0).getTextContent())
                .from(item.getElementsByTagName("from").item(0).getTextContent())
                .heading(item.getElementsByTagName("heading").item(0).getTextContent())
                .body(getBodyText(item))
                .build();
    }

    private String getBodyText(Element item) {
        final Node body = item.getElementsByTagName("body").item(0);
        if (body == null) {
            return null;
        }
        return body.getTextContent();
    }
}
