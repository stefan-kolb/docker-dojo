package de.uniba.dsg.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

public class Api {
    private static final Logger LOGGER = Logger.getLogger(Api.class.getName());

    private static SpotifyApi spotifyApi;
    private static volatile LocalDateTime expirationTime;
    private static final Object lock = new Object();

    static {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(System.getenv("spotifyClientId"))
                .setClientSecret(System.getenv("spotifyClientSecret"))
                .build();
    }

    private Api() {
    }

    public static SpotifyApi getInstance() {
        synchronized (lock) {
            try {
                signIn();
            } catch (SpotifyWebApiException | IOException e) {
                LOGGER.severe("Failed to authenticate with Spotify API.");
            }
            return spotifyApi;
        }
    }

    private static void signIn() throws SpotifyWebApiException, IOException {
        if (expirationTime == null || expirationTime.isBefore(LocalDateTime.now())) {
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials credentials = clientCredentialsRequest.execute();
            // expiration is in seconds
            expirationTime = LocalDateTime.now().plusSeconds(credentials.getExpiresIn() - 5);
            spotifyApi.setAccessToken(credentials.getAccessToken());
        }
    }
}
