/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author irene
 */
public class GridSocios implements Serializable{
    Socios socio;
    List<String> prestamosSocio;
    List<String> reservasSocio;

    public GridSocios() {
    }

    public GridSocios(Socios socio, List<String> prestamosSocio, List<String> reservasSocio) {
        this.socio = socio;
        this.prestamosSocio = prestamosSocio;
        this.reservasSocio = reservasSocio;
    }

    public Socios getSocio() {
        return socio;
    }

    public void setSocio(Socios socio) {
        this.socio = socio;
    }

    public List<String> getPrestamosSocio() {
        return prestamosSocio;
    }

    public void setPrestamosSocio(List<String> prestamosSocio) {
        this.prestamosSocio = prestamosSocio;
    }

    public List<String> getReservasSocio() {
        return reservasSocio;
    }

    public void setReservasSocio(List<String> reservasSocio) {
        this.reservasSocio = reservasSocio;
    }

    @Override
    public String toString() {
        return "GridSocios{" + "socio=" + socio + ", prestamosSocio=" + prestamosSocio + ", reservasSocio=" + reservasSocio + '}';
    }
    
    
    
}
