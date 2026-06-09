package org.example.bookapi;

import jakarta.ws.rs.ApplicationPath;
import org.example.bookapi.security.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication() {
        packages("org.example.bookapi.services");
        register(AuthenticationFilter.class);
    }
}