/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.Collection;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author irene
 */
@Entity
@Table(name = "libro")
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l"),
    @NamedQuery(name = "Libro.findByIdLibro", query = "SELECT l FROM Libro l WHERE l.idLibro = :idLibro"),
    @NamedQuery(name = "Libro.findByDisponible", query = "SELECT l FROM Libro l WHERE l.disponible = :disponible"),
    @NamedQuery(name = "Libro.findByDisponiblePrestamoPorId", query = "SELECT l FROM Libro l WHERE l.disponible = true AND l.idLibro = :idLibro"),
    @NamedQuery(name = "Libro.findByReservado", query = "SELECT l FROM Libro l WHERE l.reservado = :reservado")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLibro")
    private Integer idLibro;
    @Column(name = "disponible")
    private Boolean disponible;
    @Column(name = "reservado")
    private Boolean reservado;
    @JoinColumns({
        @JoinColumn(name = "isbn", referencedColumnName = "isbn"),
        @JoinColumn(name = "isbn", referencedColumnName = "isbn")})
    @ManyToOne(optional = false)
    @JsonbTransient
    private Libros libros;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLibro")
    @JsonbTransient
    private Collection<Reservas> reservasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libro")
    @JsonbTransient
    private Collection<Prestamos> prestamosCollection;

    //calculado
    @Column(name = "isbn", updatable = false, insertable = false)
    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public Libro() {
    }

    //constructor propio
    public Libro(Libros libro) {
        this.isbn = libro.getIsbn();
        this.idLibro = 0;
        this.libros = libro;
        this.reservado = false;
        this.disponible = true;
    }

    public Libro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean getReservado() {
        return reservado;
    }

    public void setReservado(Boolean reservado) {
        this.reservado = reservado;
    }

    public Libros getLibros() {
        return libros;
    }

    public void setLibros(Libros libros) {
        this.libros = libros;
    }

    public Collection<Reservas> getReservasCollection() {
        return reservasCollection;
    }

    public void setReservasCollection(Collection<Reservas> reservasCollection) {
        this.reservasCollection = reservasCollection;
    }

    public Collection<Prestamos> getPrestamosCollection() {
        return prestamosCollection;
    }

    public void setPrestamosCollection(Collection<Prestamos> prestamosCollection) {
        this.prestamosCollection = prestamosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLibro != null ? idLibro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.idLibro == null && other.idLibro != null) || (this.idLibro != null && !this.idLibro.equals(other.idLibro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Libro[ idLibro=" + idLibro + " ]";
    }
}
