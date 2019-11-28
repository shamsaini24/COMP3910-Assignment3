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

import ca.bcit.assignment3.model.TimesheetRowModel;

/**
 * Handel CRUD actions for TimesheetRows class.
 * @author Sham, Kang
 * @version 1.0
 *
 */
public class TimesheetRowManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/employeeTimesheet")
    private DataSource ds;

    /** Manager for Timesheet objects.*/
    @Inject private TimesheetManager timesheetManager;
    
    /**
     * Find a TimesheetRows record from database.
     * 
     * @param id primary key for record.
     * @return the TimesheetRows record with key = id, null if not found.
     */
    public TimesheetRowModel find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM TimesheetRows "
                                    + "where TimesheetRowId = '" + id + "'");
                    if (result.next()) {
                        return new TimesheetRowModel(result.
                                getInt("TimesheetRowId"),
                                timesheetManager.find(result.
                                        getInt("TimesheetId")), 
                                result.getInt("ProjectID"),
                                result.getString("WorkPackage"),
                                result.getBigDecimal("SatHours"),
                                result.getBigDecimal("SunHours"),
                                result.getBigDecimal("MonHours"),
                                result.getBigDecimal("TueHours"),
                                result.getBigDecimal("WedHours"),
                                result.getBigDecimal("ThursHours"),
                                result.getBigDecimal("FriHours"),
                                result.getString("Notes"));
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
     * Persist TimesheetRowModel record into database. id must be unique.
     * 
     * @param timesheetRow the record to be persisted.
     */
    public void persist(TimesheetRowModel timesheetRow) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO TimesheetRows VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setInt(1, timesheetRow.getTimesheetRowId());
                    stmt.setInt(2, timesheetRow.getTimesheetModel().getTimesheetId());
                    stmt.setInt(3, timesheetRow.getProjectID());
                    stmt.setString(4, timesheetRow.getWorkPackage());
                    stmt.setBigDecimal(5, timesheetRow.getHour(0));
                    stmt.setBigDecimal(6, timesheetRow.getHour(1));
                    stmt.setBigDecimal(7, timesheetRow.getHour(2));
                    stmt.setBigDecimal(8, timesheetRow.getHour(3));
                    stmt.setBigDecimal(9, timesheetRow.getHour(4));
                    stmt.setBigDecimal(10, timesheetRow.getHour(5));
                    stmt.setBigDecimal(11, timesheetRow.getHour(6));
                    stmt.setString(12, timesheetRow.getNotes());
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
            System.out.println("Error in persist " + timesheetRow);
            ex.printStackTrace();
        }
    }

    /**
     * merge TimeesheetRowModel record fields into existing database record.
     * 
     * @param timesheetRow the record to be merged.
     */
    public void merge(TimesheetRowModel timesheetRow) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection
                            .prepareStatement("UPDATE TimesheetRows SET TimesheetId = ?, ProjectID = ?, " 
                                    + "WorkPackage = ?, SatHours = ?, SunHours = ?, MonHours = ?, "
                                    + "TueHours = ?, WedHours = ?, ThursHours = ?, FriHours = ?, "
                                    + "Notes = ? WHERE TimesheetRowId =  ?");
                    stmt.setInt(1, timesheetRow.getTimesheetModel().getTimesheetId());
                    stmt.setInt(2, timesheetRow.getProjectID());
                    stmt.setString(3, timesheetRow.getWorkPackage());
                    stmt.setBigDecimal(4, timesheetRow.getHour(0));
                    stmt.setBigDecimal(5, timesheetRow.getHour(1));
                    stmt.setBigDecimal(6, timesheetRow.getHour(2));
                    stmt.setBigDecimal(7, timesheetRow.getHour(3));
                    stmt.setBigDecimal(8, timesheetRow.getHour(4));
                    stmt.setBigDecimal(9, timesheetRow.getHour(5));
                    stmt.setBigDecimal(10, timesheetRow.getHour(6));
                    stmt.setString(11, timesheetRow.getNotes());
                    stmt.setInt(12, timesheetRow.getTimesheetRowId());
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
            System.out.println("Error in merge " + timesheetRow);
            ex.printStackTrace();
        }
    }

    /**
     * Remove TimesheetRowModel from database.
     * 
     * @param timesheetRow record to be removed from database
     */
    public void remove(TimesheetRowModel timesheetRow) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM TimesheetRows WHERE TimesheetRowId = ?");
                    stmt.setInt(1, timesheetRow.getTimesheetRowId());
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
            System.out.println("Error in remove " + timesheetRow);
            ex.printStackTrace();
        }
    }
    
    /**
     * Return TimesheetRows table for a given timesheet.
     * 
     * @return TimesheetRowModel[] of all records of a given timesheet
     *  from TimesheetRows table
     * @param timesheetId the ID of the timesheet to select
     */
    public TimesheetRowModel[] getByTimesheet(int timesheetId) {
        ArrayList<TimesheetRowModel> timesheetRows =
                new ArrayList<TimesheetRowModel>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "SELECT * FROM TimesheetRows WHERE TimesheetId = ? "
                            + "ORDER BY TimesheetRowId");
                    stmt.setInt(1, timesheetId);
                    ResultSet result = stmt.executeQuery();
                    while (result.next()) {
                        timesheetRows.add(new TimesheetRowModel(result.getInt("TimesheetRowId"),
                                timesheetManager.find(result.getInt("TimesheetId")), 
                                result.getInt("ProjectID"),
                                result.getString("WorkPackage"),
                                result.getBigDecimal("SatHours"),
                                result.getBigDecimal("SunHours"),
                                result.getBigDecimal("MonHours"),
                                result.getBigDecimal("TueHours"),
                                result.getBigDecimal("WedHours"),
                                result.getBigDecimal("ThursHours"),
                                result.getBigDecimal("FriHours"),
                                result.getString("Notes")));
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

        TimesheetRowModel[] timeRowarray =
                new TimesheetRowModel[timesheetRows.size()];
        return timesheetRows.toArray(timeRowarray);
    }

    /**
     * Return TimesheetRows table as array of TimesheetRowModels.
     * 
     * @return TimesheetRowModel[] of all records in TimesheetRows table
     */
    public TimesheetRowModel[] getAll() {
        Connection connection = null;        
        ArrayList<TimesheetRowModel> timesheetRows =
                new ArrayList<TimesheetRowModel>();
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM TimesheetRows ORDER BY TimesheetRowId");
                    while (result.next()) {
                        timesheetRows.add(new TimesheetRowModel(
                                result.getInt("TimesheetRowId"),
                                timesheetManager.find(result.
                                        getInt("TimesheetId")), 
                                result.getInt("ProjectID"),
                                result.getString("WorkPackage"),
                                result.getBigDecimal("SatHours"),
                                result.getBigDecimal("SunHours"),
                                result.getBigDecimal("MonHours"),
                                result.getBigDecimal("TueHours"),
                                result.getBigDecimal("WedHours"),
                                result.getBigDecimal("ThursHours"),
                                result.getBigDecimal("FriHours"),
                                result.getString("Notes")));
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

        TimesheetRowModel[] timeRowarray =
                new TimesheetRowModel[timesheetRows.size()];
        return timesheetRows.toArray(timeRowarray);
    }
}