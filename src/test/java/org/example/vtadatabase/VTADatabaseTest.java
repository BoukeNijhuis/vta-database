package org.example.vtadatabase;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

@QuarkusTest
public class VTADatabaseTest {
    @Test
    public void getAllClients_returnsListOfClients() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("size()", is(11))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue());
    }

    @Test
    public void getClientById_returnsClient() {
        given()
                .when().get("/clients/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void getClientById_whenClientDoesNotExist_returns404() {
        given()
                .when().get("/clients/999999")
                .then()
                .statusCode(404);
    }
}


