import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TokensTest {
    @Test
    public void tokensTest() throws InterruptedException {
        JsonPath stepOne = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = stepOne.get("token");
        Integer timeLeft = stepOne.get("seconds");

        JsonPath stepTwo = RestAssured
                .given()
                .param("token", token)
                .expect()
                .body(Matchers.containsString("Job is NOT ready"))
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Thread.sleep(timeLeft * 1000);
        JsonPath stepThree = RestAssured
                .given()
                .param("token", token)
                .expect()
                .body(Matchers.containsString("result"),Matchers.containsString("Job is ready"))
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
    }
}
