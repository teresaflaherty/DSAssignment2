/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import java.io.Serializable;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import javax.ejb.Stateless;

/**
 *
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "home")
@Stateless
public class home implements Serializable{
    // Database Access Variables
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    // Search Variables
    private String searchjobterm;
    private String searchtitle = "All Gigs";

    //Getters
    /**
     * Get the value of the Search Title (Visible at the top of the home page)
     * 
     * @param in the current term being searched
     *
     * @return value of searchtitle
     */
    public String getSearchtitle(String in) {
        if( in == null || in.isEmpty())
        {
            searchtitle = "All Gigs";
        }
        else
        {
            searchtitle = "Search Results for \""+ in +"\" "; 
        }
       return searchtitle;
    }
    
    
    /**
     * Get the value of the Term currently being searched
     *
     * @return value of searchjobterm
     */
    public String getSearchjobterm() {
        return searchjobterm;
    }

    
    // Setters
    /**
     * Set the value of the Search Title
     *
     * @param searchtitle the new title of the search page
     */
    public void setSearchtitle(String searchtitle) {
        this.searchtitle = searchtitle;
    }
    
    
    /**
     * Set the value of the Search Title
     *
     * @param searchjobterm the new term being searched
     */
    public void setSearchjobterm(String searchjobterm) {
        this.searchjobterm = searchjobterm;
    }

    
    /**
     * Search all Open Jobs for a Job that has the searched JobID or has matching keywords
     *
     * @return All Jobs relevant to the search term
     */
    public ArrayList<job> search() {
        // The list of Jobs the search returns
        ArrayList<job> JobsList = new ArrayList<>();
        
        // If the search is empty add all Open Jobs to the list
        if(searchjobterm == null || searchjobterm.isEmpty()){
            JobsList=getAllJobs(0);
        }
        else{
            try {
                Connection connect;
                Statement stmt = null;
                ResultSet result;

                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                try {
                    // See if search term matches a JobID first
                    int id;
                    id = Integer.parseInt(searchjobterm);
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID=" + id);

                    // If a Job is returned, add it to the JobsList
                    job job;
                    while (result.next()) {

                        job= new job();
                        job.setId(result.getInt("JobID"));
                        job.setTitle(result.getString("Title"));
                        job.setKeywords(result.getString("Keywords"));
                        job.setDescription(result.getString("Description"));
                        job.setPayment(result.getInt("PaymentOffer"));
                        job.setJobstatus(result.getInt("JobStatus"));
                        job.setProviderId(result.getInt("ProviderID"));
                        job.setFreelancerId(result.getInt("FreelancerID"));

                        JobsList.add(job);
                    }
                // If the Search term does not match JobID, exception will be caught
                // This means the keywords will only be searched if search is not a JobID
                } catch (Exception e) {
                    // Store the searchjobterm as a String of keywords
                    String Keywords = searchjobterm;
                    
                    // Execute a query to find any Job Descriptions with keywords found in the searchjobterm
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE Keywords LIKE '%" + Keywords + "%'");
                    
                    // Add any found jobs to the JobsList
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
                        JobsList.add(job);
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
        }
        // Return the Jobs found to match the searchjobterm
        return JobsList;
    }
    
    /**
     * Return a single Job Description using its JobID in a list form to be used in a UI Repeat
     * 
     * @param jobID the ID of the Job being requested
     *
     * @return The Job Description matching the given ID
     */
    public ArrayList<job> getJobDescription(int jobID) {
        // The list the Job will be stored in
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();

                // Execute a query to get the Job matching the given JobID
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID = " + jobID);

                // Add the Job from the results to the list
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
                    
                    JobsList.add(job);
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
        // Return the Job in list format
        return JobsList;
    }
    
    
    /**
     * Return all Jobs assigned to a given Provider or Freelancer with a given Status
     * 
     * @param type the type of User whose assigned jobs is being requested
     * @param roleID the ProviderID/FreelancerID of the User
     * @param status the Status of Jobs to be returned
     *
     * @return All Job Descriptions assigned to the User with the given Status
     */
    public ArrayList<job> getAssignedJobs(String type, int roleID, int status) {
        // The list of Jobs to be added to and returned
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                // Execute a query to get all Jobs matching the Status and roleID
                // Query different depending on User Type
                if(type.equals("Freelancer")) {
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobStatus = "+ status +" AND FreelancerID = "+ roleID);
                }
                else {
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobStatus = "+ status +" AND ProviderID = "+ roleID);
                }
                
                // Add all jobs from the result to the JobsList
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
                    
                    JobsList.add(job);
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
        // Return the list of Jobs Assigned to the User with the correct Status
        return JobsList;   
    }
    
    
    /**
     * Get all Job Descriptions currently in the database for a certain Status
     * 
     * @param jobStatus the Status of the Jobs being requested
     *
     * @return All Jobs
     */
    public ArrayList<job> getAllJobs(int jobStatus) {
        // The list of Jobs to be added to and returned
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get all Jobs with the given Status
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobStatus = "+ jobStatus);
                
                // Add all returned Jobs to the JobsList
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
                    
                    JobsList.add(job);
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
        // Return the list of all Jobs
        return JobsList;
    }
    
    
    /**
     * Get all Freelancers currently in the database
     *
     * @return All Freelancers
     */
    public ArrayList<freelancer> getAllFreelancers() {
        // The list of Freelancers to be added to and returned
        ArrayList<freelancer> FreelancersList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get all Freelancers
                result = stmt.executeQuery("SELECT * FROM Freelancers");
                
                // Add all returned Freelancers to the FreelancersList
                freelancer freelancer;
                while (result.next()) {
                    
                    freelancer= new freelancer();
                    freelancer.setId(result.getInt("FreelancerID"));
                    freelancer.setUserId(result.getInt("UserID"));
                    freelancer.setBalance(result.getInt("Balance"));
                    freelancer.setBio(result.getString("Message"));
                    freelancer.setSkills(result.getString("Skills"));

                    FreelancersList.add(freelancer);
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
        // Return the list of all Freelancers
        return FreelancersList;
    }

    
    /**
     * Get all Providers currently in the database
     *
     * @return All Providers
     */
    public ArrayList<provider> getAllProviders() {
        // The list of Providers to be added to and returned
        ArrayList<provider> ProvidersList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get all Providers
                result = stmt.executeQuery("SELECT * FROM Providers");
                
                // Add all returned Providers to the ProvidersList
                provider provider;
                while (result.next()) {
                    
                    provider= new provider();
                    provider.setId(result.getInt("ProviderID"));
                    provider.setUserId(result.getInt("UserID"));
                    
                    ProvidersList.add(provider);
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
        // Return the list of all Providers
        return ProvidersList;
    }
    
    
    /**
     * Get all Administrators currently in the database
     *
     * @return All Administrators
     */
    public ArrayList<admin> getAllAdministrators() {
        // The list of Administrators to be added to and returned
        ArrayList<admin> AdministratorsList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get all Administrators
                result = stmt.executeQuery("SELECT * FROM Administrators");
                
                // Add all returned Administrators to the AdministratorsList
                admin admin;
                while (result.next()) {
                    
                    admin = new admin();
                    admin.setId(result.getInt("AdministratorID"));
                    admin.setUserId(result.getInt("UserID"));
                    
                    AdministratorsList.add(admin);
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
        return AdministratorsList;
    }
    
    
    /**
     * Get the Name of any User from their UserID
     * 
     * @param userID the UserID of the User whose Name is being requested
     *
     * @return The Name of the User with the given UserID
     */
    public String getUserName(int userID) {
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Execute a query to get the User matching that UserID
                result = stmt.executeQuery("SELECT * FROM Users WHERE UserID = " + userID);
                
                // If a User is found, return their Name
                if (result.next()) {
                   return result.getString("Name");
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
        // Return null if anything goes wrong
        return null;
    }
    
    /**
     * Get the FreelancerID, ProviderID, or AdministratorID of a User from their Email
     * 
     * @param type the Type of the User (Freelancer or Provider)
     * @param email the Email of the User
     *
     * @return The FreelancerID/ProviderID/AdministratorID of the User whose Email is given
     */
    public int getRoleID(String type, String email) {
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Prepare and execute a query to get the User matching the given Email
                String query = "SELECT * FROM Users WHERE Email = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, email);
                result = pst.executeQuery();
                
                // Retrieve the UserID from the results and store it to find their RoleID
                int userID = -1;
                if (result.next()) {
                   userID = result.getInt("UserID");
                }
                
                // Execute the query to a different table depending on the User's Type
                switch(type) {
                    case "Freelancer":
                        // Get and return the User's FreelancerID
                        result = stmt.executeQuery("SELECT * FROM Freelancers WHERE UserID = " + userID);
                        if (result.next()) {
                           return result.getInt("FreelancerID");
                        }
                        break;
                    case "Provider":
                        // Get and return the User's ProviderID
                        result = stmt.executeQuery("SELECT * FROM Providers WHERE UserID = " + userID);
                        if (result.next()) {
                           return result.getInt("ProviderID");
                        }
                        break;
                    case "Administrator":
                        // Get and return the User's AdministratorID
                        result = stmt.executeQuery("SELECT * FROM Administrators WHERE UserID = " + userID);
                        if (result.next()) {
                           return result.getInt("AdministratorID");
                        }
                        break;
                    default:
                        break;
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
        // Return ID of 0 if something goes wrong
        return 0;
    }
    
    
    /**
     * Checks if User has access to a certain page (based on role), redirects if not
     * 
     * @param page the page the User is trying to access
     * @param email the Email of the User
     *
     * @return The page if they have access, the page to redirect to if not
     */
    public String checkAccess(String page, String email) {
        // All of the pages broken into which roles should have access to them
        // Note: login and register are not here because all users can access them
        // Also, Admin can only access adminaccount page so no list was made for them
        String[] freelancerPages = {"applicationcomplete", "home", "job", "myaccount"};
        String[] providerPages = {"applicantapproved", "jobcomplete", "jobdetails", "jobwithdrawn", "provideraccount"};
        String[] administratorPages = {"adminaccount"};
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                stmt = connect.createStatement();
                
                // Prepare and execute a query to get the User matching the given Email
                String query = "SELECT * FROM Users WHERE Email = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, email);
                result = pst.executeQuery();
                
                // Retrieve the UserID from the results and store it to find their RoleID
                int userID = -1;
                if (result.next()) {
                   userID = result.getInt("UserID");
                }
                
                // Check if the User is a Freelancer
                result = stmt.executeQuery("SELECT * FROM Freelancers WHERE UserID = " + userID);
                // If they are
                if (result.next()) {
                    // Loop through all Freelancer pages
                    for(String s : freelancerPages) {
                        // Grant them access if in list of freelancerPages
                        if(s.equals(page)){
                            return page;
                        }
                    }
                    // If page can't be found in list it means they don't have access,
                    // Redirect them to the home page
                    return "home";
                }
                
                // Check if the User is a Provider
                result = stmt.executeQuery("SELECT * FROM Providers WHERE UserID = " + userID);
                // If they are
                if (result.next()) {
                    // Loop through all Provider pages
                    for(String s : providerPages) {
                        // Grant them access if in list of providerPages
                        if(s.equals(page)){
                            return page;
                        }
                    }
                    // If page can't be found in list it means they don't have access,
                    // Redirect them to their Provider Account page
                    return "provideraccount";
                }
                
                
                // Check if the User is a Administrator
                result = stmt.executeQuery("SELECT * FROM Administrators WHERE UserID = " + userID);
                // If they are
                if (result.next()) {
                    // Loop through all Provider pages
                    for(String s : administratorPages) {
                        // Grant them access if in list of administratorPages
                        if(s.equals(page)){
                            return page;
                        }
                    }
                    // If page can't be found in list it means they don't have access,
                    // Redirect them to their Administrator Account page
                    return "adminaccount";
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
        // Return the login page if something goes wrong
        return "login";
    }
}