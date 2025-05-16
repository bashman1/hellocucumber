//package hellocucumber;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//
//
//public class APIStepDefinition {
//    String baseUrl = "";
//    String headerKey = "";
//    String headerValue = "";
//    Response response;
//    String body = "";
//
//
//    @Given("Set Base URL {string} for API call")
//    public void setBaseUrlString(String url){
//        baseUrl = url;
//    }
//
//    @Then("Add header {string} with value {string}")
//    public void addHeaders(String h_key, String h_value){
//        headerKey = h_key;
//        headerValue = h_value;
//    }
//
//    @Then("Add body with value {string}")
//    public void addBody(String requestBody) {
//        body = requestBody;
//    }
//
//    @When("Call {string} on path {string}")
//    public void callMethod(String method, String path) {
//        RequestSpecification request = given()
//                .baseUri(baseUrl)
//                .header(headerKey, headerValue)
//                .body(body)
//                .contentType("application/json");
//
//        switch (method.toUpperCase()) {
//            case "POST":
//                response = request.when().post(path);
//                break;
//            case "GET":
//                response = request.when().get(path);
//                break;
//            default:
//                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
//        }
//    }
//
//    @Then("Check status code equals {int}")
//    public void onCheckStatusCode(int httpStatusCode){
//        response.then().statusCode(httpStatusCode);
//    }
//
//    @Then("response contains {string}.{string}  equals {string}")
//    public void responseContainsHeadersEquals(String object, String field, String expectedValue){
//        if (object.equals("headers")) {
//            response.then().header(field, expectedValue);
//        }
//    }
//
//    @Then("response contains {string} equals {string}")
//    public void responseContainsField(String field, String expectedValue) {
//        response.then().body(field, equalTo(expectedValue));
//    }
//
//    @Then("response contains {string}.{string} equals {string}")
//    public void responseContainsNestedField(String object, String field, String expectedValue) {
//        String jsonPath = object + "." + field;
//        response.then().body(jsonPath, equalTo(expectedValue));
//    }
//
//
//}
package hellocucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APIStepDefinition {

    private static final String APPLICATION_JSON = "application/json";

    private String baseUrl;
    private String headerKey;
    private String headerValue;
    private String requestBody;
    private Response response;

    // -------------------- Given Steps --------------------

    @Given("Set Base URL {string} for API call")
    public void setBaseUrl(String url) {
        this.baseUrl = url;
    }

    @Then("Add header {string} with value {string}")
    public void addHeader(String key, String value) {
        this.headerKey = key;
        this.headerValue = value;
    }

    @Then("Add body with value {string}")
    public void setRequestBody(String body) {
        this.requestBody = body;
    }

    // -------------------- When Step --------------------

    @When("Call {string} on path {string}")
    public void callHttpMethod(String method, String path) {
        RequestSpecification request = given()
                .baseUri(baseUrl)
                .contentType(APPLICATION_JSON)
                .header(headerKey, headerValue);

        if ("POST".equalsIgnoreCase(method)) {
            if (requestBody != null && !requestBody.trim().isEmpty()) {
                request = request.body(requestBody);
            }
            response = request.post(path);
        } else if ("GET".equalsIgnoreCase(method)) {
            // Do NOT add body for GET
            response = request.get(path);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    // -------------------- Then Steps --------------------

    @Then("Check status code equals {int}")
    public void validateStatusCode(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("response contains {string} equals {string}")
    public void validateJsonResponseField(String jsonPath, String expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }

    @Then("response contains {string}.{string} equals {string}")
    public void validateHeaderOrNestedField(String category, String field, String expectedValue) {
        if ("headers".equalsIgnoreCase(category)) {
            response.then().header(field, expectedValue);
        } else {
            String jsonPath = category + "." + field;
            response.then().body(jsonPath, equalTo(expectedValue));
        }
    }
}
