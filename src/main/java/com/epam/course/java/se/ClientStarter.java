package com.epam.course.java.se;


import wsclient.HelloService;
import wsclient.User;

import java.net.MalformedURLException;
import java.net.URL;


public class ClientStarter {
    public static void main(String[] args) throws MalformedURLException {


        final HelloService helloService =
                new HelloService(/*new URL("http://127.0.0.1:8080/hello")*/);
        final wsclient.Hello helloPort = helloService.getHelloPort();
        final User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        System.out.println(helloPort.sayHello(user));
    }
}
