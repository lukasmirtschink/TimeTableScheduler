package userPackage;

/**
 * Administrator for the system
 * 
 * @author John O Riordan
 */
public final class Admin extends User {
    private String adminID;
    
    public Admin(String adminID, String email, String firstName, String surName, String userID) {
        super(email, firstName, surName, userID);
        this.adminID = adminID;
    }

    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
    
    /**
     * Returns the admin ID
     * 
     * @return Admin ID 
     */
    public String getAdminID() {
        return adminID;
    }

    /**
     * Sets the admin ID
     * 
     * @param adminID admin ID
     */
    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
    
    /**
     * Remove a user from the database
     * 
     * @return True if the user is removed successfully
     */
    public boolean removeUser() {
        return false;
    }
    
    /**
     * Validate whether a lecturer is actually a lecturer
     * 
     * @return True if the lecturer is valid
     */
    public boolean validateLecturer() {
        return false;
    }
    
    /**
     * Perform a backup of the system
     */
    public void backupDatabse() {
    }
}
