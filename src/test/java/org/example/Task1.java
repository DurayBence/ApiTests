package org.example;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Task:
/*
There is a resource https://nghttp2.org/httpbin/, which describes all requests supported by the service.

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

    //"data" > "resources" > "directGroups"
    //"paths" > "###" > requestType > "responses" > responseCodeInt
    @Getter
    public class HttpBinBody {
        private Paths paths;
        @Getter
        public class Paths {
            private List<Request> requests;
            @Getter
            public class Request {
                private String name;
                private List<QueryType> queryTypes;
                @Getter
                public class QueryType {
                    private List<Response> responses;
                    @Getter
                    public class Response {
                        private int responseCode;       //MAP VALUE
                    }
                }
            }
        }
    }

    public static HashMap<String, Integer> getResponses() {
        headers.put("User-Agent", "Learning Automation");
        HashMap<String, Integer> resultMap = new HashMap<>();

        List<HttpBinBody.Paths.Request> requests = RestAssured
                .given().baseUri(baseURL).headers(headers).get("/spec.json")
                .as(HttpBinBody.class, ObjectMapperType.GSON)
                .getPaths().getRequests();
        for (HttpBinBody.Paths.Request req : requests) {
            for (HttpBinBody.Paths.Request.QueryType qt: req.getQueryTypes()) {
                for (HttpBinBody.Paths.Request.QueryType.Response resp : qt.getResponses()) {
                    if (resp.responseCode != 200) {
                        resultMap.put(baseURL + req.name + " " + qt, resp.responseCode);
                    }
                }
            }
        }

        return resultMap;
    }

    public static void main(String[] args) {
        for (Map.Entry<String, Integer> e : getResponses().entrySet()) {
            System.out.println(e);
        }
    }
}
