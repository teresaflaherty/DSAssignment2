/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author gabed
 */
@Named(value = "bean")
@RequestScoped
public class bean {

    /**
     * Creates a new instance of bean
     */
    private String name;
    private String password;
    private String email; 
    private String bio;
    private String skills;
    private String type; //freelancer or provider
    
    
    
    public String getType(){
    return type;
    }
    
    public String getSkills() {
        return skills;
    }
  
    public String geBio() {
        return bio;
    }
    
    public String getName() {
        return name;
    }
    public String getemail() {
        return email;
    }
    
    public void setEmail(String email){
        this.email=email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

  
    public bean() {
    }
    
}