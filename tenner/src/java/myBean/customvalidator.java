/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Gabriel Denys (17223857), Teresa Flaherty (17017157), Eoghan O'Connor (16110625), Raymond McCreesh (15211428)
 */
@ManagedBean
@FacesValidator("customvalidtor")
public class customvalidator implements Validator{
    // Object variables
    private String txt;
    
    
    // Constructor
    public customvalidator() {
        
    }
    
    /**
     * Get the value of the Text being Validated
     *
     * @return the text
     */
    public String getTxt() {
        return txt;
    }

    
    /**
     * Set the value of the Text being Validated
     *
     * @param txt the text
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }
    
    /**
     * Check that the given text is a Valid Email Address
     *
     * @param fc the Faces Context of the Client
     * @param uic the UI Component 
     * @param obj the Object to be validated
     */
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object obj)
			throws ValidatorException {
                // Create a pattern for the validation
                Pattern pattern = Pattern.compile( "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                // Check if the given Email matches the pattern
                Matcher matcher = pattern.matcher((String) obj);
                // Throw an exception if the given email does not match the pattern
		if (!matcher.matches()) {
                    FacesMessage msg = new FacesMessage(
                            "Please enter a valid email address");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    
                    throw new ValidatorException(msg);
        }
    
    
    }
}
