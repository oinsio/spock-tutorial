package com.example

import spock.lang.Specification
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = DemoApplication)
class DemoApplicationTest extends Specification {

    def "should launch Spring application"() {
        expect:
            true
    }

}