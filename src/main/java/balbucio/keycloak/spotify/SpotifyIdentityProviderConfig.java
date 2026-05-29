package balbucio.keycloak.spotify;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;

@Getter
@Setter
public class SpotifyIdentityProviderConfig extends OAuth2IdentityProviderConfig {

    public static final String DEFAULT_AUTH_URL = "https://accounts.spotify.com/authorize";
    public static final String DEFAULT_TOKEN_URL = "https://accounts.spotify.com/api/token";
    public static final String DEFAULT_USER_INFO_URL = "https://api.spotify.com/v1/me";
    public static final String USER_INFO_URL = "userInfoUrl";

    public SpotifyIdentityProviderConfig() {
        setAuthorizationUrl(DEFAULT_AUTH_URL);
        setTokenUrl(DEFAULT_TOKEN_URL);
        setDefaultScope("user-read-email user-read-private");
        setStoreToken(true);
        setUserInfoUrl(DEFAULT_USER_INFO_URL);
    }

    public SpotifyIdentityProviderConfig(org.keycloak.models.IdentityProviderModel model) {
        super(model);
    }

    public String getUserInfoUrl() {
        return getConfig().getOrDefault(USER_INFO_URL, DEFAULT_USER_INFO_URL);
    }

    public void setUserInfoUrl(String userInfoUrl) {
        getConfig().put(USER_INFO_URL, userInfoUrl);
    }
}

