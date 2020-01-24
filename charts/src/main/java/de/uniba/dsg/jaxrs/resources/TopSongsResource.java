package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.BadRequestException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import de.uniba.dsg.jaxrs.Api;
import de.uniba.dsg.jaxrs.exceptions.ErrorMessage;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.jaxrs.models.Song;

@Path("charts")
public class TopSongsResource {

    @GET
    @Path("{artist}")
    public List<Song> getPopularSongs(@PathParam("artist") String artistId) {
        GetArtistsTopTracksRequest tracksRequest = Api.getInstance().getArtistsTopTracks(artistId, CountryCode.DE).build();

        try {
            List<Track> trackList = Arrays.asList(tracksRequest.execute());

            List<Song> songs = new ArrayList<>();

            for (Track track : trackList) {
                Song s = new Song();
                s.setId(s.getId());
                s.setArtist(Arrays.asList(track.getArtists()).stream().map(a -> a.getName()).collect(Collectors.joining(", ")));
                s.setTitle(track.getName());
                songs.add(s);
            }
            // Limit results
            if (songs.size() > 5) {
                songs = Arrays.asList(Arrays.copyOf(songs.toArray(new Song[songs.size()]), 5));
            }

            return songs;
        } catch (BadRequestException e) {
            throw new ResourceNotFoundException(new ErrorMessage(e.getMessage()));
        } catch (SpotifyWebApiException | IOException e) {
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}
