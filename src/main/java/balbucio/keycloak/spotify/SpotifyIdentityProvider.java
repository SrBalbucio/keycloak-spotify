package balbucio.keycloak.spotify;

import lombok.extern.jbosslog.JBossLog;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@JBossLog
public class SpotifyIdentityProvider extends AbstractOAuth2IdentityProvider<SpotifyIdentityProviderConfig>
        implements SocialIdentityProvider<SpotifyIdentityProviderConfig> {

    public SpotifyIdentityProvider(KeycloakSession session, SpotifyIdentityProviderConfig config) {
        super(session, config);
    }

    @Override
    protected String getDefaultScopes() {
        return "user-read-email user-read-private";
    }

    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        final SpotifyUserProfile profile = getUserProfile(accessToken);
        if (isBlank(profile.getId())) {
            throw new IdentityBrokerException("Spotify response does not include user id");
        }

        final BrokeredIdentityContext context = new BrokeredIdentityContext(profile.getId(), getConfig());
        context.setBrokerUserId(profile.getId());
        context.setUsername(profile.getDisplayName());
        context.setModelUsername(context.getUsername());
        context.setEmail(profile.getEmail());
        context.setIdp(this);
        context.setUserAttribute("spotify.id", profile.getId());
        context.setUserAttribute("spotify.display_name", Objects.toString(profile.getDisplayName(), ""));
        context.setUserAttribute("spotify.country", Objects.toString(profile.getCountry(), ""));

        getPrimaryImage(profile.getImages()).ifPresent(url -> {
            context.setUserAttribute("spotify.image_url", url);
            context.setUserAttribute("picture", url);
        });

        return context;
    }

    private SpotifyUserProfile getUserProfile(String accessToken) {
        final HttpGet request = new HttpGet(getConfig().getUserInfoUrl());
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        try (CloseableHttpResponse response = session.getProvider(HttpClientProvider.class)
                .getHttpClient()
                .execute(request)) {
            final int status = response.getStatusLine().getStatusCode();
            final String payload = response.getEntity() == null
                    ? ""
                    : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status != HttpStatus.SC_OK) {
                throw new IdentityBrokerException("Spotify userinfo request failed with status " + status + ": " + payload);
            }

            return JsonSerialization.readValue(payload, SpotifyUserProfile.class);
        } catch (IOException e) {
            throw new IdentityBrokerException("Could not fetch Spotify user profile", e);
        }
    }

    private java.util.Optional<String> getPrimaryImage(List<SpotifyUserProfile.Image> images) {
        if (images == null || images.isEmpty()) {
            return java.util.Optional.empty();
        }
        return images.stream()
                .map(SpotifyUserProfile.Image::getUrl)
                .filter(Objects::nonNull)
                .filter(url -> !isBlank(url))
                .findFirst();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


