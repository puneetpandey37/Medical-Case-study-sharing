package com.elyx.model.cases;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "case_share")
@NamedQueries({
	@NamedQuery(name = "CaseSharing.findCasesSharedToMe", query = "SELECT cs.cases FROM CaseSharing cs where cs.userId = :id order by cs.dateShared desc"),
	@NamedQuery(name = "CaseSharing.findCasesSharedByCaseAndUserId", query = "SELECT cs FROM CaseSharing cs where cs.userId = :userId and cs.cases.id = :caseId order by cs.dateShared desc")
})
public class CaseSharing {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
//	@Column(name = "case_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false, updatable = true, insertable = true)
	@JsonBackReference
	private Case cases;
	@Column(name = "user_id")
	private long userId;
	@Column(name = "share_date")
	private String dateShared;

	public String getDateShared() {
		return dateShared;
	}

	public void setDateShared(String dateShared) {
		this.dateShared = dateShared;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CaseSharing)) {
			return false;
		}
		CaseSharing that = (CaseSharing) obj;
		EqualsBuilder eb = new EqualsBuilder();
		
		String thisObject = this.id+""+this.userId+""+this.cases.toString();
		String thatObject = that.id +""+that.userId+""+that.cases.toString();
		
		eb.append(thisObject, thatObject);
		return eb.isEquals();
	}

}
