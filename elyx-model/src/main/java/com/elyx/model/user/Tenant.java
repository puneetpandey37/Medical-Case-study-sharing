package com.elyx.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Table(name="tenant")
@NamedQueries({
    @NamedQuery(name = "Tenant.findAll", query = "SELECT t FROM Tenant t"),
    @NamedQuery(name = "Tenant.findByName", query = "SELECT t FROM Tenant t WHERE t.name = :name")})
public class Tenant {

	@Id
    @GeneratedValue
    @Column(name="id", unique=true, nullable=false)
	private long id;
	@Column(name="name")
	private String name;
	@Column(name="type")
	private String type;
	@Column(name="description")
	private String description;
	@Column(name="status")
	private int status;
	@Column(name="create_date")
	private String dateCreated;
	@Column(name="update_date")
	private String dateUpdated;
	@JoinColumn(name = "parent_tenant_id", referencedColumnName = "id")
    @ManyToOne
    private Tenant parentTenant;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public Tenant getParentTenant() {
		return parentTenant;
	}
	public void setParentTenant(Tenant parentTenant) {
		this.parentTenant = parentTenant;
	}
}
