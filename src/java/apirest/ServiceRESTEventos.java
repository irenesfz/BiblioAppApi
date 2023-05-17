/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.EventosJpaController;
import clases.Eventos;
import clases.Mensaje;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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
@Path("eventos")
public class ServiceRESTEventos {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    public ServiceRESTEventos() {
    }
    @Context
    private HttpServletRequest httpRequest;

    /* private String getRolUserIdentificado() {
        String rol = "";
        if (httpRequest.getSession().getAttribute("rol") != null) {
            if (((String) httpRequest.getSession().getAttribute("rol")).equals("administrador")) {
                rol = (String) httpRequest.getSession().getAttribute("rol");
            }
        }
        return rol;
    }*/

    //GET ALL EVENTOS
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Eventos> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            EventosJpaController dao = new EventosJpaController(emf);
            lista = dao.findEventosEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
            }

        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();

            Response response = Response
                    .status(statusResul)
                    .entity(lista)
                    .build();
            return response;
        }
    }

    //EDITA EVENTO
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putEvento(Eventos eventoAEditar,
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
                EventosJpaController dao = new EventosJpaController(emf);
                Eventos eventoFound = dao.findEventos(eventoAEditar.getIdEvento());
                if (eventoFound != null) {
                    dao.edit(eventoAEditar);
                    mensaje.setMensaje("Evento actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar evento");
                }
                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
            return response;
        }
    }

    //añade
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postEvento(Eventos eventoACrear,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.BAD_GATEWAY;
        Mensaje mensaje = new Mensaje();

        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                EventosJpaController dao = new EventosJpaController(emf);
                Eventos eventoFound = dao.findEventos(eventoACrear.getIdEvento());
                if (eventoFound == null) {
                    Calendar cal = Calendar.getInstance();
                    //fecha de hoy
                    eventoACrear.setFechaPublicacion(cal.getTime());
                    dao.create(eventoACrear);
                    mensaje.setMensaje("Evento insertado");
                } else {
                    statusResul = Response.Status.FOUND;
                    mensaje.setMensaje("Error, el evento ya existe");
                }
                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje("Error genérico");
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
    public Response deleteEvento(@PathParam("id") int id,
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
                EventosJpaController daoEventos = new EventosJpaController(emf);

                int num = daoEventos.deleteEventosJPQL(daoEventos, id);
                if (num != 0) {
                    mensaje.setMensaje("Evento eliminado");
                } else {
                    mensaje.setMensaje("Error al eliminar el evento");
                }

                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @GET
    @Path("fecha/{fecha}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEventoDestacado(@PathParam("fecha") String fecha) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Response response = null;
        List<Eventos> lista = null;
        try {

            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            EventosJpaController dao = new EventosJpaController(emf);
            EntityManager em = dao.getEntityManager();
            TypedQuery<Eventos> consultaPorFecha
                    = em.createNamedQuery("Eventos.findByFechaEventoLike", Eventos.class)
                            .setParameter("fechaEvento", fecha + "%");

            lista = consultaPorFecha.getResultList();
            if (lista == null) {
                statusResul = Response.Status.NOT_FOUND;
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();

            response = Response
                    .status(statusResul)
                    .entity(lista)
                    .build();
        }
        return response;
    }
}
