/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author my pc
 */
@Named(value = "myaccount")
@RequestScoped
public class myaccount {
        private static final String URL = "jdbc:derby://localhost:1527/tenner";
    private static final String USER = "app";

   
    private static final String PASSWD = "app";


//    if status =3 then all jobs for provider are returned
    public ArrayList<job> getJob(String email,int status) {
        System.out.println("Entered getjob");
        
      ArrayList<job> JobsList = new ArrayList<>();
        int personid=getUSERID(email);
        System.out.println(personid);
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

                if(status==3){
                    try{
                        System.out.println("Entered try ");
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE PROVIDERID=" + personid);
                   }
                   catch(Exception e){
                       System.out.println("Entered catch ");
                        result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE FREELANCERRID=" + personid);
                   }
                        
                    }
                
                else{
                    
                    try{
                        System.out.println("Entered try 2");
                    result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE (PROVIDERID=" + personid+
                                "AND JOBSTATUS="+status+")");
                   }
                   catch(Exception e){
                       System.out.println("Entered catch2 ");
                       result = stmt.executeQuery("SELECT * FROM JOBDescriptions WHERE (FREELANCERID=" + personid+
                                "AND JOBSTATUS="+status+")");
                   }

                    }
                
                System.out.println("exited try");
                job job;
                while (result.next()) {
                       System.out.println("Job infor");
                       System.out.println(result.getInt("JobID"));
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
    
    
    public int getUSERID(String email){
        System.out.println(email);
        System.out.println("Enter userid");
    int userID = -1;
    int personid=0;
    try {
               Connection connect = null;
               Statement stmt = null;
               Statement stmt2= null;
               Statement stmt3= null;
               ResultSet result;
               ResultSet result2;
               ResultSet result3;
               try {
                   // connect to db - make sure derbyclient.jar is added to your project
                   connect = DriverManager.getConnection(URL, USER, PASSWD);

                   // Retrieve UserID from type and ID
                   String query = "SELECT * FROM Users WHERE Email = ?";

                   //Connect to the database with queries
                   PreparedStatement pst = connect.prepareStatement(query);

                   //Get text entered into textfields
                   //put them into the corresponding queries
                   pst.setString(1, email);

                   //execute the queries
                   result = pst.executeQuery();


                   while (result.next()) {
                       userID = result.getInt(1);
                   }
                   System.out.println("USER ID IS ");
                   System.out.println(userID);
                   
                   
                       System.out.println(" userid try");
                    String query2 = "SELECT * FROM providers WHERE USERID = ?";
                                       //Connect to the database with queries
                   PreparedStatement pst2 = connect.prepareStatement(query2);
                                      //Get text entered into textfields
                   //put them into the corresponding queries
                   pst2.setInt(1, userID);
                                      //execute the queries
                   result2 = pst2.executeQuery();


                   while (result2.next()) {
                       personid = result2.getInt(1);
                   }
                   
                   
                   if(personid <100){
                       System.out.println(" userid catch");
                     String query3 = "SELECT * FROM Freelancers WHERE USERID = ?";
                                          PreparedStatement pst3 = connect.prepareStatement(query3);
                                      //Get text entered into textfields
                   //put them into the corresponding queries
                   pst3.setInt(1, userID);
                                      //execute the queries
                   result3 = pst3.executeQuery();


                   while (result3.next()) {
                       personid = result3.getInt(1);
                   }
                   }
                   System.out.println(" RESULTING ID");
                   System.out.println(personid);
               

                   


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

    return personid;
  }
    
}

