/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author irene
 */
@Entity
@Table(name = "interbibliotecario")
@NamedQueries({
    @NamedQuery(name = "Interbibliotecario.findAll", query = "SELECT i FROM Interbibliotecario i"),
    @NamedQuery(name = "Interbibliotecario.findByIdInterbibliotecario", query = "SELECT i FROM Interbibliotecario i WHERE i.idInterbibliotecario = :idInterbibliotecario"),
    @NamedQuery(name = "Interbibliotecario.findByIsbn", query = "SELECT i FROM Interbibliotecario i WHERE i.isbn = :isbn"),
    @NamedQuery(name = "Interbibliotecario.findByTitulo", query = "SELECT i FROM Interbibliotecario i WHERE i.titulo = :titulo"),
    @NamedQuery(name = "Interbibliotecario.findByFinalizada", query = "SELECT i FROM Interbibliotecario i WHERE i.finalizada = :finalizada")})
public class Interbibliotecario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInterbibliotecario")
    private Integer idInterbibliotecario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "isbn")
    private String isbn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "finalizada")
    private boolean finalizada;

    public Interbibliotecario() {
    }

    public Interbibliotecario(Integer idInterbibliotecario) {
        this.idInterbibliotecario = idInterbibliotecario;
    }

    public Interbibliotecario(Integer idInterbibliotecario, String isbn, String titulo, boolean finalizada) {
        this.idInterbibliotecario = idInterbibliotecario;
        this.isbn = isbn;
        this.titulo = titulo;
        this.finalizada = finalizada;
    }

    public Integer getIdInterbibliotecario() {
        return idInterbibliotecario;
    }

    public void setIdInterbibliotecario(Integer idInterbibliotecario) {
        this.idInterbibliotecario = idInterbibliotecario;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInterbibliotecario != null ? idInterbibliotecario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Interbibliotecario)) {
            return false;
        }
        Interbibliotecario other = (Interbibliotecario) object;
        if ((this.idInterbibliotecario == null && other.idInterbibliotecario != null) || (this.idInterbibliotecario != null && !this.idInterbibliotecario.equals(other.idInterbibliotecario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Interbibliotecario[ idInterbibliotecario=" + idInterbibliotecario + " ]";
    }
    
}
