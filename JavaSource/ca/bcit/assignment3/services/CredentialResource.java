package ca.bcit.assignment3.services;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.CredentialManager;
import ca.bcit.infosys.employee.Credentials;

@Path("/credentials")
/**
 * Handling activities related to Credentials using REST.
 * @author Sham, Kang
 * @version 1.0
 *
 */
public class CredentialResource {
    
   @Inject
   private CredentialManager credentialDB;

   /**
    * Default CredentialResource constructor.
    */
   public CredentialResource() {
   }

  

   @GET
   @Path("{id}")
   @Produces("application/xml")
   /**
    * Gets a specific credential entry, by EmpNum.
    * @param id identifier for the entry
    * @return the found Credential entry
    */
   public Credentials getCredential(@PathParam("id") int id) {
       Credentials credential = credentialDB.find(id);
      if (credential == null) {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return credential;
   }
   
   @GET
   @Produces("application/xml")
   public Credentials[] getAll() {
       return credentialDB.getAll();
   }
}
