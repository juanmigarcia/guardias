/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import exceptions.ExceptionNoDisponibilidadMedico;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *
 * @author garciapj
 */
public class GestorGuardias {

    List<Guardia> listadoGuardias;
    private static final String ELECCION_MEDICO_MAS_GUARDIAS_PENDIENTES = "MAS_GUARDIAS_PENDIENTES";
    private static final String ELECCION_MEDICO_ALEATORIO  = "ALEATORIO";
    private static final String ELECCION_MEDICO_MEJOR_PROBABILIDAD  = "PROBABILIDAD";

    public void repartirGuardias(List<DiaCalendario> listadoDiasCalendario, List<Medico> listadoMedicos) throws ExceptionNoDisponibilidadMedico {
        String tipoEleccionMedico = ELECCION_MEDICO_MEJOR_PROBABILIDAD;
        this.setearDiasCalendarioEnGuardias(listadoDiasCalendario);
        this.fijarGuardiasObligatorias(listadoMedicos);
        this.repartirGuardiasFindesYFestivos(listadoMedicos, tipoEleccionMedico);
        this.repartirGuardiasLaborables(listadoMedicos, tipoEleccionMedico);
    }

    private void setearDiasCalendarioEnGuardias(List<DiaCalendario> listadoDiasCalendario) {
        listadoGuardias = new ArrayList<>();
        for (DiaCalendario diaCalendario : listadoDiasCalendario) {
            Guardia guardia = new Guardia();
            guardia.setDiaCalendario(diaCalendario);
            listadoGuardias.add(guardia);
        }
    }

    private void fijarGuardiasObligatorias(List<Medico> listadoMedicos) {
        for (Guardia guardia : listadoGuardias) {
            if (!"".equals(guardia.getDiaCalendario().getPeticionMedico())) {
                guardia.setMedicoAsignado(Medico.getMedicoPorSigla(listadoMedicos, guardia.getDiaCalendario().getPeticionMedico()));
            }
        }
    }

    private void repartirGuardiasFindesYFestivos(List<Medico> listadoMedicos, String tipoEleccionMedico) throws ExceptionNoDisponibilidadMedico {
        for (Guardia guardia : listadoGuardias) {
            if ((guardia.getDiaCalendario().getEsFinde() || guardia.getDiaCalendario().getEsFestivo()) && (guardia.getMedicoAsignado() == null)) {
                //Asignamos medico
                guardia.setMedicoAsignado(this.getMedicoCompatible(guardia, listadoMedicos, tipoEleccionMedico));
            }
        }
    }

    private void repartirGuardiasLaborables(List<Medico> listadoMedicos, String tipoEleccionMedico) throws ExceptionNoDisponibilidadMedico {
        for (Guardia guardia : listadoGuardias) {
            if (guardia.getMedicoAsignado() == null) {
                //Asignamos medico
                guardia.setMedicoAsignado(this.getMedicoCompatible(guardia, listadoMedicos, tipoEleccionMedico));
            }
        }
    }

    private Medico getMedicoCompatible(Guardia guardia, List<Medico> listadoMedicos, String tipoEleccionMedico) throws ExceptionNoDisponibilidadMedico {

        List<Medico> listadoMedicosDisponibles = guardia.getDiaCalendario().getListadoMedicosDisponibles();
        Medico medicoAleatorio = null;
        if (listadoMedicos.isEmpty()) {
            throw new ExceptionNoDisponibilidadMedico();
        } else if (listadoMedicos.size() == 1) {
            medicoAleatorio = listadoMedicos.get(0);
        } else {
            Medico medicoGuardiaDiaAnterior = this.getMedicoGuardiaDiaAnterior(guardia);
            if (medicoGuardiaDiaAnterior != null) {
                listadoMedicosDisponibles.remove(medicoGuardiaDiaAnterior);
            }
            Medico medicoGuardiaDiaPosterior = this.getMedicoGuardiaDiaPosterior(guardia);
            if (medicoGuardiaDiaPosterior != null) {
                listadoMedicosDisponibles.remove(medicoGuardiaDiaPosterior);
            }
            if(tipoEleccionMedico.equals(ELECCION_MEDICO_MAS_GUARDIAS_PENDIENTES)) {
                medicoAleatorio = this.obtenerMedicoConMasGuardiasPendientes(listadoMedicosDisponibles);
            } else if(tipoEleccionMedico.equals(ELECCION_MEDICO_ALEATORIO)) {
                medicoAleatorio = this.getMedicoAleatorio(listadoMedicosDisponibles);
            } else if(tipoEleccionMedico.equals(ELECCION_MEDICO_MEJOR_PROBABILIDAD)) {
                medicoAleatorio = this.getMedicoAleatorio(listadoMedicosDisponibles);  
            }
        }

        //Se miran los disponibles para ese dia (no tengan consulta, no tenga vacas y no tenga guardia el dia de antes o despues)
        //Si hay uno... se asigna
        //Si hay varios... se coge los que tengan mas guardias pendientes de poner ese mes (guardias que tienen que hacer ese mes - guardias que llevan hechas)
        //Si hay varios con el mismo de guardias pendientes... se obtiene uno aleatorio (igual se puede intentar poner que esa semana no haya hecho guardias)
        //Si hay uno con mas guardias pendientes... se asigna
        //Get Medico Aleatorio
        /*Medico medicoAleatorio = this.getMedicoAleatorio(listadoMedicos);
        if(medicoAleatorioTieneLibre(medicoAleatorio, diaCalendario)){
            if(medicoCompatibleEnDia(medicoAleatorio, diaCalendario)){
                return medicoAleatorio;
            } else {
                this.getMedicoCompatible(diaCalendario, listadoMedicos);
                //TODO igual es mejor poner un metodo de buscar la mejor opcion para evitar bucles infinitos y mejorar eficiencia
            }
        } else {
            this.getMedicoCompatible(diaCalendario, listadoMedicos);
            //TODO igual es mejor poner un metodo de buscar la mejor opcion para evitar bucles infinitos y mejorar eficiencia
        }*/
        return medicoAleatorio;
    }

   
    private Medico obtenerMedicoConMasGuardiasPendientes(List<Medico> listadoMedicos) throws ExceptionNoDisponibilidadMedico {
        //TODO:
        //buscar el medico que tenga mas guardias pendientes para intentar ir haciendolas (pensar que puede ser que otro medico tenga vacaciones)
        if(listadoMedicos.isEmpty()){
            return null;
            //throw new ExceptionNoDisponibilidadMedico();
        } else {
            if (listadoMedicos.size() == 1) {
                return listadoMedicos.get(0);
            } else {
                Medico medicoConMasGuardiasPendientes = listadoMedicos.get(0);
                List<Medico> listaMedicosConMasGuardiasPendientes = new ArrayList<>();
                listaMedicosConMasGuardiasPendientes.add(medicoConMasGuardiasPendientes);
                int i=1;
                while (i<listadoMedicos.size()) {
                    if(medicoConMasGuardiasPendientes.getGuardiasPendientesPorHacer() < listadoMedicos.get(i).getGuardiasPendientesPorHacer()) {
                        medicoConMasGuardiasPendientes = listadoMedicos.get(i);
                        listaMedicosConMasGuardiasPendientes.clear();
                        listaMedicosConMasGuardiasPendientes.add(listadoMedicos.get(i));
                    } else if(medicoConMasGuardiasPendientes.getGuardiasPendientesPorHacer() == listadoMedicos.get(i).getGuardiasPendientesPorHacer()) {
                        listaMedicosConMasGuardiasPendientes.add(listadoMedicos.get(i));
                    }
                    i++;
                }
                if(listaMedicosConMasGuardiasPendientes.size()==1) {
                    return listaMedicosConMasGuardiasPendientes.get(0);
                } else {
                    return this.getMedicoAleatorio(listaMedicosConMasGuardiasPendientes);
                }
            }
        }
    }
    
