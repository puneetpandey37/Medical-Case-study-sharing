package com.elyx.model.cases;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.elyx.model.keyvalue.CaseAdditionalInfo;
import com.elyx.model.user.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "cases")
@NamedQueries({
		@NamedQuery(name = "Case.findByUserId", query = "SELECT c FROM Case c where c.user.id = :id order by c.id desc"),
		@NamedQuery(name = "Case.findByTenantId", query = "SELECT c FROM Case c where c.user.parentTenant.id = :id and c.status = 0 and c.user.id != :userId order by c.id desc"), // being
		@NamedQuery(name = "Case.findByCaseId", query = "SELECT c FROM Case c where c.id = :id order by c.id desc") 
		})
public class Case implements Comparable<Case> {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = true, insertable = true)
	// @Column(name = "user_id")
	private User user;
	@Column(name = "gender")
	private String gender;
	@Column(name = "lifestyle")
	private String lifestyle;
	@Column(name = "drugs")
	private String drugs;
	@Column(name = "symptoms")
	private String symptoms;
	@Column(name = "desease_type")
	private String deseaseType;
	@Column(name = "diagnosis_history")
	private String diagnosisHistory;
	@Column(name = "create_date")
	private String dateCreated;
	@Column(name = "update_date")
	private String dateUpdated;
	private int status;
	@Column(name = "images")
	private int numberOfImages;
	// @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	// @JoinColumn(name = "case_id", referencedColumnName = "id")
	// private List<CaseImage> caseImages;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "case_id", referencedColumnName = "id")
	private List<CaseAdditionalInfo> addtionalInfo;
	/*
	 * @Transient
	 * 
	 * @JsonSerialize
	 * 
	 * @JsonDeserialize
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "case_id", referencedColumnName = "id")
	private Set<CaseSharing> caseSharing;

	@Transient
	@JsonSerialize
	@JsonDeserialize
	private List<String> imageURLs;
	@Column(name="drugs_prescribed")
	private String prescribedDrugs;
	@Column(name="investigations")
	private String investigations;
	@Column(name="queries")
	private String queries;

	public Case() {

	}

	public Case(long id, User user, String gender, String lifestyle,
			String drugs, String symptoms, String deseaseType,
			String diagnosisHistory, String dateCreated, String dateUpdated,
			int status, int numberOfImages,
			List<CaseAdditionalInfo> addtionalInfo) {
		super();
		this.id = id;
		this.user = user;
		this.gender = gender;
		this.lifestyle = lifestyle;
		this.drugs = drugs;
		this.symptoms = symptoms;
		this.deseaseType = deseaseType;
		this.diagnosisHistory = diagnosisHistory;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
		this.status = status;
		this.numberOfImages = numberOfImages;
		this.addtionalInfo = addtionalInfo;
	}

	// public Case(long id, User user, String gender, String lifestyle,
	// String drugs, String symptoms, String deseaseType,
	// String diagnosisHistory, String dateCreated, String dateUpdated,
	// int status, List<CaseImage> caseImages) {
	// super();
	// this.id = id;
	// this.user = user;
	// this.gender = gender;
	// this.lifestyle = lifestyle;
	// this.drugs = drugs;
	// this.symptoms = symptoms;
	// this.deseaseType = deseaseType;
	// this.diagnosisHistory = diagnosisHistory;
	// this.dateCreated = dateCreated;
	// this.dateUpdated = dateUpdated;
	// this.status = status;
	// this.caseImages = caseImages;
	// }

	// public void addCaseImage(CaseImage caseImage) {
	// if (caseImage == null) {
	// return;
	// } else {
	// if (caseImages == null) {
	// caseImages = new ArrayList<CaseImage>();
	// }
	// caseImages.add(caseImage);
	// caseImage.setCases(this);
	// }
	// }

	public long getId() {
		return id;
	}

	public List<CaseAdditionalInfo> getAddtionalInfo() {
		return addtionalInfo;
	}

	public void setAddtionalInfo(List<CaseAdditionalInfo> addtionalInfo) {
		this.addtionalInfo = addtionalInfo;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLifestyle() {
		return lifestyle;
	}

	public void setLifestyle(String lifestyle) {
		this.lifestyle = lifestyle;
	}

	public String getDrugs() {
		return drugs;
	}

	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getDeseaseType() {
		return deseaseType;
	}

	public void setDeseaseType(String deseaseType) {
		this.deseaseType = deseaseType;
	}

	public String getDiagnosisHistory() {
		return diagnosisHistory;
	}

	public void setDiagnosisHistory(String diagnosisHistory) {
		this.diagnosisHistory = diagnosisHistory;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(int numberOfImages) {
		this.numberOfImages = numberOfImages;
	}

	public List<String> getImageURLs() {
		return imageURLs;
	}

	public void setImageURLs(List<String> imageURLs) {
		this.imageURLs = imageURLs;
	}

	// public List<CaseImage> getCaseImages() {
	// return caseImages;
	// }
	//
	// public void setCaseImages(List<CaseImage> caseImages) {
	// this.caseImages = caseImages;
	// }

	public Set<CaseSharing> getCaseSharing() {
		return caseSharing;
	}

	public void setCaseSharing(Set<CaseSharing> caseSharing) {
		this.caseSharing = caseSharing;
	}

	public String getPrescribedDrugs() {
		return prescribedDrugs;
	}

	public void setPrescribedDrugs(String prescribedDrugs) {
		this.prescribedDrugs = prescribedDrugs;
	}

	public String getInvestigations() {
		return investigations;
	}

	public void setInvestigations(String investigations) {
		this.investigations = investigations;
	}

	public String getQueries() {
		return queries;
	}

	public void setQueries(String queries) {
		this.queries = queries;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Case)) {
			return false;
		}
		Case that = (Case) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(id, that.id);
		return eb.isEquals();
	}

	public int compareTo(Case medCase) {

		if (this.id > medCase.getId()) {
			return 1;
		} else {
			return -1;
		}
	}
}
