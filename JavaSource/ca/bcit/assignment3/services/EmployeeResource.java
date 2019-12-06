package ca.bcit.assignment3.services;


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
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

import java.net.URI;

@Path("/employees")
/**
 * Handling any Employee related activities using REST.
 * @author Sham, Kang
 * @version 1.0
 *
 */
public class EmployeeResource {
   @Inject
   private EmployeeManager employeeDB;
   
   @Inject
   private CredentialManager credentialDB;
   
   @Inject 
   private TokenManager tokenDB;
   
   /**
    * Default EmployeeResource constructor.
    */ 
   public EmployeeResource() {
   }

  
   /**
    * Creates an Employee, along with their Credentials.
    * @param token A String
    * @param employee Employee to be created
    * @return Response to indicate successs
    */
   @POST
   @Consumes("application/xml")
   //URI: .../COMP3910-Assignment3/services/employees?token=[Token String]
   public Response createEmployee(@QueryParam("token") String token,
           EmployeeModel employee) {
       TokenModel retrivedToken = tokenDB.find(token);
       if (tokenDB.verifyToken(token) && retrivedToken.getEmpNum() == 0) {
           employeeDB.persist(employee);
           Credentials creds = employee.getCreds();
           if (creds != null) {
               credentialDB.persist(new CredentialsModel(employee,
                       creds.getUserName(), creds.getPassword()));
           } else {
               credentialDB.persist(new CredentialsModel(employee,
                       employee.getUserName(), "defaultPassword"));
           }
           System.out.println("Created supplier " + employee.getEmpNumber());
           return Response.created(URI.create("/employees/" +
           employee.getEmpNumber())).build();
       }
       return Response.notModified().build();
   }
   
/**
 * Retrieves an employee by their id.
 * @param id identifies specific employee
 * @return Employee found
 */
   @GET
   @Path("{id}")
   @Produces("application/xml")
   public Employee getEmployee(@PathParam("id") int id) {
       Employee employee = employeeDB.find(id);
      if (employee == null) {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return employee;
   }

   /**
    * Update's an employee by their id.
    * @param id identifies specific employee
    * @param token verification token
    * @param update updated EmployeeModel
    */
   @PUT
   @Path("{empNum}")
   @Consumes("application/xml")
   public void updateEmployee(@PathParam("empNum") int id,
           @QueryParam("token") String token, EmployeeModel update) {
       
       TokenModel retrivedToken = tokenDB.find(token);
       if (tokenDB.verifyToken(token) && (retrivedToken.getEmpNum() == 0 
               || retrivedToken.getEmpNum() == update.getEmpNumber())) {
       
       Employee current = employeeDB.find(id);
       if (current == null) {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
       }
        
       current.setName(update.getName());
       current.setUserName(update.getUserName());
       current.setEmpNumber(update.getEmpNumber());
      
       employeeDB.merge((EmployeeModel) current);
       }
   }
   
   /**
    * Deletes an employee by their Employee Number.
    * @param empNum identifies an employee
    * @param token verification token
    * @return boolean, indicating success or failure
    */
   @DELETE
   @Path("{empNum}")
   public boolean deleteOrderByEmpNum(@PathParam("empNum") int empNum,
           @QueryParam("token") String token) {
       TokenModel retrivedToken = tokenDB.find(token);
       if (tokenDB.verifyToken(token) && retrivedToken.getEmpNum() == 0) {
           employeeDB.remove(empNum);
           credentialDB.remove(credentialDB.find(empNum));
           return true;
       }
    return false;
   }
   
   
   @GET
   @Produces("application/xml")
   public Employee[] getAll() {
       return employeeDB.getAll();
   }
}
