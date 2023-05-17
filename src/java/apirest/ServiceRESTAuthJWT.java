package apirest;

import biblioapp.SociosJpaController;
import clases.Mensaje;
import clases.Socios;
import clases.Usuario;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.JWT_Util;

/**
 * REST Web Service
 *
 * @author irene
 */
@Path("jwt")
public class ServiceRESTAuthJWT {

    @Context
    private HttpServletRequest httpRequest;

    private static final String SECRET_KEY = "my_super_secret_key";
    private static final String APPNAME = "BIBLIOAPP";
    private static final String ISSUER = "biblioapp.com";
    private static final int EXPIRATION_MINUTES = 300;
    private static final String PERSISTENCE_UNIT = "ApiRestBiblioAppPU";
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AuthJWT
     */
    public ServiceRESTAuthJWT() {
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(@QueryParam("apikey") String token) {
        Response response;
        Response.Status statusResul;

        boolean validado = false;
        String usuario = "";
        if (token != null) {
            JsonObject jsonJWT = JWT_Util.validateJWT(token, httpRequest);
            if (jsonJWT.containsKey("validate")) {
                if (jsonJWT.getBoolean("validate")) {
                    validado = true;
                    usuario = jsonJWT.getString("usuario");
                } else {
                    System.out.println(jsonJWT.getString("id_sesion_recibida"));
                    System.out.println(jsonJWT.getString("id_sesion_actual"));
                }
            }
        }

        if (!validado) {
            statusResul = Response.Status.FORBIDDEN;

            JsonObjectBuilder jsonOB = Json.createObjectBuilder();
            jsonOB.add("mensaje", "Debes identificarte");
            JsonObject json = jsonOB.build();

            response = Response
                    .status(statusResul)
                    .entity(json)
                    .build();

        } else {
            statusResul = Response.Status.OK;

            JsonObjectBuilder jsonOB = Json.createObjectBuilder();
            jsonOB.add("usuario", usuario);
            jsonOB.add("mensaje", "Usuario autorizado");
            JsonObject json = jsonOB.build();

            response = Response
                    .status(statusResul)
                    .entity(json)
                    .build();
        }

        return response;
    }

    @POST
    @Path("auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validar(Usuario usuario) {
        EntityManagerFactory emf = null;
        Response.Status status;
        Mensaje mensaje = new Mensaje();
        JsonObject json = null;
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        SociosJpaController dao = new SociosJpaController(emf);
        List<Socios> socio = dao.compruebaSocio(dao, usuario);
        if (socio.size() == 1) {
            long tiempo = System.currentTimeMillis();
            Header header = Jwts.header();
            header.setType("JWT");
            String jwt = Jwts.builder()
                    .setHeader((Map<String, Object>) header)
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .setId(httpRequest.getSession().getId())
                    .setSubject(APPNAME)
                    .setIssuer(ISSUER)
                    .setIssuedAt(new Date(tiempo))
                    .setExpiration(new Date(tiempo + (EXPIRATION_MINUTES * 60 * 1000)))
                    .claim("usuario", socio.get(0).getDni())
                    .claim("rol", socio.get(0).getRol())
                    .compact();

            // time en milisegundos = 15 minutos * 60 segundos * 1000 milisegundos
            json = Json.createObjectBuilder()
                    .add("JWT", jwt).build();
            status = Response.Status.CREATED;
        } else {
            status = Response.Status.UNAUTHORIZED;
            json = Json.createObjectBuilder()
                    .add("JWT", "ERROR").build();
        }
        return Response.status(status).entity(json).build();
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLogout() {
        Response response;
        Response.Status statusResul;
        JsonObject json;

        if (httpRequest.getSession().getAttribute("usuario") != null) {
            httpRequest.getSession().removeAttribute("usuario");
            statusResul = Response.Status.ACCEPTED;

            JsonObjectBuilder jsonOB = Json.createObjectBuilder();
            jsonOB.add("id_session", httpRequest.getSession().getId());
            jsonOB.add("usuario", "");
            jsonOB.add("rol", "");
            jsonOB.add("mensaje", "Logout");
            json = jsonOB.build();

            response = Response
                    .status(statusResul)
                    .entity(json)
                    .build();

        } else {
            statusResul = Response.Status.NOT_MODIFIED;

            response = Response
                    .status(statusResul)
                    .build();
        }
        return response;
    }

    @POST
    @Path("destroysession")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDestroy() {
        Response response;
        Response.Status statusResul;
        JsonObject json;

        httpRequest.getSession().invalidate();

        statusResul = Response.Status.ACCEPTED;

        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        jsonOB.add("id_session", httpRequest.getSession().getId());
        jsonOB.add("usuario", "");
        jsonOB.add("rol", "");
        jsonOB.add("mensaje", "Logout");
        json = jsonOB.build();

        response = Response
                .status(statusResul)
                .entity(json)
                .build();

        return response;
    }

    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getText(@QueryParam("apikey") String token) {

        Response.Status status = Response.Status.NOT_ACCEPTABLE;;

        boolean validado = false;
        if (token != null) {
            JsonObject jsonJWT = JWT_Util.validateJWT(token, httpRequest);
            if (jsonJWT.containsKey("validate")) {
                if (jsonJWT.getBoolean("validate")) {
                    status = Response.Status.ACCEPTED;
                }
            }
            return Response.status(status).entity(jsonJWT).build();
        } else {
            return Response.status(status).build();
        }

    }

}
