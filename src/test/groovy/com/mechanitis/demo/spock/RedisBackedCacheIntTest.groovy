package com.mechanitis.demo.spock

import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class RedisBackedCacheIntTest extends Specification {

    @Shared
    def redisDockerImageName = "redis:5.0.3-alpine"
    @Shared
    def redisExposedPort = 6379
    @Shared
    RedisBackedCache underTest

    @Shared
    GenericContainer redis = new GenericContainer<>(redisDockerImageName)
        .withExposedPorts(redisExposedPort)

    def setupSpec() {

        def address = redis.host
        def port = redis.firstMappedPort

        println("host:port=${address}:${port}")

        // Now we have an address and port for Redis, no matter where it is running
        underTest = new RedisBackedCache(address, port)
    }

    def "should put and get data"() {

        given:
            underTest.put("test", "example")

        when:
            def result = underTest.get("test")

        then:
            result == "example"
    }
}
