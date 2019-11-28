package ca.bcit.assignment3.model;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

/**
 * 
 * CredentialModel extends Credential to add in the primary key of 
 * the Credential table.
 * @author Sham, Kang
 * @version 1.0
 */
public class CredentialsModel extends ca.bcit.infosys.employee.Credentials {

    /** reference to the employee that owens the credential. */
    private Employee employee;
    
    /**
     * default constructor to construct the employee.
     */
    public CredentialsModel() {
        super();
        employee = new Employee();
    }
    
    /**
     * Constructor to set the employee, employeeName, and password.
     * @param emp Employee
     * @param empUsername String
     * @param empPassword String
     */
    public CredentialsModel(Employee emp, String empUsername,
            String empPassword) {
        super();
        setUserName(empUsername);
        setPassword(empPassword);
        employee = emp;
    }
    
    /**
     * Constructor to set the Credentials.
     * @param cred Credential
     */
    public CredentialsModel(Credentials cred) {
        setUserName(cred.getUserName());
        setPassword(cred.getPassword());
        employee = new EmployeeModel();
    }
    
    /**
     * get the owner of the credential.
     * @return employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * setthe owner of the Credential.
     * @param employee a String
     */
    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
    }
}