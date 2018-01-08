import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class SponsoredServiceHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(SponsoredServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(SponsoredServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}