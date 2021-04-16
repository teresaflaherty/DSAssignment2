/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;

@Named(value = "provider")
@RequestScoped

/**
 *
 * @author my pc
 */
public class freelancer {
    
    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app"; 
    
    public void jobstatus(int Jobid, int jobstatus){
        
        // TODO add your handling code here:
                try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n"; 
                try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
//                int id;
//                id= Integer.parseInt(Jobid);
//                
                 //Prepare a query to insert values into Athlete Coaches table
                String query = "UPDATE JOBDescriptions SET JOBSTATUS=? where id="+Jobid;

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                
                  pst.setString(1,"JOBSTATUS"+ jobstatus); 
                
                //execute the queries
                pst.executeUpdate();
                

                
                //execute the queries
                pst.executeUpdate();
                
                                //Get text entered into textfields
                //put them into the corresponding queries
     
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }

    
    }
    
}
