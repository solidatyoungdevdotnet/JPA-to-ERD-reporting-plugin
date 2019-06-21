package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
@Table(name="report_definition")
@Entity
public class ReportDefinition {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;


//	@Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
//	@Basic(fetch = FetchType.LAZY)
//	@Type(type="org.hibernate.type.BinaryType")
//	@Lob
//    @Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
	@Column(name="definition", columnDefinition="bytea")
	private byte[] definition;
	@javax.persistence.OneToOne( fetch=FetchType.LAZY)
	@javax.persistence.JoinColumn(name = "report_id",referencedColumnName="id")
	private Report report;	

	

	public ReportDefinition(byte[] xml)
	{
		this.definition=xml;
	}
	
	public ReportDefinition() {
        super();
    }

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

	


	public byte[] getDefinition() {
        return this.definition;
    }

	public void setDefinition(byte[] definition) {
        this.definition = definition;
    }

	public Report getReport() {
        return this.report;
    }

	public void setReport(Report report) {
        this.report = report;
    }
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Definition: ").append(java.util.Arrays.toString(getDefinition())).append(", ");
        sb.append("Report: ").append(getReport());
        return sb.toString();
    }
	
	public String getDefinitionString()
	{
		return new String(this.getDefinition());
	}

	
}
