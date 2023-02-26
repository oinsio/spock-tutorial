package com.mechanitis.demo.spock

import spock.lang.Shared
import spock.lang.Specification
import org.testcontainers.spock.Testcontainers
import org.mockserver.client.MockServerClient
import org.testcontainers.utility.DockerImageName
import org.testcontainers.containers.MockServerContainer
import org.mockserver.model.HttpStatusCode
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import com.mechanitis.demo.spock.utils.SimpleHttpClient

@Testcontainers
class MockServerTest extends Specification {

    @Shared
    def mockServerImage = DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient.class.getPackage().getImplementationVersion());

    @Shared
    MockServerContainer mockServer = new MockServerContainer(mockServerImage)

    def "should return 'Peter the person!'"() {
        given:
            def mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort())

            mockServerClient
                    .when(request().withMethod("GET")
                            .withPath("/person")
                            .withQueryStringParameter("name", "peter"))
                    .respond(response()
                            .withStatusCode(HttpStatusCode.OK_200.code())
                            .withBody("Peter the person!"))
        expect:
            mockServerClient.hasStarted()
            SimpleHttpClient.responseFromMockserver(mockServer, "/person?name=peter") == "Peter the person!"
    }

}
