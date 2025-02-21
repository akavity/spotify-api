package org.akavity;

import org.akavity.utils.tokens.AuthUrlCreator;

public class GetAuthUrl {
    public static void main(String[] args) {
        AuthUrlCreator authUrlCreator = new AuthUrlCreator("user-read-private user-read-email");
        String codeVerifier = authUrlCreator.getCODE_VERIFIER();
        String authUrl = authUrlCreator.getAUTH_URL();
        System.out.println("Code verifier: " + codeVerifier);
        System.out.println("Redirecting to: " + authUrl);
    }
}
