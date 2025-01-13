package org.example;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

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
    private static final HashMap<String, Integer> resultMap = new HashMap<>();

    //"paths" > #request > #queryType > "responses"
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
                    private String name;
                    private List<Response> responses;
                    @Getter
                    public class Response {
                        private int responseCode;
                    }
                }
            }
        }
    }

    static List<HttpBinBody.Paths.Request> getRequestList() {
        headers.put("User-Agent", "Learning Automation");
        return RestAssured.given().headers(headers).baseUri(baseURL).get("/spec.json")
                .as(HttpBinBody.class, ObjectMapperType.GSON)
                .getPaths().getRequests();
    }

    static void runRequestStoreNon200Responses(HttpBinBody.Paths.Request req) {
        for (HttpBinBody.Paths.Request.QueryType qt: req.getQueryTypes()) {
            Response response = switch (qt.getName().toLowerCase()) {
                case "get" -> RestAssured.given().headers(headers).baseUri(baseURL).get(req.name);
                case "post" -> RestAssured.given().headers(headers).baseUri(baseURL).post(req.name);
                case "put" -> RestAssured.given().headers(headers).baseUri(baseURL).put(req.name);
                case "patch" -> RestAssured.given().headers(headers).baseUri(baseURL).patch(req.name);
                case "delete" -> RestAssured.given().headers(headers).baseUri(baseURL).delete(req.name);
                default -> null;
            };

            if (response != null && response.getStatusCode() != 200) {
                resultMap.put(baseURL + req.getName() + " " + qt.getName(), response.getStatusCode());
            }
        }
    }

    public static void main(String[] args) {
        List<HttpBinBody.Paths.Request> requests = getRequestList();

        for (HttpBinBody.Paths.Request req : requests) {
            runRequestStoreNon200Responses(req);
        }

        System.out.println(resultMap.size());
    }
}
