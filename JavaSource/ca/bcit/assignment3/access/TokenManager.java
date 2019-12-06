package ca.bcit.assignment3.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import ca.bcit.assignment3.model.TokenModel;

public class TokenManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/employeeTimesheet")
    private DataSource ds;

    /**
     * Find Token record from database.
     * 
     * @param username
     *            primary key for record.
     * @return the Employee record with key = id, null if not found.
     */
    public TokenModel find(String token) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Tokens "
                                    + "where TokenID = '" + token + "'");
                    if (result.next()) {
                        Timestamp time = result.getTimestamp("ExpTime");
                        Date dateTime = new java.util.Date(time.getTime());
                        return new TokenModel(result.getString("TokenID"),
                                result.getInt("EmpNum"),
                                dateTime);
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
            System.out.println("Error in find " + token);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find Token record from database.
     * 
     * @param id
     *            primary key for record.
     * @return the Employee record with key = id, null if not found.
     */
    public TokenModel find(int empNum) {
        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt
                            .executeQuery("SELECT * FROM Tokens "
                                    + "where EmpNum = '" + empNum + "'");
                    if (result.next()) {
                        
                        Timestamp time = result.getTimestamp("ExpTime");
                        Date dateTime = new java.util.Date(time.getTime());
                        return new TokenModel(
                                result.getString("TokenID"),
                                result.getInt("EmpNum"),
                                dateTime);
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
            System.out.println("Error in find " + empNum);
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
    public void persist(TokenModel token) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO Tokens VALUES (?, ?, ?, ?)");
                    stmt.setString(1, token.getToken());
                    stmt.setInt(2, token.getEmpNum());
                    stmt.setDate(3, new java.sql.Date(
                            token.getExpDate().getTime()));
                    stmt.setTimestamp(4, new java.sql.Timestamp(token.getExpDate().getTime()));
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
            System.out.println("Error in persist " + token);
            ex.printStackTrace();
        }
    }

//    /**
//     * merge Employee record fields into existing database record.
//     * 
//     * @param employee
//     *            the record to be merged.
//     */
//    public void merge(TokenModel token) {
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        try {
//            try {
//                connection = ds.getConnection();
//                try {
//                    stmt = connection.prepareStatement(
//                            "UPDATE Tokens "
//                            + "SET Token = ?"
//                            + ", EmpName = ?"
//                            + "WHERE EmpNum =  ?");
//                    stmt.setString(1, employee.getUserName());
//                    stmt.setString(2, employee.getName());
//                    stmt.setInt(3, employee.getEmpNumber());
//                    stmt.executeUpdate();
//                } finally {
//                    if (stmt != null) {
//                        stmt.close();
//                    }
//
//                }
//            } finally {
//                if (connection != null) {
//                    connection.close();
//                }
//            }
//        } catch (SQLException ex) {
//            System.out.println("Error in merge " + employee);
//            ex.printStackTrace();
//        }
//    }

    /**
     * Remove employee from database.
     * 
     * @param employee
     *            record to be removed from database
     */
    public void remove(TokenModel token) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Tokens WHERE TokenID =  ?");
                    stmt.setString(1, token.getToken());
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
            System.out.println("Error in remove " + token);
            ex.printStackTrace();
        }
    }
    
    /**
     * Remove employee from database.
     * 
     * @param employee
     *            record to be removed from database
     */
    public void remove(String token) {
        Connection connection = null;        
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Tokens WHERE TokenID =  ?");
                    stmt.setString(1, token);
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
            System.out.println("Error in remove " + token);
            ex.printStackTrace();
        }
    }

    /**
     * Return Employees table as array of Employees.
     * 
     * @return Employee[] of all records in Employees table
     */
    public TokenModel[] getAll() {
        Connection connection = null;        
        ArrayList<TokenModel> tokenList = new ArrayList<TokenModel>();
        Statement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Tokens ORDER BY EmpNum");
                    while (result.next()) {
                        tokenList.add(new TokenModel(
                                result.getString("TokenID"), 
                                result.getInt("EmpNum"),
                                result.getDate("ExpDate")));
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

        TokenModel[] catarray = new TokenModel[tokenList.size()];
        return tokenList.toArray(catarray);
    }
    
    public boolean verifyToken(String token) {
        TokenModel foundToken = find(token);
        Date date = new Date();
        long diff = date.getTime() - foundToken.getExpDate().getTime();
        long diffHours = diff / (60 * 60 * 1000) % 24;

        if(diffHours > 1) {
            remove(foundToken);
            return false;
        }
        return true;
    }
}
