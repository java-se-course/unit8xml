package com.epam.course.java.se;

import lombok.Getter;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import static javax.jws.soap.SOAPBinding.Style.RPC;

@WebService
@SOAPBinding(style = RPC)
public class Hello {
    @Getter
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(propOrder = {"firstName", "lastName"})
    public static class User {
        @XmlElement(required = true)
        private String firstName;
        @XmlElement(required = true)
        private String lastName;
    }

    public String sayHello(User user) {
        return String.format("Hello, %s %s!", user.firstName, user.lastName);
    }
}
