package com.turnkey.turnquest.gis.quotation.api_tests;

import static io.restassured.RestAssured.*;

import com.turnkey.turnquest.gis.quotation.api_tests.utils.Constants;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;

public class UploadDocuments {

    private String accessToken;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        baseURI = Constants.BASE_URI;
        accessToken = getNewAccessToken();

    }

    private String getNewAccessToken() {
        Response tokenResponse =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("client_id", "web-client")
                        .formParam("client_secret", "62f8c3fb-0fba-4358-a8da-80b52dd1e32b")
                        .formParam("username", "eva.mutuku@agencify.insure")
                        .formParam("password", "123456789")
                        .formParam("scope", "email profile")
                        .post(Constants.AUTH_URI);

        return tokenResponse.jsonPath().getString("access_token");
    }


    @Test
    public void testPostUploadDocuments() {

        // The request body
        String requestBody = "{"
                + "\"document\": \"document-name\","
                + "\"documentId\": 0,"
                + "\"id\": 0,"
                + "\"organizationId\": 0,"
                + "\"quotationId\": 0,"
                + "\"quotationNo\": \"string\""
                + "}";

        Response response =

                given()
                        .auth().oauth2(accessToken)
                        .contentType(ContentType.JSON)
                        .body(requestBody).  // Include the request body
                when()
                        .post("gis/quotation/quote-documents/save").
                then()
                        .statusCode(200)
                        .assertThat()

                        .body("createdBy", equalTo("Eva Mutuku "))
                        .body("modifiedBy", equalTo("Eva Mutuku "))
                        .body("document", equalTo("document-name"))
                        .body("documentId", equalTo(0))
                        .body("quotationRiskId", equalTo(null))
                        .body("quotationId", equalTo(0))
                        .body("quotationNo", equalTo("string"))
                        .body("organizationId", equalTo(9784005))
                        .body("isValuationLetter", equalTo("N"))
                        .extract().response();



        System.out.println("Response: " + response.asString());

    }
}