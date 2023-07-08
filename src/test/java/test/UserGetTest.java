package test;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Get user information")
public class UserGetTest extends BaseTestCase {
    @Test
    @Description("This test check an information about unauthorized user")
    @DisplayName("Test negative get information about user")
    public void testGetUserDataNoAuth() {
        Response response = ApiCoreRequests
                .makeGetRequestWithoutAuth("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(response, "username");
        Assertions.assertJsonHasNotField(response, "firstName");
        Assertions.assertJsonHasNotField(response, "lastName");
        Assertions.assertJsonHasNotField(response, "email");
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @Owner(value = "Ивано Иван Иванович")
    @Link(name = "Ссылка на swagger", url = "https://playground.learnqa.ru/api/map")
    @Description("This test successfully get an information about authorized user")
    @DisplayName("Test positive get information about user")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        int userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

        Response response = ApiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userIdOnAuth, header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(response, expectedFields);
    }

    @Test
    @Description("This test check an information about unauthorized user by authorized user")
    @DisplayName("Test negative get information about user")
    public void testGetUserDetailsAuthAsOtherUser() throws HttpException {
        int length = 1;
        boolean useLetters = false;
        boolean useNumbers = true;

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response response = ApiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"
                        + RandomStringUtils.random(length, useLetters, useNumbers), header, cookie);

        if (response.getStatusCode() == 200) {
            Assertions.assertJsonHasField(response, "username");
            Assertions.assertJsonHasNotField(response, "firstName");
            Assertions.assertJsonHasNotField(response, "lastName");
            Assertions.assertJsonHasNotField(response, "email");
        } else if (response.getStatusCode() == 404) {
            Assertions.assertResponseTextEquals(response, "User not found");
        } else {
            throw new HttpException("Unknown status code: " + response.getStatusCode());
        }
    }
}
