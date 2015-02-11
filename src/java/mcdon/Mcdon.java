package mcdon;

//import com.sun.xml.internal.fastinfoset.util.StringArray;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Mcdon {
//    private static final String fileName = "C:\\Users\\t4ojpa00\\Desktop\\mcdonaldsprices.txt";
    private static final String fileName = "C:\\Users\\Pasi\\Desktop\\mcdonaldsprices.txt";
    private final Scanner fs;
    private String kenttäerotin = "\t";
    private String seuraavaRivi;
    private String[] otsikotTuotteet;
    private String[] otsikotRyhmät;
    private String vientilauseRyhmät;
    private String vientilauseTuotteet;
    
    private Connection db;
    
    
    private Mcdon() throws FileNotFoundException, SQLException {
        fs = new Scanner(new File(fileName));
        haeSeuraavaRivi();
        db=DriverManager.getConnection(
                "jdbc:derby://localhost:1527/mcdon",
                "mcdon", "mcdon");
    }
    
    private void haeSeuraavaRivi() {
        if (fs.hasNextLine()) {
            seuraavaRivi = fs.nextLine();
        } else {
            seuraavaRivi = null;
        }
    }
    
    private void lueSep() {
        if (seuraavaRivi==null) return;
        if (seuraavaRivi.startsWith("sep=")) {
            kenttäerotin = seuraavaRivi.substring(4);
            haeSeuraavaRivi();
            System.out.println("Fieldsep schanged to "+kenttäerotin);
        }
    }
    
    private void luoOtsikotJaLauseet() throws SQLException{
        if (seuraavaRivi==null) return;
//        otsikot=seuraavaRivi.split(kenttäerotin);
//        seuraavaRivi.split(kenttäerotin);
        /*actually, nothing here to see 
        */
        
//        System.out.print("Otsikot: ");
//        for (String otsikko:otsikot){
//            System.out.print(otsikko+" ");
//        }
//        for(int i=0;i<otsikot.length;i++){
//            otsikot[i]=otsikot[i].toLowerCase().replace(" ", "_");
//            if (otsikot[i].equals("date")){
//                otsikot[i]="tradedate";
//            }
//        }
//        System.out.println("Otsikot: "+String.join(" ",otsikot));
//        System.out.println("");
//        haeSeuraavaRivi();
        
        otsikotTuotteet=("tuote koko hinta").split(" ");
        otsikotRyhmät=("ryhma").split(" ");
        
        
        poistaVanhatTaulut();
        
        StringBuilder luontilauseTuotteet=new StringBuilder();
        StringBuilder vientilauseTuotteet=new StringBuilder();
        luontilauseTuotteet.append("create table tuotteet(");
        vientilauseTuotteet.append("insert into tuotteet values (");

        for (String sarake:otsikotTuotteet){
            if (sarake.equals("tuote")){
            vientilauseTuotteet.append("?");
            luontilauseTuotteet.append(sarake).append(" "+" varchar (256)");
            }
            else if (!sarake.isEmpty()){
                
                if (sarake.equals("hinta")){
                luontilauseTuotteet.append(" , ")
                .append(sarake)
                .append(" "+" decimal (16,4)");
                }
                else{
                luontilauseTuotteet.append(" , ")
                .append(sarake)
                .append(" "+" varchar (256)");
                }
                
                vientilauseTuotteet.append(",?");
            }
        }
        luontilauseTuotteet.append(",numero integer primary key");
        vientilauseTuotteet.append(",?");

        luontilauseTuotteet.append(",ryhma integer");
        vientilauseTuotteet.append(",?");

        
        vientilauseTuotteet.append(")");
        luontilauseTuotteet.append(")");
        this.vientilauseTuotteet=vientilauseTuotteet.toString();

        System.out.println(luontilauseTuotteet);
        System.out.println(vientilauseTuotteet);
        db.createStatement().execute(luontilauseTuotteet.toString());

        StringBuilder luontilauseRyhmät=new StringBuilder();
        StringBuilder vientilauseRyhmät=new StringBuilder();
        luontilauseRyhmät.append("create table ryhmat(");
        vientilauseRyhmät.append("insert into ryhmat values (");
        for (String sarake:otsikotRyhmät){
            if (sarake.equals("ryhma")){
            vientilauseRyhmät.append("?");
            luontilauseRyhmät.append(sarake).append(" "+" varchar (256) primary key");
            }
            else if (!sarake.isEmpty()){
                luontilauseTuotteet.append(" , ")
                .append(sarake)
                .append(" "+" decimal (16,4)");
                vientilauseRyhmät.append(",?");
            }
            
        }
        vientilauseRyhmät.append(")");
        luontilauseRyhmät.append(")");
        
        this.vientilauseRyhmät=vientilauseRyhmät.toString();
        System.out.println(luontilauseRyhmät);
        System.out.println(vientilauseRyhmät);
        db.createStatement().execute(luontilauseRyhmät.toString());
        
    }

    private void poistaVanhatTaulut() throws SQLException {
        //poista vanha
        ResultSet taulut=
                db.getMetaData().getTables(null, null, "TUOTTEET", null);
        if (taulut.next()){
            System.out.println(taulut.getString("TABLE_NAME"));
            db.createStatement().execute("drop table tuotteet");
        }else{
            System.out.println("ei taulua nimeltä \"tuotteet\"");
        }
        taulut=
                db.getMetaData().getTables(null, null, "RYHMAT", null);
        if (taulut.next()){
            System.out.println(taulut.getString("TABLE_NAME"));
            db.createStatement().execute("drop table ryhmat");
        }else{
            System.out.println("ei taulua nimeltä \"ryhmat\"");
        }
        //luo uusi taulu ja vientilause
    }
    private void lueJaTalletaHinnasto() throws SQLException {
        
        PreparedStatement psTuotteet = db.prepareStatement(vientilauseTuotteet);
        PreparedStatement psRyhmät = db.prepareStatement(vientilauseRyhmät);
        int tuotelaskuri=0;
        int ryhmälaskuri=0;
        while (seuraavaRivi!=null) {
                //tallennetaan rivi
            
            String kentät[]=seuraavaRivi.split(kenttäerotin);
            if (kentät.length>1){
//                tuote 
                tuotelaskuri++;
                try{
                    
                for (int i=0;i<kentät.length;i++){
                    kentät[i]=kentät[i].replace("$", "");
                    
                    //the last col will be the price
                    if (i<kentät.length-1){
                    if (kentät[i].isEmpty()){
                        psTuotteet.setString(i+1,null);
                    }
                    else{
                        psTuotteet.setString(i+1,kentät[i]);
                    }
                    }
                    else{
                    if (kentät[i].isEmpty()){
                        psTuotteet.setBigDecimal(i+1,null);
                    }
                    else{
                        psTuotteet.setFloat(i+1,new Float (kentät[i]));
                    }
                    }
                }
                psTuotteet.setInt(kentät.length+1, tuotelaskuri);
                psTuotteet.setInt(kentät.length+2, ryhmälaskuri);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                psTuotteet.execute();
            } else{
                ryhmälaskuri++;
                try{
                for (int i=0;i<kentät.length;i++){
                    
//                    kentät[i]=kentät[i].replace(",", ".");
                    if (kentät[i].isEmpty()){
                        psRyhmät.setString(i+1,null);
                    }
                    else{
                        psRyhmät.setString(i+1,kentät[i]);
                    }
                }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                psRyhmät.execute();
                
            }
            
            
            haeSeuraavaRivi();
            
        }
//        db.commit();
    }
    public static void main(String[] args) throws Exception {
        Mcdon mcdon = new Mcdon();
        mcdon.lueSep();
        mcdon.luoOtsikotJaLauseet();
        mcdon.lueJaTalletaHinnasto();
    }
    
}
