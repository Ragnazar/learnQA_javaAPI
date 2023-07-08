package test;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    @Test
    @Description("This test check registration of an user with existing email")
    @DisplayName("Test negative registration user")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);
        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextEquals(response, "Users with email '" + email + "' already exists");
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @Owner(value = "Ивано Иван Иванович")
    @Link(name = "Ссылка на swagger", url = "https://playground.learnqa.ru/api/map")
    @Description("This test successfully register a new user")
    @DisplayName("Test positive registration user")
    public void testCreateUserSuccessfulCreate() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(response, 200);
        Assertions.assertJsonHasField(response, "id");
    }

    @Test
    @Description("This test check registration of an user with incorrect email")
    @DisplayName("Test negative registration user")
    public void testCreateUserWithIncorrectEmail() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "wrongmailexample.com");

        userData = DataGenerator.getRegistrationData(userData);

        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(response, "Invalid email format");
        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonHasNotField(response, "id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @Description("This test check registration of an user without one of specified fields")
    @DisplayName("Test negative registration user")
    public void testCreateUserWithoutSpecifiedField(String field) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(field);

        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(response, "The following required params are missed: " + field);
        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonHasNotField(response, "id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    @Description("This test check registration of an user with very short name")
    @DisplayName("Test negative registration user")
    public void testCreateUserWithOneCharacterName(String field) {
        int length = 1;
        boolean useLetters = true;
        boolean useNumbers = false;

        Map<String, String> userData = new HashMap<>();
        userData.put(field, RandomStringUtils.random(length, useLetters, useNumbers));
        userData = DataGenerator.getRegistrationData(userData);

        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(response, "The value of '" + field + "' field is too short");
        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonHasNotField(response, "id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    @Description("This test check registration of an user with very long name")
    @DisplayName("Test negative registration user")
    public void testCreateUserWithLongName(String field) {
        int length = 251;
        boolean useLetters = true;
        boolean useNumbers = false;

        Map<String, String> userData = new HashMap<>();
        userData.put(field, RandomStringUtils.random(length, useLetters, useNumbers));
        userData = DataGenerator.getRegistrationData(userData);

        Response response = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(response, "The value of '" + field + "' field is too long");
        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertJsonHasNotField(response, "id");
    }


}
