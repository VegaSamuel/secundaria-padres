package com.mycompany.escuelaalumnoscursos;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        resources.add(seguridad.JWTFilter.class);
        resources.add(com.mycompany.escuelaalumnoscursos.resources.AlumnosCursosResource.class);
        
        return resources;
    }
}
