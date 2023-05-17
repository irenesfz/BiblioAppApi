/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static utils.DateConverter.StringToDate;

/**
 *
 * @author irene
 */
@Entity
@Table(name = "eventos")
@NamedQueries({
    @NamedQuery(name = "Eventos.findAll", query = "SELECT e FROM Eventos e"),
    @NamedQuery(name = "Eventos.findByIdEvento", query = "SELECT e FROM Eventos e WHERE e.idEvento = :idEvento"),
    @NamedQuery(name = "Eventos.findByTitulo", query = "SELECT e FROM Eventos e WHERE e.titulo = :titulo"),
    @NamedQuery(name = "Eventos.findByDescripcion", query = "SELECT e FROM Eventos e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "Eventos.findByFechaPublicacion", query = "SELECT e FROM Eventos e WHERE e.fechaPublicacion = :fechaPublicacion"),
    @NamedQuery(name = "Eventos.findByFechaEvento", query = "SELECT e FROM Eventos e WHERE e.fechaEvento = :fechaEvento"),
    @NamedQuery(name = "Eventos.findByFechaEventoLike", query = "SELECT e FROM Eventos e WHERE e.fechaEvento LIKE :fechaEvento")})
public class Eventos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEvento")
    private Integer idEvento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "imagen")
    private String imagen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_publicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPublicacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_evento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEvento;

    public Eventos() {
    }

    public Eventos(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public Eventos(Integer idEvento, String titulo, String descripcion, Date fechaPublicacion, Date fechaEvento) {
        this.idEvento = idEvento;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaEvento = fechaEvento;
    }

    public Eventos(Integer idEvento, String titulo, String descripcion, Date fechaPublicacion, String fechaEvento) {
        this.idEvento = idEvento;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaEvento = StringToDate(fechaEvento);
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventos)) {
            return false;
        }
        Eventos other = (Eventos) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Eventos[ idEvento=" + idEvento + " ]";
    }

}
