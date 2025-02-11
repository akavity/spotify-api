package org.akavity;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.akavity.specifications.Specifications;
import org.akavity.utils.SpotifyToken;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class SpTest {
    SpotifyToken spotifyToken = new SpotifyToken();
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void test() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .header(spotifyToken.getAccessTokenHeader())
                .when()
                .get("v1/artists/4Z8W4fKeB5YxbusRsdQVPb")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String name = jsonPath.get("name");
        System.out.println(name);
        //Assert.assertEquals(error, "Missing password");
    }
}
