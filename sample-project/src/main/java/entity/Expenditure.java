package entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * I made this table just to be big (bigger by comparison to the other little tables in the project 
 * @author myoung
 *
 */
@Table(name="expenditure")
@Entity
public class Expenditure {

	@Id
	@Column(unique = true,name="id",length=16)
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;
	

    @javax.persistence.ManyToOne(fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;
    
    
    @javax.persistence.ManyToOne(fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee assignee;
    
    
    
	@Column(name = "description", length=721) // I know that length is random.  It is about building a good sample
	private String description;
	
	
	@Column(name = "amount", length = 30, scale = 2)
	private  BigDecimal amount;

	@Column(name = "budgeted", length = 30, scale = 2)
	private  BigDecimal budgeted; // This is null if  not planned
	
	@Column(name = "overage", length = 30, scale = 2)
	private  BigDecimal overage;
	
	@Column(name = "cap_ex")
	private  Boolean capitalEpenditure;
	
	@Column(name = "required_supervisor_approval")
	private  Boolean requiredSupervisorApproval;
	
	@Column(name = "recurring")
	private  Boolean recurring;
	
	@Column(name = "planned")
	private  Boolean planned; //an unplanned expense is not always an emergency expense.
							  // unplanned expenses still need to go through normal requisition process
	
	@Column(name = "emergency")
	private  Boolean emergency; // All emergency are unplanned (planned = false) but not all unplanned
								// expenditures are emergency
	
	   
	@Column(name = "emergency_justification", length=3999)
	private String emergencyJustification;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_ts")
	private Date createTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_ts")
	private Date updateTs;

	@Column(name = "create_username")
	private String createUsername;

	@Column(name = "update_username")
	private String updateUsername;
	
	
}
