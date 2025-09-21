package com.example.async

import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import java.util.concurrent.atomic.AtomicBoolean

class AsyncServiceTest extends Specification {

    def "should eventually receive a message"() {
        given:
            def messageReceived = new AtomicBoolean(false)
            def asyncService = new AsyncService()

        when:
            asyncService.sendMessageAsync().thenRun {
                messageReceived.set(true)
            }

        then:
            new PollingConditions(timeout: 2, initialDelay: 0.1).eventually {
                assert messageReceived.get()
            }
    }

}