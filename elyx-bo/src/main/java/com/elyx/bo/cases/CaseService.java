package com.elyx.bo.cases;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elyx.common.exceptions.ElyxCaseSharingException;
import com.elyx.common.notification.GcmPushNotification;
import com.elyx.common.s3.S3Service;
import com.elyx.common.security.ElyxLoggedInUserUtil;
import com.elyx.common.util.DateTimeUtil;
import com.elyx.common.util.ElyxUtility;
import com.elyx.dal.cases.CaseManager;
import com.elyx.dal.user.UserManager;
import com.elyx.model.cases.AdditionalInfo;
import com.elyx.model.cases.Case;
import com.elyx.model.cases.CaseSharing;
import com.elyx.model.user.User;

@Component
public class CaseService {

	private CaseManager caseManager;
	private UserManager userManager;
	private ElyxLoggedInUserUtil elyxLoggedInUserUtil;
	private S3Service s3Service = new S3Service("elyx-bucket");
	private GcmPushNotification gcmPushNotification;

	@Autowired
	public void setElyxLoggedInUserUtil(
			ElyxLoggedInUserUtil elyxLoggedInUserUtil) {
		this.elyxLoggedInUserUtil = elyxLoggedInUserUtil;
	}

	@Autowired
	public void setCaseManager(CaseManager caseManager) {
		this.caseManager = caseManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public Case getCase(Long id) {

		Case medCase = caseManager.getCase(id);
		int numberOfImages = medCase.getNumberOfImages();

		List<String> imageURLs = s3Service.getCaseImageDownloadUrls(medCase,
				numberOfImages);
		medCase.getUser().setImageURL(s3Service.getUserDPDownloadUrl(medCase.getUser()));
		medCase.setImageURLs(imageURLs);
		return medCase;
	}
	
	
	public List<Case> getCasesByUser(String orderBy, String order, Integer limit, Integer page) {

		long loggedInUserId = elyxLoggedInUserUtil .getLoggedInUserId();
		ElyxUtility elyxUtility = new ElyxUtility();
		String dynamicQuery = elyxUtility.getUserDynamicQuery(orderBy, order, limit, page, loggedInUserId);
		List<Case> cases = caseManager.getCasesByUserId(dynamicQuery);
		if(cases != null)
			for (Case medCase : cases) {
				int numberOfImages = medCase.getNumberOfImages();
				List<String> imageURLs = null;
				if (numberOfImages > 0)
					imageURLs = s3Service.getCaseImageDownloadUrls(medCase, numberOfImages);
				User loggedInUser = elyxLoggedInUserUtil.getLoggedInUser();
				medCase.getUser().setImageURL(s3Service.getUserDPDownloadUrl(loggedInUser));
				medCase.setImageURLs(imageURLs);
			}

		return cases;
	}

	/*public List<Case> getCasesByUser(String orderBy, String order, Integer limit, Integer page) {

		int start = getPageNumber(page);
		int end = page * limit;
		int maxResult = end - start;
		List<Case> cases = caseManager.getCasesByUserId(elyxLoggedInUserUtil 
				.getLoggedInUserId(), start, maxResult, orderBy, order);

		for (Case medCase : cases) {
			int numberOfImages = medCase.getNumberOfImages();
			List<String> imageURLs = null;
			if (numberOfImages > 0)
				imageURLs = s3Service.getCseeImageUrls(medCase, numberOfImages);
			User loggedInUser = elyxLoggedInUserUtil.getLoggedInUser();
			medCase.getUser().setImageURL(s3Service.getUserDPUrl(loggedInUser));
			medCase.setImageURLs(imageURLs);
		}

		return cases;
	}*/

	/*private int getPageNumber(Integer page) {
		int start = 1;
		if (page > 1)
			start = (page - 1) * 10;
		return start;
	}*/

	public List<Case> getCasesSharedWithTenant() {

		List<Case> cases = caseManager.getCasesSharedWithTenant(
				elyxLoggedInUserUtil.getLoggedInUserTenantId(),
				elyxLoggedInUserUtil.getLoggedInUserId());
		if(cases != null)
			for (Case medCase : cases) {
				int numberOfImages = medCase.getNumberOfImages();
				List<String> imageURLs = null;
				if (numberOfImages > 0)
					imageURLs = s3Service.getCaseImageDownloadUrls(medCase, numberOfImages);
				User loggedInUser = elyxLoggedInUserUtil.getLoggedInUser();
				medCase.getUser().setImageURL(s3Service.getUserDPDownloadUrl(loggedInUser));
				medCase.setImageURLs(imageURLs);
			}

		return cases;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Case updateCase(Case medCase) {
		medCase.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		return caseManager.createCase(medCase);
	}

	public Case createCase(Case medCase) {
		medCase.setDateCreated(DateTimeUtil.getCurrentDateTime());
		medCase.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		User user = new User();
		user.setId(elyxLoggedInUserUtil.getLoggedInUserId());
		medCase.setUser(user);
		Set<CaseSharing> sharedCases = medCase.getCaseSharing();
		if (sharedCases == null) {
			medCase.setStatus(0);
		} else {
			medCase.setStatus(1);
		}
		medCase = caseManager.createCase(medCase);
		int numberOfImages = medCase.getNumberOfImages();
		List<String> imageURLs = s3Service.getCaseImageUploadUrls(medCase,
				numberOfImages);
		medCase.setImageURLs(imageURLs);

		return medCase;
	}

	public List<String> getAdditionalInfo() {

		List<String> output = new ArrayList<String>();
		output.add(AdditionalInfo.ADDINFO_1);
		output.add(AdditionalInfo.ADDINFO_2);
		output.add(AdditionalInfo.ADDINFO_3);
		output.add(AdditionalInfo.ADDINFO_4);
		return output;
	}

	public List<Case> getCasesSharedWithMe() {
		long userId = elyxLoggedInUserUtil.getLoggedInUserId();
		List<Case> cases = caseManager.getCasesSharedWithMe(userId);
		if(cases != null)
			for (Case medCase : cases) {
	
				int numberOfImages = medCase.getNumberOfImages();
				String userImageUrl = null;
				List<String> caseImageUrls = s3Service.getCaseImageDownloadUrls(medCase,
						numberOfImages);
				if (medCase.getUser() != null) {
					userImageUrl = s3Service.getUserDPDownloadUrl(medCase.getUser());
					medCase.getUser().setImageURL(userImageUrl);
				}
	
				medCase.setImageURLs(caseImageUrls);
	
			}

		return cases;
	}

	public Set<Case> getAllOpenAndSHredToMeCases() {

		Set<Case> cases = new TreeSet<Case>();
		List<Case> sharedToMeCases = getCasesSharedWithMe();

		for (Case sharedToMeCase : sharedToMeCases) {
			cases.add(sharedToMeCase);
		}

		List<Case> tenantOpenCases = getCasesSharedWithTenant();

		for (Case tenantOpenCase : tenantOpenCases) {
			cases.add(tenantOpenCase);
		}
		return cases;
	}

	public String shareCase(Case elyxCase) throws ElyxCaseSharingException {

		gcmPushNotification = new GcmPushNotification();
//		String caseShareMessage = "shared a case with you";
		Set<CaseSharing> caseSharings = elyxCase.getCaseSharing();
		StringBuilder errorMessage = new StringBuilder();
		for (CaseSharing share : caseSharings) {
			share.setDateShared(DateTimeUtil.getCurrentDateTime());
			long userId = share.getUserId();
			long caseId = share.getCases().getId();
			User user = userManager.getUserById(userId);
			CaseSharing sharedCase = caseManager.getShareCaseByCaseAndUserId(
					userId, caseId);
			if (sharedCase != null) {
				errorMessage.append(user.getId());
				errorMessage.append(",");
				continue;
			}
			caseManager.shareCase(share);
			User loggedInUser = elyxLoggedInUserUtil.getLoggedInUser();
			String sharingUserName = loggedInUser.getFirstName();
			String caseShareMessage = sharingUserName + " " + "shared a case with you";
			gcmPushNotification.notifyUser(user, caseShareMessage, String.valueOf(share.getCases().getId()));
		}
		if (errorMessage.length() > 0) {
			String exceptionMsg = "Failed: " + "{"
					+ errorMessage.substring(0, errorMessage.length() - 1)
					+ "}";
			throw new ElyxCaseSharingException(exceptionMsg);
		}
		return "success";
	}

}
