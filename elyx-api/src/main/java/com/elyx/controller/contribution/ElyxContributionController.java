package com.elyx.controller.contribution;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.contribution.ContributionService;
import com.elyx.common.exceptions.ElyxInvalidRequestException;
import com.elyx.model.contribution.Contribution;

@Controller
@RequestMapping("/contributions")
public class ElyxContributionController {

	private ContributionService contributionService;
	@Autowired
	public void setContributionService(ContributionService contributionService) {
		this.contributionService = contributionService;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Contribution contribute(@RequestBody Contribution contribution) throws ElyxInvalidRequestException{
		return contributionService.contribute(contribution);
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public Contribution updateContribution(@RequestBody Contribution contribution) throws ElyxInvalidRequestException{
		return contributionService.updateContribution(contribution);
	}
	
	@RequestMapping(value = "/user/case",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Contribution> getContributionsByUserIdAndCaseId(@RequestParam("userId") long userId, @RequestParam("caseId") long caseId){
		return contributionService.getContributionsByUserIdAndCaseId(userId, caseId);
	}
	
	@RequestMapping(value = "/case",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Contribution> getContributionsByCaseId(@RequestParam("caseId")long caseId){
		return contributionService.getContributionsByCaseId(caseId);
	}
	
	@RequestMapping(value = "/user",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Contribution> getContributionsByUserId(@RequestParam("userId")long userId){
		return contributionService.getContributionsByUserId(userId);
	}
	@RequestMapping(value = "/detail",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Contribution getContributionDetail(@RequestParam("contributionId")long contributionId) {
		return contributionService.getContributionDetail(contributionId);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public String deleteContribution(@RequestParam("contributionId")long contributionId) throws ElyxInvalidRequestException {
		contributionService.deleteContribution(contributionId);
		return "deleted";
	}
}
