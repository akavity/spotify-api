package org.akavity;

import org.akavity.utils.SpotifyToken;

import java.io.IOException;

public class GetToken {
    public static void main(String[] args) throws IOException {
        SpotifyToken token = new SpotifyToken();
        token.get();
        System.out.println(token.accessToken);
        System.out.println(token.expiresIn);
    }
}
