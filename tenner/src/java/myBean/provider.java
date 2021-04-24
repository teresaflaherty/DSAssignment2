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
    
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    private int id, userId;
    
    public provider(int id, int userId){
        this.id=id;
        this.userId=userId;
    }

    public provider(){
        
    }
    
    
    //Getters
    public int getId(){
        return id;
    }
        
    public int getUserId(){
        return userId;
    }
    
    
    //Setters
    public void setId(int id){
        this.id = id;
    }
        
    public void setUserId(int userId){
        this.userId = userId;
    }
    

    public void addJob(String title, String keywords, String description, int payment, int jobstatus, int providerID){
        try {
            Connection connect = null;
            Statement stmt = null; 
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                String query = "INSERT INTO JobDescriptions (Title,Keywords,Description,PaymentOffer,JobStatus,ProviderID) "
                        +"VALUES(?,?,?,?,?,?)";

                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, title);
                pst.setString(2, keywords);
                pst.setString(3, description);
                pst.setInt(4, payment);
                pst.setInt(5, jobstatus);
                pst.setInt(6, providerID);
                
                pst.executeUpdate();
                                       
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
    
    }

    
    // Deletes all FreelancerOffers for specified job and assigns freelancer to the job
    public String acceptOffer(int jobID, int freelancerID){
        System.out.println("got here lol" + jobID + freelancerID);
        try {
            Connection connect = null;
            Statement stmt = null;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                // Delete all offers for this job from FreelancerOffers table
                String query = "DELETE FROM FreelancerOffers where JobID  = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                //execute the queries
                pst.executeUpdate();

                // Change the job status to 1 (in progress), assign the freelancer to the job
                query = "UPDATE JobDescriptions SET JobStatus = 1, FreelancerID = ? WHERE JobID = ?";
                pst = connect.prepareStatement(query);
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
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        } 
        return "applicantapproved";
    }

    public String markDone(int jobID){
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();

                // Prepare a query to insert values into JOBDescriptions table
                String query = "UPDATE JobDescriptions SET JobStatus = 2 WHERE JobID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                pst.executeUpdate();
                
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID = " + jobID);
                int freelancerID = 0;
                int newBalance = 0;
                while (result.next()) {
                    freelancerID = result.getInt("FreelancerID");
                    newBalance = result.getInt("PaymentOffer");
                }
                
                result = stmt.executeQuery("SELECT * FROM Freelancers WHERE FreelancerID = " + freelancerID);
                while (result.next()) {
                    newBalance += result.getInt("Balance");
                }
                
                // Prepare a query to insert values into JOBDescriptions table
                query = "UPDATE Freelancers SET Balance = ? WHERE FreelancerID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, newBalance);
                pst.setInt(2, freelancerID);
                pst.executeUpdate();
                
                return "jobcomplete";

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        } 
        return null;
    }
}
