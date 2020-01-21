package io.kubeless;

//import io.kubeless.Event;
//import io.kubeless.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ProductsMain {
    static URI ENDPOINT1 = URI.create("http://ptsv2.com/t/hoilc-1579580195/post");
    static URI ENDPOINT2 = URI.create("http://loyalty-api-aws.eu-central-1.elasticbeanstalk.com/v1/api/Loyalties/2/Products");
    static String AUTH = "Poiu1234Key";
    static String JSON_HYBRIS = "{\"catalogVersion\":{\"active\":true,\"version\":\"Online\",\"catalog\":{\"id\":\"apparelProductCatalog\",\"integrationKey\":\"apparelProductCatalog\"},\"integrationKey\":\"Online|apparelProductCatalog\"},\"supercategories\":[{\"name\":\"Ski Gear\",\"code\":\"skigear\",\"integrationKey\":\"skigear\",\"localizedAttributes\":[{\"name\":\"Ski Gear\",\"language\":\"en\"},{\"name\":\"Skiausrüstung\",\"language\":\"de\"}]},{\"name\":\"Toko\",\"code\":\"Toko\",\"integrationKey\":\"Toko\",\"localizedAttributes\":[{\"name\":\"Toko\",\"language\":\"en\"},{\"name\":\"Toko\",\"language\":\"de\"}]},{\"name\":\"Snow\",\"code\":\"snow\",\"integrationKey\":\"snow\",\"localizedAttributes\":[{\"name\":\"Snow\",\"language\":\"en\"},{\"name\":\"Snow\",\"language\":\"de\"}]},{\"name\":\"Tools\",\"code\":\"100200\",\"integrationKey\":\"100200\",\"localizedAttributes\":[{\"name\":\"Tools\",\"language\":\"en\"},{\"name\":\"Werkzeug\",\"language\":\"de\"}]}],\"code\":\"29531\",\"name\":\"Snowboard Ski Tool Toko Side Edge Angle Pro 88 Grad\",\"integrationKey\":\"Online|apparelProductCatalog|29531\",\"localizedAttributes\":[{\"name\":\"Snowboard Ski Tool Toko Side Edge Angle Pro 88 Grad\",\"language\":\"en\"}]}";

    public static void main(String[] args) throws IOException {

        Map<String, String> pack = parse(JSON_HYBRIS);
        Integer responseCode = post(ENDPOINT1, pack);
        System.out.println(responseCode.toString());
    }

    public static Map<String, String> parse(String json) {
        JSONObject parsed = new JSONObject(json);

        Map<String, String> pack = new HashMap<>();
        pack.put("EndDate", "2020-01-21T04:13:13.851Z");
        pack.put("ExternalId", parsed.getString("code"));
        pack.put("MemberActivityTypeId", "string");
        pack.put("Name", parsed.getString("name"));
        pack.put("StartDate", "2020-01-21T04:13:13.851Z");

        return pack;
    }

    public static Integer post(URI uri, Map<String, String> map) throws IOException {
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
