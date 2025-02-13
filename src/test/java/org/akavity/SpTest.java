package org.akavity;

import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.akavity.annotations.TestData;
import org.akavity.enums.PathEnum;
import org.akavity.models.AlbumData;
import org.akavity.models.AlbumTracksData;
import org.akavity.models.ArtistData;
import org.akavity.models.TopTracksData;
import org.akavity.specifications.Specifications;
import org.akavity.utils.JsonReader;
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

    @TestData(jsonFile = "artistData", model = "ArtistData")
    @Test(description = "API GET Artist", dataProviderClass = JsonReader.class, dataProvider = "getData")
    public void getArtist(ArtistData artist) {
        specifications = new Specifications(URL, artist.getStatusCode());
        Response response = given()
                .header(header)
                .when()
                .get(artist.getResponse())
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String actualName = jsonPath.get(PathEnum.ARTISTS.getPath());

        Assert.assertEquals(actualName, artist.getExpectedName());
    }

    @TestData(jsonFile = "topTracksData", model = "TopTracksData")
    @Test(description = "API GET Top Tracks", dataProviderClass = JsonReader.class, dataProvider = "getData")
    public void getTopTracks(TopTracksData topTracks) {
        specifications = new Specifications(URL, topTracks.getStatusCode());
        Response response = given()
                .header(header)
                .when()
                .get(topTracks.getResponse())
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> actualTracks = utils.extractTracks(jsonPath, PathEnum.TOP_TRACKS);
        List<String> expectedTracks = new ArrayList<>(topTracks.getListOfTracks());

        Assert.assertTrue(actualTracks.containsAll(expectedTracks));
    }

    @TestData(jsonFile = "albumData", model = "AlbumData")
    @Test(description = "API GET Album", dataProviderClass = JsonReader.class, dataProvider = "getData")
    public void getAlbum(AlbumData album) {
        specifications = new Specifications(URL, album.getStatusCode());
        Response response = given()
                .header(header)
                .when()
                .get(album.getResponse())
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String type = jsonPath.get(PathEnum.ALBUM_TYPE.getPath());
        int totalTracks = jsonPath.get(PathEnum.ALBUM_TOTAL_TRACKS.getPath());

        Assert.assertEquals(type, album.getType());
        Assert.assertEquals(totalTracks, album.getTotalTracks());
    }

    @TestData(jsonFile = "albumTracksData", model = "AlbumTracksData")
    @Test(description = "API GET Album Tracks", dataProviderClass = JsonReader.class, dataProvider = "getData")
    public void getAlbumTracks(AlbumTracksData album) {
        specifications = new Specifications(URL, album.getStatusCode());
        Response response = given()
                .header(header)
                .when()
                .get(album.getResponse())
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> actualAlbumTracks = utils.extractTracks(jsonPath, PathEnum.ALBUMS_TRACKS);
        List<String> expectedAlbumTracks = new ArrayList<>(album.getListOfTracks());

        Assert.assertTrue(actualAlbumTracks.containsAll(expectedAlbumTracks));
    }
}
