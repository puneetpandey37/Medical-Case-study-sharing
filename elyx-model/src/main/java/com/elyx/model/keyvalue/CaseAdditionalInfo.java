package com.elyx.model.keyvalue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.elyx.model.cases.Case;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Table(name = "case_additional_information")
public class CaseAdditionalInfo {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
//	@Column(name = "case_id")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false, updatable = true, insertable = true)
	@JsonBackReference
	private Case cases;
	@Column(name = "type")
	private String key;
	@Column(name = "value")
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Case getCases() {
		return cases;
	}
	public void setCases(Case cases) {
		this.cases = cases;
	}
}
