package module_3;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderTest {
    @Test
    public void headerTest() {

        Response saveExpected = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers headers = saveExpected.getHeaders();
        System.out.println(headers);

        String expected = headers.getValue("x-secret-homework-header");

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        assertEquals(expected, response.getHeader("x-secret-homework-header"), "Unknown header");
    }
}
