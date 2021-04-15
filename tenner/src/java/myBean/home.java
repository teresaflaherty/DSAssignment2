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
                    // get data out - note: index starts at 1 !!!!
                    job = new job(result.getInt("JobID"),
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
                
                job job;
                while (result.next()) {
                    // get data out - note: index starts at 1 !!!!
                    job = new job(result.getInt("JobID"),
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
        for (job searchedjob : JobsList) {
            html_output += "<div class=\"col\">\n"
                    + "                <div class=\"gig_card\">\n"
                    + "                    <div class=\"gig_card_title\">\n"
                    + "                        " + searchedjob.getTitle() + "\n"
                    + "                    </div>\n"
                    + "                    <a class=\"view_gig\">View Gig</a>\n"
                    + "                </div>\n"
                    + "            </div>";
        }

        return html_output;
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
                    // get data out - note: index starts at 1 !!!!
                    job = new job(result.getInt("JobID"),
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
    public String allJobs() {
        ArrayList<job> JobsList = new ArrayList<>();
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
                    // get data out - note: index starts at 1 !!!!
                    job = new job(result.getInt("JobID"),
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
        for (job searchedjob : JobsList) {
            html_output += "<div class=\"col\">\n"
                    + "                <div class=\"gig_card\">\n"
                    + "                    <div class=\"gig_card_title\">\n"
                    + "                        " + searchedjob.getTitle() + "\n"
                    + "                    </div>\n"
                    + "                    <a class=\"view_gig\">View Gig</a>\n"
                    + "                </div>\n"
                    + "            </div>";
        }
        System.out.println(JobsList);
        return html_output;
    }

    public void searchStringChanged(ValueChangeEvent vce) {
        searchjobterm = vce.getNewValue().toString();
        search();
    }

}
