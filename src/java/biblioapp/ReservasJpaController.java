/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.NonexistentEntityException;
import clases.Reservas;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
public class ReservasJpaController implements Serializable {

    public ReservasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservas reservas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reservas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservas reservas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reservas = em.merge(reservas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reservas.getIdReserva();
                if (findReservas(id) == null) {
                    throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.");
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
            Reservas reservas;
            try {
                reservas = em.getReference(Reservas.class, id);
                reservas.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.", enfe);
            }
            em.remove(reservas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservas> findReservasEntities() {
        return findReservasEntities(true, -1, -1);
    }

    public List<Reservas> findReservasEntities(int maxResults, int firstResult) {
        return findReservasEntities(false, maxResults, firstResult);
    }

    private List<Reservas> findReservasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservas.class));
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

    public Reservas findReservas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservas> rt = cq.from(Reservas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
        //**********************
    //MÉTODOS PROPIOS
    //**********************
    public List<Reservas> buscaReservasNoFinalizadasPorSocioJPQL(ReservasJpaController dao, int idSocio) {
        EntityManager em = null;
        List<Reservas> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT r FROM Reservas r WHERE r.socioId = " + idSocio
                    + " AND r.finalizada = " + false);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public int quitaNotificationJPQL(ReservasJpaController dao, int idReserva) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Reservas r SET r.notificacion = false WHERE r.idReserva = " + idReserva
            );
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

    public int poneNotificationJPQL(ReservasJpaController dao, int idReserva) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Reservas r SET r.notificacion = true WHERE r.idReserva = " + idReserva
            );
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

    public List<Reservas> buscaReservasPorSocioJPQL(ReservasJpaController dao, int idSocio) {
        EntityManager em = null;
        List<Reservas> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT r FROM Reservas r WHERE r.socioId = " + idSocio);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

}
