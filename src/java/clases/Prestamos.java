/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author irene
 */
@Entity
@Table(name = "prestamos")
@NamedQueries({
    @NamedQuery(name = "Prestamos.findAll", query = "SELECT p FROM Prestamos p"),
    @NamedQuery(name = "Prestamos.findByIdPrestamo", query = "SELECT p FROM Prestamos p WHERE p.idPrestamo = :idPrestamo"),
    @NamedQuery(name = "Prestamos.findByFechaPrestamo", query = "SELECT p FROM Prestamos p WHERE p.fechaPrestamo = :fechaPrestamo"),
    @NamedQuery(name = "Prestamos.findByFechaTope", query = "SELECT p FROM Prestamos p WHERE p.fechaTope = :fechaTope"),
    @NamedQuery(name = "Prestamos.findByFechaDevolucion", query = "SELECT p FROM Prestamos p WHERE p.fechaDevolucion = :fechaDevolucion"),
    @NamedQuery(name = "Prestamos.findByFechaLikeTope", query = "SELECT p FROM Prestamos p WHERE p.fechaTope LIKE :fechaTope AND p.fechaDevolucion IS NULL AND p.idSocio = :idSocio")})
public class Prestamos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPrestamo")
    private Integer idPrestamo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_prestamo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrestamo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_tope")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTope;
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @JoinColumns({
        @JoinColumn(name = "idlibro", referencedColumnName = "idlibro"),
        @JoinColumn(name = "idlibro", referencedColumnName = "idlibro")})
    @ManyToOne(optional = false)
    @JsonbTransient
    private Libro libro;

    //calculado
    @Column(name = "idLibro", updatable = false, insertable = false)
    private int idLibro;

    public int getIdLibro() {
        return idLibro;
    }

    @JoinColumns({
        @JoinColumn(name = "idSocio", referencedColumnName = "idSocio"),
        @JoinColumn(name = "idSocio", referencedColumnName = "idSocio")})
    @ManyToOne(optional = false)
    @JsonbTransient
    private Socios socios;

    //calculado
    @Column(name = "idSocio", updatable = false, insertable = false)
    private int idSocio;

    public int getIdSocio() {
        return idSocio;
    }

    public Prestamos() {
    }

    public Prestamos(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    //ctores propio
    public Prestamos(Integer idPrestamo, Date fechaPrestamo, Date fechaTope, Libro libro, Socios socio) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaTope = fechaTope;
        this.idSocio = socio.getIdSocio();
        this.socios = socio;
        this.libro = libro;
        this.fechaDevolucion = null;
        this.idLibro = libro.getIdLibro();
    }

    public Prestamos(Integer idPrestamo, Date fechaPrestamo, Date fechaTope) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaTope = fechaTope;
    }

    public Prestamos(Integer idPrestamo, Date fechaPrestamo, Date fechaTope, int idSocio) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaTope = fechaTope;
        this.idSocio = idSocio;
    }

    public Prestamos(Date fechaDevolucion, Date fechaTope, int idLibro, int idPrestamo, int idSocio) {
        this.fechaDevolucion = fechaDevolucion;
        this.fechaTope = fechaTope;
        this.idLibro = idLibro;
        this.idPrestamo = idPrestamo;
        this.idSocio = idSocio;
    }
     public Prestamos(Date fechaDevolucion, Date fechaPrestamo, Date fechaTope, int idLibro, int idPrestamo, int idSocio)
        {
            this.fechaDevolucion = fechaDevolucion;
            this.fechaPrestamo = fechaPrestamo;
            this.fechaTope = fechaTope;
            this.idLibro = idLibro;
            this.idPrestamo = idPrestamo;
            this.idSocio = idSocio;
        }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaTope() {
        return fechaTope;
    }

    public void setFechaTope(Date fechaTope) {
        this.fechaTope = fechaTope;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrestamo != null ? idPrestamo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestamos)) {
            return false;
        }
        Prestamos other = (Prestamos) object;
        if ((this.idPrestamo == null && other.idPrestamo != null) || (this.idPrestamo != null && !this.idPrestamo.equals(other.idPrestamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Prestamos[ idPrestamo=" + idPrestamo + " ]";
    }

}
