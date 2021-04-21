package myBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import java.sql.*;
import javax.swing.JOptionPane;
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

         System.out.println("worked");


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

        String sql = "CREATE TABLE Administrators(AdminID INTEGER PRIMARY KEY "
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
    
    public  void run() {
        
        db app = new db();
//        JOptionPane.showMessageDialog(null, "Starting Application ...");

        // add all tables to the database
        app.addTableUsers();
        app.addTableProviders();
        app.addTableFreelancers();
        app.addTableAdministrators();
        app.addTableJobDescriptions();
        app.addTableFreelancerOffers();

//        System.exit(0);
    }

    
}