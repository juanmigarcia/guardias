/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author garciapj
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Inicializar medicos del servicio
        List<Medico> listadoMedicos = new ArrayList();
        Medico medico1 = new Medico("Silvia", "Benito", "Costey", "S", 6, 3);
        listadoMedicos.add(medico1);
        Medico medico2 = new Medico("Vicky", "Caballero", "", "V", 6, 4);
        listadoMedicos.add(medico2);
        Medico medico3 = new Medico("Roberto", "", "", "R", 7, 5);
        listadoMedicos.add(medico3);
        Medico medico4 = new Medico("Nuria", "", "", "N", 6, 6);
        listadoMedicos.add(medico4);
        Medico medico5 = new Medico("Carolina", "Castaño", "", "C", 6, 7);
        listadoMedicos.add(medico5);
        System.out.println("---------------------------------------------------------------------------------------");

        try {
            //Inicializar calendario
            CalendarioExcel calendario = new CalendarioExcel("C:/calendario.xls", listadoMedicos);
            List<DiaCalendario> listadoDias = calendario.getListadoDiasCalendario();
            for (int i = 0; i < listadoDias.size(); i++) {
                System.out.println(listadoDias.get(i));
            }

            //Se reparten las guardias
            GestorGuardias gestorGuardias = new GestorGuardias();
            gestorGuardias.repartirGuardias(calendario.getListadoDiasCalendario(), listadoMedicos);
            for (Guardia guardia : gestorGuardias.getListadoGuardias()) {
                System.out.println(guardia);
            }
            System.out.println("---------------------------------------------------------------------------------------");

            //Se hace un informe para saber el numero de guardias asignadas globalmente
            for (Medico medico : listadoMedicos) {
                System.out.println("Medico " + medico.getNombreCompleto() + " total guardias:" + medico.getTotalGuardiasRealizadasPeriodo());
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.FINE, "ERRORRRR");
        }
    }
}
