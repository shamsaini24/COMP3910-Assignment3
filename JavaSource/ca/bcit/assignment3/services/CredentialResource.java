package ca.bcit.assignment3.services;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.CredentialManager;
import ca.bcit.assignment3.model.CredentialsModel;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/credentials")
public class CredentialResource
{
   @Inject
   private CredentialManager credentialDB;

   public CredentialResource()
   {
   }

   
   //WE NEED TO FIGURE OUT HOW TO PASS A REFERENCE TO AN EMPLOYEE IN THE OBJECT
   @POST
   @Consumes("application/xml")
   public Response createCredential(CredentialsModel credential)
   {
      credentialDB.persist(credential);
      System.out.println("Created credential for " + credential.getUserName());
      return Response.created(URI.create("/credentials/" + credential.getUserName())).build();

   }

   @GET
   @Path("{id}")
   @Produces("application/xml")
   public Credentials getCredential(@PathParam("id") int id)
   {
       Credentials credential = credentialDB.find(id);
      if (credential == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return credential;
   }

   @PUT
   @Path("{id}")
   @Consumes("application/xml")
   public void updateSupplier(@PathParam("id") int id, Employee update)
   {
       Credentials current = credentialDB.find(id);
      if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

//      current.setAddress(update.getAddress());
//      current.setCity(update.getCity());
//      current.setContactName(update.getContactName());
//      current.setContactTitle(update.getContactTitle());
//      current.setCountry(update.getCountry());
//      current.setEmailAddress(update.getEmailAddress());
//      current.setFaxNumber(update.getFaxNumber());
//      current.setNotes(update.getNotes());
//      current.setPaymentTerms(update.getPaymentTerms());
//      current.setPhoneNumber(update.getPhoneNumber());
//      current.setPostalCode(update.getPostalCode());
//      current.setStateOrProvince(update.getStateOrProvince());
//      current.setName(update.getName());
      
      credentialDB.merge((CredentialsModel) current);
   }
   
   @GET
   @Produces("application/xml")
   public Credentials[] getAll() {
       return credentialDB.getAll();
   }
}
