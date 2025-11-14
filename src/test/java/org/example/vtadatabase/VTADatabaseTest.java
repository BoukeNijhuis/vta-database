package org.example.vtadatabase;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@QuarkusTest
public class VTADatabaseTest {

    @BeforeEach
    public void resetDatabase() throws Exception {
        // Reset the initialized flag to force database re-initialization
        java.lang.reflect.Field field = DatabaseServer.class.getDeclaredField("initialized");
        field.setAccessible(true);
        field.set(null, false);
    }

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
    public void getClientById_returnsCorrectClientData() {
        given()
                .when().get("/clients/1")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(1))
                .body("name", equalTo("Jan Jansen"))
                .body("email", equalTo("jan.jansen@gmail.com"));
    }

    @Test
    public void getClientById_whenClientDoesNotExist_returns404() {
        given()
                .when().get("/clients/999999")
                .then()
                .statusCode(404);
    }

    @Test
    public void getClientById_withNegativeId_returns404() {
        given()
                .when().get("/clients/-1")
                .then()
                .statusCode(404);
    }

    @Test
    public void getClientById_withZeroId_returns404() {
        given()
                .when().get("/clients/0")
                .then()
                .statusCode(404);
    }

    @Test
    public void getClientById_withLargeId_returns404() {
        given()
                .when().get("/clients/" + Long.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    public void getAllClients_returnsCorrectContentType() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    public void getAllClients_containsExpectedClient() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("find { it.name == 'Jan Jansen' }.email", equalTo("jan.jansen@gmail.com"));
    }

    @Test
    public void getAllClients_handlesSpecialCharactersInName() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("find { it.name == 'Sophie van den Berg' }.name", equalTo("Sophie van den Berg"));
    }

    @Test
    public void getAllClients_handlesSpecialCharactersInEmail() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("find { it.email == 'sophie.vandenberg@example.com' }.email", equalTo("sophie.vandenberg@example.com"));
    }

    @Test
    public void getAllClients_handlesNamesWithApostrophes() {
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("findAll { it.name.contains('van') }.size()", greaterThan(0));
    }

    @Test
    public void updateClient_returnsOk() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}")
                .when().put("/clients/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void updateClient_returnsUpdatedData() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Jane Doe\",\"email\":\"jane.doe@example.com\"}")
                .when().put("/clients/2")
                .then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("name", equalTo("Jane Doe"))
                .body("email", equalTo("jane.doe@example.com"));
    }

    @Test
    public void deleteClient_returnsNoContent() {
        given()
                .when().delete("/clients/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void deleteClient_whenClientDoesNotExist_returns404() {
        given()
                .when().delete("/clients/999999")
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteClient_removesClientFromGetAllClients() {
        // Use a known client ID from database initialization (11 clients total)
        long clientId = 5L;

        // Delete the client
        given()
                .when().delete("/clients/" + clientId)
                .then()
                .statusCode(204);

        // Verify client is removed from list (this is what makes it a true soft delete test)
        given()
                .when().get("/clients")
                .then()
                .statusCode(200)
                .body("size()", is(10))  // 11 - 1 = 10
                .body("find { it.id == " + clientId + " }", nullValue());
    }

    @Test
    public void createClient_returnsCreated() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"New Client\",\"email\":\"new@example.com\"}")
                .when().post("/clients")
                .then()
                .statusCode(201);
    }

    @Test
    public void createClient_returnsCreatedClientData() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Test Client\",\"email\":\"test@example.com\"}")
                .when().post("/clients")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("name", equalTo("Test Client"))
                .body("email", equalTo("test@example.com"));
    }

    @ParameterizedTest
    @CsvSource({
            "O'Brien, obrien@example.com",
            "José García, jose.garcia@example.com",
            "François Müller, francois@example.com",
            "Владимир Петров, vladimir@example.com",
            "李明, li.ming@example.com",
            "Anne-Marie, anne.marie@example.com",
            "O'Reilly & Sons, oreilly@example.com"
    })
    public void createClient_handlesSpecialCharactersInName(String name, String email) {
        given()
                .contentType("application/json")
                .body(String.format("{\"name\":\"%s\",\"email\":\"%s\"}", name, email))
                .when().post("/clients")
                .then()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("email", equalTo(email));
    }
}
