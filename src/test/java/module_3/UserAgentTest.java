package module_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentTest {

    @ParameterizedTest
    @ValueSource(strings =
            {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                    "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                    "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
            })
    public void userAgentTest(String userAgent) {

        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        switch (userAgent) {
            case "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30":
                assertEquals("Mobile", response.getString("platform"), "Unknown platform");
                assertEquals("No", response.getString("browser"), "Unknown browser");
                assertEquals("Android", response.getString("device"), "Unknown device");
                break;
            case "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1":
                assertEquals("Mobile", response.getString("platform"), "Unknown platform");
                assertEquals("Chrome", response.getString("browser"), "Unknown browser");
                assertEquals("iOs", response.getString("device"), "Unknown device");
                break;
            case "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)":
                assertEquals("Googlebot", response.getString("platform"), "Unknown platform");
                assertEquals("Unknown", response.getString("browser"), "Unknown browser");
                assertEquals("Unknown", response.getString("device"), "Unknown device");
                break;
            case "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0":
                assertEquals("Web", response.getString("platform"), "Unknown platform");
                assertEquals("Chrome", response.getString("browser"), "Unknown browser");
                assertEquals("No", response.getString("device"), "Unknown device");
                break;
            case "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1":
                assertEquals("Mobile", response.getString("platform"), "Unknown platform");
                assertEquals("No", response.getString("browser"), "Unknown browser");
                assertEquals("iPhone", response.getString("device"), "Unknown device");
                break;
            default:
                throw new IllegalArgumentException("User Agent value is known: " + userAgent);
        }
    }
}