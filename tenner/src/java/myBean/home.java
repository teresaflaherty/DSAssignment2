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

import javax.faces.event.ValueChangeEvent;

@Named(value = "home")
@RequestScoped
public class home {

    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    private String searchjobterm;

    public String getSearchjobterm() {
        return searchjobterm;
    }

    public void setSearchjobterm(String searchjobterm) {
        this.searchjobterm = searchjobterm;
    }

//    RETURNS ALL JOBS WITH KEYWORDs OR ID THAT WAS SEARCHED
    public ArrayList<job> search() {
        ArrayList<job> JobsList = new ArrayList<>();
        // need two nested try-blocks, as code in finally may throw exception

        try {
            Connection connect = null;
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
                if (searchjobterm.isEmpty()) {
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions");
                } else {
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE jobid=" + id);
                }
                
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
                if (Keywords == null || (Keywords.length()==0 || Keywords.isEmpty())) {
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions");
                } else {
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE keywords LIKE '%" + Keywords + "%'");
                }
                
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

//    RETURNS THE JOB DESCRIPTION OF A SELECTED GIG OR JOB WHEN PERSON CLICKS VIEW
    public ArrayList<job> getJobDescription(int jobid) {
        ArrayList<job> JobsList = new ArrayList<>();

        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n";
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE Jobid=" + jobid);

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

//        RETURNS ALL JOBS, TO BE USED WHEN HOME.HTML IS OPENED
    public ArrayList<job> allJobs() {
        String searched=getSearchjobterm();
         
        ArrayList<job> JobsList = new ArrayList<>();
        if(searched==null){
        // need two nested try-blocks, as code in finally may throw exception
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n";
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                // obtain statement from connection
                stmt = connect.createStatement();
                // execute statement - note DB needs to perform full processing
                // on calling executeQuery
                result = stmt.executeQuery("SELECT * FROM JOBDescriptions");
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
     
        System.out.println(JobsList);
               }
         else{
             
             JobsList=search();
         }
        return JobsList;
    }
    

 

    public void searchStringChanged(ValueChangeEvent vce) {
        searchjobterm = vce.getNewValue().toString();
        allJobs();
    }
    
    


}