package org.akavity.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumData {
    int statusCode;
    String response;
    String expectedName;
    String type;
    int totalTracks;
}
