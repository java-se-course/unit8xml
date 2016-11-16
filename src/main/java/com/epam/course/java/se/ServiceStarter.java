package com.epam.course.java.se;


import wsclient.HelloService;
import wsclient.User;

import javax.xml.ws.Endpoint;

public class ServiceStarter {
    public static void main(String[] args) {
        final String url = "http://192.168.0.100:80/hello";
        Endpoint.publish(url, new Hello());
        System.out.println("Server started @ " + url);

//        final HelloService helloService = new HelloService();
//        final wsclient.Hello helloPort = helloService.getHelloPort();
//        final User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        System.out.println(helloPort.sayHello(user));
    }
}