    private Medico getMedicoAleatorio(List<Medico> listadoMedicos) throws ExceptionNoDisponibilidadMedico {
        if (!listadoMedicos.isEmpty()) {
            //Obtenemos un medico al azar            
            Random random = new Random();
            return listadoMedicos.get(random.nextInt(listadoMedicos.size()));
        } else {
            throw new ExceptionNoDisponibilidadMedico();
        }
    }

    private Medico getMedicoGuardiaDiaAnterior(Guardia guardia) {

        Guardia guardiaDiaAnterior = this.obtenerGuardiaPorDiaAnterior(guardia.getDiaCalendario(), listadoGuardias);
        if (guardiaDiaAnterior != null) {
            return guardiaDiaAnterior.getMedicoAsignado();
        }
        return null;
    }

    private Medico getMedicoGuardiaDiaPosterior(Guardia guardia) {
        Guardia guardiaDiaPosterior = this.obtenerGuardiaPorDiaPosterior(guardia.getDiaCalendario(), listadoGuardias);
        if (guardiaDiaPosterior != null) {
            return guardiaDiaPosterior.getMedicoAsignado();
        }
        return null;
    }

    private Guardia obtenerGuardiaPorDiaAnterior(DiaCalendario diaCalendario, List<Guardia> listaGuardias) {
        Calendar calendarDiaAnterior = Calendar.getInstance();
        // Configuramos la fecha que se recibe
        calendarDiaAnterior.setTime(diaCalendario);
        // numero de horas a añadir, o restar en caso de horas<0
        calendarDiaAnterior.add(Calendar.DATE, -1);

        return obtenerGuardiaPorDia(calendarDiaAnterior, listaGuardias);
    }

    private Guardia obtenerGuardiaPorDiaPosterior(DiaCalendario diaCalendario, List<Guardia> listaGuardias) {
        Calendar calendarDiaPosterior = Calendar.getInstance();
        // Configuramos la fecha que se recibe
        calendarDiaPosterior.setTime(diaCalendario);
        // numero de horas a añadir, o restar en caso de horas<0
        calendarDiaPosterior.add(Calendar.DATE, +1);

        return obtenerGuardiaPorDia(calendarDiaPosterior, listaGuardias);
    }

    private Guardia obtenerGuardiaPorDia(Calendar calendar, List<Guardia> listaGuardias) {
        for (Guardia guardia : listaGuardias) {
            Calendar calendarDia = Calendar.getInstance();
            calendarDia.setTime(guardia.getDiaCalendario());
            if (calendarDia.compareTo(calendar) == 0) {
                return guardia;
            }
        }
        return null;
    }

    void reiniciarGuardiasRealizadas(List<Medico> listadoMedicos) {
        for (Medico medico : listadoMedicos) {
            medico.setTotalGuardiasRealizadasPeriodo(0);            
        }
    }
    
    public List<Guardia> getListadoGuardias() {
        return listadoGuardias;
    }

    public void setListadoGuardias(List<Guardia> listadoGuardias) {
        this.listadoGuardias = listadoGuardias;
    }
}
