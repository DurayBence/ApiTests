package org.example;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

//Useful links:
/*
https://qwiki.nixs.com/pages/viewpage.action?pageId=698385362
https://restfulapi.net/
https://rest-assured.io/
https://www.baeldung.com/rest-assured-tutorial
https://www.baeldung.com/java-org-json
*/

public class Basics {
    public static void main(String[] args) {
        Response response;

        //Basic HTTP requests
        /*
        Response responseGet = RestAssured.get("https://httpbin.org/get");
        Response responsePut = RestAssured.put("http://httpbin.org/put");
        Response responseDelete = RestAssured.delete("http://httpbin.org/delete");
        Response responseHead = RestAssured.head("http://httpbin.org/get");
        Response responseOptions = RestAssured.options("http://httpbin.org/get");
        System.out.println(responseGet.getBody().asString());
        */

        //Passing parameters
        /*
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3[]", "value3,value4"); //list of values
        response = RestAssured.given().queryParams(params).get("http://httpbin.org/get");
        System.out.println(response.getBody().asString());
         */

        //GET response content
        /*
        response = RestAssured.get("https://httpbin.org/links/0/1");
        System.out.println(response.getBody().asString());
        */

        //Reading header
        /*
        response = RestAssured.given()
                .header("user-agent", "my-app/0.0.1")
                .get("https://httpbin.org/get");
        System.out.println(response.getHeaders());
        */

        //POST request
        /*
        HashMap<String, String> postData = new HashMap<>();
        postData.put("Key1", "value1");
        postData.put("Key2", "value2");
        response = RestAssured.given()
                .header("user-agent", "rtfm/0.0.1")
                .formParams(postData)
                .post("http://httpbin.org/post");
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody().asString());
        /*

         */
        //JSON parsing
        /*
        response = RestAssured.get("https://httpbin.org/get");
        JsonPath jsonPath = response.jsonPath();
        System.out.println(jsonPath.getString("url"));
        */

        //Basic authentication
        /*
        response = RestAssured.given()
                .auth()
                .basic("user", "passwd")
                .get("https://httpbin.org/basic-auth/user/passwd");
        System.out.println(response.getBody().asString());
        */

        //Exception handling
        /*
        try {
            response = RestAssured.get("https://httpbin.org/status/500");
            response.then().statusCode(200); // Expecting a 200 OK status code
        } catch (AssertionError e) {
            System.out.println("Oops. HTTP Error occurred: " + e.getMessage());
        }
        try {
            response = RestAssured.get("http://urldoesnotexistforsure.bom");
        } catch (Exception e) {
            System.out.println("Seems like DNS lookup failed: " + e.getMessage());
        }
        try {
            response = RestAssured.given()
                    .timeout(1)
                    .get("https://httpbin.org/user-agent");
        } catch (Exception e) {
            System.out.println("Oops. Timeout occurred: " + e.getMessage());
        }
        */
    }
}