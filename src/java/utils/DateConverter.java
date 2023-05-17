/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author irene
 */
public class DateConverter {

    public static Date StringToDate(String strFecha) {
        Calendar calendar = Calendar.getInstance();
        // year - AAAA
        // month - the month between 0-11.
        // date - the day of the month between 1-31.        
        calendar.set(
                Integer.valueOf(strFecha.substring(0, 4)),
                Integer.valueOf(strFecha.substring(5, 7)) - 1,
                Integer.valueOf(strFecha.substring(8, 10)));
        
       // calendar.add(Calendar.DATE, 1);
        
        return calendar.getTime();
    }
    
    public static String DateToString(Date fecha) {
        return new SimpleDateFormat("YYYY-MM-DD").format(fecha);
    }     

}
