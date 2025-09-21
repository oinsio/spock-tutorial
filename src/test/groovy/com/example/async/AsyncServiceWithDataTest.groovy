package com.example.async

import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

class AsyncServiceWithDataTest extends Specification {

    def "should return async data"() {
        given:
            def asyncService = new AsyncServiceWithData()
            double timeout = 1
            def dataVariable = new BlockingVariable<String>(timeout)

        when:
            asyncService.fetchDataAsync {data ->
                dataVariable.set(data)
            }

        then:
            dataVariable.get() == "async data"
    }

}