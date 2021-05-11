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

/**
 *
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "freelancer")
@RequestScoped
public class freelancer {
    //Database Access Variables
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    // Object Variables
    private int id, userId, balance;
    private String bio, skills;
    
    
    // Constructors
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
    /**
     * Get the value of the Freelancer's ID
     *
     * @return value of id
     */
    public int getId(){
        return id;
    }
    

    /**
     * Get the value of the Freelancer's UserID
     *
     * @return value of userId
     */
    public int getUserId(){
        return userId;
    }
    
    
    /**
     * Get the value of the Freelancer's Balance
     *
     * @return value of balance
     */
    public int getBalance(){
        return balance;
    }
    
    
    /**
     * Get the value of the Freelancer's Bio
     *
     * @return value of bio
     */
    public String getBio(){
        return bio;
    }
    
    
    /**
     * Get the value of the Freelancer's Skills
     *
     * @return value of skills
     */
    public String getSkills(){
        return skills;
    }

    
    //Setters
    /**
     * Set the value of the Freelancer's ID
     *
     * @param id the new FreelancerID
     */
    public void setId(int id){
        this.id = id;
    }
    

    /**
     * Set the value of the Freelancer's UserID
     *
     * @param userId the new UserID
     */
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    
    /**
     * Set the value of the Freelancer's Balance
     *
     * @param balance the new balance
     */
    public void setBalance(int balance){
        this.balance = balance;
    }
    
    
    /**
     * Set the value of the Freelancer's Bio
     *
     * @param bio the new bio
     */
    public void setBio(String bio){
        this.bio = bio;
    }
      
    
    /**
     * Set the value of the Freelancer's Skills
     *
     * @param skills the new skills
     */
    public void setSkills(String skills){
        this.skills = skills;
    }
    
    
    /**
     * Retrieve the current Freelancer's Balance
     * 
     * @param freelancerID the ID of the freelancer whose balance is requested
     *
     * @return the balance on record for the Freelancer
     */
    public int getCurrentBalance(int freelancerID) {
        try {
            Connection connect = null;
            Statement stmt = null;

            ResultSet result;

            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                // Prepare and execute a query to get the Freelancer's information
                String query = "SELECT * FROM FREELANCERS WHERE FreelancerID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);
                result = pst.executeQuery();

                // Retrieve and return the Balance from the result
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
        // Deal with any potential exceptions
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        // If something goes wrong, return 0
        return 0;
    }
    
    /**
     * Retrieve the current Freelancer's Balance
     * 
     * @param jobID the ID of the Job the Freelancer is applying for
     * @param freelancerID the ID of the freelancer applying
     *
     * @return the next page to navigate to, applicationcomplete if application successful
     */
    public String apply(int jobID, int freelancerID){
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                // Prepare and execute a query to add an Offer with the given IDs to the table
                String query = "INSERT INTO FreelancerOffers(JobID, FreelancerID) VALUES(?, ?)";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                pst.setInt(2, freelancerID);
                pst.executeUpdate();
                
                // obtain statement from connection
                stmt = connect.createStatement();
                
                // Retrieve the Job ID
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE jobID ="+ jobID);
                int jobId = 0;
                int ProviderID=0;
                while (result.next()) {
                    jobId = result.getInt("jobId");
                    ProviderID = result.getInt("ProviderID");
                }
                
                // Prepare and execute a query to insert new Log into Logging Job table
                query = "INSERT INTO  LoggingJob(JobID, ProviderID, freelancerID, JobStatus)"
                        + "VALUES(?, ?, ?, ?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, jobId);
                pst.setInt(2, ProviderID);
                pst.setInt(3, freelancerID);
                pst.setInt(4, 0);
                pst.executeUpdate();
                
                // Navigate to the "applicationcomplete" page
                return "applicationcomplete";
                
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }
        // Deal with any potential exceptions
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        // In the event of application failure do not redirect
        return null;
    }
    
    
    /**
     * Withdraw a Freelancer's Offer for a given job
     * 
     * @param freelancerID the ID of the freelancer withdrawing the offer
     * @param jobID the ID of job they are withdrawing from
     */
    public void withdrawApplication(int freelancerID, int jobID) {
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                //Prepare and execute query to delete the offer from the table
                String query = "DELETE FROM FreelancerOffers WHERE FreelancerID  = ? AND JobID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);
                pst.setInt(2, jobID);
                pst.executeUpdate();
                
                
                
                // obtain statement from connection
                stmt = connect.createStatement();
                
                // Retrieve the Job ID
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE jobID ="+ jobID);
                int jobId = 0;
                int ProviderID=0;
                while (result.next()) {
                    jobId = result.getInt("jobId");
                    ProviderID = result.getInt("ProviderID");
                }
                
                // Prepare and execute a query to insert new Log into Logging Job table
                query = "INSERT INTO  LoggingJob(JobID, ProviderID, freelancerID, JobStatus)"
                        + "VALUES(?, ?, ?, ?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, jobId);
                pst.setInt(2, ProviderID);
                pst.setInt(3, freelancerID);
                pst.setInt(4, 0);
                pst.executeUpdate();
                
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }

        // Deal with any potential exceptions
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
    }
    
    
    /**
     * Retrieve all Offers for a given Freelancer
     * 
     * @param freelancerID the ID of the Freelancer whose offers are needed
     * 
     * @return the list of jobs the Freelancer has offered to do
     */
    public ArrayList<job> getOffers(int freelancerID) {
        // List of offers and JobIDs
        ArrayList<job> OffersList = new ArrayList<>();
        ArrayList<Integer> JobIDList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to retrieve all Offers for the given Freelancer
                result = stmt.executeQuery("SELECT * FROM FreelancerOffers WHERE FreelancerID = " + freelancerID);
                
                // Store all JobIDs from the result into JobIDListt
                while (result.next()) {
                    JobIDList.add(result.getInt("JobID"));
                }
                
                // Loop through all JobIDs in JobIDList and add each job to OffersList
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
        // Deal with any potential exceptions
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        // Return the list of Jobs that the Freelancer has offered to do
        return OffersList;
    }
}
