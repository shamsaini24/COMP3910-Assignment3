package ca.bcit.assignment3.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("newPass")
@RequestScoped

/**
 * bean that will help with changing passwords
 *
 */
public class ChangePassword {
    /**
     * field for old password.
     */
    private String oldPassword;
    
    /**
     * field for new password.
     */
    private String newPassword;
    
    /**
     * field for new password repeated.
     */
    private String repeatPassword;

    /**
     * getter for oldPassword.
     * @return oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Setter for oldPasswrod.
     * @param oldPassword String
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * getter for newPassword.
     * @return newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * getter for newPassword.
     * @param newPassword String
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * getter for repeatPassword.
     * @return repeatPassword
     */
    public String getRepeatPassword() {
        return repeatPassword;
    }

    /**
     * getter for repeatPassword.
     * @param repeatPassword String
     */
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
