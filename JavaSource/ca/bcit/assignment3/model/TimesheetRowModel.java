package ca.bcit.assignment3.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TimesheetRowModel that extends TimesheetRow in order to create the
 *  primary key (timesheetRowId).
 * and link each TimesheetRowModel with the Timesheet
 * @author Sham, Kang
 * @version 1.0
 */
@XmlRootElement(name = "timesheetrow")
public class TimesheetRowModel extends ca.bcit.infosys.timesheet.TimesheetRow {

    /**
     * TimesheetModel wons the timesheetrow.
     */
    private TimesheetModel timesheetModel;
    /**
     * primary key for each timesheetrow.
     */
    @Id
    @Column(name="TimesheetRowID")
    private int timesheetRowId;
   
    
    public TimesheetRowModel() {
        super();
        timesheetModel = new TimesheetModel();
    }
    
    /**
     * TimesheetRow Constructor.
     * @param id
     * @param timesheetModel
     * @param projectId
     * @param wp
     * @param satHour
     * @param sunHour
     * @param monHour
     * @param tueHour
     * @param wedHour
     * @param thursHour
     * @param friHour
     * @param notes
     */
    public TimesheetRowModel(int id, TimesheetModel timesheetModel,
            int projectId, String wp, BigDecimal satHour, BigDecimal sunHour,
            BigDecimal monHour, BigDecimal tueHour, BigDecimal wedHour,
            BigDecimal thursHour, BigDecimal friHour, String notes) {
        // call super to construct parent
        super(projectId, wp, new BigDecimal[]{satHour, sunHour, monHour,
                tueHour, wedHour, thursHour, friHour}, notes);
        this.timesheetRowId = id;
        this.timesheetModel = timesheetModel;
    }
    /**
     * Getter for timesheetModel.
     * @return timesheetModel
     */
    public TimesheetModel getTimesheetModel() {
        return timesheetModel;
    }
    
    /**
     * Setter for timesheetModel.
     * @param timesheetModel TimesheetModel
     */
    public void setTimesheetModel(TimesheetModel timesheetModel) {
        this.timesheetModel = timesheetModel;
    }
    
    /**
     * Getter for timesheetRowId.
     * @return timesheetRowId
     */
    @XmlAttribute
    public int getTimesheetRowId() {
        return timesheetRowId;
    }
    
    /**
     * Setter for timesheetRowId.
     * @param timesheetRowId int
     */
    public void setTimesheetRowId(int timesheetRowId) {
        this.timesheetRowId = timesheetRowId;
    }
    
}
