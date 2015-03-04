/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userDataPackage;

import toolsPackage.Database;
import toolsPackage.Hash;
import toolsPackage.Validator;
import userPackage.UserType;
import static userPackage.UserType.ADMIN;
import static userPackage.UserType.LECTURER;

/**
 *
 * @author cdol1
 */
public class SignUpRequest extends UserRequest {
    private String id = "";
    private String title = "";
    private String firstName = "";
    private String surname = "";
    private String email = "";
    private UserType userType;
    
    public SignUpRequest() {
        initialiseErrorArray(6);
        id = "";
        title = "";
        firstName = "";
        surname = "";
        email = "";
    }
    
    public void resetForm() {
        id = "";
        title = "";
        firstName = "";
        surname = "";
        email = "";
        clearErrors();
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public UserType getUserType() {
        return userType;
    }

    public void setId(String id) {
        switch (userType) {
            case LECTURER:
                if (errorInString(id)) {
                    addErrorMessage(0, "Please enter your lecturer ID.");
                } else {
                    this.id = Validator.escapeJava(id);
                    setValidData(0, true);
                }
                break;
            default:
                if (errorInString(id)) {
                    addErrorMessage(0, "Please enter your student ID.");
                } else {
                    this.id = Validator.escapeJava(id);
                    setValidData(0, true);
                }
                break;
        }
    }
    
    public void setTitle(String title) {
        if (userType.equals(UserType.LECTURER)) {
            if (errorInString(title)) {
                addErrorMessage(1, "Incorrect title entered.");
                System.err.println("Error with title.");
            } else {
                this.title = Validator.escapeJava(title);
                setValidData(1, true);
            }
        } else {
            setValidData(1, true);
        }
    }
    
    public void setFirstName(String firstName) {
        if (errorInString(firstName)) {
            addErrorMessage(2, "Incorrect first name entered.");
            System.err.println("Error with first name.");
        } else {
            this.firstName = Validator.escapeJava(firstName);
            setValidData(2, true);
        }
    }
    
    public void setSurname(String surname) {
        if (errorInString(surname)) {
            addErrorMessage(3, "Incorrect surname entered.");
            System.err.println("Error with surname.");
        } else {
            this.surname = Validator.escapeJava(surname);
            setValidData(3, true);
        }
    }
    
    public void setEmail(String email) {
        if (Validator.isValidEmail(email)) {
            this.email = Validator.escapeJava(email);
            setValidData(4, true);
        } else {
            addErrorMessage(4, "Incorrect email entered.");
            System.err.println("Error with email.");
        }
    }
    
    public String getId() {
        return Validator.unescapeJava(id);
    }
    
    public String getTitle() {
        return Validator.unescapeJava(title);
    }
    
    public String getFirstName() {
        return Validator.unescapeJava(firstName);
    }
    
    public String getSurname() {
        return Validator.unescapeJava(surname);
    }
    
    public String getEmail() {
        return Validator.unescapeJava(email);
    }
 
    private boolean validPasswords() {
        boolean result = true;
        
        String firstPassword = (String)getRequest().getParameter("password");
        String secondPassword = (String)getRequest().getParameter("rePassword");
        
        if (errorInString(firstPassword)) {
            addErrorMessage(5, "Enter a password");
            result = false;
        } else if (errorInString(secondPassword)) {
            addErrorMessage(5, "Please re-enter your password");
            result = false;
        } else if (!firstPassword.equals(secondPassword)) {
            addErrorMessage(5, "Passwords are different.");
            result = false;
        } else {
            setValidData(5, result);
        }
        return result;
    }
    
    /**
     * Inserts the new user into the database
     * Used when user signs up to the system
     * 
     * @return True if insert can be executed
     */
    public boolean signUp() {
        boolean result = false;
        if (validPasswords() && isValid()) {
            Database db = Database.getSetupDatabase();
            String passwordHash = Hash.sha1((String)getRequest().getParameter("password"));
            
            result = db.insert("INSERT INTO User (passwordHash, email, firstName, surname) "
                               + "VALUES (\"" + passwordHash + "\", \"" + email + "\", \"" + firstName + "\", \"" + surname + "\");");
            
            if (result) {
                int uid = db.getPreviousAutoIncrementID("User");
                switch (userType) {
                    case STUDENT:
                        result = db.insert("INSERT INTO Student (uid, studentid) "
                                   + "VALUES (\"" + uid + "\", \"" + id + "\");");
                        break;
                    case LECTURER:
                        result = db.insert("INSERT INTO Lecturer (uid, lecturerid, title) "
                                   + "VALUES (\"" + uid + "\", \"" + id + "\", \"" + title + "\");");
                        break;
                }
            }
            
            if (!result) {
                System.err.println("Problem creating user in database.");
            } else {
                resetForm();
                setFormLoaded(false);
            }
            db.close();
        }        
        return result;
    }
}