import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService("apartments")
@ApplicationPath("v1")
public class SponsoredApplication extends Application {
}
