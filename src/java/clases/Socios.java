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
@Table(name = "socios")
@NamedQueries({
    @NamedQuery(name = "Socios.findAll", query = "SELECT s FROM Socios s"),
    @NamedQuery(name = "Socios.findByIdSocio", query = "SELECT s FROM Socios s WHERE s.idSocio = :idSocio"),
    @NamedQuery(name = "Socios.findByRol", query = "SELECT s FROM Socios s WHERE s.rol = :rol"),
    @NamedQuery(name = "Socios.findByDni", query = "SELECT s FROM Socios s WHERE s.dni = :dni"),
    @NamedQuery(name = "Socios.findByNombre", query = "SELECT s FROM Socios s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "Socios.findByApellidos", query = "SELECT s FROM Socios s WHERE s.apellidos = :apellidos"),
    @NamedQuery(name = "Socios.findByDireccion", query = "SELECT s FROM Socios s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "Socios.findByCorreo", query = "SELECT s FROM Socios s WHERE s.correo = :correo"),
    @NamedQuery(name = "Socios.findByTelefono", query = "SELECT s FROM Socios s WHERE s.telefono = :telefono"),
    @NamedQuery(name = "Socios.findByFechaNacimiento", query = "SELECT s FROM Socios s WHERE s.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Socios.findByContrasenya", query = "SELECT s FROM Socios s WHERE s.contrasenya = :contrasenya"),
    @NamedQuery(name = "Socios.findByCategoriasInteres", query = "SELECT s FROM Socios s WHERE s.categoriasInteres = :categoriasInteres")})
public class Socios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSocio")
    private Integer idSocio;
    @Size(max = 13)
    @Column(name = "rol")
    private String rol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "dni")
    private String dni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 65)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telefono")
    private int telefono;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "imagen")
    private String imagen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "contrasenya")
    private String contrasenya;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "categorias_interes")
    private String categoriasInteres;

    public Socios() {
    }

    public Socios(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public Socios(Integer idSocio, String dni, String nombre, String apellidos, String direccion, String correo, int telefono, String imagen, String contrasenya, String categoriasInteres) {
        this.idSocio = idSocio;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.imagen = imagen;
        this.contrasenya = contrasenya;
        this.categoriasInteres = categoriasInteres;
    }

    public Socios(Integer idSocio, String dni, String nombre, String apellidos, String direccion, String fechaNacimiento, String correo, int telefono, String imagen, String contrasenya, String categoriasInteres) {
        this.idSocio = idSocio;
        this.dni = dni;
        this.fechaNacimiento = StringToDate(fechaNacimiento);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.imagen = imagen;
        this.contrasenya = contrasenya;
        this.categoriasInteres = categoriasInteres;
    }

    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getCategoriasInteres() {
        return categoriasInteres;
    }

    public void setCategoriasInteres(String categoriasInteres) {
        this.categoriasInteres = categoriasInteres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSocio != null ? idSocio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Socios)) {
            return false;
        }
        Socios other = (Socios) object;
        if ((this.idSocio == null && other.idSocio != null) || (this.idSocio != null && !this.idSocio.equals(other.idSocio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clases.Socios[ idSocio=" + idSocio + " ]";
    }

}
