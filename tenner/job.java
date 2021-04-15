package myBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author my pc
 */
public class job {
    String title,keywords,description;
    int id,payment,jobstatus,providerId,freelancerId;
    
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
    
    public String getDescript(){
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
//    //Setters
//    public void setTitle(String title){
//        this.title=title;
//    }
//    public void setKeywords(String keywords){
//         this.keywords=keywords;
//    }
//    
//    public void setDescript(String descript){
//        this.descript=descript;
//    }
//    public void setId(int id){
//        this.id=id;
//    }
//    
//    public void setPayment(double payment){
//       this.payment=payment;
//    }
    
}

