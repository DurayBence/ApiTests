package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

//Task
/*
There is a service with endpoint http://api.mathjs.org/v4/
Documentation: http://api.mathjs.org/

Create addition, subtraction, multiplication, division and square root tests using using the selected test framework.

Send requests with 'User-Agent': 'Learning Automation'
To extract the square root in the request, use the GET method, for other operations - POST
Use parameterized tests
For each method use a separate mark, to be able to run tests of a separate method
*/

public class TestMathJsApi {
    private static final HashMap<String, String> headers = new HashMap<>();
    private static final String baseURL = "https://api.mathjs.org/v4/";

    private static String encodeExpressionToAscii(String expr) {
        return expr.replaceAll(" ", "%20")
                    .replaceAll("\\(", "%28")
                    .replaceAll("\\)", "%29")
                    .replaceAll("\\^", "%5E");
    }

    private static double calculateSimpleExpressionByApi(String expr) {
        headers.put("User-Agent", "Learning Automation");
        String asciiExpr = encodeExpressionToAscii(expr);
        Response response = RestAssured.given()
                .headers(headers)
                .get(baseURL + "?expr=" + asciiExpr + "&precision=14");
        return Double.parseDouble(response.getBody().asString());
    }

    private static String calculateComplexExpressionByApi(String expr) {
        headers.put("User-Agent", "Learning Automation");
        String jsonString = "{\"expr\":\"" + expr + "\",\"precision\":14}";
        Response response = RestAssured.given()
                .headers(headers)
                .contentType("application/json")
                .body(jsonString)
                .post(baseURL);
        return response.jsonPath().getString("result");
    }

    private static Stream<Arguments> addParams() {
        return Stream.of(
                Arguments.of("5+3", 8.0D),
                Arguments.of("-3+7", 4.0D),
                Arguments.of("3.2+0.06", 3.26D)
        );
    }
    @ParameterizedTest
    @MethodSource("addParams")
    void addTwoNumbers(String expr, double expected) {
        assertEquals(expected, calculateSimpleExpressionByApi(expr));
    }

    private static Stream<Arguments> subtractParams() {
        return Stream.of(
                Arguments.of("5-3", 2.0D),
                Arguments.of("-1-8", -9.0D),
                Arguments.of("1.6-0.24", 1.36D)
        );
    }
    @ParameterizedTest
    @MethodSource("subtractParams")
    void subtractTwoNumbers(String expr, double expected) {
        assertEquals(expected, calculateSimpleExpressionByApi(expr));
    }

    private static Stream<Arguments> multiplyParams() {
        return Stream.of(
                Arguments.of("5*3", 15.0D),
                Arguments.of("-2*6", -12.0D),
                Arguments.of("1.6*3.2", 5.12D)
        );
    }
    @ParameterizedTest
    @MethodSource("multiplyParams")
    void multiplyTwoNumbers(String expr, double expected) {
        assertEquals(expected, calculateSimpleExpressionByApi(expr));
    }

    private static Stream<Arguments> divideParams() {
        return Stream.of(
                Arguments.of("6/3", 2.0D),
                Arguments.of("-80/16", -5.0D),
                Arguments.of("1/8", 0.125D),
                Arguments.of("2/3", 0.66666666666667D)
        );
    }
    @ParameterizedTest
    @MethodSource("divideParams")
    void divideTwoNumbers(String expr, double expected) {
        assertEquals(expected, calculateSimpleExpressionByApi(expr));
    }

    private static Stream<Arguments> sqrtParams() {
        return Stream.of(
                Arguments.of("sqrt(16)", "4"),
                Arguments.of("sqrt(1)", "1"),
                Arguments.of("sqrt(0.9801)", "0.99"),
                Arguments.of("sqrt(2)", "1.4142135623731"),
                Arguments.of("sqrt(-4)", "2i")
        );
    }
    @ParameterizedTest
    @MethodSource("sqrtParams")
    void sqrtTwoNumbers(String expr, String expected) {
        assertEquals(expected, calculateComplexExpressionByApi(expr));
    }
}
