/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author my pc
 */
@Named(value = "job")
@SessionScoped
public class job implements Serializable {

    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    private String title,keywords,description;
    private int id,payment,jobstatus,providerId,freelancerId;
   
//    BEANS MUST HAVE AN EMPTY CONSTRUCTOR WITH NO PARAMETERS

 
    public job(int id,String title,String keywords,String description,int payment,int jobstatus,int providerId, int freelancerId){
        this.title=title;
        this.keywords=keywords;
        this.description=description;
        this.id=id;
        this.payment=payment;
        this.jobstatus=jobstatus;
        this.providerId=providerId;
        this.freelancerId=freelancerId;
    }

    public job(){
        
    }
   
    
    //Getters
    public int getId(){
        return id;
    }
        
    public String getTitle(){
        return title;
    }
    public String getKeywords(){
        return keywords;
    }
    
    public String getDescription(){
        return description;
    }
      
    public int getPayment(){
        return payment;
    }
    
    public int getJobstatus(){
        return jobstatus;
    }
    
    public int getProviderId(){
        return providerId;
    }
    
    public int getFreelancerId(){
        return freelancerId;
    } 

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public void setJobstatus(int jobstatus) {
        this.jobstatus = jobstatus;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
    } 
    
    public String removeJob(boolean admin){
        try {
            Connection connect = null;
            Statement stmt = null;
            Statement stmt2 = null;
            ResultSet result;
            ResultSet result2;
            String data = "Results:\n"; 
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                //Prepare a query to insert values into Athlete Coaches table
                String query = "DELETE FROM FREELANCEROFFERS where JOBID="+id;
                PreparedStatement pst = connect.prepareStatement(query);
                //execute the queries
                pst.executeUpdate();

                String query2 = "DELETE FROM JOBDescriptions where JOBID="+id;

                //Connect to the database with queries
                PreparedStatement pst2 = connect.prepareStatement(query2);

                //execute the queries
                pst2.executeUpdate();

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
        
        if(!admin) {
            return "jobwithdrawn";
        }
        return null;
    }
}
