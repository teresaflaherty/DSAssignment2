/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsassign2;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author teresaflaherty
 */
public class DSAssign2 {

    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app";

    /**
     * Adds a table of Users to the database
     */
    public void addTableUsers() {

        String sql = "CREATE TABLE Users(UserID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Name VARCHAR(30), Email VARCHAR(50), Password VARCHAR(30))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, 
                    "Table Users successfully created");

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
        }
    }
    
    
//    /**
//     * Adds a new table of Roles to the database
//     */
//    public void addTableRoles() {
//
//        String sql = "CREATE TABLE Roles(ID INTEGER PRIMARY KEY "
//                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
//                + "Name VARCHAR(60))";
//        // use try with resource
//        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
//                Statement stmt = connect.createStatement();) {
//
//            // execute statement 
//            stmt.executeUpdate(sql);
//
//            JOptionPane.showMessageDialog(null, 
//                    "Table Providers successfully created");
//
//            // deal with any potential exceptions
//            // note: all resources are closed automatically - no need for finally
//        } catch (SQLException sqle) {
//            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
//            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
//        }
//    }
//    
//    /**
//     * Adds a new table of User Roles to the database
//     */
//    public void addTableUserRoles() {
//
//        String sql = "CREATE TABLE Providers(UserID INTEGER FOREIGN KEY REFERENCES Users(UserID), "
//                + "RoleID INTEGER FOREIGN KEY REFERENCES Roles(RoleID), "
//                + "PRIMARY KEY(UserID, RoleID)";
//        // use try with resource
//        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
//                Statement stmt = connect.createStatement();) {
//
//            // execute statement 
//            stmt.executeUpdate(sql);
//
//            JOptionPane.showMessageDialog(null, 
//                    "Table Providers successfully created");
//
//            // deal with any potential exceptions
//            // note: all resources are closed automatically - no need for finally
//        } catch (SQLException sqle) {
//            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
//            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
//        }
//    }
    
    
    /**
     * Adds a table of Providers to the database
     */
    public void addTableProviders() {

        String sql = "CREATE TABLE Providers(ProviderID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Name VARCHAR(60), UserID INTEGER FOREIGN KEY REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, 
                    "Table Providers successfully created");

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a new table of Freelancers to the database
     */
    public void addTableFreelancers() {

        String sql = "CREATE TABLE Freelancers(FreelancerID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Name VARCHAR(60), Skills VARCHAR(500), Message VARCHAR(500), "
                + "Balance INTEGER, UserID INTEGER FOREIGN KEY REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, 
                    "Table Providers successfully created");

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
        }
    }

    
    /**
     * Adds a table of Administrators to the database
     */
    public void addTableAdministrators() {

        String sql = "CREATE TABLE Administrators(AdminID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Name VARCHAR(60), UserID INTEGER FOREIGN KEY REFERENCES Users(UserID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, 
                    "Table Providers successfully created");

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
        }
    }
    
    
    /**
     * Adds a table of Job Descriptions to the database
     */
    public void addTableJobDescriptions() {

        String sql = "CREATE TABLE JobDescriptions(ID INTEGER PRIMARY KEY "
                + "GENERATED ALWAYS AS IDENTITY (START WITH 1000, INCREMENT BY 1), "
                + "Title VARCHAR(40), Keywords VARCHAR(100), Description VARCHAR(1000), "
                + "PaymentOffer INTEGER, JobStatus INTEGER"
                + "ProviderID INTEGER FOREIGN KEY REFERENCES Providers(ProviderID), "
                + "FreelancerID INTEGER FOREIGN KEY REFERENCES Freelancers(ProviderID))";
        // use try with resource
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWD);
                Statement stmt = connect.createStatement();) {

            // execute statement 
            stmt.executeUpdate(sql);

            JOptionPane.showMessageDialog(null, 
                    "Table Providers successfully created");

            // deal with any potential exceptions
            // note: all resources are closed automatically - no need for finally
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Message: " + sqle.getMessage());
            JOptionPane.showMessageDialog(null, "Code: " + sqle.getSQLState());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DSAssign2 app = new DSAssign2();
        JOptionPane.showMessageDialog(null, "Starting Application ...");

        // call addTablePerson
        app.addTableUsers();
        app.addTableProviders();

        System.exit(0);
    }
    
}
