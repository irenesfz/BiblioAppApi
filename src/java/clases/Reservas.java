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
@Table(name = "reservas")
@NamedQueries({
    @NamedQuery(name = "Reservas.findAll", query = "SELECT r FROM Reservas r"),
    @NamedQuery(name = "Reservas.findByIdReserva", query = "SELECT r FROM Reservas r WHERE r.idReserva = :idReserva"),
    @NamedQuery(name = "Reservas.findByFechaReserva", query = "SELECT r FROM Reservas r WHERE r.fechaReserva = :fechaReserva"),
    @NamedQuery(name = "Reservas.findByFinalizada", query = "SELECT r FROM Reservas r WHERE r.finalizada = :finalizada"),
    @NamedQuery(name = "Reservas.findByNotificacion", query = "SELECT r FROM Reservas r WHERE r.notificacion = :notificacion")})
public class Reservas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idReserva")
    private Integer idReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_reserva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReserva;
    @Column(name = "finalizada")
    private Boolean finalizada;
    @Column(name = "notificacion")
    private Boolean notificacion;
    @JoinColumn(name = "idLibro", referencedColumnName = "idlibro")
    @JsonbTransient
    @ManyToOne(optional = false)
    private Libro idLibro;

    //calculado
    @Column(name = "idLibro", updatable = false, insertable = false)
    private int libroId;

    public int getLibroId() {
        return libroId;
    }

    @JoinColumn(name = "idSocio", referencedColumnName = "idSocio")
    @ManyToOne(optional = false)
    @JsonbTransient
    private Socios idSocio;

    //calculado
    @Column(name = "idSocio", updatable = false, insertable = false)
    private int socioId;

    public int getSocioId() {
        return socioId;
    }

    public Reservas() {
    }
    
    //constructor propio
    public Reservas(Libro libro, Date fechaReserva, Socios socio) {
        this.fechaReserva = fechaReserva;
        this.idLibro = libro;
        this.libroId = libro.getIdLibro();
        this.idSocio = socio;
        this.socioId = socio.getIdSocio();
        this.finalizada = false;
        this.notificacion = false;
    }

    public Reservas(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Reservas(Integer idReserva, Date fechaReserva) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public Boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }

    public Libro getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Libro idLibro) {
        this.idLibro = idLibro;
    }

    public Socios getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socios idSocio) {
        this.idSocio = idSocio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReserva != null ? idReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservas)) {
            return false;
        }
        Reservas other = (Reservas) object;
        if ((this.idReserva == null && other.idReserva != null) || (this.idReserva != null && !this.idReserva.equals(other.idReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Reservas[ idReserva=" + idReserva + " ]";
    }

}
