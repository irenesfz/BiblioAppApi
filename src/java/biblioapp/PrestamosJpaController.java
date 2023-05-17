/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clases.Libro;
import clases.Prestamos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author irene
 */
public class PrestamosJpaController implements Serializable {

    public PrestamosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prestamos prestamos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro = prestamos.getLibro();
            if (libro != null) {
                libro = em.getReference(libro.getClass(), libro.getIdLibro());
                prestamos.setLibro(libro);
            }
            em.persist(prestamos);
            if (libro != null) {
                libro.getPrestamosCollection().add(prestamos);
                libro = em.merge(libro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prestamos prestamos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamos persistentPrestamos = em.find(Prestamos.class, prestamos.getIdPrestamo());
            Libro libroOld = persistentPrestamos.getLibro();
            Libro libroNew = prestamos.getLibro();
            if (libroNew != null) {
                libroNew = em.getReference(libroNew.getClass(), libroNew.getIdLibro());
                prestamos.setLibro(libroNew);
            }
            prestamos = em.merge(prestamos);
            if (libroOld != null && !libroOld.equals(libroNew)) {
                libroOld.getPrestamosCollection().remove(prestamos);
                libroOld = em.merge(libroOld);
            }
            if (libroNew != null && !libroNew.equals(libroOld)) {
                libroNew.getPrestamosCollection().add(prestamos);
                libroNew = em.merge(libroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prestamos.getIdPrestamo();
                if (findPrestamos(id) == null) {
                    throw new NonexistentEntityException("The prestamos with id " + id + " no longer exists.");
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
            Prestamos prestamos;
            try {
                prestamos = em.getReference(Prestamos.class, id);
                prestamos.getIdPrestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamos with id " + id + " no longer exists.", enfe);
            }
            Libro libro = prestamos.getLibro();
            if (libro != null) {
                libro.getPrestamosCollection().remove(prestamos);
                libro = em.merge(libro);
            }
            em.remove(prestamos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prestamos> findPrestamosEntities() {
        return findPrestamosEntities(true, -1, -1);
    }

    public List<Prestamos> findPrestamosEntities(int maxResults, int firstResult) {
        return findPrestamosEntities(false, maxResults, firstResult);
    }

    private List<Prestamos> findPrestamosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamos.class));
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

    public Prestamos findPrestamos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prestamos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamos> rt = cq.from(Prestamos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
        //**
    //MÉTODOS PROPIOS
    //**

    public int eliminaPrestamo(PrestamosJpaController dao, int id) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("DELETE FROM Prestamos p WHERE p.idPrestamo = " + id);
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

    public List<Prestamos> prestamoNoFinalizadoJPQL(PrestamosJpaController dao) {
        EntityManager em = null;
        List<Prestamos> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT P FROM Prestamos p WHERE p.fechaDevolucion IS NULL");
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<Prestamos> prestamoNoFinalizadoPorFechaJPQL(PrestamosJpaController dao, String fecha) {
        EntityManager em = null;
        List<Prestamos> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT P FROM Prestamos p WHERE p.fechaDevolucion IS NULL AND p.fechaTope LIKE '" + fecha+"'%");
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<Prestamos> buscaPrestamoPorSocioJPQL(PrestamosJpaController dao, int idSocio) {
        EntityManager em = null;
        List<Prestamos> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT p FROM Prestamos p WHERE p.idSocio = " + idSocio + " ORDER BY p.fechaPrestamo DESC");
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<Prestamos> buscaPrestamosNoFinalizadosPorSocioJPQL(PrestamosJpaController dao, int idSocio) {
        EntityManager em = null;
        List<Prestamos> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT p FROM Prestamos p WHERE p.idSocio = " + idSocio + " AND p.fechaDevolucion = " + null);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

}
