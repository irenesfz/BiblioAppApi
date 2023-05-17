/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author irene
 */
public class JWT_Util {
    
    private static final String SECRET_KEY = "my_super_secret_key";

    public static JsonObject validateJWT(String token, HttpServletRequest request) {
        JsonObject json = null;
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        try {
            String[] chunks = token.split("\\.");
            if (chunks.length == 3) {
                Base64.Decoder decoder = Base64.getDecoder();
                String header = new String(decoder.decode(chunks[0]));
                String payload = new String(decoder.decode(chunks[1]));
                String signature = chunks[2];

                JSONParser parser = new JSONParser();
                JSONObject jsonHeader = (JSONObject) parser.parse(header);
                String algoritmo = "";
                if (jsonHeader.containsKey("alg")) {
                    algoritmo = jsonHeader.get("alg").toString();
                }
                String tipo = "";
                if (jsonHeader.containsKey("typ")) {
                    tipo = jsonHeader.get("typ").toString();
                }

                if ((algoritmo.equals("HS256")) && (tipo.equals("JWT"))) {
                    try {
                        Claims claims = Jwts.parser()
                                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                                .parseClaimsJws(token)
                                .getBody();

                        // https://es.wikipedia.org/wiki/JSON_Web_Token
                        jsonOB.add("Subject", claims.getSubject());
                        jsonOB.add("Issuer", claims.getIssuer());
                        jsonOB.add("IssuedAt", claims.getIssuedAt().toString());
                        jsonOB.add("Expiration", claims.getExpiration().toString());
                        jsonOB.add("usuario", claims.get("usuario", String.class));
                        jsonOB.add("rol", claims.get("rol", String.class));
                        jsonOB.add("id_sesion_recibida", claims.getId());
                        jsonOB.add("id_sesion_actual", request.getSession().getId());
                        jsonOB.add("validate_session", request.getSession().getId().equals(claims.getId()));
                        long tiempo = System.currentTimeMillis();
                        jsonOB.add("validate_expiration", tiempo < claims.getExpiration().getTime());
                        jsonOB.add("validate", (request.getSession().getId().equals(claims.getId())) && (tiempo < claims.getExpiration().getTime()));
                        jsonOB.add("resul", "decrypted");
                        json = jsonOB.build();

                    } catch (io.jsonwebtoken.SignatureException ex) {
                        jsonOB.add("error", "apikey no válida");
                        json = jsonOB.build();
                    }
                } else {
                    jsonOB.add("error", "header no válido");
                    jsonOB.add("alg", algoritmo);
                    jsonOB.add("typ", tipo);
                    jsonOB.add("header", header);
                    json = jsonOB.build();
                }
            } else {
                jsonOB.add("error", "apikey no válida");
                json = jsonOB.build();
            }

        } catch (Exception ex) {
            //Logger.getLogger(AuthJWT.class.getName()).log(Level.SEVERE, null, ex);
            jsonOB.add("error", "apikey sin estructura válida");
            json = jsonOB.build();
        }
        return json;
    }

    public static Response getData(@QueryParam("apikey") String token, HttpServletRequest request) {
        Response response;
        Response.Status statusResul;

        boolean validado = false;
        String usuario = "";
        if (token != null) {
            JsonObject jsonJWT = JWT_Util.validateJWT(token, request);
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

    public static boolean validaSocio(String token, HttpServletRequest request) {
        boolean validado = false;
        if (token != null) {
            JsonObject jsonJWT = validateJWT(token,request);
            if (jsonJWT.containsKey("validate")) {
                if (jsonJWT.getBoolean("validate")) {
                    String usuario = jsonJWT.getString("rol");
                    if (usuario.equals("socio")) {
                        validado = true;
                    }
                }
            }
        }
        return validado;
    }

    public static boolean validaTrabajador(String token, HttpServletRequest request) {
        boolean validado = false;
        if (token != null) {
            JsonObject jsonJWT = validateJWT(token,request);
            if (jsonJWT.containsKey("validate")) {
                if (jsonJWT.getBoolean("validate")) {
                    String usuario = jsonJWT.getString("rol");
                    if (usuario.equals("administrador")) {
                        validado = true;
                    }
                }
            }
        }
        return validado;
    }
}
