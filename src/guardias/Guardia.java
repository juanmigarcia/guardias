/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import java.util.List;

/**
 *
 * @author garciapj
 */
public class Guardia {

    private DiaCalendario diaCalendario;
    private Medico medicoAsignado;

    public Medico getMedicoAsignado() {
        return medicoAsignado;
    }

    public void setMedicoAsignado(Medico medicoAsignado) {
        if(medicoAsignado!=null) {
            this.medicoAsignado = medicoAsignado;
            this.medicoAsignado.agregarGuardiaAnyo();
        }
    }

    public DiaCalendario getDiaCalendario() {
        return diaCalendario;
    }

    public void setDiaCalendario(DiaCalendario diaCalendario) {
        this.diaCalendario = diaCalendario;
    }

    @Override
    public String toString() {
        String strMedicosDisponibles = "";
        List<Medico> listadoMedicosDisponibles = diaCalendario.getListadoMedicosDisponibles();
        for (Medico medicoDisponible : listadoMedicosDisponibles) {
            strMedicosDisponibles += (medicoDisponible.getNombreCompleto() + " ");
        }
        String strMedicoAsignado = "";
        if(this.getMedicoAsignado()!=null) {
            strMedicoAsignado = this.getMedicoAsignado().getNombreCompleto();
        }

        return TratarFechas.getStringDate(diaCalendario) + " "
                + ((diaCalendario.getEsFestivo() || diaCalendario.getEsFinde()) ? "FIESTA" : "LABORAL")
                + " asignado a " + strMedicoAsignado
                + " | medicos disponibles " + strMedicosDisponibles;
    }

}
