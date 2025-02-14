package org.akavity.enums;

public enum PathEnum {
    TOP_TRACKS("tracks.name"),
    ARTISTS("name"),
    ALBUM_TYPE("album_type"),
    ALBUM_TOTAL_TRACKS("total_tracks"),
    ALBUMS_TRACKS("items.name"),
    ERROR_STATUS("error.status"),
    ERROR_MESSAGE("error.message");

    private final String path;

    PathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
