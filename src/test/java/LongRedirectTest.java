import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirectTest {
    @Test
    public void longRedirectTest() {
        String uri = "https://playground.learnqa.ru/api/long_redirect";

        while (true) {
            Response checkLocation = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(uri)
                    .andReturn();

            Response goLocation = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(uri)
                    .andReturn();

            uri = checkLocation.getHeader("Location");
            if (goLocation.getStatusCode() == 200) {
                break;
            }
        }
    }
}
