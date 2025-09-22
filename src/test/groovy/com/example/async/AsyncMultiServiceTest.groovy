package com.example.async

import spock.lang.Specification
import spock.util.concurrent.BlockingVariables


class AsyncMultiServiceTest extends Specification {

    def "should capture multiple async results via BlockingVariables"() {

        given:
            def service = new AsyncMultiService()
            double timeout = 1
            def results = new BlockingVariables(timeout)

        when:
            service.fetchUserAsync { user -> results['user'] = user }
            service.fetchOrderAsync { order -> results['order'] = order }

        then:
            results['user'] == "Alice"
            results['order'] == "#123"
    }

}