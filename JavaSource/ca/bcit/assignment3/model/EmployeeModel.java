package ca.bcit.assignment3.model;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * EmployeeModel extends Employee to add in the primary key and editable.
 * @author Sham, Kang
 * @version 1.0
 */
public class EmployeeModel extends ca.bcit.infosys.employee.Employee {
    /**
     * boolean to tell us if the employee is admin.
     */
    private boolean admin;
    
    /**
     * boolean that tells us if the employee is logged in.
     */
    private boolean loggedIn;
    
    /**
     * boolean to edit the users.
     */
    private boolean editable;
    
    Credentials creds = new Credentials();
    /**
     * The argument-containing constructor. Used to create the initial employees.
     * who have access as well as the administrator.
     *
     * @param empName the name of the employee.
     * @param number the empNumber of the user.
     * @param id the loginID of the user.
     */
    public EmployeeModel(final String empName, final int number, final String id) {
        super(empName, number, id);
        if(id.equals("admin")) {
            setAdmin(true);
        }
        setLoggedIn(false);
        setEditable(false);
    }
    
    /**
     * Default constructor to set everything to default.
     */
    public EmployeeModel () {
        super();
        setAdmin(false);
        setLoggedIn(false);
        setEditable(false);
    }
    
    /**
     * Constructor to set the admin logged in user.
     * @param emp Employee
     */
    public EmployeeModel(Employee emp) {
        super(emp.getName(), emp.getEmpNumber(), emp.getUserName());
        if (emp.getUserName().equals("admin")) {
            setAdmin(true);
        } else {
            setAdmin(false);
        }
        setLoggedIn(false);
        setEditable(false);
    }

    /**
     * check if logged in user is admin.
     * @return boolean
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * set the admin.
     * @param admin boolean
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * check if any employee logged in.
     * @return boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * set the logged in.
     * @param loggedIn boolean
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * check if the fields are editable.
     * @return editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * set the editable.
     * @param editable boolean
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * get the credentials.
     * @return Credentials
     */
    public Credentials getCreds() {
        return creds;
    }

    /**
     * set the credentials.
     * @param creds Credentials
     */
    public void setCreds(Credentials creds) {
        this.creds = creds;
    }
}