/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.InterbibliotecarioJpaController;
import clases.Interbibliotecario;
import clases.Mensaje;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.JWT_Util;

/**
 * REST Web Service
 *
 * @author irene
 */
@Path("interbibliotecario")
public class ServiceRESTInterbibliotecario {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest httpRequest;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    /**
     * Creates a new instance of ServiceRESTInterbibliotecario
     */
    public ServiceRESTInterbibliotecario() {
    }

    @GET
    @Path("create-session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession() {

        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        jsonOB.add("session", httpRequest.getSession().getId());
        JsonObject json = jsonOB.build();

        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();

    }

    //GET ALL
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Interbibliotecario> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            InterbibliotecarioJpaController dao = new InterbibliotecarioJpaController(emf);
            lista = dao.findInterbibliotecarioEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList<>();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                response = JWT_Util.getData(token, httpRequest);
            } else {
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            }
            return response;
        }
    }

    //AÑADE PRÉSTAMO Interbibliotecario
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response creaInterbibliotecario(Interbibliotecario prestamoACrear,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.FOUND;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                InterbibliotecarioJpaController dao = new InterbibliotecarioJpaController(emf);
                Interbibliotecario prestamo = dao.findInterbibliotecario(prestamoACrear.getIdInterbibliotecario());
                if (prestamo == null) {
                    dao.create(prestamoACrear);
                    mensaje.setMensaje("Prétamo interbibliotecario creado");
                    statusResul = Response.Status.OK;
                }

                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje("Error al eliminar el prétamo interbibliotecario");
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    //EDITA PRÉSTAMO Interbibliotecario
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editaInterbibliotecario(Interbibliotecario prestamoAEditar,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.BAD_GATEWAY;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                InterbibliotecarioJpaController dao = new InterbibliotecarioJpaController(emf);
                Interbibliotecario prestamo = dao.findInterbibliotecario(prestamoAEditar.getIdInterbibliotecario());
                if (prestamo != null) {
                    dao.edit(prestamoAEditar);
                    mensaje.setMensaje("Prétamo interbibliotecario editado");
                    statusResul = Response.Status.OK;
                }

                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje("Error al eliminar el prétamo interbibliotecario");
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    //ELIMINA EVENTO
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteInterbibliotecario(@PathParam("id") int id,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.BAD_GATEWAY;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                InterbibliotecarioJpaController dao = new InterbibliotecarioJpaController(emf);
                Interbibliotecario prestamo = dao.findInterbibliotecario(id);
                if (prestamo != null) {
                    dao.destroy(id);
                    mensaje.setMensaje("Prétamo interbibliotecario eliminado");
                }

                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje("Error al eliminar el prétamo interbibliotecario");
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

}
