/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;

@Named(value = "freelancer")
@RequestScoped

/**
 *
 * @author my pc
 */
public class freelancer {
    
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    private int id, userId, balance;
    private String bio, skills;
    
    public freelancer(int id, int userId, int balance, String bio, String skills){
        this.id=id;
        this.userId=userId;
        this.balance=balance;
        this.bio=bio;
        this.skills=skills;
    }

    public freelancer(){
        
    }
    
    
    //Getters
    public int getId(){
        return id;
    }
        
    public int getUserId(){
        return userId;
    }
    
    public int getBalance(){
        return balance;
    }
    
    public String getBio(){
        return bio;
    }
      
    public String getSkills(){
        return skills;
    }

    
    //Setters
    public void setId(int id){
        this.id = id;
    }
        
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public void setBalance(int balance){
        this.balance = balance;
    }
    
    public void setBio(String bio){
        this.bio = bio;
    }
      
    public void setSkills(String skills){
        this.skills = skills;
    }
    
    public void apply(int job_id, String email){

    int freelancerid=getUSERID(email);
      try {
                Connection connect = null;
                Statement stmt = null;
                ResultSet result;
                String data = "Results:\n"; 
                    try {
                    // connect to db - make sure derbyclient.jar is added to your project
                    connect = DriverManager.getConnection(URL, USER, PASSWD);

                     //Prepare a query to insert values into JOBDescriptions table
                    
                  String query = "INSERT INTO FREELANCEROFFERS(JOBID,FREELANCERID)"+
                          " VALUES(?,?)";

                   //Connect to the database with queries
                   PreparedStatement pst = connect.prepareStatement(query);


                   //Get text entered into textfields
                   //put them into the corresponding queries
                   pst.setInt(1, job_id);
                   pst.setInt(2, freelancerid);
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
    
     public int getUSERID(String email){

    int userID = -1;
    int personid=0;
    try {
               Connection connect = null;
               Statement stmt = null;
               Statement stmt2= null;
               ResultSet result;
               ResultSet result2;
               try {
                   // connect to db - make sure derbyclient.jar is added to your project
                   connect = DriverManager.getConnection(URL, USER, PASSWD);

                   // Retrieve UserID from type and ID
                   String query = "SELECT * FROM Users WHERE Email = ?";

                   //Connect to the database with queries
                   PreparedStatement pst = connect.prepareStatement(query);

                   //Get text entered into textfields
                   //put them into the corresponding queries
                   pst.setString(1, email);

                   //execute the queries
                   result = pst.executeQuery();


                   while (result.next()) {
                       userID = result.getInt(1);
                   }

                    String query2 = "SELECT * FROM Freelancers WHERE USERID = ?";
                                       //Connect to the database with queries
                   PreparedStatement pst2 = connect.prepareStatement(query2);
                                      //Get text entered into textfields
                   //put them into the corresponding queries
                   pst2.setInt(1, userID);
                                      //execute the queries
                   result2 = pst2.executeQuery();


                   while (result2.next()) {
                       personid = result2.getInt(1);
                   }

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

    return personid;
  }
    
}
