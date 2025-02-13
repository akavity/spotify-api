package org.akavity.enums;

public enum PathEnum {
    TOP_TRACKS("tracks.name"),
    ARTISTS("name"),
    ALBUMS_TRACKS("items.name");

    private final String path;

    PathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
