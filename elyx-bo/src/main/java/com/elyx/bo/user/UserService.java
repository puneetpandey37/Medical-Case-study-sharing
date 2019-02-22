package com.elyx.bo.user;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elyx.bo.cases.CaseService;
import com.elyx.common.exceptions.ElyxUserDatExistsException;
import com.elyx.common.s3.S3Service;
import com.elyx.common.security.ElyxLoggedInUserUtil;
import com.elyx.common.security.ValidateUserAccess;
import com.elyx.common.util.DateTimeUtil;
import com.elyx.common.util.RandomNumberUtil;
import com.elyx.common.util.SmsGatewayUtil;
import com.elyx.dal.user.UserManager;
import com.elyx.model.user.DoctorSpecialization;
import com.elyx.model.user.Tenant;
import com.elyx.model.user.User;
import com.elyx.model.user.UserDetail;

@Component
public class UserService {

	private UserManager userManager;
	private ValidateUserAccess validateUserAccess;
	private ElyxLoggedInUserUtil elyxLoggedInUserUtil;
	private CaseService caseService;
	private S3Service s3Service = new S3Service("elyx-bucket");

	public ElyxLoggedInUserUtil getElyxLoggedInUserUtil() {
		return elyxLoggedInUserUtil;
	}

	@Autowired
	public void setElyxLoggedInUserUtil(
			ElyxLoggedInUserUtil elyxLoggedInUserUtil) {
		this.elyxLoggedInUserUtil = elyxLoggedInUserUtil;
	}

	public ValidateUserAccess getValidateUserAccess() {
		return validateUserAccess;
	}

	@Autowired
	public void setValidateUserAccess(ValidateUserAccess validateUserAccess) {
		this.validateUserAccess = validateUserAccess;
	}

	@Autowired
	public void setCaseService(CaseService caseService) {
		this.caseService = caseService;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<User> getUsers(Long tenantId) {
		List<User> users = null;
		if(tenantId == null) {
			users = userManager.getUsers(elyxLoggedInUserUtil
					.getLoggedInUserTenantId());
		} else {
			users = userManager.getUsers(tenantId);
		}
		for (User user : users) {
			user.setImageURL(s3Service.getUserDPDownloadUrl(user));
		}
		return users;
	}

	public User createUser(User user/* , String clientId, String clientSecret */)
			throws ElyxUserDatExistsException, IOException {
		String mobileNo = user.getPhone();
		// validateUserAccess.validateClientSecret(clientId, clientSecret);
		validateUserAccess.checkIfUserDataExists(user);
		user.setDateCreated(DateTimeUtil.getCurrentDateTime());
		user.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		user.setStatus(1);
		user.setImages(1);
		Tenant tenant = new Tenant();
		tenant.setId(1l);
		user.setParentTenant(tenant);
		user.setLogin(user.getPhone());
		int otp = RandomNumberUtil.getRandomNumber();
		user.setOtp(otp);
		SmsGatewayUtil smsGateway = new SmsGatewayUtil();
		User result = userManager.createUser(user);
		smsGateway.sendMessage(mobileNo, otp, "OTP");

		/*S3Options s3Options = new S3Options("elyx-bucket",
				"AKIAJD3OQVNTI56LBDZA",
				"4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		PresignedS3URLService s3urlService = new PresignedS3URLService(
				s3Options);
		GenerationParams params = new GenerationParams("user/images/dp/"
				+ result.getId() + ".jpg", true);
		String imageURL = s3urlService.getPresignedUploadS3URL(params);*/
		String imageURL = s3Service.getUserDPUploadUrl(result);
		result.setImageURL(imageURL);
		return result;
	}

	public boolean verifyUser(String mobileNumber, int otp) {

		boolean isAuthorized = false;
		User userByOtp = userManager.getUserByOtp(mobileNumber, otp);
		if (userByOtp != null) {
			userByOtp.setStatus(1);
			isAuthorized = true;
		}
		return isAuthorized;
	}

	public User updateUser(User user) {
		User loggedInUser = elyxLoggedInUserUtil.getLoggedInUser();
		user.setLogin(loggedInUser.getLogin());
		user.setPhone(loggedInUser.getPhone());
		user.setId(loggedInUser.getId());
		user.setEmail(loggedInUser.getEmail());
		user.setImages(loggedInUser.getImages());
		user.setImageURL(loggedInUser.getImageURL()); 
		Tenant tenant = new Tenant();
		tenant.setId(1l);
		user.setParentTenant(tenant);
		user.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		return userManager.createUser(user);
	}

	public UserDetail getUser(Long id) {
		UserDetail result = new UserDetail();
		User me;
		if (id != null){
			me = userManager.getUserById(id);
			me.setImageURL(s3Service.getUserDPDownloadUrl(me));
		}
		else {
			me = userManager.getUserByUserName(elyxLoggedInUserUtil
					.getLoggedInUserName());
			me.setImageURL(s3Service.getUserDPDownloadUrl(me));
		}
		result.setMe(me);
		result.setUsers(getUsers(me.getParentTenant().getId()));
		result.setToMeCases(caseService.getCasesSharedWithMe());
		result.setMyCases(caseService.getCasesByUser(null, null, null, null));
		return result;
	}

	public EnumSet<DoctorSpecialization> getDoctorSpecialization() {
		// List<DoctorSpecialization> docSpecialization = new
		// ArrayList<DoctorSpecialization>();
		// docSpecialization.add(DoctorSpecialization.ANAESTHESIOLOGY);
		EnumSet<DoctorSpecialization> docSpecializationEnums = EnumSet
				.allOf(DoctorSpecialization.class);
		return docSpecializationEnums;
	}

	public List<User> searchUser(String input) {
		String searchCriteria = "%"+input+"%";
		List<User> users = userManager.searchUser(searchCriteria);
		
		for(User user : users) {
			user.setImageURL(s3Service.getUserDPDownloadUrl(user));
		}
		return users;
	}

	public void updateNumberOfImages(int images, long UserId) {
		userManager.updateNumberOfImages(images, UserId);
	}
}
