/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author irene
 */
@javax.ws.rs.ApplicationPath("v1")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(apirest.ServiceRESTAuthJWT.class);
        resources.add(apirest.ServiceRESTEventos.class);
        resources.add(apirest.ServiceRESTInterbibliotecario.class);
        resources.add(apirest.ServiceRESTLibro.class);
        resources.add(apirest.ServiceRESTLibros.class);
        resources.add(apirest.ServiceRESTPrestamos.class);
        resources.add(apirest.ServiceRESTReservas.class);
        resources.add(apirest.ServiceRESTSocios.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }
    
}
