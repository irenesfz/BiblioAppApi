/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.NonexistentEntityException;
import clases.Socios;
import clases.Usuario;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author irene
 */
public class SociosJpaController implements Serializable {

    private static final String CLAVE_PRIVADA = "biblioproy";

    public SociosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Socios socios) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(socios);
            em.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            // Aqui tira los errores de constraint
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println("\n\n\n"+actual.toString());
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Socios socios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            socios = em.merge(socios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = socios.getIdSocio();
                if (findSocios(id) == null) {
                    throw new NonexistentEntityException("The socios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Socios socios;
            try {
                socios = em.getReference(Socios.class, id);
                socios.getIdSocio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socios with id " + id + " no longer exists.", enfe);
            }
            em.remove(socios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Socios> findSociosEntities() {
        return findSociosEntities(true, -1, -1);
    }

    public List<Socios> findSociosEntities(int maxResults, int firstResult) {
        return findSociosEntities(false, maxResults, firstResult);
    }

    private List<Socios> findSociosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Socios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Socios findSocios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Socios.class, id);
        } finally {
            em.close();
        }
    }

    public int getSociosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Socios> rt = cq.from(Socios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    //***************************
    //MÉTODOS PROPIOS
    //**************************

    public boolean validaContrasenya(Socios socio, String passwordAComparar) {
        if (socio != null) {
            return hash(passwordAComparar).equals(socio.getContrasenya());
        } else {
            return false;
        }
    }

    public static String hash(String password) {
        String generatedPassword = "";
        try {
            password = password + CLAVE_PRIVADA;
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = password.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ce) {
            System.out.println("Error " + ce.getMessage());
        }
        return generatedPassword;
    }

    public int resetContrasenyaJPQL(SociosJpaController dao, String dni) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Socios s SET s.contrasenya='" + hash(dni)
                    + "' WHERE s.dni='" + dni + "'");
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }

    public List<Socios> compruebaSocio(SociosJpaController dao, Usuario usuario) {
        EntityManager em = null;
        List<Socios> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT s FROM Socios s "
                    + " WHERE s.dni='" + usuario.getUsername()
                    + "' AND s.contrasenya= '" + hash(usuario.getPassword())
                    + "'");
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }

        }
        return lista;
    }

    public int updateContrasenyaJPQL(SociosJpaController dao, String dni, String newPassword, String oldPassword) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Socios s SET s.contrasenya='" + hash(newPassword)
                    + "' WHERE s.dni='" + dni + "' AND s.contrasenya= '" + hash(oldPassword) + "'");
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }

    public int updateSocioCorreoJPQL(SociosJpaController dao, String correo, int id) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Socios s SET s.correo = '" + correo + "' WHERE s.idSocio='" + id + "'");
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }

    public int updateSocioCategoriasJPQL(SociosJpaController dao, String categorias, int id) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Socios s SET s.categoriasInteres = '" + categorias + "' WHERE s.idSocio='" + id + "'");
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }

    public int updateSocioImagenJPQL(SociosJpaController dao, String imagen, int id) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Socios s SET s.imagen = '" + imagen + "' WHERE s.idSocio='" + id + "'");
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }


    public int deleteSocioJPQL(SociosJpaController dao, int idSocio) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("DELETE FROM Socios s WHERE s.idSocio=" + idSocio);
            em.getTransaction().begin();
            resul = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return resul;
    }
}
