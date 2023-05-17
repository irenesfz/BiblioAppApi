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
public class GridReservas implements Serializable {
    int idReserva;
    String nombreSocio;
    String dniSocio;
    int idLibro;
    String tituloLibro;
    String isbn;
    String imagenPortada;
    Date fechaReserva;
    boolean finalizada;

    public GridReservas() {
    }

    public GridReservas(int idReserva, String nombreSocio, String dniSocio, int idLibro, String tituloLibro, String isbn, String imagenPortada, Date fechaReserva, boolean finalizada) {
        this.idReserva = idReserva;
        this.nombreSocio = nombreSocio;
        this.dniSocio = dniSocio;
        this.idLibro = idLibro;
        this.tituloLibro = tituloLibro;
        this.isbn = isbn;
        this.imagenPortada = imagenPortada;
        this.fechaReserva = fechaReserva;
        this.finalizada = finalizada;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
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

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    @Override
    public String toString() {
        return "GridReservas{" + "idReserva=" + idReserva + ", nombreSocio=" + nombreSocio + ", dniSocio=" + dniSocio + ", idLibro=" + idLibro + ", tituloLibro=" + tituloLibro + ", isbn=" + isbn + ", imagenPortada=" + imagenPortada + ", fechaReserva=" + fechaReserva + ", finalizada=" + finalizada + '}';
    }
    
}
