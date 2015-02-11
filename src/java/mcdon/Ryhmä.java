/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcdon;

import java.util.ArrayList;

/**
 *
 * @author t4ojpa00
 */
public class Ryhmä {
    String nimi;
    ArrayList<Tuote> tuotteet;

    public Ryhmä(String nimi,int numero) {
        if (tuotteet==null){
            tuotteet = new ArrayList<>();
        }
        this.numero = numero;
        this.nimi = nimi;
        System.out.println(this.nimi);
    }

    public String getNimi() {
        return nimi;
    }

    public ArrayList<Tuote> getTuotteet() {
        return tuotteet;
    }
    int numero;

    public int getNumero() {
        return numero;
    }

    public void lisääTuote(Tuote t) {
        tuotteet.add(t);
    }
    
}
