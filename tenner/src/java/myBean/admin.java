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

@Named(value = "admin")
@RequestScoped

public class admin {
    
    private static final String URL = "jdbc:derby://localhost:1527/sample";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    
    public String registerUser(String name, String password,String email, String type, ArrayList<String> skills, String bio){
        
        try {
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
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                
                // Check if user with email already exists
                String query = "SELECT * FROM Users WHERE Email = ?";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, email);

                //execute the queries
                result = pst.executeQuery();
                
                if(result.next()) {
                    return null;
                }
                else {
                    //Prepare a query to insert values into Users table
                    query = "INSERT INTO Users (Name,Email,Password) VALUES(?,?,?)";

                    //Connect to the database with queries
                    pst = connect.prepareStatement(query);

                    //Get text entered into textfields
                    //put them into the corresponding queries
                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, password);


                    //execute the queries
                    pst.executeUpdate();

                    //stores UserID to insert into Provider/Freelancer table
                    stmt = connect.createStatement();
                    result = stmt.executeQuery("SELECT MAX(UserID) FROM Users");
                    int userID = 1000;
                    while (result.next()) {
                        userID = result.getInt("UserID");
                    }

                    switch (type) {
                        case "Provider":
                            //Prepare a query to insert values into Providers table
                            query = "INSERT INTO Providers (UserID) VALUES(?)";
                            //Connect to the database with queries
                            pst = connect.prepareStatement(query);
                            //Get text entered into textfields
                            //put them into the corresponding queries
                            pst.setInt(1, userID);
                            //execute the queries
                            pst.executeUpdate();
                            return "provideraccount";
                        case "Freelancer":
                            String skills_string = String.join(",", skills);
                            //Prepare a query to insert values into Users table
                            query = "INSERT INTO Freelancers (Skills, Message, Balance, UserID) VALUES(?, ?, ?, ?)";
                            //Connect to the database with queries
                            pst = connect.prepareStatement(query);
                            //Get text entered into textfields
                            //put them into the corresponding queries
                            pst.setString(1, skills_string);
                            pst.setString(2, bio);
                            pst.setInt(3, 0);
                            pst.setInt(4, userID);
                            //execute the queries
                            pst.executeUpdate();
                            return "home";
                        case "Administrator":
                            //Prepare a query to insert values into Providers table
                            query = "INSERT INTO Administrators (UserID) VALUES(?)";
                            //Connect to the database with queries
                            pst = connect.prepareStatement(query);
                            //Get text entered into textfields
                            //put them into the corresponding queries
                            pst.setInt(1, userID);
                            //execute the queries
                            pst.executeUpdate();
                            return "adminpage";
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

        // deal with any potential exceptions
        // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
            return null;
        }
    }
    
    public String editProfile(String name, String email, String password, ArrayList<String> skills, String bio){
        int freelancerID = 1000;
        try {
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
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);

                // Retrieve UserID from type and ID
                String query = "SELECT * FROM Freelancers WHERE FreelancerID = ?";
                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setInt(1, freelancerID);
                //execute the queries
                result = pst.executeQuery();

                int userID = -1;
                while (result.next()) {
                    userID = result.getInt("UserID");
                }

                query = "UPDATE Users SET Name = ?, Email = ?, Password = ? WHERE UserID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.setInt(4, userID);
                
                pst.executeUpdate();
                
                
                String skills_string = String.join(",", skills);
                //Prepare a query to insert values into Users table
                query = "UPDATE Freelancers SET Skills = ?, Message = ? WHERE FreelancerID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, skills_string);
                pst.setString(2, bio);
                pst.setInt(3, freelancerID);

                //execute the queries
                pst.executeUpdate();

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
    
    
    public void removeUser(String type, int ID){
        
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                // Retrieve UserID from type and ID
                String query = "SELECT * FROM ?s WHERE ?ID = ?";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, type);
                pst.setString(2, type);
                pst.setInt(3, ID);

                //execute the queries
                result = pst.executeQuery();
                
                int userID = -1;
                while (result.next()) {
                    userID = result.getInt(1);
                }
                
                // Delete from type table
                query = "DELETE FROM ?s WHERE ?ID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, type);
                pst.setString(2, type);
                pst.setInt(3, ID);

                //execute the queries
                pst.executeUpdate();
                
                // Delete from type table
                query = "DELETE FROM Users WHERE UserID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setInt(1, userID);

                //execute the queries
                pst.executeUpdate();

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
    }
    
    
    public String login(String email, String passwordEntry) {
        
        try {
            String entryHashed = "";
            try{
                entryHashed = hashPassword(passwordEntry);
            }
            catch(NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }

            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                String query = "SELECT * FROM Users WHERE Email = ?";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setString(1, email);

                //execute the queries
                result = pst.executeQuery();
                
                String password = "";
                int userID = -1;
                while (result.next()) {
                    password = result.getString("password");
                    userID = result.getInt(1);
                }
                
                query = "SELECT * FROM Providers WHERE UserID = ?";

                //Connect to the database with queries
                pst = connect.prepareStatement(query);

                //Get text entered into textfields
                //put them into the corresponding queries
                pst.setInt(1, userID);

                //execute the queries
                result = pst.executeQuery();
                
                String type;
                if (result.next()) {
                    type = "Provider";
                }
                else {
                    type = "Freelancer";
                }
                
                // If password given matches the one on file
                if (entryHashed.equals(password)) {
                    // Have frontend redirect according to user type
                    switch (type) {
                        case "Freelancer":
                            return "home";
                        case "Provider":
                            return "provideraccount";
                        case "Administrator":
                            return "adminpage";
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
            
        // deal with any potential exceptions
        // note: all resources are closed automatically - no need for finally
        } catch (SQLException sql) {
            //sql.printStackTrace();
            System.out.println(sql.getMessage());
            System.out.println(sql.getSQLState());
        }
        
        return null;
    }
    
    
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xFF & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
        
    }
}
