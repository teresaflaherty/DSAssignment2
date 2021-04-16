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
    public String search() {
        ArrayList<Job> JobsList = new ArrayList<>();
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
                
                Job job;
                while (result.next()) {
                    // get data out - note: index starts at 1 !!!!
                    job = new Job(result.getInt("JobID"),
                            result.getString("title"),
                            result.getString("keywords"),
                            result.getString("description"),
                            result.getInt("paymentoffer"),
                            result.getInt("Jobstatus"),
                            result.getInt("providerId"),
                            result.getInt("freelancerId"));

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
                
                Job job;
                while (result.next()) {
                    // get data out - note: index starts at 1 !!!!
                    job = new Job(result.getInt("JobID"),
                            result.getString("title"),
                            result.getString("keywords"),
                            result.getString("description"),
                            result.getInt("paymentoffer"),
                            result.getInt("Jobstatus"),
                            result.getInt("providerId"),
                            result.getInt("freelancerId"));

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
        String html_output = "";
        for (Job searchedjob : JobsList) {
             html_output += "<div class=\"col\">\n"
                    + "                <div class=\"gig_card\">\n"
                    + "                    <div class=\"gig_card_title\">\n"
                    + "                        " + searchedjob.getTitle() + "\n"
                    + "                    </div>\n"
                    + "                    <h:commandLink class=\"view_gig\" action='#{home.openGig("+searchedjob.getId()+")}'>View Gig</h:commandLink\n"
                    + "                </div>\n"
                    + "            </div>";
        }

        return html_output;
    }

//    RETURNS THE JOB DESCRIPTION OF A SELECTED GIG OR JOB WHEN PERSON CLICKS VIEW
    public ArrayList<Job> getJobDescription(int jobid) {
        ArrayList<Job> JobsList = new ArrayList<>();

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

                Job job;
                while (result.next()) {
                    // get data out - note: index starts at 1 !!!!
                    job = new Job(result.getInt("JobID"),
                            result.getString("title"),
                            result.getString("keyword"),
                            result.getString("description"),
                            result.getInt("paymentoffer"),
                            result.getInt("Jobstatus"),
                            result.getInt("providerId"),
                            result.getInt("freelancerId"));

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
    public ArrayList<Job> allJobs() {
        ArrayList<Job> JobsList = new ArrayList<>();
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
                Job job;
                while (result.next()) {
                    // get data out - note: index starts at 1 !!!!
                    job = new Job(result.getInt("JobID"),
                            result.getString("title"),
                            result.getString("keywords"),
                            result.getString("description"),
                            result.getInt("paymentoffer"),
                            result.getInt("Jobstatus"),
                            result.getInt("providerId"),
                            result.getInt("freelancerId"));
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
        return JobsList;
    }
    

 

    public void searchStringChanged(ValueChangeEvent vce) {
        searchjobterm = vce.getNewValue().toString();
        search();
    }

}
