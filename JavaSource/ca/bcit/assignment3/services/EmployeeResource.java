package ca.bcit.assignment3.services;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.EmployeeManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.assignment3.model.TokenModel;
import ca.bcit.infosys.employee.Employee;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/employees")
public class EmployeeResource {
   @Inject
   private EmployeeManager employeeDB;
   
   @Inject 
   private TokenManager tokenDB;
   
   public EmployeeResource()
   {
   }

   @POST
   @Consumes("application/xml")
   public Response createEmployee(@QueryParam("token") String token, EmployeeModel employee) {//URI is as follows: .../COMP3910-Assignment3/services/employees?token=[Token String]
       TokenModel retrivedToken = tokenDB.find(token);
       if(tokenDB.verifyToken(token) && retrivedToken.getEmpNum()==0) {
           employeeDB.persist(employee);
           System.out.println("Created supplier " + employee.getEmpNumber());
           return Response.created(URI.create("/employees/" + employee.getEmpNumber())).build();
       }
       return Response.notModified().build();

      
   }

   @GET
   @Path("{id}")
   @Produces("application/xml")
   public Employee getEmployee(@PathParam("id") int id) {
       Employee employee = employeeDB.find(id);
      if (employee == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      return employee;
   }

   @PUT
   @Path("{id}")
   @Consumes("application/xml")
   public void updateSupplier(@PathParam("id") int id, Employee update) {
       Employee current = employeeDB.find(id);
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
      current.setName(update.getName());
      
      employeeDB.merge((EmployeeModel) current);
   }
   
   @GET
   @Produces("application/xml")
   public Employee[] getAll() {
       return employeeDB.getAll();
   }
}
