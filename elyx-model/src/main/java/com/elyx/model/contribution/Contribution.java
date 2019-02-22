package com.elyx.model.contribution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.elyx.model.cases.Case;
import com.elyx.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="contribution")
@NamedQueries({
    @NamedQuery(name = "Contribution.findByCaseId", query = "SELECT c FROM Contribution c WHERE c.caseId = :id order by c.id desc"),
    @NamedQuery(name = "Contribution.findByUserId", query = "SELECT c FROM Contribution c WHERE c.user.id = :id order by c.id desc"),
    @NamedQuery(name = "Contribution.findByUserIdAndCaseId", query = "SELECT c FROM Contribution c WHERE c.user.id = :userId and c.caseId = :caseId order by c.id desc"),
    @NamedQuery(name = "Contribution.findDetail", query = "SELECT c FROM Contribution c WHERE c.id = :id order by c.id desc")
    })
public class Contribution {
	@Id
    @GeneratedValue
    @Column(name="id", unique=true, nullable=false)
	private long id;
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = true, insertable = true)
//	@Column(name = "user_id")
	private User user;
	@Column(name="case_id")
	private long caseId;
	@Column(name="message")
	private String message;
	@Column(name="contribute_date")
	private String dateContributed;
	@Column(name="update_date")
	private String dateUpdated;
	@Column(name="image_url")
	private String imageURL;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getCaseId() {
		return caseId;
	}
	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDateContributed() {
		return dateContributed;
	}
	public void setDateContributed(String dateContributed) {
		this.dateContributed = dateContributed;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
