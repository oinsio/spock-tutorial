package com.mechanitis.demo.spock

import spock.lang.Specification
import spock.lang.Subject

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
        expect:
            1 == 1
    }

    def "should demonstrate given-when-then"() {

        given:
            def polygon = new Polygon(4)

        when:
            int sides = polygon.numberOfSides

        then:
            sides == 4
    }

    def "should expect Exceptions"() {
        when:
            new Polygon(0)

        then:
            def exception = thrown(TooFewSidesException)
            exception.numberOfSides == 0
    }

    def "should expect an Exception to be thrown for invalid inputs: #sides"() {
        when:
            new Polygon(sides)

        then:
            def exception = thrown(TooFewSidesException)
            exception.numberOfSides == sides

        where:
            sides << [-1, 0, 1, 2]
    }

    def "should be able to create a polygon with #sides sides"() {
        expect:
            new Polygon(sides).numberOfSides == sides

        where:
            sides << [3, 4, 5, 6, 7, 8, 14]
    }

    def "should use data tables for calculating max. Max of #a and #b is #max"() {
        expect:
            Math.max(a, b) == max

        where:
            a | b || max
            1 | 3 || 3
            7 | 4 || 7
            0 | 0 || 0
    }

    def "should be able to mock a concrete class"() {
        given:
            def renderer = Mock(Renderer)
            @Subject
            def polygon = new Polygon(4, renderer)

        when:
            polygon.draw()

        then:
            4 * renderer.drawLine()
    }

    def "should be able to create a stub"() {
        given: "a palette with red as the primary color"
            def palette = new Palette(Color.Red)

        and: "a render initialised with the red palette"
            @Subject
            def renderer = new Renderer(palette)

        expect: "the renderer to use the palette's primary color as foreground"
            renderer.getForegroundColor() == Color.Red
    }

    def "should use a helper method"() {
        given:
            def renderer = Mock(Renderer)
            def shapeFactory = new ShapeFactory(renderer)

        when:
            def polygon = shapeFactory.createDefaultPolygon()

        then:
            checkDefaultShape(polygon, renderer)
    }

    private static void checkDefaultShape(polygon, Renderer renderer) {
        assert polygon.numberOfSides == 4
        assert polygon.renderer == renderer
    }

    def "should use a helper method with()"() {
        given:
            def renderer = Mock(Renderer)
            def shapeFactory = new ShapeFactory(renderer)

        when:
            def polygon = shapeFactory.createDefaultPolygon()

        then:
            with(polygon) {
                numberOfSides == 4
                renderer == renderer
            }
    }

    def "should demonstrate 'verifyAll'"() {
        given:
            def renderer = Mock(Renderer)
            def shapeFactory = new ShapeFactory(renderer)

        when:
            def polygon = shapeFactory.createDefaultPolygon()

        then:
            verifyAll(polygon) {
                numberOfSides == 4
                renderer == renderer
            }
    }
}
