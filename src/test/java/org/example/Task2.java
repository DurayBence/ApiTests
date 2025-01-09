package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

//Task
/*
There is a form http://httpbin.org/forms/post
By clicking on the ‘Submit order’ button, a post request is sent to http://httpbin.org/post, and
a json with all the artifacts of the request is returned.

Write a script that will send a POST request, return a json part of the response ('form') and all response headers.

Examine the request/response when the button on 'Submit order' is clicked http://httpbin.org/forms/post
Write a method that will take two parameters: the POST request body and headers.
Return a list with two HashMaps: the 'form' object of the response body and response headers
Send requests with 'User-Agent': 'Learning Automation'
*/

public class Task2 {
    private static final String baseUrl = "http://httpbin.org";
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final HashMap<String, String> postBody = new HashMap<>();
    private static final HashMap<String, String> responseForms = new HashMap<>();

    private static void buildPostData() {
        postBody.put("custname", "Thomas Tester");
        postBody.put("custtel", "+49 69 1234 567");
        postBody.put("custemail", "thomastester@asdfg123.org");
        postBody.put("size", "small");
        postBody.put("topping[]", "bacon,cheese,onion,mushroom");
        postBody.put("delivery", "15:45");
        postBody.put("comments", "I'm starving...");
    }

    public static void createResponseFormsMap(String[] args) {
        headers.put("User-Agent", "Learning Automation");
        buildPostData();
        Response response = RestAssured
                .given().baseUri(baseUrl)
                .headers(headers)
                .formParams(postBody)
                .post(baseUrl + "/post");
        JsonPath jsonPath = response.jsonPath();

        responseForms.put("header", response.getHeader("form"));
        responseForms.put("body", jsonPath.getString("form"));
    }

    public static void main(String[] args) {
        System.out.println("Header form:" + responseForms.get("header"));
        System.out.println("Body form:" + responseForms.get("body"));
    }
}
