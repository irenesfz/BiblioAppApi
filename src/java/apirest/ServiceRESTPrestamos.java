/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.LibroJpaController;
import biblioapp.LibrosJpaController;
import biblioapp.PrestamosJpaController;
import biblioapp.SociosJpaController;
import clases.GridPrestamos;
import clases.Libro;
import clases.Libros;
import clases.Mensaje;
import clases.Prestamos;
import clases.Socios;
import java.util.ArrayList;
import java.util.Date;
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
@Path("prestamos")
public class ServiceRESTPrestamos {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    /**
     * Creates a new instance of ServiceRESTPrestamos
     */
    public ServiceRESTPrestamos() {
    }
    @Context
    private HttpServletRequest httpRequest;

    //GETT ALL préstamos
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Prestamos> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            lista = dao.findPrestamosEntities();
            if (lista.isEmpty()) {
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
        List<Prestamos> lista = null;
        List<GridPrestamos> listaGrid = new ArrayList();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            LibrosJpaController daoLibros = new LibrosJpaController(emf);
            LibroJpaController daoLibro = new LibroJpaController(emf);
            SociosJpaController daoSocio = new SociosJpaController(emf);
            Socios s;
            Libros libros;
            Libro libro;
            //lista prestamos
            lista = dao.findPrestamosEntities();
            //por cada prestamo, cargo los datos
            for(Prestamos prestamo : lista)
            {
                s = daoSocio.findSocios(prestamo.getIdSocio());
                libro = daoLibro.findLibro(prestamo.getIdLibro());
                libros = daoLibros.findLibros(libro.getIsbn());
                listaGrid.add(new GridPrestamos(prestamo.getIdPrestamo(), 
                        s.getNombre() + " "+ s.getApellidos(), s.getDni(), 
                        prestamo.getIdLibro(), libros.getTitulo(), libro.getIsbn(), 
                        libros.getImagen(),
                        prestamo.getFechaPrestamo(), prestamo.getFechaTope(), 
                        prestamo.getFechaDevolucion()));
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
    
    //BUSCA PRESTAMOS NO FINALIZADOS
    @GET
    @Path("noFinalizados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoFinalizados(@QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Prestamos> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            lista = dao.prestamoNoFinalizadoJPQL(dao);
            if (lista.isEmpty()) {
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

    //BUSCA PRESTAMOS POR IDSOCIO
    @GET
    @Path("socio/{idSocio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrestamosSocio(@PathParam("idSocio") String idSocio,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Prestamos> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            lista = dao.buscaPrestamoPorSocioJPQL(dao, Integer.parseInt(idSocio));
            if (lista.isEmpty()) {
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

    //BUSCA PRESTAMOS POR IDSOCIO
    @GET
    @Path("noFinalizadoSocio/{idSocio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrestamosNoFinalizadoSocio(@PathParam("idSocio") int idSocio,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        // Response response = null;
        Response.Status statusResul = Response.Status.OK;
        List<Prestamos> lista = new ArrayList();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            lista = dao.buscaPrestamosNoFinalizadosPorSocioJPQL(dao, idSocio);
            if (lista.isEmpty() || lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList();
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

    //INSERTA Prestamo
    @POST
    @Path("{idLibro}/{idSocio}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPrestamo(Prestamos prestamo, @PathParam("idSocio") int idSocio, 
            @PathParam("idLibro") int idLibro, @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Mensaje mensaje = new Mensaje();
        EntityManager em = null;
        Response.Status statusResul = Response.Status.CREATED;
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                statusResul = Response.Status.UNAUTHORIZED;
                mensaje.setMensaje("Debes identificarte");
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                PrestamosJpaController dao = new PrestamosJpaController(emf);
                Prestamos prestamoFound = dao.findPrestamos(prestamo.getIdPrestamo());
                if (prestamoFound == null) {
                    //coge una unidad de libro disponible
                    SociosJpaController daoSocio = new SociosJpaController(emf);
                    Socios s = daoSocio.findSocios(idSocio);

                    LibroJpaController daoLibro = new LibroJpaController(emf);
                    Libro l = daoLibro.findLibro(idLibro);
                    dao.create(new Prestamos(prestamo.getIdPrestamo(), prestamo.getFechaPrestamo(), prestamo.getFechaTope(), l, s));
                    mensaje.setMensaje("Préstamo insertado");
                } else {
                    statusResul = Response.Status.FOUND;
                    mensaje.setMensaje("Error, el préstamo ya existe");
                }
            }
        } catch (Exception ex) {
            mensaje.setMensaje(ex.getMessage());
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                response = JWT_Util.getData(token, httpRequest);
            } else {
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
            emf.close();
        }
        return response;
    }

    //FINALIZA PRESTAMO
    @PUT
    @Path("{idPrestamo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizaPrestamo(@PathParam("idPrestamo") int idPrestamo,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                statusResul = Response.Status.UNAUTHORIZED;
                mensaje.setMensaje("Debes identificarte");
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                PrestamosJpaController dao = new PrestamosJpaController(emf);
                Prestamos prestamoFound = dao.findPrestamos(idPrestamo);
                if (prestamoFound != null) {
                    prestamoFound.setFechaDevolucion(new Date());
                    dao.edit(prestamoFound);
                    mensaje.setMensaje("Préstamo finalizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar préstamo");
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

    //ELIMINA Préstamo
    @DELETE
    @Path("{idPrestamo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrestamo(@PathParam("idPrestamo") int idPrestamo,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.BAD_REQUEST;
        Mensaje mensaje = new Mensaje();
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                statusResul = Response.Status.UNAUTHORIZED;
                mensaje.setMensaje("Debes identificarte");
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                PrestamosJpaController dao = new PrestamosJpaController(emf);
                Prestamos prestamoFound = dao.findPrestamos(idPrestamo);
                if (prestamoFound != null) {
                    if (dao.eliminaPrestamo(dao, idPrestamo) == 1) {
                        mensaje.setMensaje("Préstamo eliminado");
                        statusResul = Response.Status.OK;
                    } else {
                        mensaje.setMensaje("Error al eliminar el préstamo");
                    }
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error, el préstamo no existe");
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
    @Path("fecha/{fecha}/{idSocio}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPrestamoDestacado(@PathParam("fecha") String fecha,
            @PathParam("idSocio") int idSocio,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Response response = null;
        List<Prestamos> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            PrestamosJpaController dao = new PrestamosJpaController(emf);
            EntityManager em = dao.getEntityManager();
            TypedQuery<Prestamos> consultaPorFecha
                    = em.createNamedQuery("Prestamos.findByFechaLikeTope", Prestamos.class)
                            .setParameter("fechaTope", fecha + "%")
                            .setParameter("idSocio", idSocio);
            lista = consultaPorFecha.getResultList();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList<>();
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
        }
        return response;
    }
}
