package com.mechanitis.demo.spock.utils

import lombok.Cleanup
import org.testcontainers.containers.MockServerContainer

class SimpleHttpClient {

    static String responseFromMockserver(MockServerContainer mockServer, String path) throws IOException {
        URLConnection urlConnection = new URL(mockServer.getEndpoint() + path).openConnection()
        @Cleanup
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
        return reader.readLine()
    }
}
