package ca.bcit.assignment3.services;

import java.net.URI;
import java.util.Base64;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.CredentialManager;
import ca.bcit.assignment3.access.EmployeeManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.CredentialsModel;
import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.assignment3.model.TokenModel;


@Path("/login")
public class LoginResource {
    @Inject
    private CredentialManager credentialDB;
    
    @Inject
    private EmployeeManager employeeDB;
  
    @Inject TokenManager tokenDB;

    public LoginResource(){}

    
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public TokenModel verifyLogin(CredentialsModel credential) {
        
        EmployeeModel emp = employeeDB.find(credential.getUserName());
        
       if(credentialDB.find(emp.getEmpNumber()) == null) {//If credentials exist
           return null;
       } else {
           String originalInput = String.valueOf(emp.getEmpNumber());
           String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
           if(tokenDB.find(emp.getEmpNumber()) != null) { //If token already exists
               return tokenDB.find(emp.getEmpNumber());
           }
           TokenModel userToken  = new TokenModel(encodedString, emp.getEmpNumber(), new Date());
           tokenDB.persist(userToken);
//           byte[] decodedBytes = Base64.getUrlDecoder().decode(testoken.getToken());
//           String decodedUrl = new String(decodedBytes);
           
           return userToken;
       }
    }
    
    @DELETE
    public String logout(@QueryParam("token") String token ) {
        if(tokenDB.verifyToken(token)) {
            tokenDB.remove(token);
            return "Logged Out";
        }
     return "Logout Failed";
    }

//    @GET
//    @Path("{id}")
//    @Produces("application/xml")
//    public Credentials getCredential(@PathParam("id") int id)
//    {
//        Credentials credential = credentialDB.find(id);
//       if (credential == null)
//       {
//          throw new WebApplicationException(Response.Status.NOT_FOUND);
//       }
//       return credential;
//    }

//    @PUT
//    @Path("{id}")
//    @Consumes("application/xml")
//    public void updateSupplier(@PathParam("id") int id, Employee update)
//    {
//        Credentials current = credentialDB.find(id);
//       if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
//
////       current.setAddress(update.getAddress());
////       current.setCity(update.getCity());
////       current.setContactName(update.getContactName());
////       current.setContactTitle(update.getContactTitle());
////       current.setCountry(update.getCountry());
////       current.setEmailAddress(update.getEmailAddress());
////       current.setFaxNumber(update.getFaxNumber());
////       current.setNotes(update.getNotes());
////       current.setPaymentTerms(update.getPaymentTerms());
////       current.setPhoneNumber(update.getPhoneNumber());
////       current.setPostalCode(update.getPostalCode());
////       current.setStateOrProvince(update.getStateOrProvince());
////       current.setName(update.getName());
//       
//       credentialDB.merge((CredentialsModel) current);
//    }
    
    @GET
    @Produces("application/xml")
    public TokenModel[] getAll() {
        return tokenDB.getAll();
    }
}
