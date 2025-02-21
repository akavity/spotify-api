package org.akavity;

import org.akavity.utils.Utils;
import org.akavity.utils.tokens.AuthToken;

public class GetAuthToken {
    public static void main(String[] args) {
        Utils utils = new Utils();
        String redirectUrl = "";
        String codeVerifier = "";
        String code = utils.extractCodeFromURL(redirectUrl);
        System.out.println(code);
        AuthToken authToken = new AuthToken(code, codeVerifier);
        System.out.println("Access Token: " + authToken.getAccessToken());
        System.out.println("Refresh Token: " + authToken.getRefreshToken());
    }
}
