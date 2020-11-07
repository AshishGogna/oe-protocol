package api;

import api.models.ApiUser;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Author: Ashish Gogna
 */

public class NodeApplication extends Application<Configuration> {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeApplication.class);

    /** Public functions */
    @Override
    public void initialize(Bootstrap<Configuration> b) { }

    @Override
    public void run(Configuration c, Environment e) throws Exception {

        //Register services
        e.jersey().register(new NodeApi(e.getValidator()));
        e.jersey().register(new AuthDynamicFeature(
                new ApiAuthFilter.Builder<ApiUser>()
                        .setAuthenticator(new ApiAuthenticator())
                        .setUnauthorizedHandler(new ResponseHandler401())
                        .buildAuthFilter()
        ));
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(ApiUser.class));
        e.jersey().register(new JsonProcessingExceptionMapper(true));
        e.jersey().register(MultiPartFeature.class);

        final FilterRegistration.Dynamic cors = e.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "POST");
        //cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        LOGGER.info("Registered REST resources.");
    }
}
