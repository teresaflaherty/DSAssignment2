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
import java.util.ArrayList;

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
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                //Prepare a query to insert values into Athlete Coaches table
                String query = "DELETE FROM FreelancerOffers where JobID  = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, id);
                //execute the queries
                pst.executeUpdate();

                query = "DELETE FROM JobDescriptions where JobID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);
                pst.setInt(1, id);

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
        
        if(!admin) {
            return "jobwithdrawn";
        }
        return null;
    }
    
    public ArrayList<freelancer> getOffers() {
        ArrayList<freelancer> OffersList = new ArrayList<>();
        ArrayList<Integer> FreelancerIDList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                result = stmt.executeQuery("SELECT * FROM FreelancerOffers WHERE JobID = " + id);
                while (result.next()) {
                    FreelancerIDList.add(result.getInt("FreelancerID"));
                }
                
                for(int i = 0; i < FreelancerIDList.size(); i++) {
                    result = stmt.executeQuery("SELECT * FROM Freelancers WHERE FreelancerID = " + FreelancerIDList.get(i));
                    freelancer freelancer;
                    while (result.next()) {
                        freelancer= new freelancer();
                        freelancer.setId(result.getInt("FreelancerID"));
                        freelancer.setUserId(result.getInt("UserID"));
                        freelancer.setBalance(result.getInt("Balance"));
                        freelancer.setBio(result.getString("Message"));
                        freelancer.setSkills(result.getString("Skills"));

                        //Add values to list
                        OffersList.add(freelancer);
                    }
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
        return OffersList;
    }
}
