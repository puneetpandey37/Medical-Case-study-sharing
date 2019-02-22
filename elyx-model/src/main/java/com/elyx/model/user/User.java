package com.elyx.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u where u.parentTenant.id = :id"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id order by u.lastName"),
    @NamedQuery(name = "User.findByMobAndOtp", query = "SELECT u FROM User u WHERE u.phone = :phone and u.otp = :otp"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName order by u.lastName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login order by u.lastName"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password order by u.lastName"),
    @NamedQuery(name = "User.findByAddress", query = "SELECT u FROM User u WHERE u.address = :address order by u.lastName"),
    @NamedQuery(name = "User.findOtpForMobileNo", query = "SELECT u.otp FROM User u WHERE u.login = :login"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findPassword", query = "SELECT u.password FROM User u WHERE u.login = :login order by u.lastName"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone order by u.lastName")})
@NamedNativeQueries({
	@NamedNativeQuery(name = "User.userSearch", query = "select * from user u where u.first_name like :input "
			+ " or u.last_name like :input or u.specialization like :input "
			+ " or u.location like :input or u.email like :input", resultClass = User.class),
	})
public class User {

	@Id
    @GeneratedValue
    @Column(name="id", unique=true, nullable=false)
	private long id;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="specialization")
	private String specialization;
	@Column(name="login")
	private String login;
	@Column(name="password")
	@JsonIgnore
	private String password;
	@Column(name="location")
	private String location;
	@Column(name="address")
	private String address;
	@Column(name="email")
	private String email;
	@Column(name="phone")
	private String phone;
	@Column(name="status")
	private int status;
	@Column(name="create_date")
	private String dateCreated;
	@Column(name="update_date")
	private String dateUpdated;
    @ManyToOne
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant parentTenant;
    @Column(name= "gcm_id")
    private String gcmId;
    @Column(name="otp")
    private int otp;
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String imageURL;
    @Column(name = "images")
    private Integer images;
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getGcmId() {
		return gcmId;
	}
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public Integer getImages() {
		return images;
	}
	public void setImages(Integer images) {
		this.images = images;
	}
}
