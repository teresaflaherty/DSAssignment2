/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "admin")
@RequestScoped
public class admin {
    // Database access variables
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    // Object variables
    private int id, userId;
    
    
    // Constructors
    public admin(int id, int userId){
        this.id=id;
        this.userId=userId;
    }

    public admin(){
        
    }
    
    
    // Getters
    /**
     * Get the value of the Administrator's ID
     *
     * @return value of id
     */
    public int getId(){
        return id;
    }
    
    
    /**
     * Get the value of the Administrator's UserID
     *
     * @return value of userId
     */    
    public int getUserId(){
        return userId;
    }
    
    
    //Setters
    /**
     * Set the value of the Administrator's ID
     *
     * @param id the new AdministratorID
     */
    public void setId(int id){
        this.id = id;
    }
    
    
    /**
     * Set the value of the Administrator's UserID
     *
     * @param userId the new userID for the Administrator
     */
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    
    /**
     * Register a new User in the system
     *
     * @param admin whether or not an Administrator is the one registering someone
     * @param name the name of the User being added to the system
     * @param password the new user's password
     * @param email the email address of the new user
     * @param type what type of user they are (Freelancer, Provider, or Administrator
     * @param skills what skills the user has (only has values if Freelancer)
     * @param bio the User's message to Providers (only has value if Freelancer)
     * 
     * @return the next page to navigate to, value dependent on user type
     */
    public String registerUser(boolean admin, String name, String password,String email, String type, ArrayList<String> skills, String bio){
        try {
            // Hash the password entry given by the user to store encrypted version
            try{
                password = hashPassword(password);
            }
            catch(NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }

            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                // Query database to find if User with given Email exists
                String query = "SELECT * FROM Users WHERE Email = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, email);
                result = pst.executeQuery();
                
                // If User exists, return null to stay on registration page
                if(result.next()) {
                    return null;
                }
                // If User does not exist, create User
                else {
                    // Prepare and execute a query to insert new User into Users table
                    query = "INSERT INTO Users (Name,Email,Password) VALUES(?,?,?)";
                    pst = connect.prepareStatement(query);
                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, password);
                    pst.executeUpdate();

                    // Retrieve UserID to insert into Provider/Freelancer table
                    stmt = connect.createStatement();
                    result = stmt.executeQuery("SELECT MAX(UserID) FROM Users");
                    int userID = -1;
                    while (result.next()) {
                        userID = result.getInt(1);
                    }
                    
                    // Perform different actions depending on what type of User is being registered
                    switch (type) {
                        case "Provider":
                            // Prepare and execute a query to insert new Provider into Providers table
                            query = "INSERT INTO Providers (UserID) VALUES(?)";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, userID);
                            pst.executeUpdate();
                            
                            // Will only redirect to the Provider Account page if the Provider registered themselves
                            // If Administrator registered for them it will not redirect away from Admin Account Page
                            if(!admin) {
                                return "provideraccount";
                            }
                            break;
                        case "Freelancer":
                            // Join the list of skills into a String so they can be stored in the table
                            String skills_string = String.join(", ", skills);

                            // Prepare and execute a query to insert new Freelancer into Freelancers table
                            query = "INSERT INTO Freelancers (Skills, Message, Balance, UserID) VALUES(?, ?, ?, ?)";
                            pst = connect.prepareStatement(query);
                            pst.setString(1, skills_string);
                            pst.setString(2, bio);
                            pst.setInt(3, 0);
                            pst.setInt(4, userID);
                            pst.executeUpdate();
                            
                            // Will only redirect to the Home page if the Freelancer registered themselves
                            // If Administrator registered for them it will not redirect away from Admin Account Page
                            if(!admin) {
                                return "home";
                            }
                            break;
                        case "Administrator":
                            // Prepare and execute a query to insert new Freelancer into Freelancers table
                            query = "INSERT INTO Administrators (UserID) VALUES(?)";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, userID);
                            pst.executeUpdate();
                            
                            // Administrators cannot register themselves, so check of admin value is not needed here
                            
                            break;
                        default:
                            break;
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
        // In the event of registration failure do not redirect
        return null;
    }
    
    
    /**
     * Edit existing profile (only called by freelancers but in admin to access hashPassword function)
     *
     * @param freelancerID the ID of the freelancer whose profile is being edited
     * @param name the new Name value
     * @param password the User's new password
     * @param email the new Email Address
     * @param skills the new list of skills
     * @param bio the User's new message to Providers
     */
    public void editProfile(int freelancerID, String name, String email, String password, ArrayList<String> skills, String bio){
        try {
            // Hash the password entry given by the user to store encrypted version
            try{
                password = hashPassword(password);
            }
            catch(NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }

            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                // Prepare and execute a query to get the Freelancer's information
                String query = "SELECT * FROM Freelancers WHERE FreelancerID = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setInt(1, freelancerID);
                result = pst.executeQuery();
                
                // Retrieve and store the UserID from the results
                int userID = -1;
                while (result.next()) {
                    userID = result.getInt("UserID");
                }
                
                // Prepare and execute a query to update the table with the User's new Name, Email, and Password
                query = "UPDATE Users SET Name = ?, Email = ?, Password = ? WHERE UserID = ?";
                pst = connect.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.setInt(4, userID);
                pst.executeUpdate();
                
                // Join the list of skills into a String so they can be stored in the table
                String skills_string = String.join(",", skills);
                
                // Prepare and execute a query to update the table with the Freelancer's new Skills and Message
                query = "UPDATE Freelancers SET Skills = ?, Message = ? WHERE FreelancerID = ?";
                pst = connect.prepareStatement(query);
                pst.setString(1, skills_string);
                pst.setString(2, bio);
                pst.setInt(3, freelancerID);
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
     * Remove an existing User from the database
     *
     * @param type the type of User being removed
     * @param roleID the FreelancerID/ProviderID/AdministratorID of the User being removed
     */
    public void removeUser(String type, int roleID){
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                String query;
                PreparedStatement pst;
                int userID = -1;
                
                // Perform different actions depending on type
                switch(type) {
                        case "Freelancer":
                            // Prepare and execute a query to get the Freelancer's information
                            query = "SELECT * FROM Freelancers WHERE FreelancerID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            result = pst.executeQuery();
                            
                            // Retrieve and store the UserID from the results
                            while (result.next()) {
                                userID = result.getInt("UserID");
                            }
                            
                            // Prepare and execute a query to delete all Offers tied to the Freelancer
                            query = "DELETE FROM FreelancerOffers WHERE FreelancerID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            
                            // Prepare and execute a query to delete all Jobs tied to the Freelancer
                            query = "DELETE FROM JobDescriptions WHERE FreelancerID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            
                            // Prepare and execute a query to delete the Freelancer
                            query = "DELETE FROM Freelancers WHERE FreelancerID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            break;
                        case "Provider":
                            // Prepare and execute a query to get the Provider's information
                            query = "SELECT * FROM Providers WHERE ProviderID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            result = pst.executeQuery();
                            
                            // Retrieve and store the UserID from the results
                            while (result.next()) {
                                userID = result.getInt("UserID");
                            }
                            
                            // Prepare and execute a query to get all Jobs tied to the Provider
                            query = "SELECT * FROM JobDescriptions WHERE ProviderID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            result = pst.executeQuery();
                            
                            int jobID;
                            while(result.next()) {
                                // Prepare and execute a query to delete all Offers for each Job tied to the Provider
                                jobID = result.getInt("JobID");
                                query = "DELETE FROM FreelancerOffers WHERE JobID = ?";
                                pst = connect.prepareStatement(query);
                                pst.setInt(1, jobID);
                                pst.executeUpdate();
                            }
                            
                            // Prepare and execute a query to delete all Jobs tied to the Provider
                            query = "DELETE FROM JobDescriptions WHERE ProviderID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            
                            // Prepare and execute a query to delete the Provider
                            query = "DELETE FROM Providers WHERE ProviderID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            break;
                        case "Administrator":
                            // Prepare and execute a query to get the Administrator's information
                            query = "SELECT * FROM Administrators WHERE AdministratorID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            result = pst.executeQuery();
                            
                            // Retrieve and store the UserID from the results
                            while (result.next()) {
                                userID = result.getInt("UserID");
                            }
                            
                            // Prepare and execute a query to delete the Administrator
                            query = "DELETE FROM Administrators WHERE AdministratorID = ?";
                            pst = connect.prepareStatement(query);
                            pst.setInt(1, roleID);
                            pst.executeUpdate();
                            break;
                        default:
                            break;
                }
                
                // Prepare and execute a query to delete the User
                query = "DELETE FROM Users WHERE UserID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, userID);
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
     * Log a User into the system
     *
     * @param email the User's email input
     * @param passwordEntry the User's password input
     * 
     * @return the next page to navigate to, value dependent on user type
     */
    public String login(String email, String passwordEntry) {
        try {
            // Hash the Password Entry to compare to the hashed Password on file
            String entryHashed = "";
            try{
                entryHashed = hashPassword(passwordEntry);
            }
            catch(NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }

            Connection connect = null;
            Statement stmt = null;
            ResultSet result, resultP, resultF;
            try {
                // Connect to the database
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                // Prepare and execute a query to get the User for the given Email
                String query = "SELECT * FROM Users WHERE Email = ?";
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, email);
                result = pst.executeQuery();
                
                // Retrieve the User's password and UserID from the results
                String password = "";
                int userID = -1;
                while (result.next()) {
                    password = result.getString("password");
                    userID = result.getInt(1);
                }
                
                // Prepare and execute a query to check if the User is a Provider
                query = "SELECT * FROM Providers WHERE UserID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, userID);
                resultP = pst.executeQuery();
                
                // Prepare and execute a query to check if the User is a Freelancer
                query = "SELECT * FROM freelancers WHERE UserID = ?";
                pst = connect.prepareStatement(query);
                pst.setInt(1, userID);
                resultF = pst.executeQuery();
                
                // Prepare and execute a query to insert new log for Login into LoggingLogin table
                query = "INSERT INTO  LoggingLogin(UserId, Password) VALUES(?, ?)";
                pst = connect.prepareStatement(query);
                pst.setInt(1, userID);
                pst.setString(2, entryHashed);
                pst.executeUpdate();
                
                // Store the User's type depending on the above queries
                String type;
                if (resultP.next()) {
                    type = "Provider";
                }
                else if (resultF.next()){
                    type = "Freelancer";
                }
                else {
                    type = "Administrator";
                }
                
                // If the Password Entry matches the one on file
                if (entryHashed.equals(password)) {
                    // Redirect User according to their type
                    switch (type) {
                        case "Freelancer":
                            return "home";
                        case "Provider":
                            return "provideraccount";
                        case "Administrator":
                            return "adminaccount";
                        default:
                            return null;
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
        // In the event of login failure do not redirect
        return null;
    }
    
    
    /**
     * Hash a Password so it can be stored securely in the database
     *
     * @param password the Password to be hashed
     * 
     * @throws NoSuchAlgorithmException in the event "SHA-256" cannot be found
     * 
     * @return the next page to navigate to, value dependent on user type
     */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        // Create a SHA-256 Message Digest
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Hash the password using the MessageDigest
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        // Turn the hashed byte array into a StringBuilder
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xFF & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // Convert the StringBuilder to String and return
        return hexString.toString();
        
    }
}
