/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;

@Named(value = "reg")
@RequestScoped

public class reg {
    
    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    private String name;
    private String password;
    private String email;
    
    public void addjob(String name, String password,String email){
        
              try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n"; 
                try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                 //Prepare a query to insert values into Athlete Coaches table
                String query = "INSERT INTO Users (Name,Email,Password) VALUES(?,?,?)";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                
                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1,name);
                pst.setString(2,password);
                pst.setString(3,email);

                
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
