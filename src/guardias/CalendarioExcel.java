/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardias;

import exceptions.ExceptionColumnaDisponibilidad;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author garciapj
 */
public class CalendarioExcel {

    private List<DiaCalendario> listadoDiasCalendario;
    private static final int COMIENZO_FILAS = 1;
    private static final int COLUMNA_FECHA_MES = 0;
    private static final int COLUMNA_FESTIVO = 2;
    private static final int COLUMNA_PETICION = 8;

    private static final String ES_FESTIVO = "F";

    public static final String PETICION_SILVIA = "S";
    public static final String PETICION_VICKY = "V";
    public static final String PETICION_ROBERTO = "R";
    public static final String PETICION_NURIA = "N";
    public static final String PETICION_CAROLINA = "C";

    public static final String CONSTANTE_FIESTA = "FIESTA";
    public static final String CONSTANTE_CONSULTA = "CONSULTA";

    private static final Logger LOGGER = Logger.getLogger(CalendarioExcel.class.getName());

    private List<Medico> listadoMedicos;

    public CalendarioExcel(String ccalendarioxls, List<Medico> listaMedicos) {
        setListadoMedicos(listaMedicos);
        File excel = new File(ccalendarioxls);
        this.leerExcelFile(excel);
    }

    private void leerExcelFile(File excelFile) {
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(excelFile);
            // Representación del más alto nivel de la hoja excel.
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);
            // Elegimos la hoja que se pasa por parámetro.
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            // Objeto que nos permite leer un fila de la hoja excel, y de aquí extraer el contenido de las celdas.
            HSSFRow hssfRow;
            // Obtengo el número de filas ocupadas en la hoja
            int rows = hssfSheet.getLastRowNum();
            // Cadena que usamos para almacenar la lectura de la celda
            Date cellCalendario;
            String cellFestivo;
            String cellPeticion;

            List<DiaCalendario> listadoDias = new ArrayList<>();
            // Para este ejemplo vamos a recorrer las filas obteniendo los datos que queremos            
            for (int r = COMIENZO_FILAS; r < rows; r++) {
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null) {
                    break;
                } else {
                    cellCalendario = hssfRow.getCell(COLUMNA_FECHA_MES).getDateCellValue();

                    if (cellCalendario != null) {
                        cellFestivo = hssfRow.getCell(COLUMNA_FESTIVO) == null ? "" : hssfRow.getCell(COLUMNA_FESTIVO).getStringCellValue();
                        LOGGER.log(Level.FINE, "Row: {0} -> [Columna {1}: {2}] [Columna {3}: {4}] ", new Object[]{r, COLUMNA_FECHA_MES, cellFestivo, COLUMNA_FESTIVO, cellFestivo});

                        DiaCalendario diaCalendario = new DiaCalendario();
                        diaCalendario.setTime(cellCalendario.getTime());

                        //Se comprueba si es sabado o Domingo
                        if (TratarFechas.esFinde(diaCalendario)) {
                            diaCalendario.setEsFinde(Boolean.TRUE);
                            //TODO setear nivel de importancia (cada dia del finde tiene una importancia)
                        } else {
                            diaCalendario.setEsFinde(Boolean.FALSE);
                        }

                        if (ES_FESTIVO.equalsIgnoreCase(cellFestivo)) {
                            //Por norma general los festivos se pondrán a dedo
                            diaCalendario.setEsFestivo(Boolean.TRUE);                            
                        } else {
                            diaCalendario.setEsFestivo(Boolean.FALSE);
                        }

                        //No se tiene en cuenta que tipo de falta tiene (solo tiene que estar vacio)
                        this.obtenerDisponibilidadMedicosEnExcel(listadoMedicos, hssfRow, diaCalendario);


                        cellPeticion = hssfRow.getCell(COLUMNA_PETICION) == null ? "" : hssfRow.getCell(COLUMNA_PETICION).getStringCellValue();
                        LOGGER.log(Level.FINE, "----{0}", cellPeticion);
                        diaCalendario.setPeticionMedico(cellPeticion);

                        listadoDias.add(diaCalendario);
                    }
                }
            }
            this.setListadoDiasCalendario(listadoDias);
        } catch (FileNotFoundException fileNotFoundException) {
            LOGGER.log(Level.WARNING, "The file not exists (No se encontro el fichero): {0}", fileNotFoundException);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error in file procesing (Error al procesar el fichero): {0}", ex);
        } catch (ExceptionColumnaDisponibilidad ex) {
            LOGGER.log(Level.WARNING, "Error en lectura de celdas al leer la dispoibilidad, hay alguna celda que no es ni " + CONSTANTE_FIESTA + " ni " + CONSTANTE_CONSULTA + "{0}", ex);
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Error in file processing after close it (Error al procesar el fichero despues de cerrarlo): {0}", ex);
            }
        }
    }


    private void obtenerDisponibilidadMedicosEnExcel(List<Medico> listaMedicos, HSSFRow hssfRow, DiaCalendario diaCalendario) throws ExceptionColumnaDisponibilidad {
        for (Medico medico : listaMedicos) {
            this.comprobarDisponibilidadExcel(hssfRow, diaCalendario, medico.getColumnaExcelDisponibilidad(), medico.getSiglaExcel());
        }
    }    
    private void comprobarDisponibilidadExcel(HSSFRow hssfRow, DiaCalendario diaCalendario, int opcionDisponible, String siglaMedico) throws ExceptionColumnaDisponibilidad {
        if (hssfRow.getCell(opcionDisponible) == null || "".equals(hssfRow.getCell(opcionDisponible).getStringCellValue())) {
            Medico medico = Medico.getMedicoPorSigla(listadoMedicos, siglaMedico);
            diaCalendario.agregarMedicoDisponible(medico);
        } else if (!hssfRow.getCell(opcionDisponible).getStringCellValue().equalsIgnoreCase(CONSTANTE_FIESTA)
                && !hssfRow.getCell(opcionDisponible).getStringCellValue().equalsIgnoreCase(CONSTANTE_CONSULTA)) {
            throw new ExceptionColumnaDisponibilidad();
        }
    }

    public List<DiaCalendario> getListadoDiasCalendario() {
        return listadoDiasCalendario;
    }

    public void setListadoDiasCalendario(List<DiaCalendario> listadoDiasCalendario) {
        this.listadoDiasCalendario = listadoDiasCalendario;
    }

    public List<Medico> getListadoMedicos() {
        return listadoMedicos;
    }

    public void setListadoMedicos(List<Medico> listadoMedicos) {
        this.listadoMedicos = listadoMedicos;
    }
}
