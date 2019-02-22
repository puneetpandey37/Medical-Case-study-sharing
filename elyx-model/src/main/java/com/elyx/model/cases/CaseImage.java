package com.elyx.model.cases;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
@Table(name="case_images")
public class CaseImage {

	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
    public Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false, updatable = true, insertable = true)
    @JsonBackReference
    public Case cases;
    @Column(name="image_url")
    public String imageUrl;
    @Column(name="create_date")
    public String dateCreated;
	public Case getCases() {
		return cases;
	}
	public void setCases(Case cases) {
		this.cases = cases;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
}
