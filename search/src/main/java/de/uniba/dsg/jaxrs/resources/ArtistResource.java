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
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import de.uniba.dsg.jaxrs.Api;
import de.uniba.dsg.jaxrs.exceptions.ErrorMessage;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.jaxrs.models.Artist;

@Path("artists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArtistResource {
    @GET
    @Path("search")
    public Artist queryArtist(@QueryParam("artist") String artist) {
        try {
            final SearchArtistsRequest req = Api.getInstance().searchArtists(artist).build();

            List<com.wrapper.spotify.model_objects.specification.Artist> artists = Arrays.asList(req.execute().getItems());
            if (artists.isEmpty()) {
                throw new ResourceNotFoundException(new ErrorMessage(String.format("No artist found for artist %s", artist)));
            }

            com.wrapper.spotify.model_objects.specification.Artist retArtist = artists.get(0);
            Artist art = new Artist();
            art.setName(retArtist.getName());
            art.setId(retArtist.getId());

            return art;
        } catch (IOException | SpotifyWebApiException e) {
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}
