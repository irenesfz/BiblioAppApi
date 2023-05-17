/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.LibroJpaController;
import biblioapp.LibrosJpaController;
import biblioapp.PrestamosJpaController;
import biblioapp.ReservasJpaController;
import biblioapp.SociosJpaController;
import static biblioapp.SociosJpaController.hash;
import clases.GridSocios;
import clases.Mensaje;
import clases.Prestamos;
import clases.Reservas;
import clases.Socios;
import java.util.ArrayList;
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
@Path("socio")
public class ServiceRESTSocios {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    @Context
    private HttpServletRequest httpRequest;

    /**
     * Creates a new instance of ServiceRESTSocios
     */
    public ServiceRESTSocios() {
    }

//********************************************************************************************
    //GET ALL SOCIOS
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSocios(@QueryParam("apikey") String token) {
        Response response = null;
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Socios> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            lista = dao.findSociosEntities();
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
        }
        return response;
    }

    @GET
    @Path("grid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSociosGrid(@QueryParam("apikey") String token) {
        Response response = null;
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Socios> lista;
        List<GridSocios> listaGrid = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            lista = dao.findSociosEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
            } else {
                ReservasJpaController daoReservas = new ReservasJpaController(emf);
                PrestamosJpaController daoPrestamos = new PrestamosJpaController(emf);
                LibroJpaController daoLibro = new LibroJpaController(emf);
                LibrosJpaController daoLibros = new LibrosJpaController(emf);
                String isbn;
                for (Socios socio : lista) {
                    List<String> listaReservas = new ArrayList<>();
                    List<String> listaPrestamos = new ArrayList<>();
                    isbn = "";
                    daoReservas.buscaReservasNoFinalizadasPorSocioJPQL(daoReservas, socio.getIdSocio());
                    daoPrestamos.buscaPrestamosNoFinalizadosPorSocioJPQL(daoPrestamos, socio.getIdSocio());

                    for (Reservas reserva : daoReservas.buscaReservasNoFinalizadasPorSocioJPQL(daoReservas, socio.getIdSocio())) {
                        isbn = daoLibro.findLibro(reserva.getLibroId()).getIsbn();
                        listaReservas.add(daoLibros.findLibros(isbn).getTitulo());
                    }

                    for (Prestamos prestamo : daoPrestamos.buscaPrestamosNoFinalizadosPorSocioJPQL(daoPrestamos, socio.getIdSocio())) {
                        isbn = daoLibro.findLibro(prestamo.getIdLibro()).getIsbn();
                        listaPrestamos.add(daoLibros.findLibros(isbn).getTitulo());
                    }

                    listaGrid.add(new GridSocios(socio,
                            listaPrestamos,
                            listaReservas));

                }
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
        }
        return response;
    }

    //GET SOCIO POR ID
    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSocioId(@PathParam("id") int id,
            @QueryParam("apikey") String token
    ) {
        EntityManagerFactory emf = null;
        Response response;
        Response.Status statusResul = Response.Status.OK;
        Socios obj = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            obj = dao.findSocios(id);
            if (obj == null) {
                statusResul = Response.Status.NOT_FOUND;
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                response = JWT_Util.getData(token, httpRequest);
            } else {
                response = Response
                        .status(statusResul)
                        .entity(obj)
                        .build();
            }
            emf.close();
        }
        return response;
    }

    //GET SOCIO POR DNI
    @GET
    @Path("dni/{dni}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSocioDni(@PathParam("dni") String dni,
            @QueryParam("apikey") String token
    ) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        Response response;
        Response.Status statusResul = Response.Status.OK;
        Socios obj = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);

            em = dao.getEntityManager();
            TypedQuery<Socios> consultaSocios
                    = em.createNamedQuery("Socios.findByDni", Socios.class);
            consultaSocios.setParameter("dni", dni);
            obj = consultaSocios.getSingleResult();
            if (obj == null) {
                statusResul = Response.Status.NOT_FOUND;
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            if (!JWT_Util.validaSocio(token, httpRequest) && !JWT_Util.validaTrabajador(token, httpRequest)) {
                response = JWT_Util.getData(token, httpRequest);
            } else {
                response = Response
                        .status(statusResul)
                        .entity(obj)
                        .build();
            }
            emf.close();
        }
        return response;
    }

    //AÑADE SOCIOS
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSocio(Socios socio,
            @QueryParam("apikey") String token
    ) {
        EntityManagerFactory emf = null;
        Response response = null;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        Response.Status statusResul = Response.Status.CREATED;
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                SociosJpaController dao = new SociosJpaController(emf);
                Socios socioFound = dao.findSocios(socio.getIdSocio());
                socio.setContrasenya(hash(socio.getDni()));
                if (socioFound == null) {
                    dao.create(socio);
                    mensaje.setMensaje("Socio creado");
                } else {
                    statusResul = Response.Status.FOUND;
                    mensaje.setMensaje("Error socio encontrado");
                }
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje(ex.getMessage().toString());
            System.out.println("\n\n\n\n\n\n\n" + ex.getMessage() + "\n\n\n\n\n\n\n");
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();

            emf.close();
        }
        return response;
    }

    //EDITA SOCIOS
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putSocio(Socios socioAEditar,
            @QueryParam("apikey") String token
    ) {
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
                SociosJpaController dao = new SociosJpaController(emf);
                Socios socioFound = dao.findSocios(socioAEditar.getIdSocio());
                if (socioFound != null) {
                    dao.edit(socioAEditar);
                    mensaje.setMensaje("Socio actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar socio");
                }
                emf.close();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            System.out.println("\n\n\n\n\n\n\n" + ex.getMessage() + "\n\n\n\n\n\n\n");
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    //RESETEA LA CONTRASEÑA (PONE EL DNI POR DEFECTO)
    @PUT
    @Path("resetPassword/{idSocio}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reseteaPassword(@PathParam("idSocio") int idSocio,
            @QueryParam("apikey") String token
    ) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.NOT_FOUND;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            Socios socioFound = dao.findSocios(idSocio);
            if (socioFound != null) {
                if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                    mensaje.setMensaje("Debes identificarte");
                    statusResul = Response.Status.UNAUTHORIZED;
                } else {
                    if (dao.resetContrasenyaJPQL(dao, socioFound.getDni()) == 1) {
                        mensaje.setMensaje("Contraseña actualizada");
                        statusResul = Response.Status.OK;
                    } else {
                        mensaje.setMensaje("Error al actualizar");
                    }
                }
            } else {
                mensaje.setMensaje("Error al encontrar socio");
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

    //CAMBIA CONTRASEÑA MÉTODO PARA LOS USUARIOS PARA CAMBIAR CONTRASEÑA DESDE LA APP MÓVIL
    @PUT
    @Path("cambiaPassword/{dni}/{newPass}/{oldPass}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putContrasenya(@PathParam("dni") String dni,
            @PathParam("newPass") String newPass,
            @PathParam("oldPass") String oldPass,
            @QueryParam("apikey") String token
    ) {

        EntityManagerFactory emf = null;
        Response response;
        EntityManager em = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
            } else {
                if (dao.updateContrasenyaJPQL(dao, dni, newPass, oldPass) == 1) {
                    mensaje.setMensaje("Contraseña actualizada");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al actualizar");
                }
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();

            emf.close();
        }
        return response;
    }

    @PUT
    @Path("cambiaCorreo/{id}/{correo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putEmail(@PathParam("id") int id,
            @PathParam("correo") String correo,
            @QueryParam("apikey") String token
    ) {

        EntityManagerFactory emf = null;
        Response response;
        EntityManager em = null;
        Response.Status statusResul = Response.Status.OK;

        Mensaje mensaje = new Mensaje();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                if (dao.updateSocioCorreoJPQL(dao, correo, id) == 1) {
                    mensaje.setMensaje("Socio actualizado");
                } else {
                    mensaje.setMensaje("Error al actualizar");
                    statusResul = Response.Status.NOT_FOUND;
                }
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();

            emf.close();
        }
        return response;
    }

    @PUT
    @Path("cambiaCategorias/{id}/{categorias}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putCategorias(@PathParam("id") int id,
            @PathParam("categorias") String correo,
            @QueryParam("apikey") String token
    ) {

        EntityManagerFactory emf = null;
        Response response;
        EntityManager em = null;
        Response.Status statusResul = Response.Status.OK;

        Mensaje mensaje = new Mensaje();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                if (dao.updateSocioCategoriasJPQL(dao, correo, id) == 1) {
                    mensaje.setMensaje("Socio actualizado");
                } else {
                    mensaje.setMensaje("Error al actualizar");
                    statusResul = Response.Status.NOT_FOUND;
                }
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();

            emf.close();
        }
        return response;
    }

    @PUT
    @Path("cambiaImagenPerfil/{id}/{imagen}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putImagen(@PathParam("id") int id,
            @PathParam("imagen") String imagen,
            @QueryParam("apikey") String token
    ) {
        EntityManagerFactory emf = null;
        Response response;
        EntityManager em = null;
        Response.Status statusResul = Response.Status.OK;

        Mensaje mensaje = new Mensaje();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            SociosJpaController dao = new SociosJpaController(emf);
            if (!JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                if (dao.updateSocioImagenJPQL(dao, imagen, id) == 1) {
                    mensaje.setMensaje("Socio actualizado");
                } else {
                    mensaje.setMensaje("Error al actualizar");
                    statusResul = Response.Status.NOT_FOUND;
                }
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();

            emf.close();
        }
        return response;
    }

    //ELIMINA SOCIO
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSocio(@PathParam("id") int id,
            @QueryParam("apikey") String token
    ) {
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
                SociosJpaController dao = new SociosJpaController(emf);
                Socios socioFound = dao.findSocios(id);
                if (socioFound != null) {
                    if (dao.deleteSocioJPQL(dao, id) != 0) {
                        mensaje.setMensaje("Socio eliminado");
                    } else {
                        mensaje.setMensaje("Error al eliminar el socio");
                    }
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error, el socio no existe");
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
