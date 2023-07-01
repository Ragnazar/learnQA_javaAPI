package module_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieTest {

    @Test
    public void cookieTest() {

        Response saveExpected = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String, String>    expected = saveExpected.getCookies();
        Set<String> setKeys = expected.keySet();

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        for (String k : setKeys) {
            assertEquals(expected.get(k), response.getCookie(k), "Unexpected cookie");
        }
    }
}
