package ca.bcit.assignment3.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import ca.bcit.assignment3.model.TimesheetModel;

/**
 * Handel CRUD actions for Timesheets class.
 * @author Sham, Kang
 * @version 1.0
 */
public class TimesheetManager implements Serializable {

    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/employeeTimesheet")
    private DataSource ds;

    /** Manager for Employee objects.*/
    @Inject private EmployeeManager employeeManager;
    
    /**
     * Find a Timesheet record from database.
     * 
     * @param id primary key for record.
     * @return the Timesheet record with key = id, null if not found.
     */
    public TimesheetModel find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Timesheets "
                                    + "where TimesheetId = '" + id + "'");
                    if (result.next()) {
                        return new TimesheetModel(result.getInt("TimesheetId"),
                                employeeManager.find(result.getInt("EmpNum")), 
                                result.getDate("EndWeek"));
                    } else {
                        return null;
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in find " + id);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Persist TimesheetModel record into database. id must be unique.
     * 
     * @param timesheet the record to be persisted.
     */
    public void persist(TimesheetModel timesheet) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO Timesheets VALUES (?, ?, ?)");
                    stmt.setInt(1, timesheet.getTimesheetId());
                    stmt.setInt(2, timesheet.getEmployee().getEmpNumber());
                    stmt.setDate(3, new java.sql.Date(
                            timesheet.getEndWeek().getTime()));
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in persist " + timesheet);
            ex.printStackTrace();
        }
    }

    /**
     * merge TimeesheetModel record fields into existing database record.
     * 
     * @param timesheet the record to be merged.
     */
    public void merge(TimesheetModel timesheet) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Timesheets "
                                    + "SET EmpNum = ? "
                                    + ", EndWeek = ?"
                                    + " WHERE TimesheetId = ?");
                    stmt.setInt(1, timesheet.getEmployee().getEmpNumber());
                    stmt.setDate(2, new java.sql.Date(
                            timesheet.getEndWeek().getTime()));
                    stmt.setInt(3, timesheet.getTimesheetId());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in merge " + timesheet);
            ex.printStackTrace();
        }
    }

    /**
     * Remove TimesheetModel from database.
     * 
     * @param timesheet record to be removed from database
     */
    public void remove(TimesheetModel timesheet) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Timesheets WHERE TimesheetId = ?");
                    stmt.setInt(1, timesheet.getTimesheetId());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in remove " + timesheet);
            ex.printStackTrace();
        }
    }
    
    /**
     * Return Timesheet table for a given employee.
     * 
     * @return TimesheetModel[] of all records of a given employee
     *  from Timesheet table
     * @param empNum the ID of the employee to select
     */
    public TimesheetModel[] getByEmployee(int empNum) {
        ArrayList<TimesheetModel> timesheets = new ArrayList<TimesheetModel>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "SELECT * FROM Timesheets WHERE EmpNum = ? "
                            + "ORDER BY TimesheetId");
                    stmt.setInt(1, empNum);
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        timesheets.add(new TimesheetModel(
                                result.getInt("TimesheetId"),
                                employeeManager.find(
                                        result.getInt("EmpNum")),
                                        result.getDate("EndWeek")));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }

        TimesheetModel[] timearray = new TimesheetModel[timesheets.size()];
        for (int i = 0; i < timesheets.size(); i++) {
            timearray[i] = timesheets.get(i);          
        }
        
        return timearray;
    }

    /**
     * Return Timesheet table as array of TimesheetModels.
     * 
     * @return TimesheetModel[] of all records in Timesheets table
     */
    public TimesheetModel[] getAll() {
        Connection connection = null;        
        ArrayList<TimesheetModel> timesheets = new ArrayList<TimesheetModel>();
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Timesheets ORDER BY TimesheetId");
                    while (result.next()) {
                        timesheets.add(new TimesheetModel(
                                result.getInt("TimesheetId"), 
                                employeeManager.find(result.getInt("EmpNum")),
                                result.getDate("EndWeek")));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }

        TimesheetModel[] timesheetArray = new TimesheetModel[timesheets.size()];
        return timesheets.toArray(timesheetArray);
    }
}