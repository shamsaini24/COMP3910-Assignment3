package ca.bcit.assignment3.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.sql.DataSource;

import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.infosys.employee.Employee;

/**
 * Handel CRUD actions for Employee class.
 * @author Sham, Kang
 * @version 1.0
 */
public class EmployeeManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/employeeTimesheet")
    private DataSource ds;

    /**
     * Find Employee record from database.
     * 
     * @param username
     *            primary key for record.
     * @return the Employee record with key = id, null if not found.
     */
    public EmployeeModel find(String username) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Employees "
                                    + "where EmpUsername = '" + username + "'");
                    if (result.next()) {
                        return new EmployeeModel(result.getString("EmpName"),
                                result.getInt("EmpNum"),
                                result.getString("EmpUsername"));
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
            System.out.println("Error in find " + username);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find Employee record from database.
     * 
     * @param id
     *            primary key for record.
     * @return the Employee record with key = id, null if not found.
     */
    public EmployeeModel find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Employees "
                                    + "where EmpNum = '" + id + "'");
                    if (result.next()) {
                        return new EmployeeModel(
                                result.getString("EmpName"),
                                result.getInt("EmpNum"),
                                result.getString("EmpUsername"));
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
     * Persist Employee record into database. id must be unique.
     * 
     * @param employee
     *            the record to be persisted.
     */
    public void persist(Employee employee) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO Employees VALUES (?, ?, ?)");
                    stmt.setInt(1, employee.getEmpNumber());
                    stmt.setString(2, employee.getUserName());
                    stmt.setString(3, employee.getName());
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
            System.out.println("Error in persist " + employee);
            ex.printStackTrace();
        }
    }

    /**
     * merge Employee record fields into existing database record.
     * 
     * @param employee
     *            the record to be merged.
     */
    public void merge(EmployeeModel employee) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Employees "
                            + "SET EmpUsername = ?"
                            + ", EmpName = ?"
                            + "WHERE EmpNum =  ?");
                    stmt.setString(1, employee.getUserName());
                    stmt.setString(2, employee.getName());
                    stmt.setInt(3, employee.getEmpNumber());
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
            System.out.println("Error in merge " + employee);
            ex.printStackTrace();
        }
    }

    /**
     * Remove employee from database.
     * 
     * @param employee
     *            record to be removed from database
     */
    public void remove(Employee employee) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Employees WHERE EmpNum =  ?");
                    stmt.setInt(1, employee.getEmpNumber());
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
            System.out.println("Error in remove " + employee);
            ex.printStackTrace();
        }
    }

    /**
     * Return Employees table as array of Employees.
     * 
     * @return Employee[] of all records in Employees table
     */
    public Employee[] getAll() {
        Connection connection = null;        
        ArrayList<EmployeeModel> employeeList = new ArrayList<EmployeeModel>();
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees ORDER BY EmpNum");
                    while (result.next()) {
                        employeeList.add(new EmployeeModel(
                                result.getString("EmpName"), 
                                result.getInt("EmpNum"),
                                result.getString("EmpUsername")));
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

        EmployeeModel[] catarray = new EmployeeModel[employeeList.size()];
        return employeeList.toArray(catarray);
    }
}