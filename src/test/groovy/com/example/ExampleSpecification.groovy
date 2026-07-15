package com.example

import spock.lang.Specification

class ExampleSpecification extends Specification {

    void setupSpec() {
        // setup code that needs to be run once before all methods in this class
    }

    void setup() {
        // setup code that needs to be run before every test method
    }

    void cleanup() {
        // code that tears down things at the end of every test method
    }

    void cleanupSpec() {
        // code that tears down everything at the end of all tests have run
    }

    def "should be a simple assertion"() {
        given:
            Example example = new Example();

        expect:
        example.returnIndexOfNorRepeatingCharacter(input) == output


        where:
            input         | output
            "john"        | 1
            "stethoscope" | 4
            "aabbcc"      | -1
            "sTethoscope" | 4
            ""            | -1
    }

}
