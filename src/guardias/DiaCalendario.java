/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author garciapj
 */
public class DiaCalendario extends Date {

    private Boolean esFestivo;
    private Boolean esFinde;
    private Integer nivelImportanciaFestivo;
    private String peticionMedico;
    private List<Medico> listadoMedicosDisponibles = new ArrayList();

    public void agregarMedicoDisponible(Medico medico) {
        this.listadoMedicosDisponibles.add(medico);
        medico.agregarDiaDisponible();
    }

    public String getPeticionMedico() {
        return peticionMedico;
    }

    public void setPeticionMedico(String peticionMedico) {
        this.peticionMedico = peticionMedico;
    }

    public Boolean getEsFestivo() {
        return esFestivo;
    }

    public void setEsFestivo(Boolean esFestivo) {
        this.esFestivo = esFestivo;
    }

    public Boolean getEsFinde() {
        return esFinde;
    }

    public void setEsFinde(Boolean esFinde) {
        this.esFinde = esFinde;
    }

    public Integer getNivelImportanciaFestivo() {
        return nivelImportanciaFestivo;
    }

    public void setNivelImportanciaFestivo(Integer nivelImportanciaFestivo) {
        this.nivelImportanciaFestivo = nivelImportanciaFestivo;
    }

    public List<Medico> getListadoMedicosDisponibles() {
        return listadoMedicosDisponibles;
    }

    public void setListadoMedicosDisponibles(List<Medico> listadoMedicosDisponibles) {
        this.listadoMedicosDisponibles = listadoMedicosDisponibles;
    }

    @Override
    public String toString() {
        return TratarFechas.getStringDate(this) + "\t " + (this.getEsFestivo() ? "FESTIVO" : "NO_FESTIVO") + "\t " + (this.getEsFinde() ? "FINDE" : "NO_FINDE");
    }
}
