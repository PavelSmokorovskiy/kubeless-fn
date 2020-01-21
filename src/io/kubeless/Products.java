package io.kubeless;

import io.kubeless.Event;
import io.kubeless.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Products {
    URI ENDPOINT = URI.create("http://loyalty-api-aws.eu-central-1.elasticbeanstalk.com/v1/api/Loyalties/2/Products");
    String AUTH = "Poiu1234Key";

    public String function(io.kubeless.Event event, io.kubeless.Context context) throws IOException {

        Map<String, String> pack = parse(event.Data);
        Integer responseCode = post(ENDPOINT, pack);
        return responseCode.toString();
    }

    public Map<String, String> parse(String json) {
        JSONObject parsed = new JSONObject(json);

        Map<String, String> pack = new HashMap<>();
        pack.put("EndDate", "2020-01-21T04:13:13.851Z");
        pack.put("ExternalId", parsed.getString("code"));
        pack.put("MemberActivityTypeId", "string");
        pack.put("Name", parsed.getString("name"));
        pack.put("StartDate", "2020-01-21T04:13:13.851Z");

        return pack;
    }

    public Integer post(URI uri, Map<String, String> map) throws IOException {
        String requestBody = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("accept", "*/*")
                .header("Authorization", AUTH)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .join();
    }
}
