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
        try {
            //Se crean los medicos por defecto
            List<Medico> listadoMedicos = crearMedicos();
            
            //Inicializar calendario
            CalendarioExcel calendario = new CalendarioExcel("C:/calendario.xls", listadoMedicos);
            List<DiaCalendario> listadoDias = calendario.getListadoDiasCalendario();
            for (int i = 0; i < listadoDias.size(); i++) {
                System.out.println(listadoDias.get(i));
            }      
            System.out.println("---------------------------------------------------------------------------------------");

            //Se reparten las guardias
            GestorGuardias gestorGuardias = null;
            boolean estaAjustado = false;
            int numeroPruebas = 20;
            int i = 0;
            while (!estaAjustado && i < numeroPruebas) {
                gestorGuardias = new GestorGuardias();
                gestorGuardias.reiniciarGuardiasRealizadas(listadoMedicos);
                gestorGuardias.repartirGuardias(calendario.getListadoDiasCalendario(), listadoMedicos);
                for (Medico medico : listadoMedicos) {
                    if(medico.getTotalGuardiasRealizadasPeriodo() == medico.getTotalGuardiasPrevistasPeriodo()) {
                        estaAjustado = true;
                    } else {
                        estaAjustado = false;
                        break;
                    }
                }
                i++; 
            }
            
            //Se muestran el listado de guardias que se han repartido
            for (Guardia guardia : gestorGuardias.getListadoGuardias()) {
                System.out.println(guardia);
            }
            System.out.println("---------------------------------------------------------------------------------------");

            
            //Se hace un informe para saber el numero de guardias asignadas globalmente
            for (Medico medico : listadoMedicos) {
                System.out.println("Medico " + medico.getNombreCompleto() + " total guardias: " + medico.getTotalGuardiasRealizadasPeriodo() + " dias disponibles: " + medico.getTotalDiasDisponiblesPendientesPeriodo());
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.FINE, "ERRORRRR");
        }
    }

    private static List<Medico> crearMedicos() {
        //Inicializar medicos del servicio
        List<Medico> listadoMedicos = new ArrayList();
        listadoMedicos.add(new Medico("Silvia", "Benito", "Costey", "S", 3));
        listadoMedicos.add(new Medico("Vicky", "Caballero", "", "V", 4));
        listadoMedicos.add(new Medico("Roberto", "", "", "R", 5));
        listadoMedicos.add(new Medico("Nuria", "", "", "N", 6));
        listadoMedicos.add(new Medico("Carolina", "Castaño", "", "C", 7));
        return listadoMedicos;
    }
}
