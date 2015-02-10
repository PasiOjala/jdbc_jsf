package mcdon;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author t4ojpa00
 */
@Named
@RequestScoped
public class McdonReader {
    
    
//    @Resource(lookup="jdbc/07-nokia")
//    @Resource(lookup="jdbc/mcdon")
//    private DataSource db;
    
    Connection c;
    
    PreparedStatement psR;
    
    private ArrayList<Ryhmä> ryhmät=new ArrayList<>();
    static int ryhmälaskuri=0;

    public ArrayList<Ryhmä> getRyhmät() {
        return ryhmät;
    }

    public McdonReader() throws SQLException {
         c=DriverManager.getConnection(
                "jdbc:derby://localhost:1527/mcdon",
                "mcdon", "mcdon");
//        c=db.getConnection();
         psR=c.prepareStatement("select * from ryhmat");
         ResultSet rs=psR.executeQuery();
        while(rs.next()){
            try{
                Ryhmä r=new Ryhmä(rs.getString("RYHMA"));
            ryhmät.add(r);
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
        }
                rs.close();
        psR.close();
        c.close();
    
    }
    
            
            



    private static class Ryhmä {

        String nimi;
        int numero;
        ArrayList<Tuote> tuotteet;

        private Ryhmä(String nimi) {
            
            this.numero=++ryhmälaskuri;
            this.nimi=nimi;
            System.out.println(this.nimi);
        }
    }
            private static class Tuote {

        public Tuote() {
            String nimi;
            String määrä;
            Double hinta;
        }
    }
    public static void main(String[] args) throws SQLException {
        McdonReader mc=new McdonReader();
    }
}
