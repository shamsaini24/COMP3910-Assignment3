package ca.bcit.assignment3.viewcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.assignment3.access.CredentialManager;
import ca.bcit.assignment3.access.EmployeeManager;
import ca.bcit.assignment3.model.CredentialsModel;
import ca.bcit.assignment3.model.EmployeeModel;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named("employee")
@SessionScoped

/**
 *  bean that talks to the templates and defines the employee
 * @author Sham, Kang
 *
 */
public class EmployeeController implements EmployeeList {
    
    /** Manager for Employee objects.*/
    @Inject private EmployeeManager employeeManager;
    
    /** Manager for Credential objects.*/
    @Inject private CredentialManager credentialManager;
    
    /** List to store all employees. */
    List<Employee> employeeList;
    
    /**
     * reference to the credentials of the current user.
     */
    private CredentialsModel creds = new CredentialsModel();
    
    /**
     * reference to the current user.
     */
    private EmployeeModel currentEmployee = new EmployeeModel();
    
    /**
     * employees getter.
     * @return the ArrayList of users.
     */
    public List<Employee> getEmployees() {
        return Arrays.asList(employeeManager.getAll());
    }

    /**
     * Returns employee with a given name.
     * @param username the name field of the employee
     * @return the employees.
     */
    @Override
    public Employee getEmployee(String username) {
        return employeeManager.find(username);
    }

    /**
     * Returns employee with a given name.
     * @return the employees.
     */
    @Override
    public Map<String, String> getLoginCombos() {
        return null;
    }

    /**
     * getter for currentEmployee property.  
     * @return the current user.
     */
    @Override
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Assumes single administrator and returns the employee object
     * of that administrator.
     * @return the administrator user object.
     */
    @Override
    public Employee getAdministrator() {
        return employeeManager.find("admin");
    }
    
    /**
     * login method that logs the user in.
     * @return String
     */
    public String login() {
        if (verifyUser(creds)) {
            String name = creds.getUserName();
            currentEmployee = new EmployeeModel(employeeManager.find(name));
            System.out.println("in here");
            
            currentEmployee.setLoggedIn(true);
            return "home";
        } else {
            return "fail";
        }
    }

    /**
     * Verifies that the loginID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return true if it is, false if it is not.
     */
    @Override
    public boolean verifyUser(Credentials credential) {
        String name = credential.getUserName();
        String password = credential.getPassword();
        System.out.println(password);
        Employee loginEmployee = employeeManager.find(name);
        if (loginEmployee == null) {
            return false;
        }
        int loginNum = loginEmployee.getEmpNumber();
        if (credentialManager.find(loginNum) != null) {
            return credentialManager.find(loginEmployee.getEmpNumber())
                    .getPassword().equals(password);
        } else {
            return false;
        }
    }

    /**
     * Logs the user out of the system.
     *
     * @param employee the user to logout.
     * @return a String representing the login page.
     */
    @Override
    public String logout(Employee employee) {
        currentEmployee.setLoggedIn(false);
        creds = new CredentialsModel();
        return "login";
    }

    /**
     * Deletes the specified user from the collection of Users.
     *
     * @param userToDelete the user to delete.
     */
    @Override
    public void deleteEmployee(Employee userToDelete) {
        credentialManager.remove(credentialManager.
                find(userToDelete.getEmpNumber()));
        employeeManager.remove(userToDelete);
        refreshList();
    }

    /**
     * Adds a new Employee to the collection of Employees.
     * @param newEmployee the employee to add to the collection
     */
    @Override
    public void addEmployee(Employee newEmployee) {
       employeeManager.persist(newEmployee);
    }
    
    /**
     * creates new employee and adds it to the database.
     */
    public void createEmployee() {
        Employee[] empList = employeeManager.getAll();
        int lastEmpNum = empList[empList.length - 1].getEmpNumber();
        EmployeeModel empD = new EmployeeModel("NewUser" + (lastEmpNum + 1),
                lastEmpNum + 1, "NewUser" + (lastEmpNum + 1));
        empD.setEditable(true);
        addEmployee(empD);
        CredentialsModel newCred = new CredentialsModel(
                empD, "NewUser" + empD.getEmpNumber(), "defaultPassword");
        credentialManager.persist(newCred);
        refreshList();
    }

    /**
     * setter for the creds of the current user.
     * @param creds CredentialsModel
     */
    public void setCreds(CredentialsModel creds) {
        this.creds = creds;
    }
    
    /**
     * getter for the creds of the current user.
     * @return creds
     */
    public CredentialsModel getCreds() {
        return this.creds;
    }
    
    /**
     * getter for the password of the current user.
     * @param em Employee
     * @return String
     */
    public String getPassword(Employee em) {
        return credentialManager.find(em.getEmpNumber()).getPassword();
    }
    
    /**
     * setter for the password once the users changes it, replaces 
     * update statement.
     * @param old String
     * @param newPass String
     * @param repeat String
     * @return String
     */
    public String changePassword(String old, String newPass, String repeat) {
        Employee em = getCurrentEmployee();
        if (creds.getPassword().equals(old)) {
            if (newPass.equals(repeat)) {
                creds.setPassword(newPass);
                credentialManager.merge(creds);
                return "changePwdSuccess";
            } else {
                return "changePwdFail";
            }
        } else {
            return "changePwdFail";
        }
    }

    /**
     * saves the new employee to the database.
     * @param em Employee
     * @param password String
     * @return String
     */
    public String save(Employee em, String password) {
        if (password.length() < 1 || password == null) {
            System.out.println("password is not legible");
            password = ((EmployeeModel) em).getCreds().getPassword();
        }
        System.out.println("Password" + password);
        System.out.println("Username" + em.getUserName());
        ((EmployeeModel) em).setEditable(false);
        CredentialsModel newCred = new CredentialsModel(employeeManager.
                find(em.getEmpNumber()), em.getUserName(), password);
        credentialManager.merge(newCred);
        employeeManager.merge((EmployeeModel) em);
        return null;
    }

    /**
     * set the current Employee.
     * @param currentEmployee EmployeeModel
     */
    public void setCurrentEmployee(EmployeeModel currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    /**
     * get the employee list.
     * @return List<Employee>
     */
    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            refreshList();
        }
        return employeeList;
    }
    
    /**
     * refresh the list to show on the table.
     */
    public void refreshList() {
        employeeList = getEmployees();
    }

    /**
     * set the employee list.
     * @param employeeList List<Employee>
     */
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}