/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.NonexistentEntityException;
import clases.Interbibliotecario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author irene
 */
public class InterbibliotecarioJpaController implements Serializable {

    public InterbibliotecarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Interbibliotecario interbibliotecario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(interbibliotecario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Interbibliotecario interbibliotecario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            interbibliotecario = em.merge(interbibliotecario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = interbibliotecario.getIdInterbibliotecario();
                if (findInterbibliotecario(id) == null) {
                    throw new NonexistentEntityException("The interbibliotecario with id " + id + " no longer exists.");
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
            Interbibliotecario interbibliotecario;
            try {
                interbibliotecario = em.getReference(Interbibliotecario.class, id);
                interbibliotecario.getIdInterbibliotecario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The interbibliotecario with id " + id + " no longer exists.", enfe);
            }
            em.remove(interbibliotecario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Interbibliotecario> findInterbibliotecarioEntities() {
        return findInterbibliotecarioEntities(true, -1, -1);
    }

    public List<Interbibliotecario> findInterbibliotecarioEntities(int maxResults, int firstResult) {
        return findInterbibliotecarioEntities(false, maxResults, firstResult);
    }

    private List<Interbibliotecario> findInterbibliotecarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Interbibliotecario.class));
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

    public Interbibliotecario findInterbibliotecario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Interbibliotecario.class, id);
        } finally {
            em.close();
        }
    }

    public int getInterbibliotecarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Interbibliotecario> rt = cq.from(Interbibliotecario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
