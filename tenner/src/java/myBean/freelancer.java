/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;
import java.util.ArrayList;

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
    
    public int getBalance(int freelancerID) {
        try {
            Connection connect = null;
            Statement stmt = null;

            ResultSet result;

            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                String query = "SELECT * FROM FREELANCERS WHERE FreelancerID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);

                result = pst.executeQuery();

                while (result.next()) {
                    return result.getInt("Balance");
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
        return 0;
    }
        
    public String apply(int jobID, int freelancerID){
        try {
            Connection connect = null;
            Statement stmt = null;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                String query = "INSERT INTO FreelancerOffers(JobID, FreelancerID) VALUES(?, ?)";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                pst.setInt(2, freelancerID);
                pst.executeUpdate();
                
                return "applicationcomplete";

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
        return null;
    }
    
    
    public void withdrawApplication(int freelancerID, int jobID) {
        try {
            Connection connect = null;
            Statement stmt = null;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                System.out.println("JobID = "+jobID+"  FreelancerID = ");

                //Prepare a query to insert values into Athlete Coaches table
                String query = "DELETE FROM FreelancerOffers WHERE FreelancerID  = ? AND JobID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);
                pst.setInt(2, jobID);
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
    
    
    public ArrayList<job> getOffers(int freelancerID) {
        ArrayList<job> OffersList = new ArrayList<>();
        ArrayList<Integer> JobIDList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                result = stmt.executeQuery("SELECT * FROM FreelancerOffers WHERE FreelancerID = " + freelancerID);
                while (result.next()) {
                    JobIDList.add(result.getInt("JobID"));
                }
                
                for(int i = 0; i < JobIDList.size(); i++) {
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID = " + JobIDList.get(i));
                    job job;
                    while (result.next()) {
                        job= new job();
                        job.setId(result.getInt("JobID"));
                        job.setTitle(result.getString("title"));
                        job.setKeywords(result.getString("keywords"));
                        job.setDescription(result.getString("description"));
                        job.setPayment(result.getInt("paymentoffer"));
                        job.setJobstatus(result.getInt("Jobstatus"));
                        job.setProviderId(result.getInt("providerId"));
                        job.setFreelancerId(result.getInt("freelancerId"));

                        //Add values to list
                        OffersList.add(job);
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
