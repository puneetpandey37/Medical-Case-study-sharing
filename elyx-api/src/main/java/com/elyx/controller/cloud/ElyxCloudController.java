package com.elyx.controller.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.cloud.CloudS3Service;

@Controller
@RequestMapping("/cloud")
public class ElyxCloudController {

	private CloudS3Service cloudS3Service;
	@Autowired
	public void setCloudS3Service(CloudS3Service cloudS3Service) {
		this.cloudS3Service = cloudS3Service;
	}

	@RequestMapping(value = "/s3/upload", method = RequestMethod.GET)
	@ResponseBody
	public String getPresignedUploadURL(@RequestParam(value = "buckeName", required = false) String bucketName,
			@RequestParam(value="userId", required=false) Long userId, 
			@RequestParam(value="caseId", required=false) Long caseId,
			@RequestParam(value="contributionId", required=false) Long contributionId,
			@RequestParam("type") String type) {
		
		return cloudS3Service.getPresignedUploadURL(bucketName, userId, caseId, contributionId, type);
	}
}
