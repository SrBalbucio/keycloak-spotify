package balbucio.keycloak.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyUserProfile {

    private String id;

    private String email;

    @JsonProperty("display_name")
    private String displayName;

    private String country;

    private List<Image> images;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
        private Integer width;
        private Integer height;
    }
}

