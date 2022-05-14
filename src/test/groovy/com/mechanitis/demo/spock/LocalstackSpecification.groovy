package com.mechanitis.demo.spock

import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
abstract class LocalstackSpecification extends Specification {

    @Shared
    def dockerImageName = "localstack/localstack:0.12.16"
    @Shared
    def localstackImage = DockerImageName.parse(dockerImageName)

}
