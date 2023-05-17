/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apirest;

import clases.Eventos;
import java.util.Vector;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author irene
 */
public class ServiceRESTEventosTest {
    
    public ServiceRESTEventosTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAll method, of class ServiceRESTEventos.
     */
    @Test
    public void testGetAll() {
        System.out.println("getAll");
        ServiceRESTEventos instance = new ServiceRESTEventos();
        Response expResult = null;
        Response result = instance.getAll();
        Vector<Eventos> lista = (Vector<Eventos>) result.getEntity();
        System.out.println("Response Body is =>  " + lista.size());
        assertEquals(expResult, lista.size() );
        assertEquals(200, result.getStatus() );
    }


    /**
     * Test of getEventoDestacado method, of class ServiceRESTEventos.
     */
    @Test
    public void testGetEventoDestacado() {
        System.out.println("getEventoDestacado");
        String fecha = "09-05-2022";
        ServiceRESTEventos instance = new ServiceRESTEventos();
        Response expResult = null;
        Response result = instance.getEventoDestacado(fecha);
        System.out.println("Response Body is =>  " + result);
        assertEquals(expResult, result);
        assertEquals(200, result.getStatus() );
    }
    
}
