package com.epam.course.java.se;

import lombok.Builder;
import lombok.Value;
import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaxTest {

    private static class NotesParser1 implements ContentHandler {
        @Override
        public void setDocumentLocator(Locator locator) {

        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("startDocument");
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("endDocument");
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {

        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            System.out.println("startElement " + qName);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("endElement " + qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            System.out.println("characters: " + String.copyValueOf(ch, start, length));
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {

        }

        @Override
        public void skippedEntity(String name) throws SAXException {

        }
    }

    @Test
    public void parse1() throws SAXException, IOException {
        final XMLReader reader = XMLReaderFactory.createXMLReader();

        reader.setContentHandler(new NotesParser1());

        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("xml/test_notes.xml")));
    }

    @Value
    @Builder
    private static class Note {
        int id;
        String to;
        String from;
        String heading;
        String body;
    }

    private static class NotesParser2 extends NotesParser1 {
        private Note.NoteBuilder builder;
        private StringBuilder text;

        private static enum Tags {
            NOTES, NOTE, TO, FROM, HEADING, BODY
        }

        private List<Note> notes;

        public List<Note> getNotes() {
            return notes;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            super.startElement(uri, localName, qName, atts);

            switch (Tags.valueOf(qName.toUpperCase())) {
                case NOTES:
                    notes = new ArrayList<>();
                    break;
                case NOTE:
                    builder = Note.builder();
                    builder.id(Integer.parseInt(atts.getValue("id")));
                    break;
            }

            text = new StringBuilder();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            switch (Tags.valueOf(qName.toUpperCase())) {
                case NOTE:
                    notes.add(builder.build());
                    builder = null;
                    break;
                case TO:
                    builder.to(text.toString());
                    break;
                case FROM:
                    builder.from(text.toString());
                    break;
                case HEADING:
                    builder.heading(text.toString());
                    break;
                case BODY:
                    builder.body(text.toString());
                    break;
            }
        }
    }

    @Test
    public void parse2() throws SAXException, IOException {
        final XMLReader reader = XMLReaderFactory.createXMLReader();

        final NotesParser2 parser = new NotesParser2();
        reader.setContentHandler(parser);

        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("xml/test_notes.xml")));

        for (Note note : parser.getNotes()) {
            System.out.println(note);
        }
    }
}
