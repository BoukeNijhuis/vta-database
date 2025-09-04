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
                .body("size()", is(11))               // we currently have 11 clients
                .body("[0].id", notNullValue())      // each entry has an id
                .body("[0].name", notNullValue());   // and a name
    }
}


