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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
@Path("libros")
public class ServiceRESTLibros {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";

    /**
     * Creates a new instance of ServiceRESTLibros
     */
    public ServiceRESTLibros() {
    }
    @Context
    private HttpServletRequest httpRequest;

    /**
     * Creates a new instance of ServiceRESTSocios
     */
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

    //GETT ALL DE LOS LIBROS
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            lista = dao.findLibrosEntities();
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

    //get libro por id
    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllId(@PathParam("id") int id) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Libros libros = new Libros();
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            LibroJpaController daoLibro = new LibroJpaController(emf);
            Libro libro = daoLibro.findLibro(id);
            if (libro == null) {
                statusResul = Response.Status.NO_CONTENT;
            } else {
                libros = dao.findLibros(libro.getIsbn());
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();

            Response response = Response
                    .status(statusResul)
                    .entity(libros)
                    .build();
            return response;
        }
    }
    
    //GET LIBRO POR ISBN
    @GET
    @Path("isbn/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroIsbn(@PathParam("isbn") String isbn) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        Libros obj = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            obj = dao.findLibros(isbn);
            if (obj == null) {
                statusResul = Response.Status.NOT_FOUND;
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
        } finally {
            emf.close();
            Response response = Response
                    .status(statusResul)
                    .entity(obj)
                    .build();
            return response;
        }
    }

    //BUSCA LIBRO SEGÚN CUALQUIER COINCIDENCIA
    @GET
    @Path("todo/{palabra}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroTodo(@PathParam("palabra") String palabra) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            TypedQuery<Libros> consultaLibros
                    = em.createNamedQuery("Libros.findByLikeAll", Libros.class);
            consultaLibros.setParameter("palabra", "%" + palabra + "%");
            lista = consultaLibros.getResultList();

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

    //GET LIBRO POR TITULO
    @GET
    @Path("titulo/{titulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroTitulo(@PathParam("titulo") String titulo) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            TypedQuery<Libros> consultaLibros
                    = em.createNamedQuery("Libros.findByLikeTitulo", Libros.class);
            consultaLibros.setParameter("titulo", "%" + titulo + "%");
            lista = consultaLibros.getResultList();

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

    //GET LIBRO POR AUTORES
    @GET
    @Path("autores/{autores}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroAutores(@PathParam("autores") String autores) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            TypedQuery<Libros> consultaAutores
                    = em.createNamedQuery("Libros.findByLikeAutores", Libros.class);
            consultaAutores.setParameter("autores", "%" + autores + "%");
            lista = consultaAutores.getResultList();

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

    //GET LIBRO POR EDITORIAL
    @GET
    @Path("editorial/{editorial}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroEditorial(@PathParam("editorial") String editorial) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            TypedQuery<Libros> consultaEditorial
                    = em.createNamedQuery("Libros.findByLikeEditorial", Libros.class);
            consultaEditorial.setParameter("editorial", "%" + editorial + "%");
            lista = consultaEditorial.getResultList();

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

    //GET LIBRO POR AÑO PUBLICACION
    @GET
    @Path("anyopublicacion/{anyo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroAnyoPublicacion(@PathParam("anyo") int anyo) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            TypedQuery<Libros> consultaAnyoPublicacion
                    = em.createNamedQuery("Libros.findByAnyoPublicacion", Libros.class);
            consultaAnyoPublicacion.setParameter("anyoPublicacion", anyo);
            lista = consultaAnyoPublicacion.getResultList();

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

    //GET LIBRO POR CATEGORIAS/SUBCATEGORIAS
    @GET
    @Path("categoria/{categoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibroCategorias(@PathParam("categoria") String categorias) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            em = dao.getEntityManager();
            //si es una palabra
            System.out.println("\n\n\n\n\n\n\n\n" + categorias + "\n\n\n\n\n\n\n\n");
            if (!categorias.contains("-")) {
                TypedQuery<Libros> consultaCategorias
                        = em.createNamedQuery("Libros.findByLikeCategoria", Libros.class);
                consultaCategorias.setParameter("categoria", "%" + categorias + "%");
                lista = consultaCategorias.getResultList();
                consultaCategorias
                        = em.createNamedQuery("Libros.findByLikeSubcategoria", Libros.class);
                consultaCategorias.setParameter("subcategorias", "%" + categorias + "%");
                lista.addAll(consultaCategorias.getResultList());
            } else {
                //si son varias
                String[] categoria = categorias.split("-");
                //para cada palabra
                lista = new ArrayList<>();
                for (String cat : categoria) {
                    TypedQuery<Libros> consultaCategorias
                            = em.createNamedQuery("Libros.findByLikeCategoria", Libros.class);
                    consultaCategorias.setParameter("categoria", "%" + cat + "%");
                    lista.addAll(consultaCategorias.getResultList());
                    consultaCategorias
                            = em.createNamedQuery("Libros.findByLikeSubcategoria", Libros.class);
                    consultaCategorias.setParameter("subcategorias", "%" + cat + "%");
                    lista.addAll(consultaCategorias.getResultList());
                    System.out.println("\n\n\n\n\n\n\n\n" + cat + "\n\n\n\n\n\n\n\n");
                }
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
    @Path("disponibles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibrosDisponibles() {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            lista = dao.buscaLibrosDisponibles(dao);
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
    @Path("disponibles/{palabra}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibrosDisponiblesPalabra(@PathParam("palabra") String palabra) {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            lista = dao.buscaLibrosDisponibles(dao);
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
    @Path("disponiblesReservar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibrosDisponiblesReservar() {
        EntityManagerFactory emf = null;
        Response.Status statusResul = Response.Status.OK;
        List<Libros> lista = null;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LibrosJpaController dao = new LibrosJpaController(emf);
            lista = dao.buscaLibrosParaReservar(dao);
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

    //EDITA LIBRO
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putLibro(Libros libroAEditar,
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
                LibrosJpaController dao = new LibrosJpaController(emf);
                Libros libroFound = dao.findLibros(libroAEditar.getIsbn());
                if (libroFound != null) {
                    //revisar en c#
                    //dao.edit(libroAEditar);
                    //mensaje.setMensaje("Libro actualizado");
                    int num = dao.updateLibroJPQL(dao, libroFound.getIsbn(), libroAEditar);
                    if (num > 0) {
                        mensaje.setMensaje("Libro actualizado");
                        statusResul = Response.Status.OK;
                    } else {
                        mensaje.setMensaje("Error al actualizar libro");
                    }

                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error al encontrar libro");
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

    //INSERTA LIBRO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLibro(Libros libroACrear,
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
                LibrosJpaController dao = new LibrosJpaController(emf);
                Libros libroFound = dao.findLibros(libroACrear.getIsbn());
                if (libroFound == null) {
                    libroACrear.setLibroCollection((Collection<Libro>)Collections.EMPTY_LIST);
                    dao.create(libroACrear);
                    statusResul = Response.Status.CREATED;
                    mensaje.setMensaje("Libro insertado");
                } else {
                    statusResul = Response.Status.FOUND;
                    mensaje.setMensaje("Error, el libro ya existe");
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

    //ELIMINA LIBRO
    @DELETE
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLibro(@PathParam("isbn") String isbn,
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
                LibrosJpaController daoLibros = new LibrosJpaController(emf);
                Libros libroFound = daoLibros.findLibros(isbn);
                if (libroFound != null) {
                    daoLibros.destroy(isbn);
                    statusResul = Response.Status.OK;
                    mensaje.setMensaje("Libro eliminado");
                } else {
                    statusResul = Response.Status.NOT_FOUND;
                    mensaje.setMensaje("Error, el libro no existe");
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

}
