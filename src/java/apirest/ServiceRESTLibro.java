/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import biblioapp.LibroJpaController;
import biblioapp.LibrosJpaController;
import clases.Libro;
import clases.Libros;
import clases.Mensaje;
import java.util.ArrayList;
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
@Path("libro")
public class ServiceRESTLibro {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";
    @Context
    private HttpServletRequest httpRequest;

    /**
     * Creates a new instance of ServiceRESTLibro
     */
    public ServiceRESTLibro() {
    }

    //GET ALL
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libro> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            lista = dao.findLibroEntities();
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

    @GET
    @Path("{isbnLibro}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPorId(@PathParam("isbnLibro") String isbnLibro) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libro> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);

            lista = dao.getLibroPorISBNNamedQuery(dao, isbnLibro);
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList<>();
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

    @GET
    @Path("ndisponibles/{isbnLibro}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDisponiblesPrestamoPorId(@PathParam("isbnLibro") String isbnLibro) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libro> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);

            lista = dao.getLibrosDisponiblesPrestamoPorISBNNamedQuery(dao, isbnLibro);
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList<>();
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

    //GET UNIDADES LIBROS
    @GET
    @Path("ud/isbn/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUdLibroIsbn(@PathParam("isbn") String isbn) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Libros obj = null;
        String json = "{}";
        List<Libro> listaTotal = null;
        List<Libro> listaReserva = null;
        List<Libro> listaDisponiblePrestamo = null;
        int unidadesTotales, unidadesReservadas, unidadesDisponiblesPrestamo;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            
            listaTotal = dao.getLibroPorISBNNamedQuery(dao, isbn);
            listaReserva = dao.getLibrosReservadosPorISBNNamedQuery(dao, isbn);
            listaDisponiblePrestamo = dao.getLibrosDisponiblesPrestamoPorISBNNamedQuery(dao, isbn);

            unidadesTotales = listaTotal == null ? 0 : listaTotal.size();
            unidadesReservadas = listaTotal == null ? 0 : listaReserva.size();
            unidadesDisponiblesPrestamo = listaTotal == null ? 0 : listaDisponiblePrestamo.size();
            json = "{\n\tunidadesTotales: " + unidadesTotales + ",\n\tunidadesReservadas: " + unidadesReservadas + ",\n\tunidadesDisponiblesPrestamo: " + unidadesDisponiblesPrestamo + "\n}";

        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            json = ex.getMessage();
            System.out.println("\n\n\n\n\n" + ex.getMessage() + "\n\n\n\n\n");
        } finally {
            emf.close();
            Response response = Response
                    .status(statusResul)
                    .entity(json)
                    .build();
            return response;
        }
    }

    @GET
    @Path("disponibleBorrar/{isbnLibro}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroDisponibleBorrar(@PathParam("isbnLibro") String isbnLibro) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.NOT_FOUND;
        Libro libro = null;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("No disponible");
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            libro = dao.getLibroDisponibleParaEliminarISBNNamedQuery(dao, isbnLibro);
            if (libro != null) {
                statusResul = Response.Status.FOUND;
                mensaje.setMensaje("Disponible");
            }

        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.setMensaje(ex.getMessage());
        } finally {
            emf.close();

            Response response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
            return response;
        }
    }

