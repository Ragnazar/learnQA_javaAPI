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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User delete cases")
@Feature("User delete")
public class UserDeleteTest extends BaseTestCase {
    @Test
    @Description("This test make a try to delete user with id = 2")
    @DisplayName("Test negative delete user")
    public void testDeleteAReservedUser() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = ApiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = ApiCoreRequests
                .makeDeleteRequestWithTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        Assertions.assertResponseCodeEquals(responseDeleteUser,400);
    }

    @Test
    @Description("This test successfully create a user and delete him")
    @DisplayName("Test positive delete user")
    public void testDeleteJustCreatedUser() {
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

        //DELETE
        Response responseDeleteUser = ApiCoreRequests
                .makeDeleteRequestWithTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //GET
        Response responseUserData = ApiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseTextEquals(responseUserData,"User not found");
        Assertions.assertResponseCodeEquals(responseUserData,404);
    }

    @Test
    @Description("This test authorize by just created user and make a try to delete another existing one")
    @DisplayName("Test negative delete user")
    public void testDeleteByJustCreatedUserAnotherOne() {
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

        //DELETE
        Response responseDeleteUser = ApiCoreRequests
                .makeDeleteRequestWithTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/2" ,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //GET
        Response responseUserData = ApiCoreRequests.makeGetRequestWithoutAuth(
                "https://playground.learnqa.ru/api/user/2"
        );
        Assertions.assertJsonHasField(responseUserData,"username");
    }
}
