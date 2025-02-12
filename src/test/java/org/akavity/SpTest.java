package org.akavity;

import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.akavity.specifications.Specifications;
import org.akavity.utils.SpotifyToken;
import org.akavity.utils.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class SpTest {
    Utils utils = new Utils();
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
    public void getTopTracks() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .header(header)
                .when()
                .get("v1/artists/0TnOYISbd1XYRBk9myaseg/top-tracks")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> expectedTracks = jsonPath.getList("tracks.name", String.class).stream().map(x -> utils.extractText(x, "^[^(]+")).toList();
        List<String> actualTracks = new ArrayList<>(List.of("Give Me Everything",
                "Timber",
                "DJ Got Us Fallin' In Love",
                "Time of Our Lives",
                "International Love",
                "Hotel Room Service",
                "Feel This Moment",
                "Fireball",
                "Hey Baby",
                "I Know You Want Me"));

        Assert.assertTrue(actualTracks.containsAll(expectedTracks));
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
