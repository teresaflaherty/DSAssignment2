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

public class provider {
    
    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    
    //This removes a job posting, the string JOBID can be changed for an int
    // Just delete the int id on line 36-37
        public void remove_job(int Jobid){
        
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
                
                 //Prepare a query to insert values into Athlete Coaches table
                String query = "DELETE FROM JOBDescriptions where id="+Jobid;

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                

                
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
          
  public void remove_add(String title,String keywords, String decript,int payment,int jobstatus,int providerid,int freelancerid){
               // TODO add your handling code here:
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n"; 
                try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                 //Prepare a query to insert values into Athlete Coaches table
                String query = "INSERT INTO JOBDescriptions (TITLE,KEYWORDS,DESCRIPTION,PAYMENTOFFER,JOBSTATUS,PROVIDERID,FREELANCERID)"
                        +"VALUES(?,?,?,?,?,?,?)";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                

                
                //execute the queries
                pst.executeUpdate();
                

                                       
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
