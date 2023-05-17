/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.IllegalOrphanException;
import biblioapp.exceptions.NonexistentEntityException;
import clases.Libro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clases.Libros;
import clases.Reservas;
import java.util.ArrayList;
import java.util.Collection;
import clases.Prestamos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author irene
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) {
        if (libro.getReservasCollection() == null) {
            libro.setReservasCollection(new ArrayList<Reservas>());
        }
        if (libro.getPrestamosCollection() == null) {
            libro.setPrestamosCollection(new ArrayList<Prestamos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libros libros = libro.getLibros();
            if (libros != null) {
                libros = em.getReference(libros.getClass(), libros.getIsbn());
                libro.setLibros(libros);
            }
            Collection<Reservas> attachedReservasCollection = new ArrayList<Reservas>();
            for (Reservas reservasCollectionReservasToAttach : libro.getReservasCollection()) {
                reservasCollectionReservasToAttach = em.getReference(reservasCollectionReservasToAttach.getClass(), reservasCollectionReservasToAttach.getIdReserva());
                attachedReservasCollection.add(reservasCollectionReservasToAttach);
            }
            libro.setReservasCollection(attachedReservasCollection);
            Collection<Prestamos> attachedPrestamosCollection = new ArrayList<Prestamos>();
            for (Prestamos prestamosCollectionPrestamosToAttach : libro.getPrestamosCollection()) {
                prestamosCollectionPrestamosToAttach = em.getReference(prestamosCollectionPrestamosToAttach.getClass(), prestamosCollectionPrestamosToAttach.getIdPrestamo());
                attachedPrestamosCollection.add(prestamosCollectionPrestamosToAttach);
            }
            libro.setPrestamosCollection(attachedPrestamosCollection);
            em.persist(libro);
            if (libros != null) {
                libros.getLibroCollection().add(libro);
                libros = em.merge(libros);
            }
            for (Reservas reservasCollectionReservas : libro.getReservasCollection()) {
                Libro oldIdLibroOfReservasCollectionReservas = reservasCollectionReservas.getIdLibro();
                reservasCollectionReservas.setIdLibro(libro);
                reservasCollectionReservas = em.merge(reservasCollectionReservas);
                if (oldIdLibroOfReservasCollectionReservas != null) {
                    oldIdLibroOfReservasCollectionReservas.getReservasCollection().remove(reservasCollectionReservas);
                    oldIdLibroOfReservasCollectionReservas = em.merge(oldIdLibroOfReservasCollectionReservas);
                }
            }
            for (Prestamos prestamosCollectionPrestamos : libro.getPrestamosCollection()) {
                Libro oldLibroOfPrestamosCollectionPrestamos = prestamosCollectionPrestamos.getLibro();
                prestamosCollectionPrestamos.setLibro(libro);
                prestamosCollectionPrestamos = em.merge(prestamosCollectionPrestamos);
                if (oldLibroOfPrestamosCollectionPrestamos != null) {
                    oldLibroOfPrestamosCollectionPrestamos.getPrestamosCollection().remove(prestamosCollectionPrestamos);
                    oldLibroOfPrestamosCollectionPrestamos = em.merge(oldLibroOfPrestamosCollectionPrestamos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getIdLibro());
            Libros librosOld = persistentLibro.getLibros();
            Libros librosNew = libro.getLibros();
            Collection<Reservas> reservasCollectionOld = persistentLibro.getReservasCollection();
            Collection<Reservas> reservasCollectionNew = libro.getReservasCollection();
            Collection<Prestamos> prestamosCollectionOld = persistentLibro.getPrestamosCollection();
            Collection<Prestamos> prestamosCollectionNew = libro.getPrestamosCollection();
            List<String> illegalOrphanMessages = null;
            for (Reservas reservasCollectionOldReservas : reservasCollectionOld) {
                if (!reservasCollectionNew.contains(reservasCollectionOldReservas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservas " + reservasCollectionOldReservas + " since its idLibro field is not nullable.");
                }
            }
            for (Prestamos prestamosCollectionOldPrestamos : prestamosCollectionOld) {
                if (!prestamosCollectionNew.contains(prestamosCollectionOldPrestamos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prestamos " + prestamosCollectionOldPrestamos + " since its libro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (librosNew != null) {
                librosNew = em.getReference(librosNew.getClass(), librosNew.getIsbn());
                libro.setLibros(librosNew);
            }
            Collection<Reservas> attachedReservasCollectionNew = new ArrayList<Reservas>();
            for (Reservas reservasCollectionNewReservasToAttach : reservasCollectionNew) {
                reservasCollectionNewReservasToAttach = em.getReference(reservasCollectionNewReservasToAttach.getClass(), reservasCollectionNewReservasToAttach.getIdReserva());
                attachedReservasCollectionNew.add(reservasCollectionNewReservasToAttach);
            }
            reservasCollectionNew = attachedReservasCollectionNew;
            libro.setReservasCollection(reservasCollectionNew);
            Collection<Prestamos> attachedPrestamosCollectionNew = new ArrayList<Prestamos>();
            for (Prestamos prestamosCollectionNewPrestamosToAttach : prestamosCollectionNew) {
                prestamosCollectionNewPrestamosToAttach = em.getReference(prestamosCollectionNewPrestamosToAttach.getClass(), prestamosCollectionNewPrestamosToAttach.getIdPrestamo());
                attachedPrestamosCollectionNew.add(prestamosCollectionNewPrestamosToAttach);
            }
            prestamosCollectionNew = attachedPrestamosCollectionNew;
            libro.setPrestamosCollection(prestamosCollectionNew);
            libro = em.merge(libro);
            if (librosOld != null && !librosOld.equals(librosNew)) {
                librosOld.getLibroCollection().remove(libro);
                librosOld = em.merge(librosOld);
            }
            if (librosNew != null && !librosNew.equals(librosOld)) {
                librosNew.getLibroCollection().add(libro);
                librosNew = em.merge(librosNew);
            }
            for (Reservas reservasCollectionNewReservas : reservasCollectionNew) {
                if (!reservasCollectionOld.contains(reservasCollectionNewReservas)) {
                    Libro oldIdLibroOfReservasCollectionNewReservas = reservasCollectionNewReservas.getIdLibro();
                    reservasCollectionNewReservas.setIdLibro(libro);
                    reservasCollectionNewReservas = em.merge(reservasCollectionNewReservas);
                    if (oldIdLibroOfReservasCollectionNewReservas != null && !oldIdLibroOfReservasCollectionNewReservas.equals(libro)) {
                        oldIdLibroOfReservasCollectionNewReservas.getReservasCollection().remove(reservasCollectionNewReservas);
                        oldIdLibroOfReservasCollectionNewReservas = em.merge(oldIdLibroOfReservasCollectionNewReservas);
                    }
                }
            }
            for (Prestamos prestamosCollectionNewPrestamos : prestamosCollectionNew) {
                if (!prestamosCollectionOld.contains(prestamosCollectionNewPrestamos)) {
                    Libro oldLibroOfPrestamosCollectionNewPrestamos = prestamosCollectionNewPrestamos.getLibro();
                    prestamosCollectionNewPrestamos.setLibro(libro);
                    prestamosCollectionNewPrestamos = em.merge(prestamosCollectionNewPrestamos);
                    if (oldLibroOfPrestamosCollectionNewPrestamos != null && !oldLibroOfPrestamosCollectionNewPrestamos.equals(libro)) {
                        oldLibroOfPrestamosCollectionNewPrestamos.getPrestamosCollection().remove(prestamosCollectionNewPrestamos);
                        oldLibroOfPrestamosCollectionNewPrestamos = em.merge(oldLibroOfPrestamosCollectionNewPrestamos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libro.getIdLibro();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getIdLibro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Reservas> reservasCollectionOrphanCheck = libro.getReservasCollection();
            for (Reservas reservasCollectionOrphanCheckReservas : reservasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Libro (" + libro + ") cannot be destroyed since the Reservas " + reservasCollectionOrphanCheckReservas + " in its reservasCollection field has a non-nullable idLibro field.");
            }
            Collection<Prestamos> prestamosCollectionOrphanCheck = libro.getPrestamosCollection();
            for (Prestamos prestamosCollectionOrphanCheckPrestamos : prestamosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Libro (" + libro + ") cannot be destroyed since the Prestamos " + prestamosCollectionOrphanCheckPrestamos + " in its prestamosCollection field has a non-nullable libro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Libros libros = libro.getLibros();
            if (libros != null) {
                libros.getLibroCollection().remove(libro);
                libros = em.merge(libros);
            }
            em.remove(libro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
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

    public Libro findLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    //***************
    //MÉTODOS PROPIOS
    //***************

    public List<Libro> getLibroPorISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l WHERE l.isbn LIKE '"+ isbn + "'");
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<Libro> getLibrosDisponiblesPrestamoPorISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l "
                    + "WHERE l.isbn LIKE '" + isbn + "' AND l.disponible =" + true);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<Libro> getLibrosReservadosPorISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l "
                    + "WHERE l.isbn LIKE '" + isbn + "' AND l.reservado =" + true);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public Libro getLibroDisponibleReservaPorISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l WHERE l.isbn LIKE '"
                    + isbn + "' AND l.reservado =" + false);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista.size() > 0 ? lista.get(0) : null;
    }

    public Libro getLibroDisponiblePrestamoPorISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l "
                    + "WHERE l.isbn LIKE '" + isbn + "' AND l.disponible =" + true);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista.size() > 0 ? lista.get(0) : null;
    }

    public int reservaLibro(LibroJpaController dao, String isbn, int idLibro) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Libro l SET l.reservado = " + true
                    + " WHERE l.idLibro = " + idLibro);
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

    public int libroEnPrestamo(LibroJpaController dao, String isbn, int idLibro) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Libro l SET l.reservado = " + true
                    + " WHERE l.idLibro = " + idLibro);
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

    public Libro getLibroDisponibleParaEliminarISBNNamedQuery(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l FROM Libro l "
                    + "WHERE l.isbn LIKE '" + isbn + "' AND l.disponible = " + true + " AND l.reservado = " + false);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista.size() > 0 ? lista.get(0) : null;
    }

    public int eliminaUnidadesLibros(LibroJpaController dao, String isbn) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("DELETE FROM Libro l WHERE l.isbn = '" + isbn + "'");
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

    public int eliminaUnidadLibros(LibroJpaController dao, int idLibro) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("DELETE FROM Libro l WHERE l.idLibro = '" + idLibro + "' AND l.reservado = " + false + " AND l.disponible = " + true);
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

    public List<Libro> existeLibroPorISBNNamedQuery(LibroJpaController dao, String idLibro) {
        EntityManager em = null;
        List<Libro> lista = null;
        try {
            em = dao.getEntityManager();
            TypedQuery<Libro> consultaLibro
                    = em.createNamedQuery("Libro.findByIdLibro", Libro.class);
            consultaLibro.setParameter("idLibro", idLibro);
            lista = consultaLibro.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista.isEmpty() ? null : lista;
    }
}
