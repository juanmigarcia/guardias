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
public class Medico {

    private String siglaExcel;
    private int columnaExcel;
    private String nombre;
    private String apellido1;
    private String apellido2;

    private int totalGuardiasRealizadasPeriodo;
    private int totalGuardiasPrevistasPeriodo;
    private int totalDiasDisponiblesPendientesPeriodo;
    
    //Sabados
    //private int totalGuardiasAcumuladasNivel1Anyo;
    //Viernes y Domingos
    //private int totalGuardiasAcumuladasNivel2Anyo;
    //Resto de semana
    //private int totalGuardiasAcumuladasNivel3Anyo;
    private List libranzas;

    public Medico(String nombr, String apel1, String apel2, String siglExcel, int columnaExcel) {
        this.setNombre(nombr);
        this.setApellido1(apel1);
        this.setApellido2(apel2);
        this.setSiglaExcel(siglExcel);
        this.setTotalGuardiasRealizadasPeriodo(0);
        this.setColumnaExcel(columnaExcel);
    }

    public void agregarGuardiaAnyo() {
        this.setTotalGuardiasRealizadasPeriodo(this.getTotalGuardiasRealizadasPeriodo() + 1);
    }

    public String getNombreCompleto() {
        return this.getNombre() + " " + this.getApellido1() + " " + this.getApellido2();
    }

    public static Medico getMedicoPorSigla(List<Medico> listadoMedicos, String peticionMedico) {
        for (Medico medico : listadoMedicos) {
            if (medico.getSiglaExcel().equals(peticionMedico)) {
                return medico;
            }
        }
        return null;
    }
    
    public int getGuardiasPendientesPorHacer(){
        return this.getTotalGuardiasPrevistasPeriodo()-this.getTotalGuardiasRealizadasPeriodo();
    }

    public String getSiglaExcel() {
        return siglaExcel;
    }

    public void setSiglaExcel(String siglaExcel) {
        this.siglaExcel = siglaExcel;
    }

    public List getLibranzas() {
        return libranzas;
    }

    public void setLibranzas(List libranzas) {
        this.libranzas = libranzas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public int getTotalGuardiasRealizadasPeriodo() {
        return totalGuardiasRealizadasPeriodo;
    }

    public void setTotalGuardiasRealizadasPeriodo(int totalGuardiasRealizadasPeriodo) {
        this.totalGuardiasRealizadasPeriodo = totalGuardiasRealizadasPeriodo;
    }

    public int getTotalGuardiasPrevistasPeriodo() {
        return totalGuardiasPrevistasPeriodo;
    }

    public void setTotalGuardiasPrevistasPeriodo(int totalGuardiasPrevistasPeriodo) {
        this.totalGuardiasPrevistasPeriodo = totalGuardiasPrevistasPeriodo;
    }
/*
    public int getTotalGuardiasAcumuladasNivel1Anyo() {
        return totalGuardiasAcumuladasNivel1Anyo;
    }

    public void setTotalGuardiasAcumuladasNivel1Anyo(int totalGuardiasAcumuladasNivel1Anyo) {
        this.totalGuardiasAcumuladasNivel1Anyo = totalGuardiasAcumuladasNivel1Anyo;
    }

    public int getTotalGuardiasAcumuladasNivel2Anyo() {
        return totalGuardiasAcumuladasNivel2Anyo;
    }

    public void setTotalGuardiasAcumuladasNivel2Anyo(int totalGuardiasAcumuladasNivel2Anyo) {
        this.totalGuardiasAcumuladasNivel2Anyo = totalGuardiasAcumuladasNivel2Anyo;
    }

    public int getTotalGuardiasAcumuladasNivel3Anyo() {
        return totalGuardiasAcumuladasNivel3Anyo;
    }

    public void setTotalGuardiasAcumuladasNivel3Anyo(int totalGuardiasAcumuladasNivel3Anyo) {
        this.totalGuardiasAcumuladasNivel3Anyo = totalGuardiasAcumuladasNivel3Anyo;
    }*/

    public int getColumnaExcel() {
        return columnaExcel;
    }

    public void setColumnaExcel(int columnaExcel) {
        this.columnaExcel = columnaExcel;
    }
   
    public void agregarDiaDisponible(){
        this.setTotalDiasDisponiblesPendientesPeriodo(this.getTotalDiasDisponiblesPendientesPeriodo()+1);
    }

    public int getTotalDiasDisponiblesPendientesPeriodo() {
        return totalDiasDisponiblesPendientesPeriodo;
    }

    public void setTotalDiasDisponiblesPendientesPeriodo(int totalDiasDisponiblesPendientesPeriodo) {
        this.totalDiasDisponiblesPendientesPeriodo = totalDiasDisponiblesPendientesPeriodo;
    }
}
