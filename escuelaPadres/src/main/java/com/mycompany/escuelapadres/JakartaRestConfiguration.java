package com.mycompany.escuelapadres;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import seguridad.JWTFilter;

/**
 * Configures Jakarta RESTful Web Services for the application.
 */
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {    
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        resources.add(JWTFilter.class);
        
        return resources;
    }
}

