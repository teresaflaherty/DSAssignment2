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
import javax.enterprise.context.SessionScoped;


@Named(value = "home")
@SessionScoped
public class home implements Serializable{

    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    private String searchjobterm;
    private String searchtitle = "All Gigs";

    public String getSearchtitle(String in) {
        if( in == null || in.isEmpty())
        {
            searchtitle = "All Gigs";
        }
        else
        {
            searchtitle = "Search results for \""+ in +"\" "; 
        }
       return searchtitle;
    }

     
    public void setSearchtitle(String searchtitle) {
        this.searchtitle = searchtitle;
    }
    
    
    public String getSearchjobterm() {
        return searchjobterm;
    }

    
    public void setSearchjobterm(String searchjobterm) {
        this.searchjobterm = searchjobterm;
    }

    
    // Returns all jobs with keywords or ID that was searched
    public ArrayList<job> search() {
        
        ArrayList<job> JobsList = new ArrayList<>();
        if(searchjobterm==null){
            JobsList=getAllJobs(0);
            return JobsList;
        }
        else{
        // need two nested try-blocks, as code in finally may throw exception
        try {
            Connection connect;
            Statement stmt = null;
            ResultSet result;

            // connect to db - make sure derbyclient.jar is added to your project
            connect = DriverManager.getConnection(URL, USER, PASSWD);
            try {

                int id;
                id = Integer.parseInt(searchjobterm);
                System.out.println(id);

                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
               
                    result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE jobid=" + id);
                    
            
                
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
            } catch (Exception e) {
                String Keywords = searchjobterm;
                System.out.println(Keywords);

                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
               
              
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE keywords LIKE '%" + Keywords + "%'");
                    
               
                
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        
        return JobsList;
        }
    }

    
    // Returns a single Job Description
    public ArrayList<job> getJobDescription(int jobID) {
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobID = " + jobID);

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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return JobsList;
    }

    
    // Returns a list of all jobs
    public ArrayList<job> getAllJobs(int jobStatus) {
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM JobDescriptions WHERE JobStatus = "+ jobStatus);
                // process results
                // while there are results
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return JobsList;
    }
    
    
    // Returns a list of all freelancers
    public ArrayList<freelancer> getAllFreelancers(int id) {
        ArrayList<freelancer> FreelancersList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM Freelancers");
                // process results
                // while there are results
                freelancer freelancer;
                while (result.next()) {
                    
                    freelancer= new freelancer();
                    freelancer.setId(result.getInt("FreelancerID"));
                    freelancer.setUserId(result.getInt("UserID"));
                    freelancer.setBalance(result.getInt("Balance"));
                    freelancer.setBio(result.getString("Message"));
                    freelancer.setSkills(result.getString("Skills"));

                    //Add values to list
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return FreelancersList;
    }

    
    // Returns a list of all providers
    public ArrayList<provider> getAllProviders() {
        ArrayList<provider> ProvidersList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM Providers");
                // process results
                // while there are results
                provider provider;
                while (result.next()) {
                    
                    provider= new provider();
                    provider.setId(result.getInt("ProviderID"));
                    provider.setUserId(result.getInt("UserID"));

                    //Add values to list
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return ProvidersList;
    }
    
    
    // Returns a list of all freelancers
    public ArrayList<admin> getAllAdministrators() {
        ArrayList<admin> AdministratorsList = new ArrayList<>();
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM Administrators");
                // process results
                // while there are results
                admin admin;
                while (result.next()) {
                    
                    admin = new admin();
                    admin.setId(result.getInt("AdministratorID"));
                    admin.setUserId(result.getInt("UserID"));

                    //Add values to list
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return AdministratorsList;
    }
    
    public String getUserName(int userID) {
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM Users WHERE UserID = " + userID);
                
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

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        return null;
    }
}