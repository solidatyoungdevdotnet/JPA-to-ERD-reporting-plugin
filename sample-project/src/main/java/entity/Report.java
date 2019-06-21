package entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Entity
@Table(name = "report",uniqueConstraints = @UniqueConstraint(columnNames = {
		"name","create_username"}))
public class Report {
	private static final Logger LOGGER = LoggerFactory.getLogger(Report.class);


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@Column(name = "category", length = 128, nullable = false)
	private String category;

	@Column(name = "name", length = 128, nullable = false)
	private String name;
	@Column(name = "is_public")
	private Boolean isPublic;
	@Column(name = "description", length = 128, nullable = false)
	private String description;

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

	@javax.persistence.OneToOne(fetch = FetchType.EAGER, mappedBy = "report", cascade = CascadeType.ALL, targetEntity = ReportDefinition.class)
	private ReportDefinition reportDefinition;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTs() {
		return this.createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public Date getUpdateTs() {
		return this.updateTs;
	}

	public void setUpdateTs(Date updateTs) {
		this.updateTs = updateTs;
	}

	public String getCreateUsername() {
		return this.createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public String getUpdateUsername() {
		return this.updateUsername;
	}

	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}

	public ReportDefinition getReportDefinition() {
		return reportDefinition;
	}

	public void setReportDefinition(ReportDefinition reportDefinition) {
		this.reportDefinition = reportDefinition;
	}

	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Version: ").append(getVersion()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		sb.append("IsPublic: ").append(getIsPublic()).append(", ");
		sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("CreateTs: ").append(getCreateTs()).append(", ");
		sb.append("UpdateTs: ").append(getUpdateTs()).append(", ");
		sb.append("CreateUsername: ").append(getCreateUsername()).append(", ");
		sb.append("UpdateUsername: ").append(getUpdateUsername());
		return sb.toString();
	}

}