    @GET
    @Path("nreservas/{isbnLibro}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservadosPrestamoPorId(@PathParam("isbnLibro") String isbnLibro) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libro> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            lista = dao.getLibrosReservadosPorISBNNamedQuery(dao, isbnLibro);
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                lista = new ArrayList<>();
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

    @GET
    @Path("reserva/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroReservaPorISBN(@PathParam("isbn") String isbn) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Libro libro = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            libro = dao.getLibroDisponibleReservaPorISBNNamedQuery(dao, isbn);
            if (libro == null) {
                statusResul = Response.Status.NO_CONTENT;
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();

            Response response = Response
                    .status(statusResul)
                    .entity(libro)
                    .build();
            return response;
        }
    }

    @GET
    @Path("prestamo/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroPrestamoPorISBN(@PathParam("isbn") String isbn) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Libro lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibroJpaController dao = new LibroJpaController(emf);
            lista = dao.getLibroDisponiblePrestamoPorISBNNamedQuery(dao, isbn);
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

    //marca libro como disponible
    @PUT
    @Path("disponible/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response marcaDisponible(@PathParam("id") int id,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(id);
                if (libroFound != null) {
                    libroFound.setDisponible(true);
                    dao.edit(libroFound);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    @PUT
    @Path("disponibleReserva/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response marcaReservaDisponible(@PathParam("id") int id,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)&& !JWT_Util.validaSocio(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(id);
                if (libroFound != null) {
                    libroFound.setReservado(false);
                    dao.edit(libroFound);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    @PUT
    @Path("noDisponibleReserva/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response marcaReservaNoDisponible(@PathParam("id") int id,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(id);
                if (libroFound != null) {
                    libroFound.setReservado(true);
                    dao.edit(libroFound);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    //marca libro como no disponible
    @PUT
    @Path("noDisponible/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response marcaNoDisponible(@PathParam("id") int id,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(id);
                if (libroFound != null) {
                    libroFound.setDisponible(false);
                    dao.edit(libroFound);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    //finaliza reserva
    @PUT
    @Path("finaliza/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizaReserva(@PathParam("id") int id,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(id);
                if (libroFound != null) {
                    libroFound.setReservado(false);
                    dao.edit(libroFound);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    //INSERTA UNIDAD DE LIBRO
    @POST
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLibro(@PathParam("isbn") String isbn,
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
                LibrosJpaController daoLibros = new LibrosJpaController(emf);
                Libros libroFound = daoLibros.findLibros(isbn);

                LibroJpaController daoLibro = new LibroJpaController(emf);
                Libro libroAInsertar = new Libro(libroFound);
                daoLibro.create(libroAInsertar);

                statusResul = Response.Status.CREATED;
                mensaje.setMensaje("Unidad de libro insertada");
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            if (emf != null) {
                emf.close();
            }
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        }
        return response;
    }

    //EDITA UNIDAD DE LIBRO
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putLibro(Libro libroAEditar,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(libroAEditar.getIdLibro());
                if (libroFound != null) {
                    dao.edit(libroAEditar);
                    mensaje.setMensaje("Libro actualizado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    @DELETE
    @Path("unidad/isbn/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminaUnidadLibroPorISBN(@PathParam("isbn") String isbn,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        int numero = 0;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                LibroJpaController dao = new LibroJpaController(emf);
                Libro l = dao.getLibroDisponibleParaEliminarISBNNamedQuery(dao, isbn);
                if (l != null) {
                    numero = dao.eliminaUnidadLibros(dao, l.getIdLibro());
                    mensaje.setMensaje(String.valueOf(numero));
                    if (numero == 0) {
                        statusResul = Response.Status.NO_CONTENT;
                        mensaje.setMensaje("No se ha eliminado");
                    } else {
                        mensaje.setMensaje("Unidad eliminada");
                    }
                } else {
                    mensaje.setMensaje("No se ha encontrado el libro");
                }
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

    //DELETE
    @DELETE
    @Path("isbn/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminaLibroPorISBN(@PathParam("isbn") String isbn,
            @QueryParam("apikey") String token) {
        EntityManagerFactory emf = null;
        Response response = null;
        int numero = 0;
        Response.Status statusResul = Response.Status.OK;
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje("Error genérico");
        try {
            if (!JWT_Util.validaTrabajador(token, httpRequest)) {
                mensaje.setMensaje("Debes identificarte");
                statusResul = Response.Status.UNAUTHORIZED;
            } else {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                LibroJpaController dao = new LibroJpaController(emf);
                numero = dao.eliminaUnidadesLibros(dao, isbn);
                mensaje.setMensaje(String.valueOf(numero));
                if (numero == 0) {
                    statusResul = Response.Status.NO_CONTENT;
                    mensaje.setMensaje("Unidades no eliminadas");
                } else {
                    mensaje.setMensaje("Unidades eliminadas");
                }
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

    //DELETE
    @DELETE
    @Path("{idLibro}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminaLibro(@PathParam("idLibro") int idLibro,
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
                LibroJpaController dao = new LibroJpaController(emf);
                Libro libroFound = dao.findLibro(idLibro);
                if (libroFound != null) {
                    dao.destroy(idLibro);
                    mensaje.setMensaje("Libro eliminado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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
