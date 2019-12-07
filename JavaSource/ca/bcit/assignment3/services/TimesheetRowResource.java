package ca.bcit.assignment3.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import ca.bcit.assignment3.access.TimesheetManager;
import ca.bcit.assignment3.access.TimesheetRowManager;
import ca.bcit.assignment3.access.TokenManager;
import ca.bcit.assignment3.model.TimesheetModel;
import ca.bcit.assignment3.model.TimesheetRowModel;
import ca.bcit.assignment3.model.TokenModel;


/**
 * 
 * Handling TimesheetRow CRUD on RESTful Web
 * @author Sham, Kang
 * @version 1.0
 */
@Path("/timesheetrows")
public class TimesheetRowResource {
    
    @Inject 
    private TokenManager tokenDB;
    
    @Inject
    private TimesheetRowManager timesheetRowDB;
 // TimesheetDB
    @Inject
    private TimesheetManager timesheetDB;
    
    
    /**
     * Creating a new timesheetrow
     * @param token
     * @param tr
     * @return
     */
    @POST
    @Consumes("application/xml")
    public Response createTimesheetRow(@QueryParam("token") String token, TimesheetRowModel tr) {
        TokenModel retrivedToken = tokenDB.find(token);
        System.out.println("Row R: " + retrivedToken.getEmpNum());
        if(tokenDB.verifyToken(token)) {
            TimesheetRowModel newModel = new TimesheetRowModel(timesheetRowDB.getAll().length, getCurrentTimesheet(retrivedToken.getEmpNum()),
                    tr.getProjectID(), tr.getWorkPackage(), tr.getHour(0), tr.getHour(1), tr.getHour(2), tr.getHour(3),
                    tr.getHour(4), tr.getHour(5), tr.getHour(6), tr.getNotes());
            timesheetRowDB.persist(newModel);
        }
        return Response.notModified().build();
    }
    
    /**
     * update the current week timesheet.
     * @param id
     * @param token
     * @param update
     */
    @PUT
    @Path("{timesheetrowid}")
    @Consumes("application/xml")
    public void updateTimesheetRow(@PathParam("timesheetrowid") int id, @QueryParam("token") String token, TimesheetRowModel update) {
        if(tokenDB.verifyToken(token)) {
            TimesheetRowModel current = timesheetRowDB.find(id);
            if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
            if(isCurrentTimesheet(current.getTimesheetModel())) {
                current.setProjectID(update.getProjectID());
                current.setWorkPackage(update.getWorkPackage());
                BigDecimal[] bd = new BigDecimal[]{update.getHour(0), update.getHour(1), update.getHour(2),
                        update.getHour(3), update.getHour(4), update.getHour(5), update.getHour(6)};
                current.setHoursForWeek(bd);
                current.setNotes(update.getNotes());
                
                timesheetRowDB.merge(current);
            }
        }
    }
    
    /**
     * getting the specific timesheetrow by timesheetrow id
     * @param token
     * @param id
     * @return
     */
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
    
    /**
     * gettting the current timesheet
     * @param empNum
     * @return
     */
    private TimesheetModel getCurrentTimesheet(int empNum) {
        
        TimesheetModel[] tsArr = timesheetDB.getByEmployee(empNum);
        
        for(int i = 0; i < tsArr.length; i++) {
            if(isCurrentTimesheet(tsArr[i])) {
                return tsArr[i];
            }
        }
        return null;
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
