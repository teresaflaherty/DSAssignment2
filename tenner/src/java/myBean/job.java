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
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "job")
@SessionScoped
public class job implements Serializable {
    // Database Access Variables
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    // Object Variables
    private String title,keywords,description;
    private int id,payment,jobstatus,providerId,freelancerId;

    
    // Constructors
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
    /**
     * Get the value of the Job's ID
     *
     * @return value of id
     */
    public int getId(){
        return id;
    }
        
    
    /**
     * Get the value of the Job's Title
     *
     * @return value of title
     */
    public String getTitle(){
        return title;
    }
    
    
    /**
     * Get the value of the Job's Keywords
     *
     * @return value of keywords
     */
    public String getKeywords(){
        return keywords;
    }
    
    
    /**
     * Get the value of the Job's Description
     *
     * @return value of description
     */
    public String getDescription(){
        return description;
    }
      
    
    /**
     * Get the value of the Job's Payment Offer
     *
     * @return value of payment
     */
    public int getPayment(){
        return payment;
    }
    
    
    /**
     * Get the value of the Job's Status
     *
     * @return value of jobstatus
     */
    public int getJobstatus(){
        return jobstatus;
    }
    
    
    /**
     * Get the value of the Job's ProviderID
     *
     * @return value of providerId
     */
    public int getProviderId(){
        return providerId;
    }
    
    
    /**
     * Get the value of the Job's FreelancerID
     *
     * @return value of freelancerId
     */
    public int getFreelancerId(){
        return freelancerId;
    } 

    //Setters
    /**
     * Set the value of the Job's ID
     *
     * @param id the new JobID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    /**
     * Set the value of the Job's Title
     *
     * @param title the new Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    
    /**
     * Set the value of the Job's Keywords
     *
     * @param keywords the new Keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    
    /**
     * Set the value of the Job's Description
     *
     * @param description the new Job Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    /**
     * Set the value of the Job's Payment Offer
     *
     * @param payment the new Payment Offer Amount
     */
    public void setPayment(int payment) {
        this.payment = payment;
    }

    
    /**
     * Set the value of the Job's Status
     *
     * @param jobstatus the new Status
     */
    public void setJobstatus(int jobstatus) {
        this.jobstatus = jobstatus;
    }

    
    /**
     * Set the value of the Job's ProviderID
     *
     * @param providerId the new ProviderID
     */
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    
    /**
     * Set the value of the Job's FreelancerID
     *
     * @param freelancerId the new FreelancerID
     */
    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
    } 
    
    
    /**
     * Remove a Job from the database
     *
     * @param admin Whether or not an Administrator is the one deleting the job (affects return)
     * 
     * @return the next page to navigate to (jobwithdrawn for Provider, null for Administrator)
     */
    public String removeJob(boolean admin){
        try {
            Connection connect = null;
            Statement stmt = null;
            Statement stmt2 = null;
            
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                //Prepare and execute a query to delete all FreelancerOffers tied to the Job
                String query = "DELETE FROM FreelancerOffers where JobID  = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, id);
                pst.executeUpdate();

                //Prepare and execute a query to delete the Job
                query = "DELETE FROM JobDescriptions where JobID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, id);
                pst.executeUpdate();
                
                


                // Prepare and execute a query to insert new Log into Logging Job table
                query = "INSERT INTO  LoggingJob(JobID,ProviderID,FreelancerId,JobStatus)"
                        + "VALUES(?, ?, ?, ?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, id);
                pst.setInt(2, providerId);
                pst.setInt(3, freelancerId);
                pst.setInt(4, 3);
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
                //sql.printStackTrace();
                System.out.println(sql.getMessage());
                System.out.println(sql.getSQLState());
        }
        // Redirect if Provider removing Job, do not redirect if Administrator
        if(!admin) {
            return "jobwithdrawn";
        }
        return null;
    }
    
    
    /**
     * Get all Freelancers who have offered to do this Job
     * 
     * @return the list of Freelancers who have offered
     */
    public ArrayList<freelancer> getOffers() {
        // The list of Freelancers who have offered
        ArrayList<freelancer> OffersList = new ArrayList<>();
        // The list of FreelancerIDs, used to access their information
        ArrayList<Integer> FreelancerIDList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get all FreelancerOffers tied to the Job's ID
                result = stmt.executeQuery("SELECT * FROM FreelancerOffers WHERE JobID = " + id);
                
                // Add the IDs of the returned Freelancers to the FreelancerIDList
                while (result.next()) {
                    FreelancerIDList.add(result.getInt("FreelancerID"));
                }
                
                // Loop through each FreelancerID
                for(int i = 0; i < FreelancerIDList.size(); i++) {
                    // Execute a query to get the Freelancer tied to each FreelancerID
                    result = stmt.executeQuery("SELECT * FROM Freelancers WHERE FreelancerID = " + FreelancerIDList.get(i));
                    
                    // Add the returned Freelancer to the OffersList
                    freelancer freelancer;
                    while (result.next()) {
                        freelancer= new freelancer();
                        freelancer.setId(result.getInt("FreelancerID"));
                        freelancer.setUserId(result.getInt("UserID"));
                        freelancer.setBalance(result.getInt("Balance"));
                        freelancer.setBio(result.getString("Message"));
                        freelancer.setSkills(result.getString("Skills"));

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
        // Deal with any potential exceptions
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        // Return the list of Freelancers who have offered to do the Job
        return OffersList;
    }
}
