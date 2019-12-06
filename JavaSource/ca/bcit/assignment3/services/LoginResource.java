package ca.bcit.assignment3.services;

import java.util.Base64;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ca.bcit.assignment3.access.CredentialManager;
import ca.bcit.assignment3.access.EmployeeManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.CredentialsModel;
import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.assignment3.model.TokenModel;


@Path("/login")
/**
 * Handling any Login and Logout related activities using REST.
 * @author Sham, Kang
 * @version 1.0
 *
 */
public class LoginResource {
    @Inject
    private CredentialManager credentialDB;
    
    @Inject
    private EmployeeManager employeeDB;
  
    @Inject 
    private TokenManager tokenDB;
    
    /**
     * Default LoginResource constructor.
     */
    public LoginResource(){
        
    }

    
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    /**
     * Used for login, verifies the user's credentials and creates a token.
     * @param credential
     * @return a Token for subsequent requests
     */
    public TokenModel verifyLogin(CredentialsModel credential) {
        
        EmployeeModel emp = employeeDB.find(credential.getUserName());
        
       //If credentials exist
       if (credentialDB.find(emp.getEmpNumber()) == null) {
           return null;
       } else {
           String originalInput = String.valueOf(emp.getEmpNumber());
           String encodedString = Base64.getEncoder().encodeToString(
                   originalInput.getBytes());
           
           //If token already exists
           if (tokenDB.find(emp.getEmpNumber()) != null) { 
               return tokenDB.find(emp.getEmpNumber());
           }
           TokenModel userToken  = new TokenModel(encodedString,
                   emp.getEmpNumber(), new Date());
           tokenDB.persist(userToken);
           
           return userToken;
       }
    }
    
    @DELETE
    /**
     * Logs out the user by deleting their token.
     * @param token
     * @return A String to indicate success or failure
     */
    public String logout(@QueryParam("token") String token) {
        if (tokenDB.verifyToken(token)) {
            tokenDB.remove(token);
            return "Logged Out";
        }
     return "Logout Failed";
    }
    
    @GET
    @Produces("application/xml")
    public TokenModel[] getAll() {
        return tokenDB.getAll();
    }
}
