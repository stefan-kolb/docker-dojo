package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import de.uniba.dsg.jaxrs.Api;
import de.uniba.dsg.jaxrs.exceptions.ErrorMessage;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.jaxrs.models.Cover;

@Path("covers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CoversResource {
    @GET
    @Path("{trackid}")
    public Cover getCover(@PathParam("trackid") String trackID) {
        try {
            final GetTrackRequest req = Api.getInstance().getTrack(trackID).build();
            Track track = req.execute();

            if (track == null) {
                throw new ResourceNotFoundException(new ErrorMessage("No track found for track ID " + trackID));
            }
            return new Cover(Arrays.asList(track.getAlbum().getImages()).get(1).getUrl());
        } catch (IOException | SpotifyWebApiException e) {
            throw new RemoteApiException(new ErrorMessage(e.getMessage()));
        }
    }
}
