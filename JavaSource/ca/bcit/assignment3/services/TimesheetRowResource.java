package ca.bcit.assignment3.services;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.TimesheetManager;
import ca.bcit.assignment3.access.TimesheetRowManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.TimesheetModel;
import ca.bcit.assignment3.model.TimesheetRowModel;

@Path("/timesheetrows")
public class TimesheetRowResource {
    
    @Inject 
    private TokenManager tokenDB;
    
    @Inject
    private TimesheetRowManager timesheetRowDB;
    
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public TimesheetRowModel getTimesheetRow(@PathParam("id") int id) {
       TimesheetRowModel tr = timesheetRowDB.find(id);
       if (tr == null)
       {
          throw new WebApplicationException(Response.Status.NOT_FOUND);
       }
       return tr;
    }
    
    @GET
    @Produces("application/xml")
    public TimesheetRowModel[] getAll() {
        return timesheetRowDB.getAll();
    }

}
