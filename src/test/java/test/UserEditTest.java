package test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User edit cases")
@Feature("User edit")
public class UserEditTest extends BaseTestCase {
    @Test
    @Description("This test successfully create a user and changes his firstname")
    @DisplayName("Test positive edit user")
    public void testEditJustCreatedUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = ApiCoreRequests
                .makePutRequestWithCookieAndToken(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //GET
        Response responseUserData = ApiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("This test create a user and make a try to change his firstname without auth")
    @DisplayName("Test negative edit user")
    public void testEditJustCreatedTestWithoutAuth() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = ApiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //GET
        Response responseUserData = ApiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    @Description("This test create a user and make a try to change a firstname of another user")
    @DisplayName("Test negative edit user")
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = ApiCoreRequests
                .makePutRequestWithCookieAndToken(
                        "https://playground.learnqa.ru/api/user/2",
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //GET
        Response responseUserData = ApiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/2",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertJsonByNameIsNotEqual(responseUserData, "firstName", newName);
    }

    @Test
    @Description("This test create a user and make a try to change his email to invalid one")
    @DisplayName("Test negative edit user")
    public void testEditCreatedUsersEmailToInvalid() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newEmail = "newemail.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);
        Response responseEditUser = ApiCoreRequests
                .makePutRequestWithCookieAndToken(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    @Description("This test create a user and make a try to change his firstname to very short")
    @DisplayName("Test negative edit user")
    public void testEditCreatedUsersFirstnameToVeryShort() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        int length = 1;
        boolean useLetters = true;
        boolean useNumbers = false;

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", RandomStringUtils.random(length, useLetters, useNumbers));
        Response responseEditUser = ApiCoreRequests
                .makePutRequestWithCookieAndToken(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }


}
