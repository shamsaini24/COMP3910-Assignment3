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

import ca.bcit.assignment3.model.CredentialsModel;

/**
 * Handel CRUD actions for Credentials class
 * @author Sham, Kang
 * @version 1.0
 */
public class CredentialManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/employeeTimesheet")
    private DataSource ds;
    
    /** Manager for Employee objects.*/
    @Inject private EmployeeManager employeeManager;

    /**
     * Find a Credential record from database.
     * 
     * @param id
     *            primary key for record.
     * @return the Credential record with key = id, null if not found.
     */
    public CredentialsModel find(int id) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Credentials "
                                    + "where EmpNum = '" + id + "'");
                    if (result.next()) {
                        CredentialsModel cred = new CredentialsModel();
                        cred.setPassword(result.getString("EmpPassword"));
                        cred.setUserName(result.getString("EmpUsername"));
                        return cred;
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
     * Persist Credential record into database. id must be unique.
     * 
     * @param credential
     *            the record to be persisted.
     */
    public void persist(CredentialsModel credential) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO Credentials VALUES (?, ?, ?)");
                    stmt.setInt(1, credential.getEmployee().getEmpNumber());
                    stmt.setString(2, credential.getUserName());
                    stmt.setString(3, credential.getPassword());
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
            System.out.println("Error in persist " + credential);
            ex.printStackTrace();
        }
    }

    /**
     * merge Credential record fields into existing database record.
     * 
     * @param credential
     *            the record to be merged.
     */
    public void merge(CredentialsModel credential) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "UPDATE Credentials "
                            + "SET EmpPassword = ? "
                            + ", EmpUsername = ?"
                            + "WHERE EmpNum =  ?");
                    stmt.setString(1, credential.getPassword());
                    stmt.setString(2, credential.getUserName());
                    stmt.setInt(3, credential.getEmployee().getEmpNumber());
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
            System.out.println("Error in merge " + credential);
            ex.printStackTrace();
        }
    }

    /**
     * Remove Credential from database.
     * 
     * @param credential
     *            record to be removed from database
     */
    public void remove(CredentialsModel credential) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Credentials WHERE EmpUsername =  ?");
                    stmt.setString(1, credential.getUserName());
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
            System.out.println("Error in remove " + credential);
            ex.printStackTrace();
        }
    }

    /**
     * Return Credentials table as array of Credentials.
     * 
     * @return Credentials[] of all records in Credentials table
     */
    public CredentialsModel[] getAll() {
        Connection connection = null;        
        ArrayList<CredentialsModel> categories =
                new ArrayList<CredentialsModel>();
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Credentials ORDER BY EmpNum");
                    while (result.next()) {
                        categories.add(new CredentialsModel(
                                employeeManager.find(result.
                                        getString("EmpUsername")), 
                                result.getString("EmpUsername"),
                                result.getString("EmpPassword")));
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

        CredentialsModel[] catarray = new CredentialsModel[categories.size()];
        return categories.toArray(catarray);
    }
}