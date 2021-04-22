package myBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


@Named(value = "db")
@RequestScoped
public class db {


    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";

    
    /**
     * Adds a table of Users to the database
     */
    public void addTableUsers() {

        String sql = "CREATE TABLE Users(UserID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Name VARCHAR(60), Email VARCHAR(100), Password CHAR(64))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) 
        {

            // execute statement 
            stmt.executeUpdate(sql);


            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
              System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a table of Providers to the database
     */
    public void addTableProviders() {

        String sql = "CREATE TABLE Providers(ProviderID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "UserID INTEGER, FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a new table of Freelancers to the database
     */
    public void addTableFreelancers() {

        String sql = "CREATE TABLE Freelancers(FreelancerID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Skills VARCHAR(500), Message VARCHAR(500), Balance INTEGER, "
                + "UserID INTEGER, FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }

    
    /**
     * Adds a table of Administrators to the database
     */
    public void addTableAdministrators() {

        String sql = "CREATE TABLE Administrators(AdministratorID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "UserID INTEGER, FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a table of Job Descriptions to the database
     */
    public void addTableJobDescriptions() {

        String sql = "CREATE TABLE JobDescriptions(JobID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Title VARCHAR(40), Keywords VARCHAR(100), Description VARCHAR(1000), "
                + "PaymentOffer INTEGER, JobStatus INTEGER, ProviderID INTEGER, "
                + "FreelancerID INTEGER, FOREIGN KEY(ProviderID) REFERENCES Providers(ProviderID), "
                + "FOREIGN KEY(FreelancerID) REFERENCES Freelancers(FreelancerID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

                       // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a table of Administrators to the database
     */
    public void addTableFreelancerOffers() {

        String sql = "CREATE TABLE FreelancerOffers(JobID INTEGER, FreelancerID INTEGER, "
                + "PRIMARY KEY(JobID, FreelancerID), "
                + "FOREIGN KEY(JobID) REFERENCES JobDescriptions(JobID), "
                + "FOREIGN KEY(FreelancerID) REFERENCES Freelancers(FreelancerID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

                      // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
           System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }
    }
    
    
    // Adds all tables to the database
    public void run() {
        db app = new db();

        // add all tables to the database
        app.addTableUsers();
        app.addTableProviders();
        app.addTableFreelancers();
        app.addTableAdministrators();
        app.addTableJobDescriptions();
        app.addTableFreelancerOffers();
        
        createSampleData();

    }

    
    // Fills the tables with sample data (for demonstration purposes)
    public void createSampleData() {
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            ResultSet result = stmt.executeQuery("SELECT * FROM Providers");
        
            if(!result.next()) {
                String[] names = {"Dwight Schrute", "Jim Halpert", "Pam Beasly", "Michael Scott"};
                String[] emails = {"schrutefarms@aol.com", "jimmyhalps@hotmail.com", "pam123@yahoo.com", "bestboss@gmail.com"};
                String password = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5"; // Hashed version of "12345"

                String query;
                PreparedStatement pst;

                for(int i = 0; i < names.length; i++) {
                    //Prepare a query to insert values into Users table
                    query = "INSERT INTO Users (Name,Email,Password) VALUES(?,?,?)";

                    //Connect to the database with queries
                    pst = connect.prepareStatement(query);

                    //Get text entered into textfields
                    //put them into the corresponding queries
                    pst.setString(1, names[i]);
                    pst.setString(2, emails[i]);
                    pst.setString(3, password);
                    //execute the queries
                    pst.executeUpdate();
                }

                // Add sample Provider (User Dwight Schrute)
                query = "INSERT INTO Providers (UserID) VALUES(?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, 1000);
                pst.executeUpdate();


                // Add sample Freelancers (Users Jim Halpert and Pam Beasly)
                query = "INSERT INTO Freelancers (Skills, Message, Balance, UserID) VALUES(?, ?, ?, ?)";
                pst = connect.prepareStatement(query);
                pst.setString(1, "skill_nodejs,skill_php,skill_javascript");
                pst.setString(2, "Hello! I am a freelancer and I can do a great job for you.");
                pst.setInt(3, 20);
                pst.setInt(4, 1001);
                //execute the queries
                pst.executeUpdate();

                query = "INSERT INTO Freelancers (Skills, Message, Balance, UserID) VALUES(?, ?, ?, ?)";
                pst = connect.prepareStatement(query);
                pst.setString(1, "skill_nodejs,skill_javascript");
                pst.setString(2, "Hello! I am experienced and can help you out.");
                pst.setInt(3, 80);
                pst.setInt(4, 1002);
                //execute the queries
                pst.executeUpdate();


                // Add a sample administrator (User Michael Scott)
                query = "INSERT INTO Administrators (UserID) VALUES(?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, 1003);
                pst.executeUpdate();


                String[] jobTitles = {"Build My Farm's Website", "Need a Crop Logging System", "Schrute Farms Mobile App"};
                String[] jobKeywords = {"python, website, build", "log, database, crops", "mobile, app, development"};
                String[] jobDescriptions = {
                    "Schrute farms needs a website to draw in the digital crowd. It would need 7 pages and an online shop to start selling our beets to the world",
                    "As the farm has expanded significantly, we need to be able to keep track of all of the crops somehow. Build us a system to do that please.",
                    "We need someone to build us a mobile app so we can have all of our fans can access Schrute Farms updates on the go."
                };
                int[] payments = {100, 45, 80};
                int status = 0;
                int providerID = 1000;

                for(int i = 0; i < jobTitles.length; i++ ) {
                     //Prepare a query to insert values into JOBDescriptions table
                    query = "INSERT INTO JobDescriptions (TITLE,KEYWORDS,DESCRIPTION,PAYMENTOFFER,JOBSTATUS,PROVIDERID) VALUES(?,?,?,?,?,?)";

                    //Connect to the database with queries
                    pst = connect.prepareStatement(query);
                    pst.setString(1, jobTitles[i]);
                    pst.setString(2, jobKeywords[i]);
                    pst.setString(3, jobDescriptions[i]);
                    pst.setInt(4, payments[i]);
                    pst.setInt(5, status);
                    pst.setInt(6, providerID);



                    //execute the queries
                    pst.executeUpdate();
                }

                // Add freelancer offer (Jim Halpert offers to build Website)
                query = "INSERT INTO FREELANCEROFFERS(JOBID,FREELANCERID) VALUES(?,?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, 1000);
                pst.setInt(2, 1000);
                //execute the queries
                pst.executeUpdate();

                // Add freelancer offer (Pam Beesly offers to build Website)
                query = "INSERT INTO FREELANCEROFFERS(JOBID,FREELANCERID) VALUES(?,?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, 1000);
                pst.setInt(2, 1001);
                //execute the queries
                pst.executeUpdate();
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.getSQLState());
        }

        
    }
}