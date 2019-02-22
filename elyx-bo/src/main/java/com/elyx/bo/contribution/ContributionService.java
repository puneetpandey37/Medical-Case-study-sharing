package com.elyx.bo.contribution;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elyx.common.exceptions.ElyxInvalidRequestException;
import com.elyx.common.notification.GcmPushNotification;
import com.elyx.common.s3.S3Service;
import com.elyx.common.security.ElyxLoggedInUserUtil;
import com.elyx.common.util.DateTimeUtil;
import com.elyx.dal.cases.CaseManager;
import com.elyx.dal.contribution.ContributionManager;
import com.elyx.model.cases.Case;
import com.elyx.model.contribution.Contribution;
import com.elyx.model.user.User;

@Component
public class ContributionService {

	private ContributionManager contributionManager;
	private CaseManager caseManager;
	private ElyxLoggedInUserUtil elyxLoggedInUserUtil;
	private S3Service s3Service = new S3Service("elyx-bucket");

	@Autowired
	public void setContributionManager(ContributionManager contributionManager) {
		this.contributionManager = contributionManager;
	}

	@Autowired
	public void setCaseManager(CaseManager caseManager) {
		this.caseManager = caseManager;
	}

	@Autowired
	public void setElyxLoggedInUserUtil(
			ElyxLoggedInUserUtil elyxLoggedInUserUtil) {
		this.elyxLoggedInUserUtil = elyxLoggedInUserUtil;
	}

	public Contribution contribute(Contribution contribution) {
		contribution.setDateContributed(DateTimeUtil.getCurrentDateTime());
		contribution.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		User user = new User();
		long loggedInUserId = elyxLoggedInUserUtil.getLoggedInUserId();
		user.setId(loggedInUserId);
		contribution.setUser(user);
		contribution = contributionManager.contribute(contribution);
		String imageURL = s3Service.getContributionImageUploadUrl(contribution);
		contribution.setImageURL(imageURL);
		User loggedInUser = contribution.getUser();
		contribution.getUser()
				.setImageURL(s3Service.getUserDPDownloadUrl(loggedInUser));
		GcmPushNotification notification = new GcmPushNotification();
		long caseId = contribution.getCaseId();
		Case elyxCase = caseManager.getCase(caseId);
		String messageToCaseOwner = loggedInUser.getFirstName() + " contributed to your case";
		User caseOwner = elyxCase.getUser();
		notification.notifyUser(caseOwner, messageToCaseOwner, String.valueOf(elyxCase.getId()));
		return contribution;
	}

	public List<Contribution> getContributionsByCaseId(long caseId) {

		List<Contribution> contributions = contributionManager
				.getContributionsByCaseId(caseId);

		for (Contribution contribution : contributions) {
			contribution.setImageURL(s3Service
					.getContributionImageUploadUrl(contribution));
			contribution.setImageURL(s3Service.getContributionImageDownloadUrl(contribution));
			User loggedInUser = contribution.getUser();
			contribution.getUser().setImageURL(
					s3Service.getUserDPDownloadUrl(loggedInUser));
		}
		return contributions;

	}

	public List<Contribution> getContributionsByUserIdAndCaseId(long userId,
			long caseId) {

		List<Contribution> contributions = contributionManager
				.getContributionsByUserIdAndCaseId(userId, caseId);

		for (Contribution contribution : contributions) {
			contribution.setImageURL(s3Service.getContributionImageDownloadUrl(contribution));
			User loggedInUser = contribution.getUser();
			contribution.getUser().setImageURL(
					s3Service.getUserDPDownloadUrl(loggedInUser));
		}
		return contributions;
	}

	public List<Contribution> getContributionsByUserId(long userId) {

		List<Contribution> contributions = contributionManager
				.getContributionsByUserId(userId);

		for (Contribution contribution : contributions) {
			contribution.setImageURL(s3Service.getContributionImageDownloadUrl(contribution));
			User loggedInUser = contribution.getUser();
			contribution.getUser().setImageURL(
					s3Service.getUserDPDownloadUrl(loggedInUser));
		}
		return contributions;
	}

	public Contribution getContributionDetail(long contributionId) {
		Contribution contribution = contributionManager
				.getContributionDetail(contributionId);
		contribution.setImageURL(s3Service.getContributionImageDownloadUrl(contribution));
		User loggedInUser = contribution.getUser();
		contribution.getUser()
				.setImageURL(s3Service.getUserDPDownloadUrl(loggedInUser));
		return contribution;

	}

	public Contribution updateContribution(Contribution contribution) {
		contribution.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		User user = new User();
		user.setId(elyxLoggedInUserUtil.getLoggedInUserId());
		contribution.setUser(user);
		return contributionManager.contribute(contribution);
	}

	public void deleteContribution(long contributionId)
			throws ElyxInvalidRequestException {

		Contribution contribution = getContributionDetail(contributionId);
		long userId = elyxLoggedInUserUtil.getLoggedInUserId();
		if (contribution != null) {
			if (userId != contribution.getUser().getId()) {
				throw new ElyxInvalidRequestException(
						"Cannot delete Contributions of other Users!");
			}
			contributionManager.deleteContribution(contributionId);
		}
	}
}
