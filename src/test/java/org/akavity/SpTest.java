package org.akavity;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.akavity.specifications.Specifications;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class SpTest {
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void test() {
        specifications = new Specifications(URL, 200);
        Map<String, String> user = new HashMap<>();
        user.put("Content-Type", "application/x-www-form-urlencoded");
        user.put("text", "grant_type=client_credentials&client_id=d9edbd1d682f4634b52688b96aa4af34&client_secret=5d1f3506dc1b4c16b455cd035784fff6");
        Response response = given()
                .body(user)
                .when()
                .post("https://accounts.spotify.com/api/token")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.getString("access_token");
        System.out.println(token);
    }
}
