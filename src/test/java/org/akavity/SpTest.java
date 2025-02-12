package org.akavity;

import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.akavity.specifications.Specifications;
import org.akavity.utils.SpotifyToken;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class SpTest {
    SpotifyToken spotifyToken = new SpotifyToken();
    Header header = spotifyToken.getAuthHeader();
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void getArtist() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .header(header)
                .when()
                .get("v1/artists/4Z8W4fKeB5YxbusRsdQVPb")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String name = jsonPath.get("name");
        Assert.assertEquals(name, "Radiohead");
    }

    @Test
    public void getAlbum() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .header(header)
                .when()
                .get("v1/albums/4aawyAB9vmqN3uQ7FjRGTy")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
       String type = jsonPath.get("album_type");
       int totalTracks = jsonPath.get("total_tracks");
       Assert.assertEquals(type, "album");
       Assert.assertEquals(totalTracks, 18);
    }
}
