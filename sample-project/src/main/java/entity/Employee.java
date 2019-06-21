package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Table(name="employee")
@Entity
public class Employee {

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
    @javax.persistence.JoinColumn(name = "manager_id", referencedColumnName = "id")
	private Employee manager;
    
	@Column(name = "name", length=257)
	private String name;
	
	
}
