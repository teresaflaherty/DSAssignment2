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
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@Named(value = "bean")
@SessionScoped
public class bean implements Serializable {
    // Object Variables
    private String name;
    private String password;
    private String email; 
    private String bio;
    private List<String> skills;
    private String type;
    
    // Constructor
    public bean() {
        
    }
    
    // Getters
    /**
     * Get the value of the Bean's Name
     *
     * @return value of the name
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Get the value of the Bean's Password
     *
     * @return value of the password
     */
    public String getPassword() {
        return password;
    }
    
    
    /**
     * Get the value of the Bean's Email
     *
     * @return value of the email
     */
    public String getEmail() {
        return email;
    }
    
    
    /**
     * Get the value of the Bean's Bio
     *
     * @return value of the bio
     */
    public String getBio() {
        return bio;
    }
    
    
    /**
     * Get the value of the Bean's Skills
     *
     * @return list of skills
     */
    public List<String> getSkills() {
        return skills;
    }
    
    
    /**
     * Get the value of the Bean's Type (Freelancer/Provider/Administrator)
     *
     * @return value of the name
     */
    public String getType(){
    return type;
    }
    
    
    // Setters
    /**
     * Set the value of the Bean's Name
     *
     * @param name the new bean name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * Set the value of the Bean's Password
     *
     * @param password the new bean password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    /**
     * Set the value of the Bean's Email
     *
     * @param email the new bean email
     */
    public void setEmail(String email){
        this.email=email;
    }
    
    
    /**
     * Set the value of the Bean's Bio
     *
     * @param bio the new bean bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    
    /**
     * Set the value of the Bean's Skills
     *
     * @param skills the list of new skills
     */
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    
    /**
     * Set the value of the Bean's Type (Freelancer/Provider/Administrator)
     *
     * @param type the new bean type
     */
    public void setType(String type) {
        this.type = type;
    }
}
