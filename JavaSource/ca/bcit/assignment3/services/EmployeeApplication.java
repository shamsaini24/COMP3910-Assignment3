package ca.bcit.assignment3.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class EmployeeApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();

   public EmployeeApplication()
   {
      singletons.add(new EmployeeResource());
      singletons.add(new CredentialResource());
      singletons.add(new LoginResource());
      singletons.add(new TimesheetResource());
      singletons.add(new TimesheetRowResource());
      
      classes.add(EmployeeResource.class);
      classes.add(CredentialResource.class);
      classes.add(LoginResource.class);
      classes.add(TimesheetResource.class);
      classes.add(TimesheetRowResource.class);

   }
   
   @Override
   public Set<Class<?>> getClasses(){
       return classes;
   }

   
   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }
   
}
