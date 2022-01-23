package org.sf.n4j.examples.three;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

// create a request
        var request = HttpRequest.newBuilder(
                        URI.create("https://fedhub-uat.iairgroup.com/.well-known/openid-configuration"))
                .header("accept", "application/json")
                .build();

// use the client to send the request
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseStr = response.body();

        System.out.println(responseStr);
    }
}
