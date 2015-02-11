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

//    @Resource(lookup="jdbc/mcdon")
//    private DataSource db;
    PreparedStatement psR;
    PreparedStatement psT;
    Connection c;

    private ArrayList<Ryhmä> ryhmät;
    
    private int ryhmälaskuri = 0;

    public ArrayList<Ryhmä> getRyhmät() {
        return ryhmät;
    }

    public McdonReader() throws SQLException {
        if (ryhmät==null){
            ryhmät=new ArrayList<>();}
        c = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/mcdon",
                "mcdon", "mcdon"); 
        

//        Connection c=db.getConnection();
//        c=db.getConnection();
        psR = c.prepareStatement("select * from ryhmat");
        psT = c.prepareStatement("select * from tuotteet where RYHMA = ?");
        ResultSet rs = psR.executeQuery();
        while (rs.next()) {
            try {
                Ryhmä r = new Ryhmä(rs.getString("RYHMA"),++ryhmälaskuri);
                ryhmät.add(r);
                int a=1;
                psT.setInt(1, ryhmälaskuri);
                System.out.println(psT.toString());
                ResultSet rsT = psT.executeQuery();
                while(rsT.next()){
                    Tuote t=new Tuote(rsT.getString(1),
                                        rsT.getString(2),
                                        rsT.getDouble(3)
                    );
                    r.lisääTuote(t);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        rs.close();
        psR.close();
        c.close();

    }



    public static void main(String[] args) throws SQLException {
        McdonReader mc = new McdonReader();
    }
}
