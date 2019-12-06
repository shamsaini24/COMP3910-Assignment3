package ca.bcit.assignment3.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.bcit.assignment3.access.TimesheetManager;
import ca.bcit.assignment3.access.TimesheetRowManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.TimesheetModel;
import ca.bcit.assignment3.model.TimesheetRowModel;
import ca.bcit.assignment3.model.TokenModel;

@Path("/timesheetrows")
public class TimesheetRowResource {
    
    @Inject 
    private TokenManager tokenDB;
    
    @Inject
    private TimesheetRowManager timesheetRowDB;
 // TimesheetDB
    @Inject
    private TimesheetManager timesheetDB;
    
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public TimesheetRowModel getTimesheetRow(@QueryParam("token") String token, @PathParam("id") int id) {
        TokenModel retrivedToken = tokenDB.find(token);
        TimesheetModel[] tm = null;
        ArrayList<TimesheetRowModel> rowList = new ArrayList<TimesheetRowModel>();
        
        if(tokenDB.verifyToken(token)) {
            tm =  timesheetDB.getByEmployee(retrivedToken.getEmpNum());
        }
        if(tm != null) {
            for(int i = 0; i < tm.length; i++) {
                rowList.addAll(Arrays.asList(timesheetRowDB.getByTimesheet(tm[i].getTimesheetId())));
            }
            for(int i = 0; i < rowList.size(); i++) {
                if(id == rowList.get(i).getTimesheetRowId()) 
                    return rowList.get(i);
            }
        }
        
        
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    /**
     * getting all timesheetrows for the logged in employee
     * @param token
     * @return
     */
    @GET
    @Produces("application/xml")
    public TimesheetRowModel[] getAll(@QueryParam("token") String token) {
        TokenModel retrivedToken = tokenDB.find(token);
        TimesheetModel[] tm = null;
        ArrayList<TimesheetRowModel> rowList = new ArrayList<TimesheetRowModel>();
        
        if(tokenDB.verifyToken(token)) {
            tm =  timesheetDB.getByEmployee(retrivedToken.getEmpNum());
        }
        if(tm != null) {
            for(int i = 0; i < tm.length; i++) {
                rowList.addAll(Arrays.asList(timesheetRowDB.getByTimesheet(tm[i].getTimesheetId())));
            }
            TimesheetRowModel[] trArray = new TimesheetRowModel[rowList.size()];
            return rowList.toArray(trArray);
        }
        
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

}
