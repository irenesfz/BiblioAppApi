/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author irene
 */
public class GridPrestamos implements Serializable {

    int idPrestamo;
    String nombreSocio;
    String dniSocio;
    int idLibro;
    String tituloLibro;
    String isbn;
    String imagenPortada;
    Date fechaPrestamo;
    Date fechaTope;
    Date fechaDevolucion;

    public GridPrestamos() {
    }

    public GridPrestamos(int idPrestamo, String nombreSocio, String dniSocio, int idLibro, String tituloLibro, String isbn, String imagenPortada, Date fechaPrestamo, Date fechaTope, Date fechaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.nombreSocio = nombreSocio;
        this.dniSocio = dniSocio;
        this.idLibro = idLibro;
        this.tituloLibro = tituloLibro;
        this.isbn = isbn;
        this.imagenPortada = imagenPortada;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaTope = fechaTope;
        this.fechaDevolucion = fechaDevolucion;
    }


    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    public String getDniSocio() {
        return dniSocio;
    }

    public void setDniSocio(String dniSocio) {
        this.dniSocio = dniSocio;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    @Override
    public String toString() {
        return "GridPrestamos{" + "idPrestamo=" + idPrestamo + ", nombreSocio=" + nombreSocio + ", dniSocio=" + dniSocio + ", idLibro=" + idLibro + ", tituloLibro=" + tituloLibro + ", isbn=" + isbn + ", fechaPrestamo=" + fechaPrestamo + ", fechaTope=" + fechaTope + ", fechaDevolucion=" + fechaDevolucion + '}';
    }

}
