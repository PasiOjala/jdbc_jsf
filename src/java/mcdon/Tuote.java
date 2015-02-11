/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdon;

/**
 *
 * @author t4ojpa00
 */
public class Tuote {
    String nimi;
    String määrä;
    Double hinta;

    public Tuote(String nimi, String määrä, Double hinta) {
        this.nimi = nimi;
        this.määrä = määrä;
        this.hinta = hinta;
    }

    public String getNimi() {
        return nimi;
    }

    public String getMäärä() {
        return määrä;
    }

    public Double getHinta() {
        return hinta;
    }
    
}
