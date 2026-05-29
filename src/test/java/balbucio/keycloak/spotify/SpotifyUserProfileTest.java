package balbucio.keycloak.spotify;

import org.junit.jupiter.api.Test;
import org.keycloak.util.JsonSerialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotifyUserProfileTest {

    @Test
    void shouldDeserializeSpotifyProfilePayload() throws Exception {
        String payload = "{"
                + "\"id\":\"spotify-user-1\","
                + "\"email\":\"user@example.com\","
                + "\"display_name\":\"Spotify User\","
                + "\"country\":\"BR\","
                + "\"images\":[{\"url\":\"https://image.cdn/avatar.jpg\",\"height\":300,\"width\":300}]"
                + "}";

        SpotifyUserProfile profile = JsonSerialization.readValue(payload, SpotifyUserProfile.class);

        assertEquals("spotify-user-1", profile.getId());
        assertEquals("user@example.com", profile.getEmail());
        assertEquals("Spotify User", profile.getDisplayName());
        assertEquals("BR", profile.getCountry());
        assertEquals("https://image.cdn/avatar.jpg", profile.getImages().get(0).getUrl());
    }
}

