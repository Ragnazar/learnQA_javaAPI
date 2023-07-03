package module_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieTest {

    @Test
    public void cookieTest() {

        Map<String, String> expected = new HashMap<>();
        expected.put("HomeWork", "hw_value");
        String key = expected.keySet().iterator().next();

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        assertTrue(response.getCookies().containsKey(key));
        assertEquals(expected.get(key), response.getCookie(key));


    }
}
