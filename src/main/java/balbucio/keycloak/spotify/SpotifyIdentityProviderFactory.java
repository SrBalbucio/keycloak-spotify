package balbucio.keycloak.spotify;

import com.google.auto.service.AutoService;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

@AutoService(SocialIdentityProviderFactory.class)
public class SpotifyIdentityProviderFactory extends AbstractIdentityProviderFactory<SpotifyIdentityProvider>
        implements SocialIdentityProviderFactory<SpotifyIdentityProvider> {

    public static final String PROVIDER_ID = "spotify";

    private static final List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<>();

    static {
        ProviderConfigProperty userInfoUrl = new ProviderConfigProperty();
        userInfoUrl.setName(SpotifyIdentityProviderConfig.USER_INFO_URL);
        userInfoUrl.setLabel("User Info URL");
        userInfoUrl.setType(ProviderConfigProperty.STRING_TYPE);
        userInfoUrl.setDefaultValue(SpotifyIdentityProviderConfig.DEFAULT_USER_INFO_URL);
        userInfoUrl.setHelpText("Spotify endpoint used to load the user profile.");
        CONFIG_PROPERTIES.add(userInfoUrl);
    }

    @Override
    public String getName() {
        return "Spotify";
    }

    @Override
    public SpotifyIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        SpotifyIdentityProviderConfig config = new SpotifyIdentityProviderConfig(model);
        config.setAuthorizationUrl(SpotifyIdentityProviderConfig.DEFAULT_AUTH_URL);
        config.setTokenUrl(SpotifyIdentityProviderConfig.DEFAULT_TOKEN_URL);
        if (isBlank(config.getDefaultScope())) {
            config.setDefaultScope("user-read-email user-read-private");
        }
        if (isBlank(config.getUserInfoUrl())) {
            config.setUserInfoUrl(SpotifyIdentityProviderConfig.DEFAULT_USER_INFO_URL);
        }
        return new SpotifyIdentityProvider(session, config);
    }

    @Override
    public SpotifyIdentityProviderConfig createConfig() {
        return new SpotifyIdentityProviderConfig();
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


