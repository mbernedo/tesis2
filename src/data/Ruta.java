/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Miguel
 */
public class Ruta {

    private String rutaFeat = "C:\\Users\\migue_000\\Desktop\\dataFeat.csv";
    private String rutaCLus = "C:\\Users\\migue_000\\Desktop\\dataCluster.csv";

    public Ruta() {
    }

    public String getRutaFeat() {
        return rutaFeat;
    }

    public void setRutaFeat(String rutaFeat) {
        this.rutaFeat = rutaFeat;
    }

    public String getRutaCLus() {
        return rutaCLus;
    }

    public void setRutaCLus(String rutaCLus) {
        this.rutaCLus = rutaCLus;
    }

}
