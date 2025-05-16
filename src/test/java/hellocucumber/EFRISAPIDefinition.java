package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class EFRISAPIDefinition {

    private RequestSpecification request;
    private Response response;

    // Variables for reuse
    private final String baseUrl = "https://efristest.ura.go.ug";
    private final String endpoint = "/efrisws/ws/taapp/getInformation";
    private final String content = "1234rerewrewrr";
    private final String signature = "98786876876hjhgjhgfdgf5465";
    private String requestPayload;

    @Given("I set up the EFRIS API base URI and headers")
    public void setupBaseUriAndHeaders() {
        request = given()
                .baseUri(baseUrl)
                .basePath(endpoint)
                .header("Content-Type", "application/json")
                .header("Cookie", "483d206c1a39fa19357dead6fb1d4706=006ae4c06e0d70e62d0bdc0f6f2d8f71; ed23d2d26512812193a89a1c0bbbfd6c=c81d90f0da4a8a99a9e2065ad739356b");
    }

    @And("I prepare the request payload with invalid device number {string} and TIN {string}")
    public void prepareRequestPayload(String deviceNo, String tin) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        requestPayload = String.format("""
        {
            "data": {
                "content": "%s",
                "signature": "%s",
                "dataDescription": {
                    "codeType": "0",
                    "encryptCode": "0",
                    "zipCode": "0"
                }
            },
            "globalInfo": {
                "appId": "AP04",
                "brn": "",
                "dataExchangeId": "9230489223014123",
                "deviceMAC": "123",
                "deviceNo": "%s",
                "extendField": "",
                "longitude": "0",
                "latitude": "0",
                "interfaceCode": "T101",
                "requestCode": "TP",
                "requestTime": "%s",
                "responseCode": "TA",
                "taxpayerID": "1",
                "tin": "%s",
                "userName": "admin",
                "version": "1.1.20191201"
            },
            "returnStateInfo": {
                "returnCode": "",
                "returnMessage": ""
            }
        }
        """, content, signature, deviceNo, currentTime, tin);
    }

    @When("I send a POST request to the getInformation endpoint")
    public void sendPostRequest() {
        response = request
                .body(requestPayload)
                .post();
    }

    @Then("the API should return HTTP status code {int}")
    public void verifyHttpStatusCode(int statusCode) {
        assertEquals("HTTP Status Code mismatch", statusCode, response.getStatusCode());
    }

    @Then("the return code should be {string}")
    public void verifyReturnCode(String expectedCode) {
        String actualCode = response.jsonPath().getString("returnStateInfo.returnCode");
        assertEquals("Return code mismatch", expectedCode, actualCode);
    }


    @Then("the return message should contain {string}")
    public void verifyReturnMessageContains(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("returnStateInfo.returnMessage");
        System.out.println(">>> Expected: " + expectedMessage);
        System.out.println(">>> Actual: " + actualMessage);

        Assert.assertNotNull("Return message is null", actualMessage);
        Assert.assertTrue("Return message does not contain expected text",
                actualMessage.contains(expectedMessage));
    }


}
