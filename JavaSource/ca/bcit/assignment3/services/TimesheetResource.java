package ca.bcit.assignment3.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import ca.bcit.assignment3.access.TimesheetManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.assignment3.model.TimesheetModel;
import ca.bcit.assignment3.model.TokenModel;

/**
 * 
 * Handling Timesheet CRUD on RESTful Web
 * @author Sham, Kang
 * @version 1.0
 */
@Path("/timesheets")
public class TimesheetResource {
    
    // Token DB
    @Inject 
    private TokenManager tokenDB;
    // TimesheetDB
    @Inject
    private TimesheetManager timesheetDB;
    @Inject
    private EmployeeManager employeeDB;
    
    /**
     * create a new timesheet if the current time sheet does not exist
     * @param token
     * @param tm
     * @return
     */
    @POST
    @Consumes("application/xml")
    public Response createTimesheet(@QueryParam("token") String token) {
        TokenModel retrivedToken = tokenDB.find(token);
        //if no current time sheet
        if(tokenDB.verifyToken(token) && !hasCurrentTimesheet(retrivedToken.getEmpNum())) {
            
            TimesheetModel tm = new TimesheetModel(timesheetDB.getAll().length, 
                    employeeDB.find(retrivedToken.getEmpNum()), calculateCurrentEndWeek());
            timesheetDB.persist(tm);
        }
        return Response.notModified().build();
    }
    
    /**
     * getting a specific timesheet by id for a logged in employee
     * @param token
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public TimesheetModel getTimesheet(@QueryParam("token") String token, @PathParam("id") int id) {
        TokenModel retrivedToken = tokenDB.find(token);
        TimesheetModel[] tm = null;
        // getting all timesheet for currently loggin employee
        if(tokenDB.verifyToken(token)) {
            tm = timesheetDB.getByEmployee(retrivedToken.getEmpNum());
        }
        // get the timesheet by id
        if(tm != null) {
            for(int i = 0; i < tm.length; i++) {
                if(tm[i].getTimesheetId() == id) {
                    return tm[i];
                }
            }
        }
        // throws if timesheet does not exist
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    /**
     * Getting all timesheet for the logged in employee
     * @param token
     * @return
     */
    @GET
    @Produces("application/xml")
    public TimesheetModel[] getAll(@QueryParam("token") String token) {
        TokenModel retrivedToken = tokenDB.find(token);
        if(tokenDB.verifyToken(token)) {
            return timesheetDB.getByEmployee(retrivedToken.getEmpNum());
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    /**
     * check if the user already has the timesheet for current week
     * @param empNum
     * @return
     */
    private boolean hasCurrentTimesheet(int empNum) {
        TimesheetModel[] tsArr = timesheetDB.getByEmployee(empNum);
        
        for(int i = 0; i < tsArr.length; i++) {
            if (calculateCurrentEndWeek().getYear() 
                    == tsArr[i].getEndWeek().getYear()) {
                if (calculateCurrentEndWeek().getMonth() 
                        == tsArr[i].getEndWeek().getMonth()) {
                    if (calculateCurrentEndWeek().getDay() 
                            == tsArr[i].getEndWeek().getDay()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * check if the timesheet is current timesheet
     * @param tm
     * @return
     */
    private boolean isCurrentTimesheet(TimesheetModel tm) {
        if (calculateCurrentEndWeek().getYear() 
                == tm.getEndWeek().getYear()) {
            if (calculateCurrentEndWeek().getMonth() 
                    == tm.getEndWeek().getMonth()) {
                if (calculateCurrentEndWeek().getDay() 
                        == tm.getEndWeek().getDay()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Calculates the current, real time end week.
     * @return Date current end week
     */
    private Date calculateCurrentEndWeek() {
        final Calendar c = new GregorianCalendar();
        final int currentDay = c.get(Calendar.DAY_OF_WEEK);
        final int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        return c.getTime();
    }
}
