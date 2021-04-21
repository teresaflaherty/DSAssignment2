/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author my pc
 */
@Named(value = "job")
@SessionScoped
public class job implements Serializable {

    private String title,keywords,description;
    private int id,payment,jobstatus,providerId,freelancerId;
   
//    BEANS MUST HAVE AN EMPTY CONSTRUCTOR WITH NO PARAMETERS

 
    public job(int id,String title,String keywords,String description,int payment,int jobstatus,int providerId, int freelancerId){
        this.title=title;
        this.keywords=keywords;
        this.description=description;
        this.id=id;
        this.payment=payment;
        this.jobstatus=jobstatus;
        this.providerId=providerId;
        this.freelancerId=freelancerId;
    }

    public job(){
        
    }
   
    
    //Getters
    public int getId(){
        return id;
    }
        
    public String getTitle(){
        return title;
    }
    public String getKeywords(){
        return keywords;
    }
    
    public String getDescription(){
        return description;
    }
      
    public int getPayment(){
        return payment;
    }
    
    public int getJobstatus(){
        return jobstatus;
    }
    
    public int getProviderId(){
        return providerId;
    }
    
    public int getFreelancerId(){
        return freelancerId;
    } 

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public void setJobstatus(int jobstatus) {
        this.jobstatus = jobstatus;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
    } 
}
