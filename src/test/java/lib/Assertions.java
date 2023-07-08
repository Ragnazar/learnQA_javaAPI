package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response
                .then()
                .assertThat()
                .body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response
                .then()
                .assertThat()
                .body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByNameIsNotEqual(Response Response, String name, String notExpectedValue) {
        Response
                .then()
                .assertThat()
                .body("$", not(hasKey(name)));

        String value = Response.jsonPath().getString(name);
        assertNotEquals(notExpectedValue, value, "JSON value is equal to not expected value");
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseCodeEquals(Response Response, int expectedCode) {
        assertEquals(
                expectedCode,
                Response.getStatusCode(),
                "Response code is not as expected"
        );
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            assertJsonHasField(Response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

}
