/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author garciapj
 */
public class TratarFechas {

    private static final int AJUSTE_MESES = 1;

    private TratarFechas() {
    }

    public static String getStringDate(DiaCalendario diaCalendario) {
        Calendar calendarDia = Calendar.getInstance();
        calendarDia.setTime(diaCalendario);
        
        return calendarDia.get(Calendar.DAY_OF_MONTH) + "/" + (calendarDia.get(Calendar.MONTH) + AJUSTE_MESES) + "/" + calendarDia.get(Calendar.YEAR);
    }

    public static boolean esFinde(Date calendar) {
        Calendar calendarDia = Calendar.getInstance();
        calendarDia.setTime(calendar);
        int dayOfWeek = calendarDia.get(Calendar.DAY_OF_WEEK);
        return Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek;
    }
}
