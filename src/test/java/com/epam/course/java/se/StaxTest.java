package com.epam.course.java.se;

import lombok.Builder;
import lombok.Value;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StaxTest {
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
    public void parse1() throws XMLStreamException {
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        final InputStream resourceAsStream =
                getClass().getClassLoader().getResourceAsStream("xml/test_notes_stax.xml");
        final XMLStreamReader xmlStreamReader =
                xmlInputFactory.createXMLStreamReader(resourceAsStream);

        while (xmlStreamReader.hasNext()) {
            final int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    TagName tag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (tag) {
                        case NOTES:
                            final List<Note> notes = parseNotes(xmlStreamReader);
                            processNotes(notes);
                    }

                    break;
            }
        }
    }

    private void processNotes(List<Note> notes) {
        System.out.println("Got notes:");
        for (Note note : notes) {
            System.out.println(note);
        }
    }

    private List<Note> parseNotes(XMLStreamReader xmlStreamReader) throws XMLStreamException {
        final List<Note> notes = new ArrayList<>();
        while (xmlStreamReader.hasNext()) {
            final int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    TagName tag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (tag) {
                        case NOTE:
                            final Note note = parseNote(xmlStreamReader);
                            notes.add(note);
                    }

                    break;

                case XMLStreamConstants.END_ELEMENT:
                    TagName endTag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (endTag) {
                        case NOTES:
                            return notes;
                    }
            }
        }

        throw new RuntimeException("End notes element expected");
    }

    private Note parseNote(XMLStreamReader xmlStreamReader) throws XMLStreamException {
        final Note.NoteBuilder builder = Note.builder();
        builder.id(Integer.parseInt(xmlStreamReader.getAttributeValue(null, "id")));

        while (xmlStreamReader.hasNext()) {
            final int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    TagName tag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (tag) {
                        case TO:
                            builder.to(xmlStreamReader.getElementText());
                            break;
                        case FROM:
                            builder.from(xmlStreamReader.getElementText());
                            break;
                        case HEADING:
                            builder.heading(xmlStreamReader.getElementText());
                            break;
                        case BODY:
                            builder.body(xmlStreamReader.getElementText());
                            break;
                        case META:
                            builder.meta(parseMeta(xmlStreamReader));
                            break;
                    }

                    break;

                case XMLStreamConstants.END_ELEMENT:
                    TagName endTag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (endTag) {
                        case NOTE:
                            return builder.build();
                    }
            }
        }

        throw new RuntimeException("End note element expected");
    }

    private Meta parseMeta(XMLStreamReader xmlStreamReader) throws XMLStreamException {
        final Meta.MetaBuilder builder = Meta.builder();

        while (xmlStreamReader.hasNext()) {
            final int event = xmlStreamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    TagName tag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    final String elementText = xmlStreamReader.getElementText();

                    switch (tag) {
                        case PRIORITY:
                            builder.priority(Priority.valueOf(elementText.toUpperCase()));
                            break;
                        case PRIVATE:
                            builder.priv(Boolean.parseBoolean(elementText));
                            break;
                    }

                    break;

                case XMLStreamConstants.END_ELEMENT:
                    TagName endTag = TagName.valueOf(xmlStreamReader.getLocalName().toUpperCase());

                    switch (endTag) {
                        case META:
                            return builder.build();
                    }
            }
        }

        throw new RuntimeException("End meta element expected");
    }
}
