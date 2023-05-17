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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author irene
 */
@Entity
@Table(name = "libros")
@NamedQueries({
    @NamedQuery(name = "Libros.findAll", query = "SELECT l FROM Libros l"),
    @NamedQuery(name = "Libros.findByIsbn", query = "SELECT l FROM Libros l WHERE l.isbn = :isbn"),
    @NamedQuery(name = "Libros.findByTitulo", query = "SELECT l FROM Libros l WHERE l.titulo = :titulo"),
    @NamedQuery(name = "Libros.findByAutores", query = "SELECT l FROM Libros l WHERE l.autores = :autores"),
    @NamedQuery(name = "Libros.findByDescripcion", query = "SELECT l FROM Libros l WHERE l.descripcion = :descripcion"),
    @NamedQuery(name = "Libros.findByPaginas", query = "SELECT l FROM Libros l WHERE l.paginas = :paginas"),
    @NamedQuery(name = "Libros.findByCategoria", query = "SELECT l FROM Libros l WHERE l.categoria = :categoria"),
    @NamedQuery(name = "Libros.findBySubcategorias", query = "SELECT l FROM Libros l WHERE l.subcategorias = :subcategorias"),
    @NamedQuery(name = "Libros.findByAnyoPublicacion", query = "SELECT l FROM Libros l WHERE l.anyoPublicacion = :anyoPublicacion"),
    @NamedQuery(name = "Libros.findByEditorial", query = "SELECT l FROM Libros l WHERE l.editorial = :editorial"),
    @NamedQuery(name = "Libros.findByIdioma", query = "SELECT l FROM Libros l WHERE l.idioma = :idioma"),
    //************************
    //NamedQueries MANUALES
    //************************
    @NamedQuery(name = "Libros.findByLikeTitulo", query = "SELECT l FROM Libros l WHERE l.titulo LIKE :titulo"),
    @NamedQuery(name = "Libros.findByLikeAll", query = "SELECT l FROM Libros l "
            + "WHERE l.titulo LIKE :palabra "
            + "OR l.subcategorias LIKE :palabra "
            + "OR l.editorial LIKE :palabra "
            + "OR l.anyoPublicacion LIKE :palabra "
            + "OR l.autores LIKE :palabra "
            + "OR l.idioma LIKE :palabra "
            + "OR l.categoria LIKE :palabra"),
    // @NamedQuery(name = "Libros.findByLikeIsbn", query = "SELECT l FROM Libros l WHERE l.isbn LIKE :isbn"),
    @NamedQuery(name = "Libros.findByLikeCategoria", query = "SELECT l FROM Libros l WHERE l.categoria LIKE :categoria"),
    @NamedQuery(name = "Libros.findByLikeSubcategoria", query = "SELECT l FROM Libros l WHERE l.subcategorias LIKE :subcategorias"),
    @NamedQuery(name = "Libros.findByLikeEditorial", query = "SELECT l FROM Libros l WHERE l.editorial LIKE :editorial"),
    @NamedQuery(name = "Libros.findByLikeAutores", query = "SELECT l FROM Libros l WHERE l.autores LIKE :autores")})
public class Libros implements Serializable {

    @Size(max = 150)
    @Column(name = "titulo")
    private String titulo;
    @Size(max = 100)
    @Column(name = "autores")
    private String autores;
    @Size(max = 1500)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 100)
    @Column(name = "categoria")
    private String categoria;
    @Size(max = 100)
    @Column(name = "subcategorias")
    private String subcategorias;
    @Size(max = 100)
    @Column(name = "editorial")
    private String editorial;
    @Size(max = 50)
    @Column(name = "idioma")
    private String idioma;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "imagen")
    private String imagen;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "paginas")
    private Integer paginas;
    @Column(name = "anyo_publicacion")
    private Integer anyoPublicacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libros")
    @JsonbTransient
    private Collection<Libro> libroCollection;

    public Libros() {
    }

    public Libros(String isbn, String titulo,
            String autores, String descripcion, int paginas,
            String categoria, String subcategorias,
            int anyoPublicacion, String editorial,
            String idioma, String imagen) {
        this.anyoPublicacion = anyoPublicacion;
        this.titulo = titulo;
        this.autores = autores;
        this.descripcion = descripcion;
        this.subcategorias = subcategorias;
        this.editorial = editorial;
        this.idioma = idioma;
        this.imagen = imagen;
        this.isbn = isbn;
        this.paginas = paginas;
    }

    public Libros(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Integer getAnyoPublicacion() {
        return anyoPublicacion;
    }

    public void setAnyoPublicacion(Integer anyoPublicacion) {
        this.anyoPublicacion = anyoPublicacion;
    }

    public Collection<Libro> getLibroCollection() {
        return libroCollection;
    }

    public void setLibroCollection(Collection<Libro> libroCollection) {
        this.libroCollection = libroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libros)) {
            return false;
        }
        Libros other = (Libros) object;
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Libros[ isbn=" + isbn + " ]";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(String subcategorias) {
        this.subcategorias = subcategorias;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
