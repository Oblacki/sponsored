import com.fasterxml.jackson.databind.ObjectMapper;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.enterprise.context.RequestScoped;
import com.kumuluz.ee.logs.cdi.Log;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.xml.crypto.Data;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("sponsored")
@Log
public class SponsoredResource {

    private Client httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "apartments", version = "1.0.x", environment = "dev")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        objectMapper = new ObjectMapper();
    }

    @Inject
    private RestProperties restProperties;

    @GET
    public Response getAllSponsored() {
        return Response.ok(Database.getSponsored()).build();
    }

    @GET
    @Path("list")
    public Response getAllSponsoredApartments() {
        return Response.ok(getApartments()).build();
    }

    @GET
    @Path("add/{apartmentId}")
    public Response addNewSponsored(@PathParam("apartmentId") String apartmentId) {

        if(baseUrl.isPresent()) {

                try {
                    String url = baseUrl.get() + "/v1/apartments/" + apartmentId;
                    Apartment apartment = httpClient.target(url).request().get(new GenericType<Apartment>() {});
                    Database.addSponsored(apartmentId);

                } catch (Exception e) {
                    System.out.println("Exception");
                    String msg = e.getClass().getName() + " occured: " + e.getMessage();
                    throw new InternalServerErrorException(msg);
                }

            return Response.noContent().build();
        }

        else return Response.noContent().build();

        
    }

    @DELETE
    @Path("{apartmentId}")
    public Response deleteSponsored(@PathParam("apartmentId") String apartmentId) {
        Database.deleteSponsored(apartmentId);
        return Response.noContent().build();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getApartmentsFallback")
    @Timeout//(value = 2, unit = ChronoUnit.SECONDS)
    public List<Apartment> getApartments(){

        List<String> sponsored = Database.getSponsored();
        List<Apartment> sponsoredApartments = new ArrayList<>();

        System.out.println("bla");

        if(baseUrl.isPresent() && sponsored.size() > 0) {

            for(int i = 0; i < sponsored.size(); i++){
                try {
                    String url = baseUrl.get() + "/v1/apartments/" + sponsored.get(i);
                    System.out.println(url);
                    Apartment apartment = httpClient.target(url).request().get(new GenericType<Apartment>() {});
                    System.out.println(apartment);
                    sponsoredApartments.add(apartment);

                } catch (Exception e) {
                    System.out.println("Exception");
                    String msg = e.getClass().getName() + " occured: " + e.getMessage();
                    throw new InternalServerErrorException(msg);
                }

            }

            return sponsoredApartments;
        }
        else
            return new ArrayList<>();
    }

    public List<Apartment> getApartmentsFallback() {

        List<Apartment> apartments = new ArrayList<>();

        Apartment ap = new Apartment();

        ap.setId("N/A");
        ap.setNumOfBeds(0);
        ap.setCustomerId("N/A");

        List<String> guestIds = new ArrayList<>();
        guestIds.add("N/A");
        ap.setGuestIds(guestIds);

        ap.setPricePerNight(-1.0);

        apartments.add(ap);

        return apartments;

    }
}
