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
import java.util.List;

@Named(value = "provider")
@RequestScoped

public class provider {
    
    private static final String URL = "jdbc:derby://localhost:1527/tenner;create=true";
    private static final String USER = "app";
    private static final String PASSWD = "app";
    
    private int id, userId;
    
    public provider(int id, int userId){
        this.id=id;
        this.userId=userId;
    }

    public provider(){
        
    }
    
    
    //Getters
    public int getId(){
        return id;
    }
        
    public int getUserId(){
        return userId;
    }
    
    
    //Setters
    public void setId(int id){
        this.id = id;
    }
        
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    
    //This removes a job posting, the int JOBID can be changed for an String
    // Just uncomment the int id on line 36-37
        public void remove_job(int Jobid){
        
        // TODO add your handling code here:
                try {
            Connection connect = null;
            Statement stmt = null;
            Statement stmt2 = null;
            ResultSet result;
            ResultSet result2;
            String data = "Results:\n"; 
                try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
//                int id;
//                id= Integer.parseInt(Jobid);
                
                 //Prepare a query to insert values into Athlete Coaches table
                String query = "DELETE FROM FREELANCEROFFERS where JOBID="+Jobid;
                PreparedStatement pst = connect.prepareStatement(query);
                //execute the queries
                pst.executeUpdate();
                
                String query2 = "DELETE FROM JOBDescriptions where JOBID="+Jobid;

                //Connect to the database with queries
                PreparedStatement pst2 = connect.prepareStatement(query2);
                
                //execute the queries
                pst2.executeUpdate();
                
                                //Get text entered into textfields
                //put them into the corresponding queries
     
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

  public void add_job(String title,String keywords, String decript,int payment,int jobstatus,String provideremail){
               // TODO add your handling code here:
               int providerid= getUSERID(provideremail);
               System.out.println("the providerid");
               System.out.println(providerid);
        try {
            Connection connect = null;
            Statement stmt = null;
            ResultSet result;
            String data = "Results:\n"; 
                try {
                // connect to db - make sure derbyclient.jar is added to your project
                connect = DriverManager.getConnection(URL, USER, PASSWD);
                
                 //Prepare a query to insert values into JOBDescriptions table
                String query = "INSERT INTO JOBDescriptions (TITLE,KEYWORDS,DESCRIPTION,PAYMENTOFFER,JOBSTATUS,PROVIDERID,FREELANCERID)"
                        +"VALUES(?,?,?,?,?,?,?)";

                //Connect to the database with queries
                PreparedStatement pst = connect.prepareStatement(query);
                pst.setString(1, title);
                pst.setString(2, keywords);
                pst.setString(3, decript);
                pst.setInt(4, payment);
                pst.setInt(5, jobstatus);
                pst.setInt(6, providerid);
                pst.setInt(7, 1001);
                

                
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

    public void accept_offer(int job_id){
    
      try {
                Connection connect = null;
                Statement stmt = null;
                ResultSet result;
                String data = "Results:\n"; 
                    try {
                    // connect to db - make sure derbyclient.jar is added to your project
                    connect = DriverManager.getConnection(URL, USER, PASSWD);

                     //Prepare a query to insert values into JOBDescriptions table
                    String query = "UPDATE JOBDescriptions SET JOBSTATUS=1 WHERE JOBID="+job_id;

                    //Connect to the database with queries
                    PreparedStatement pst = connect.prepareStatement(query);



                    //execute the queries
                    pst.executeUpdate();

                                    //Get text entered into textfields
                    //put them into the corresponding queries

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

  public void mark_done(int job_id){

      try {
                Connection connect = null;
                Statement stmt = null;
                ResultSet result;
                String data = "Results:\n"; 
                    try {
                        System.out.println("Updating");
                    // connect to db - make sure derbyclient.jar is added to your project
                    connect = DriverManager.getConnection(URL, USER, PASSWD);

                     //Prepare a query to insert values into JOBDescriptions table
                    String query = "UPDATE JOBDescriptions SET JOBSTATUS=2 WHERE JOBID="+job_id;

                    //Connect to the database with queries
                    PreparedStatement pst = connect.prepareStatement(query);



                    //execute the queries
                    pst.executeUpdate();

                                    //Get text entered into textfields
                    //put them into the corresponding queries

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
 
  
public bean getFreelancer(int freelancerid){
    bean freelancer = new bean();
    int userID = -1;

    String name=null;
    try {
               Connection connect = null;
               Statement stmt = null;
               Statement stmt2= null;
               ResultSet result;
               ResultSet result2;
               try {
                   System.out.println("in try ");
                   // connect to db - make sure derbyclient.jar is added to your project
                   connect = DriverManager.getConnection(URL, USER, PASSWD);

                   // Retrieve UserID from type and ID
                   String query = "SELECT * FROM FREELANCERS WHERE freelancerid =?";

                   //Connect to the database with queries
                   PreparedStatement pst = connect.prepareStatement(query);

                   //Get text entered into textfields
                   //put them into the corresponding queries
                   pst.setInt(1, freelancerid);

                   //execute the queries
                   result = pst.executeQuery();

                   
                   while (result.next()) {
                       userID = result.getInt(1);
                       freelancer.setBio(result.getString(2));
                       freelancer.setSkills((List<String>) result.getArray(3));
                      
                   }
                    System.out.println(freelancer.getBio());
                    String query2 = "SELECT * FROM users WHERE USERID = ?";
                                       //Connect to the database with queries
                   PreparedStatement pst2 = connect.prepareStatement(query2);
                                      //Get text entered into textfields
                   //put them into the corresponding queries
                   pst2.setInt(1, userID);
                                      //execute the queries
                   result2 = pst2.executeQuery();

                   
                   while (result2.next()) {
                       freelancer.setName(result2.getString(2));
                       freelancer.setEmail(result2.getString(3));
                       
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
System.out.println(freelancer.getName());
    return freelancer;
  }

public ArrayList<bean> getoffers(int job_id) {
     ArrayList<bean> freelancerList = new ArrayList<>();
     bean freelancerList2 = null;
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
                result = stmt.executeQuery("SELECT * FROM FREELANCEROFFERS Where JOBID="+job_id);
                // process results
                // while there are results
                job job;
                while (result.next()) {
                    
                    job= new job();
                    job.setId(result.getInt("JobID"));
                    job.setFreelancerId(result.getInt("freelancerId"));
                    freelancerList2=getFreelancer(job.getId());
                    freelancerList.add(freelancerList2);
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
        
        return freelancerList;
}



    public int getUSERID(String email){

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
