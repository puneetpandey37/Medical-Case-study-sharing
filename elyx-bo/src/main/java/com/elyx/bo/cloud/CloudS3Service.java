package com.elyx.bo.cloud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elyx.common.s3.S3Service;
import com.elyx.common.security.ElyxLoggedInUserUtil;
import com.elyx.dal.cases.CaseManager;
import com.elyx.dal.contribution.ContributionManager;
import com.elyx.dal.user.UserManager;
import com.elyx.model.cases.Case;
import com.elyx.model.contribution.Contribution;
import com.elyx.model.user.User;

@Component
public class CloudS3Service {
	
	private ElyxLoggedInUserUtil elyxLoggedInUserUtil;
	private UserManager userManager;
	private CaseManager caseManager;
	private ContributionManager contributionManager;
	public void setContributionManager(ContributionManager contributionManager) {
		this.contributionManager = contributionManager;
	}
	public void setCaseManager(CaseManager caseManager) {
		this.caseManager = caseManager;
	}
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	@Autowired
	public void setElyxLoggedInUserUtil(ElyxLoggedInUserUtil elyxLoggedInUserUtil) {
		this.elyxLoggedInUserUtil = elyxLoggedInUserUtil;
	}

	public String getPresignedUploadURL(String bucketName, Long userId,
			Long caseId, Long contributionId, String type) {

		User user;
		if(bucketName == null)
			bucketName = "elyx-bucket";
		if(userId == null) {
			user = elyxLoggedInUserUtil.getLoggedInUser();
		} else {
			user = userManager.getUserById(userId);
		}
		Integer numberOfImages = user.getImages();
		String result = "";
		S3Service s3Service = new S3Service(bucketName);
		
		if("user".equalsIgnoreCase(type)) {
			if(numberOfImages == null) {
				numberOfImages = 0;
			}
			userManager.updateNumberOfImages(numberOfImages+1, userId);
			user.setImages(numberOfImages + 1);
			result = s3Service.getUserDPUploadUrl(user);
		} else if ("case".equalsIgnoreCase(type)) {
			Case elyxCase = caseManager.getCase(caseId);
			List<String> urls = s3Service.getCaseImageUploadUrls(elyxCase, 1);
			result = urls.get(0);
		} else if ("contri".equalsIgnoreCase(type)) {
			Contribution contri = contributionManager.getContributionDetail(contributionId);
			result = s3Service.getContributionImageUploadUrl(contri);
		}
		return result; 
	}

	
}
