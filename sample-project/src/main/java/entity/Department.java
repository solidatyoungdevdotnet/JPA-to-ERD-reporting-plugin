package entity;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;


@Table(name="department")
@Entity
public class Department {

	@Id
	@Column(unique = true,name="id",length=16)
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@Size(max = 90)
	@Column(name="name",length=90)
	private String name;

	@Size(max = 15)
	@Column(name="short_name",length=15)
	private String shortName;


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
	
	
    @OrderBy("name")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "deparment", targetEntity = Employee.class)
    private Set<Employee> employees;


	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
//
//	public static final EntityManager entityManager() {
//		EntityManager em = new Client().entityManager;
//		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//		return em;
//	}
//
//	public static List<Client> findByIdInList(List<Long> list) {
//		Query q= entityManager().createQuery(
//				"SELECT o FROM Client o where o.id in (:clientIds) order by o.id", Client.class);
//		q.setParameter("clientIds", list);
//		return q.getResultList();
//	}
//
//
//	@Transactional
//	public void persist() {
//		if (this.entityManager == null) this.entityManager = entityManager();
//		this.entityManager.persist(this);
//	}
//
//	@Transactional
//	public void remove() {
//		if (this.entityManager == null) this.entityManager = entityManager();
//		if (this.entityManager.contains(this)) {
//			this.entityManager.remove(this);
//		} else {
//			Client attached = findClient(this.id);
//			this.entityManager.remove(attached);
//		}
//	}
//
//	@Transactional
//	public void flush() {
//		if (this.entityManager == null) this.entityManager = entityManager();
//		this.entityManager.flush();
//	}
//
//	@Transactional
//	public void clear() {
//		if (this.entityManager == null) this.entityManager = entityManager();
//		this.entityManager.clear();
//	}
//
//	@Transactional
//	public Client merge() {
//		if (this.entityManager == null) this.entityManager = entityManager();
//		Client merged = this.entityManager.merge(this);
//		this.entityManager.flush();
//		return merged;
//	}
//
//
//
//	public static long countClients() {
//		return entityManager().createQuery("SELECT COUNT(o) FROM Client o", Long.class).getSingleResult();
//	}
//
//	public static List<Client> findAllClients() {
//		return entityManager().createQuery("SELECT o FROM Client o", Client.class).getResultList();
//	}
//
//	public static Client findClient(Long id) {
//		if (id == null) return null;
//		return entityManager().find(Client.class, id);
//	}
//
//	public static Client findClientWithFields(Long id) {
//		if (id == null) return null;
//		Query q = entityManager().createQuery("from Client o left join fetch o.fields where o.id=:id", Client.class);
//		q.setParameter("id", id);
//		return (Client)q.getSingleResult();
//	}
//
//	public static List<Client> findClientEntries(int firstResult, int maxResults) {
//		return entityManager().createQuery("SELECT o FROM Client o", Client.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
//	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getId()).append(" - ").append(getName());

		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<Employee> getEmployees() {
		if (employees == null) {
			employees = new TreeSet<>();
		}
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

}
