import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class GetJSONTest {

    @Test
    public void getJSONTest() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String answer = response.getList("messages").get(1).toString();

        int indexStart = answer.indexOf("=");
        int indexEnd = answer.indexOf(",");
        String getMessage = answer.substring(indexStart + 1, indexEnd);

        System.out.println();
        System.out.println(getMessage);
    }
}

