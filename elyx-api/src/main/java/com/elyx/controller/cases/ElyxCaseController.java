package com.elyx.controller.cases;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.cases.CaseService;
import com.elyx.common.exceptions.ElyxCaseSharingException;
import com.elyx.model.cases.Case;

@Controller
@RequestMapping("/cases")
public class ElyxCaseController {

	private CaseService caseService;

	@Autowired
	public void setCaseService(CaseService caseService) {
		this.caseService = caseService;
	}

	/**
	 * @param id
	 * @return Detail of a particular Case by its caseId
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Case getCase(@RequestParam(value = "id", required = false) Long id) {
		return caseService.getCase(id);
	}

	/**
	 * @return parameters for additional information. It's Key Value pair of the
	 *         the parameters.
	 */
	@RequestMapping(value = "/additionalinfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<String> getCaseAdditionalInfo() {
		return caseService.getAdditionalInfo();
	}

	/**
	 * @return List of the Cases created by this user.
	 */
	@RequestMapping(value = "/mycases", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Case> getCasesByUser(
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "page", required = false) Integer page) {
		return caseService.getCasesByUser(orderBy, order, limit, page);
	}

	/**
	 * @return List of the Cases shared with this user.
	 */
	@RequestMapping(value = "/tomecases", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Case> getCasesSharedWithMe() {
		return caseService.getCasesSharedWithMe();
	}

	/**
	 * @return List of the Cases that are open in the Group and Cases that are
	 *         assigned to me.
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Set<Case> getAllOpenAndSHredToMeCases() {
		return caseService.getAllOpenAndSHredToMeCases();
	}

	/**
	 * @return List of the Cases created by anyone within Tenant and shared with
	 *         all the Users within the Tenant.
	 */
	@RequestMapping(value = "/open", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Case> getCasesSharedWithTenant() {
		return caseService.getCasesSharedWithTenant();
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Case updateCase(@RequestBody Case medCase) {
		return caseService.updateCase(medCase);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Case createCase(@RequestBody Case medCase) {
		return caseService.createCase(medCase);
	}

	@RequestMapping(value = "/share", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String shareCase(@RequestBody Case medCase)
			throws ElyxCaseSharingException {
		return caseService.shareCase(medCase);
	}
}
