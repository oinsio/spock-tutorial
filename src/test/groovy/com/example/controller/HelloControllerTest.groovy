package com.example.controller

import spock.lang.Specification


class HelloControllerTest extends Specification {

    def "should return greetings"() {
        given:
            def helloController = new HelloController()

        expect:
            helloController.index() == "Greetings from Spring Boot!"
    }

}