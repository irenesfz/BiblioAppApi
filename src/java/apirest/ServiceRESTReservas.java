/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.LibroJpaController;
import biblioapp.LibrosJpaController;
import biblioapp.ReservasJpaController;
import biblioapp.SociosJpaController;
import clases.GridReservas;
import clases.Libro;
import clases.Libros;
import clases.Mensaje;
import clases.Reservas;
import clases.Socios;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
@Path("reservas")
public class ServiceRESTReservas {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    /**
     * Creates a new instance of ServiceRESTReservas
     */
    public ServiceRESTReservas() {
    }

    /**
     * Retrieves representation of an instance of apirest.ServiceRESTReservas
     *
     * @return an instance of java.lang.String
     */
    @Context
    private HttpServletRequest httpRequest;

    //GETT ALL DE LAS RESERVAS
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Reservas> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ReservasJpaController dao = new ReservasJpaController(emf);
            lista = dao.findReservasEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
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
    
    @GET
    @Path("grid/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGrid(@QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Reservas> lista = null;
        List<GridReservas> listaGrid = new ArrayList();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ReservasJpaController dao = new ReservasJpaController(emf);
            LibrosJpaController daoLibros = new LibrosJpaController(emf);
            LibroJpaController daoLibro = new LibroJpaController(emf);
            SociosJpaController daoSocio = new SociosJpaController(emf);
            Socios s;
            Libros libros;
            Libro libro;
            //lista prestamos
            lista = dao.findReservasEntities();
            //por cada prestamo, cargo los datos
            for(Reservas reserva : lista)
            {
                s = daoSocio.findSocios(reserva.getSocioId());
                libro = daoLibro.findLibro(reserva.getLibroId());
                libros = daoLibros.findLibros(libro.getIsbn());
                listaGrid.add(new GridReservas(reserva.getIdReserva(), 
                        s.getNombre() + " "+ s.getApellidos(), s.getDni(), 
                        reserva.getLibroId(), libros.getTitulo(), libro.getIsbn(), 
                        libros.getImagen(), reserva.getFechaReserva(), reserva.getFinalizada()));
            }
            if (lista.isEmpty() || lista == null) {
                statusResul = Response.Status.NO_CONTENT;
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
                        .entity(listaGrid)
                        .build();
            }
            return response;
        }
    }

    //get reservas de un socio
    @GET
    @Path("socio/{idSocio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservaSocio(@PathParam("idSocio") int idSocio,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Reservas> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ReservasJpaController dao = new ReservasJpaController(emf);
            lista = dao.buscaReservasPorSocioJPQL(dao, idSocio);
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
            }

        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();
            if (!JWT_Util.validaTrabajador(token, httpRequest)
                    && !JWT_Util.validaSocio(token, httpRequest)) {
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

    //get reservas de un socio
    @GET
    @Path("noFinalizadaSocio/{idSocio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoFinalizada(@PathParam("idSocio") int idSocio,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Reservas> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ReservasJpaController dao = new ReservasJpaController(emf);
            lista = dao.buscaReservasNoFinalizadasPorSocioJPQL(dao, idSocio);
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
            }

        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();
            if (!JWT_Util.validaTrabajador(token, httpRequest)
                    && !JWT_Util.validaSocio(token, httpRequest)) {
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

    //EDITA RESERVA
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putReservas(Reservas reservaAEditar,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController dao = new ReservasJpaController(emf);
                Reservas reservasFound = dao.findReservas(reservaAEditar.getIdReserva());
                if (reservasFound != null) {
                    reservaAEditar.setIdReserva(reservasFound.getIdReserva());
                    dao.edit(reservaAEditar);
                    mensaje.setMensaje("Reserva actualizada");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar reserva");
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

    @PUT
    @Path("quitaNotification/{idReserva}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitaNotificacionReserva(@PathParam("idReserva") int idReserva,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController dao = new ReservasJpaController(emf);
                if (dao.quitaNotificationJPQL(dao, idReserva) == 1) {
                    mensaje.setMensaje("Notificación quitada");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar reserva");
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

    @PUT
    @Path("poneNotification/{idReserva}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response poneNotificacionReserva(@PathParam("idReserva") int idReserva,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController dao = new ReservasJpaController(emf);
                if (dao.poneNotificationJPQL(dao, idReserva) == 1) {
                    mensaje.setMensaje("Notificación puesta");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar reserva");
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
    @Path("add/{idSocio}/{idLibro}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postReservasPath(
            @PathParam("idSocio") int idSocio,
            @PathParam("idLibro") int idLibro,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        Date today = Calendar.getInstance().getTime();
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)
                    && !JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController dao = new ReservasJpaController(emf);

                //libro existente
                LibroJpaController daoLibro = new LibroJpaController(emf);
                Libro l = daoLibro.findLibro(idLibro);
                //socio existente
                SociosJpaController daoSocios = new SociosJpaController(emf);
                Socios s = daoSocios.findSocios(idSocio);

                dao.create(new Reservas(l, today, s));
                daoLibro.reservaLibro(daoLibro, l.getIsbn(), idLibro);

                mensaje.setMensaje("Reserva insertada");

                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje("Error genérico");
            System.out.println(ex.getMessage().toString());
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postReservas(Reservas reservaACrear,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)
                    && !JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController dao = new ReservasJpaController(emf);
                Reservas reservasFound = dao.findReservas(reservaACrear.getIdReserva());
                if (reservasFound == null) {
                    dao.create(reservaACrear);
                    mensaje.setMensaje("Reserva insertada");
                } else {
                    statusResul = Response.Status.FOUND;
                    mensaje.setMensaje("Error, la reserva ya existe");
                }
                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje(ex.getMessage());
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    //FINALIZA RESERVA
    @PUT
    @Path("{idReserva}/{idLibro}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizaReserva(@PathParam("idReserva") int idReserva,
            @PathParam("idLibro") int idLibro,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.BAD_GATEWAY;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest) && !JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                ReservasJpaController daoReservas = new ReservasJpaController(emf);
                Reservas reservaFound = daoReservas.findReservas(idReserva);
                if (reservaFound != null) {
                    reservaFound.setFinalizada(true);
                    reservaFound.setNotificacion(false);
                    daoReservas.edit(reservaFound);

                    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                    LibroJpaController dao = new LibroJpaController(emf);
                    Libro libroFound = dao.findLibro(idLibro);
                    
                    if (libroFound != null) {
                        libroFound.setReservado(false);
                        dao.edit(libroFound);
                    }
                    mensaje.setMensaje("Reserva finalizada");
                    statusResul = Response.Status.OK;
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error, el reserva no existe");
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

    //ELIMINA
    @DELETE
    @Path("{idReserva}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReservas(@PathParam("idReserva") int idReserva,
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
                ReservasJpaController daoReservas = new ReservasJpaController(emf);
                Reservas reservaFound = daoReservas.findReservas(idReserva);
                if (reservaFound != null) {
                    daoReservas.destroy(idReserva);
                    mensaje.setMensaje("Reserva eliminada");
                    statusResul = Response.Status.OK;
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error, la reserva no existe");
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

}
