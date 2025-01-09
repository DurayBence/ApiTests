package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

//Task:
/*
Investigate the service, find a way to get all service requests.
Write a script that will find all internal url codes whose responses are not equal to 200

Examine requests sent during navigation to https://nghttp2.org/httpbin/
Find a query that returns a list of all service requests (e.g. "/ip")
Get the response code of each subresource
Return HashMap with subresources whose response code is not equal to 200

Send requests with 'User-Agent': 'Learning Automation'
Find the list of resources using fiddler/developer console
The return should contain pairs like: "https://nghttp2.org/httpbin/bla/", 401
*/

public class Task1 {
    private static final String baseURL = "https://nghttp2.org/httpbin";
    private static final HashMap<String, String> headers = new HashMap<>();

    public static HashMap<String, String> getNot200Responses() {
        headers.put("User-Agent", "Learning Automation");
        HashMap<String, String> resultMap = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            String urlSuffix = "/status/" + (i+1)*100;
            Response response = RestAssured
                    .given().baseUri(baseURL)
                    .headers(headers)
                    .get(urlSuffix);
            if (response.statusCode() != 200) {
                resultMap.put(baseURL + urlSuffix, String.valueOf(response.getStatusCode()));
            }
        }
        return resultMap;
    }

    public static void main(String[] args) {
        System.out.println(getNot200Responses().entrySet());
    }
}
