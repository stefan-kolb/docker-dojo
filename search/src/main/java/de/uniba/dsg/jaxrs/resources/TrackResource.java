package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import de.uniba.dsg.jaxrs.Api;
import de.uniba.dsg.jaxrs.exceptions.ErrorMessage;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.jaxrs.models.Song;

@Path("tracks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrackResource {
    @GET
    @Path("search")
    public Song queryTrack(@QueryParam("title") String title, @QueryParam("artist") String artist) {
        try {
            final SearchTracksRequest req = Api.getInstance().searchTracks(artist + " " + title).limit(1).build();

            List<Track> tracks = Arrays.asList(req.execute().getItems());
            if (tracks.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No track found for title %s and artist %s", title, artist)));
            }

            Track track = tracks.get(0);
            Song song = new Song();
            song.setTitle(track.getName());
            song.setArtist(Arrays.asList(track.getArtists()).get(0).getName());
            song.setId(track.getId());

            return song;
        } catch (IOException | SpotifyWebApiException e) {
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}
