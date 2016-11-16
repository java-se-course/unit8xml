package com.epam.course.java.se;

import lombok.Getter;
import lombok.ToString;
import org.junit.Test;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JaxbTest {

    @XmlType(name = "priority")
    @XmlEnum
    public static enum Priority {
        @XmlEnumValue("low")
        low,

        @XmlEnumValue("high")
        high;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(propOrder = {"priority", "priv"})
    @Getter
    @ToString
    public static class Meta {
        @XmlElement(required = true)
        private Priority priority;
        @XmlElement(name = "private", required = false)
        private Boolean priv;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(propOrder = {"meta", "to", "from", "heading", "body"})
    @Getter
    @ToString
    public static class Note {
        @XmlAttribute(required = true)
        private int id;
        @XmlElement(required = false)
        private Meta meta;
        @XmlElement(required = true)
        private String to;
        @XmlElement(required = true)
        private String from;
        @XmlElement(required = true)
        private String heading;
        @XmlElement(required = false)
        private String body;


    }

    @XmlRootElement(name = "notes", namespace = "http://cources.epam.com/unit9/notes")
    @XmlAccessorType(XmlAccessType.FIELD)
    @ToString
    public static class Notes {
        @XmlElement(name = "note")
        private List<Note> notes;

        public List<Note> getNotes() {
            if (notes == null) {
                notes = new ArrayList<>();
            }
            return notes;
        }
    }

    private static class ToStringResolver extends SchemaOutputResolver {
        private Map<String, StringWriter> writers = new HashMap<>();

        public Map<String, StringWriter> getWriters() {
            return writers;
        }

        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
            System.out.printf("Namespace: %s, filename: %s\n", namespaceUri, suggestedFileName);
            final StringWriter writer = new StringWriter();
            writers.put(suggestedFileName, writer);
            final StreamResult result = new StreamResult(writer);
            result.setSystemId(suggestedFileName);
            return result;
        }
    }

    @Test
    public void test() throws JAXBException, IOException, InterruptedException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(Notes.class);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        final Notes notes;

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("xml/test_notes_stax.xml")) {
            notes = (Notes) unmarshaller.unmarshal(resourceAsStream);
            System.out.println("Parsed:");
            System.out.println(notes);
        }

        final Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("\nFormatted:");
        marshaller.marshal(notes, System.out);

        System.out.println("\nSchema:");
        final ToStringResolver outputResolver = new ToStringResolver();
        jaxbContext.generateSchema(outputResolver);

        for (Map.Entry<String, StringWriter> entry : outputResolver.getWriters().entrySet()) {
            System.out.println(entry.getKey() + ":");
            System.out.println(entry.getValue().toString());
        }
    }

}
