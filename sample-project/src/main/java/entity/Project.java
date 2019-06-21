package entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Table(name="project")
@Entity
public class Project {

	@Id
	@Column(unique = true,name="id",length=16)
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;
	

    @javax.persistence.ManyToOne(fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;
    
    
    @javax.persistence.ManyToOne(fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee assignee;
    
    @javax.persistence.ManyToOne(fetch = FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "supervisor_id", referencedColumnName = "id")
	private Employee supervisor;
    
	@Column(name = "name", length=257)
	private String name;
	
	
	@Column(name = "annual_budget", length = 30, scale = 2)
	private BigDecimal annualBudget;
	
	private transient BigDecimal unspent;
	
	
}
