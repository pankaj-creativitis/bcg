package BCG5.bcg;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("webapi")
public class JerseyAppConfiguration extends ResourceConfig{
	
    public JerseyAppConfiguration() {
        packages("BCG5.bcg");
   }

}
