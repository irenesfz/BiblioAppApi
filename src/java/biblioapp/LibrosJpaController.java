/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioapp;

import biblioapp.exceptions.IllegalOrphanException;
import biblioapp.exceptions.NonexistentEntityException;
import biblioapp.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import clases.Libro;
import clases.Libros;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author irene
 */
public class LibrosJpaController implements Serializable {

    public LibrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libros libros) throws PreexistingEntityException, Exception {
        if (libros.getLibroCollection() == null) {
            libros.setLibroCollection(new ArrayList<Libro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Libro> attachedLibroCollection = new ArrayList<Libro>();
            for (Libro libroCollectionLibroToAttach : libros.getLibroCollection()) {
                libroCollectionLibroToAttach = em.getReference(libroCollectionLibroToAttach.getClass(), libroCollectionLibroToAttach.getIdLibro());
                attachedLibroCollection.add(libroCollectionLibroToAttach);
            }
            libros.setLibroCollection(attachedLibroCollection);
            em.persist(libros);
            for (Libro libroCollectionLibro : libros.getLibroCollection()) {
                Libros oldLibrosOfLibroCollectionLibro = libroCollectionLibro.getLibros();
                libroCollectionLibro.setLibros(libros);
                libroCollectionLibro = em.merge(libroCollectionLibro);
                if (oldLibrosOfLibroCollectionLibro != null) {
                    oldLibrosOfLibroCollectionLibro.getLibroCollection().remove(libroCollectionLibro);
                    oldLibrosOfLibroCollectionLibro = em.merge(oldLibrosOfLibroCollectionLibro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLibros(libros.getIsbn()) != null) {
                throw new PreexistingEntityException("Libros " + libros + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libros libros) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libros persistentLibros = em.find(Libros.class, libros.getIsbn());
            Collection<Libro> libroCollectionOld = persistentLibros.getLibroCollection();
            Collection<Libro> libroCollectionNew = libros.getLibroCollection();
            List<String> illegalOrphanMessages = null;
            for (Libro libroCollectionOldLibro : libroCollectionOld) {
                if (!libroCollectionNew.contains(libroCollectionOldLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Libro " + libroCollectionOldLibro + " since its libros field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Libro> attachedLibroCollectionNew = new ArrayList<Libro>();
            for (Libro libroCollectionNewLibroToAttach : libroCollectionNew) {
                libroCollectionNewLibroToAttach = em.getReference(libroCollectionNewLibroToAttach.getClass(), libroCollectionNewLibroToAttach.getIdLibro());
                attachedLibroCollectionNew.add(libroCollectionNewLibroToAttach);
            }
            libroCollectionNew = attachedLibroCollectionNew;
            libros.setLibroCollection(libroCollectionNew);
            libros = em.merge(libros);
            for (Libro libroCollectionNewLibro : libroCollectionNew) {
                if (!libroCollectionOld.contains(libroCollectionNewLibro)) {
                    Libros oldLibrosOfLibroCollectionNewLibro = libroCollectionNewLibro.getLibros();
                    libroCollectionNewLibro.setLibros(libros);
                    libroCollectionNewLibro = em.merge(libroCollectionNewLibro);
                    if (oldLibrosOfLibroCollectionNewLibro != null && !oldLibrosOfLibroCollectionNewLibro.equals(libros)) {
                        oldLibrosOfLibroCollectionNewLibro.getLibroCollection().remove(libroCollectionNewLibro);
                        oldLibrosOfLibroCollectionNewLibro = em.merge(oldLibrosOfLibroCollectionNewLibro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = libros.getIsbn();
                if (findLibros(id) == null) {
                    throw new NonexistentEntityException("The libros with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libros libros;
            try {
                libros = em.getReference(Libros.class, id);
                libros.getIsbn();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libros with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Libro> libroCollectionOrphanCheck = libros.getLibroCollection();
            for (Libro libroCollectionOrphanCheckLibro : libroCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Libros (" + libros + ") cannot be destroyed since the Libro " + libroCollectionOrphanCheckLibro + " in its libroCollection field has a non-nullable libros field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(libros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libros> findLibrosEntities() {
        return findLibrosEntities(true, -1, -1);
    }

    public List<Libros> findLibrosEntities(int maxResults, int firstResult) {
        return findLibrosEntities(false, maxResults, firstResult);
    }

    private List<Libros> findLibrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libros.class));
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

    public Libros findLibros(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libros.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libros> rt = cq.from(Libros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    //***********************
    //MÉTODOS PROPIOS
    //***********************
    //posible método
    //buscar libros que no estén reservados y estén disponibles
    //SELECT l1.* FROM Libros l1 INNER JOIN Libro l2 WHERE 
    //l1.isbn = l2.isbn AND l2.reservado = false AND l2.disponible = true;
    //*************
    public List<Libros> buscaLibrosDisponibles(LibrosJpaController dao) {
        EntityManager em = null;
        List<Libros> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l.libros FROM Libro l "
                    + "WHERE l.reservado = " + false + " AND l.disponible = " + true);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }
    public List<Libros> buscaLibrosParaReservar(LibrosJpaController dao) {
        EntityManager em = null;
        List<Libros> lista = null;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("SELECT l.libros FROM Libro l "
                    + "WHERE l.reservado = " + false);
            lista = query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public int updateLibroJPQL(LibrosJpaController dao, String isbn, Libros libroAActualizar) {
        EntityManager em = null;
        int resul = 0;
        try {
            em = dao.getEntityManager();
            Query query = em.createQuery("UPDATE Libros l SET "
                    + "l.titulo='" + libroAActualizar.getTitulo()
                    + "' , l.descripcion ='" + libroAActualizar.getDescripcion()
                    + "' , l.isbn ='" + libroAActualizar.getIsbn()
                    + "' , l.autores ='" + libroAActualizar.getAutores()
                    + "' , l.categoria ='" + libroAActualizar.getCategoria()
                    + "' , l.subcategorias ='" + libroAActualizar.getSubcategorias()
                    + "' , l.editorial ='" + libroAActualizar.getEditorial()
                    + "' , l.idioma ='" + libroAActualizar.getIdioma()
                    + "' , l.paginas =" + libroAActualizar.getPaginas()
                    + " , l.imagen ='" + libroAActualizar.getImagen()
                    + "' , l.anyoPublicacion ='" + libroAActualizar.getAnyoPublicacion()
                    + "' WHERE l.isbn='" + libroAActualizar.getIsbn() + "'");
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
