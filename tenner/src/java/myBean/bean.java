/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author my pc
 */
@Named(value = "bean")
@SessionScoped
public class bean implements Serializable {


    /**
     * Creates a new instance of bean
     */
    private String name;
    private String password;
    private String email; 
    private String bio;
    private List<String> skills;
    private String type; //freelancer or provider
    
    public bean(String name, String password,String email,String bio,List<String> skills,String type){
        this.name=name;
        this.password=password;
        this.email=email;
        this.bio=bio;
        this.skills=skills;
        this.type=type;
    }
    
    
    public String getType(){
    return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<String> getSkills() {
        return skills;
    }
    
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
  
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
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
  
    public bean() {
    }
    
}
