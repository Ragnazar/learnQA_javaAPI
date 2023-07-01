package module_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {
    @Test
    public void longRedirectTest() {
        String uri = "https://playground.learnqa.ru/api/long_redirect";
        int count = 0;
        while (true) {
            Response checkLocation = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(uri)
                    .andReturn();

            uri = checkLocation.getHeader("Location");
            count++;
            if (checkLocation.getStatusCode() == 200) {
                System.out.println(count);
                break;
            }

        }
    }
}
