package org.akavity.tests;

import org.akavity.specifications.Specifications;
import org.akavity.utils.SpotifyHeaders;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.akavity.utils.tokens.Endpoints.AUTH_TOKEN;

public class AuthRespTest {
    SpotifyHeaders createHeader = new SpotifyHeaders(AUTH_TOKEN);
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void playTrack() {
        specifications = new Specifications(URL, 200);
        given()
                .headers(createHeader.getHeaders())
                .when()
                .put("v1/me/player/play")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void pauseTrack() {
        specifications = new Specifications(URL, 200);
        given()
                .headers(createHeader.getHeaders())
                .when()
                .put("v1/me/player/pause")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void skipToNextTrack() {
        specifications = new Specifications(URL, 200);
        given()
                .headers(createHeader.getHeaders())
                .when()
                .post("v1/me/player/next")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void skipToPreviousTrack() {
        specifications = new Specifications(URL, 200);
        given()
                .headers(createHeader.getHeaders())
                .when()
                .post("v1/me/player/previous")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void setPlaybackVolumeTrack() {
        specifications = new Specifications(URL, 204);
        given()
                .headers(createHeader.getHeaders())
                .when()
                .put("v1/me/player/volume?volume_percent=50")
                .then().log().all()
                .extract().response();
    }
}
