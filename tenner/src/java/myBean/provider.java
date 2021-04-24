/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.sql.*;

/**
 *
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "provider")
@RequestScoped
public class provider {
    //Database Access Variables
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    // Object Variables
    private int id, userId;
    
    
    // Constructors
    public provider(int id, int userId){
        this.id=id;
        this.userId=userId;
    }

    public provider(){
        
    }
    
    
    //Getters
    /**
     * Get the value of the Provider's ID
     *
     * @return value of id
     */
    public int getId(){
        return id;
    }
    

    /**
     * Get the value of the Provider's UserID
     *
     * @return value of UserId
     */
    public int getUserId(){
        return userId;
    }
    
    
    //Setters
    /**
     * Set the value of the Provider's ID
     *
     * @param id the new ProviderID
     */
    public void setId(int id){
        this.id = id;
    }
        
    
    /**
     * Set the value of the Provider's UserID
     *
     * @param userId the new UserID
     */
    public void setUserId(int userId){
        this.userId = userId;
    }
    

    /**
     * Add a Job to the list of Job Descriptions
     *
     * @param title the Job title
     * @param keywords the keywords associated with the Job
     * @param description the description of the Job
     * @param payment the amount of money being offered for doing the job
     * @param providerID the ID of the Provider adding the job
     */
    public void addJob(String title, String keywords, String description, int payment, int providerID){
        try {
            Connection connect = null;
            Statement stmt = null; 
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                // Prepare and execute a query to add the Job to the JobDescriptions table
                String query = "INSERT INTO JobDescriptions (Title,Keywords,Description,PaymentOffer,JobStatus,ProviderID) "
                        +"VALUES(?,?,?,?,?,?)";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, title);
                pst.setString(2, keywords);
                pst.setString(3, description);
                pst.setInt(4, payment);
                pst.setInt(5, 0); // All new jobs should have Open status when created
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
        // Deal with any potential exceptions
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
    
    }

    
    /**
     * Accept a Freelancer's Offer to do a job
     *
     * @param jobID the ID of the Job the offer is being accepted for
     * @param freelancerID the ID of the Freelancer being accepted
     * 
     * @return the next page to navigate to (applicantapproved)
     */
    public String acceptOffer(int jobID, int freelancerID){
        System.out.println("got here lol" + jobID + freelancerID);
        try {
            Connection connect = null;
            Statement stmt = null;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                // Delete all offers for this job from FreelancerOffers table
                // Doing this removes the Freelancer's ability to revoke the offer once they have been accepted
                String query = "DELETE FROM FreelancerOffers where JobID  = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                pst.executeUpdate();

                // Change the job status to 1 (in progress), assign the freelancer to the job
                query = "UPDATE JobDescriptions SET JobStatus = 1, FreelancerID = ? WHERE JobID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);
                pst.setInt(2, jobID);
                pst.executeUpdate();
                
                // Navigate to the "applicantapproved" page
                return "applicantapproved";

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
        // Do not redirect if unsuccessful
        return null;
    }

    
    /**
     * Mark an In Progress Job as Complete
     *
     * @param jobID the ID of the Job being marked done
     * 
     * @return the next page to be navigated to (jobcomplete if successful)
     */
    public String markDone(int jobID){
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();

                // Prepare and execute a query to update the JobDescription Status
                String query = "UPDATE JobDescriptions SET JobStatus = 2 WHERE JobID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, jobID);
                pst.executeUpdate();
                
                // Prepare and execute a query to get the ID of the Freelancer doing the Job
                // and the Job's Payment Offer - this is to be added to their current balance
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID = " + jobID);
                int freelancerID = 0;
                int newBalance = 0;
                while (result.next()) {
                    freelancerID = result.getInt("FreelancerID");
                    newBalance = result.getInt("PaymentOffer");
                }
                
                // Get the Freelancer's current Balance and add it to the Payment Amount to get their new Balance
                result = stmt.executeQuery("SELECT * FROM Freelancers WHERE FreelancerID = " + freelancerID);
                while (result.next()) {
                    newBalance += result.getInt("Balance");
                }
                
                // Prepare and execute a query to update the Freelancer's Balance with the new amount
                query = "UPDATE Freelancers SET Balance = ? WHERE FreelancerID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, newBalance);
                pst.setInt(2, freelancerID);
                pst.executeUpdate();
                
                // Redirect to the "jobcomplete" page
                return "jobcomplete";

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
        // Do not redirect if unsuccessful
        return null;
    }
}
