package com.elyx.controller.user;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.user.UserService;
import com.elyx.cloud.exception.S3AccessException;
import com.elyx.cloud.s3.access.PresignedS3URLService;
import com.elyx.cloud.s3.model.GenerationParams;
import com.elyx.cloud.s3.model.S3Options;
import com.elyx.common.s3.S3Service;
import com.elyx.dal.user.UserManager;
import com.elyx.model.cases.Case;
import com.elyx.model.contribution.Contribution;
import com.elyx.model.user.User;
import com.test.PresignedUrlForPutGenerator;

@Controller
@RequestMapping("/test")
public class TestController {
	
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public String presignedDownloadURL(@RequestParam("buckeName") String bucketName, String path) {
		
//		S3Options s3Options = new S3Options("elyx-bucket", "AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		S3Options s3Options = new S3Options(bucketName, "AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		PresignedS3URLService s3urlService = new PresignedS3URLService(s3Options);
//		return s3urlService.getPresignedDownloadS3URL("user/images/dp/33.jpg", "33.jpg");
//		return s3urlService.getPresignedDownloadS3URL(
//				"case/images/14/4.jpg", "4.jpg");
		
		return s3urlService.getPresignedDownloadS3URL(path, "4.jpg");
	}
	
	
	/*@RequestMapping(value = "/upload", method = RequestMethod.GET)
	@ResponseBody
	public String presignedUploadURL(@RequestParam("buckeName") String bucketName, @RequestParam("path") String path) {
		
//		S3Options s3Options = new S3Options("elyx-bucket", "AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		S3Options s3Options = new S3Options(bucketName, "AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		PresignedS3URLService s3urlService = new PresignedS3URLService(s3Options);
//		GenerationParams params = new GenerationParams("test-put/sachin.jpg", true);
		GenerationParams params = new GenerationParams(path, true);
		return s3urlService.getPresignedUploadS3URL(params);
	}*/
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	@ResponseBody
	public String presignedUploadURL(@RequestParam("buckeName") String bucketName,
			@RequestParam(value="userId", required=false) Long userId, 
			@RequestParam(value="caseId", required=false) Long caseId,
			@RequestParam(value="contributionId", required=false) Long contributionId,
			@RequestParam("type") String type) {
		
		String result = "";
		S3Service s3Service = new S3Service(bucketName);
		
		if("user".equalsIgnoreCase(type)) {
			User user = new User();
			user.setId(userId);
			result = s3Service.getUserDPUploadUrl(user);
		} else if ("case".equalsIgnoreCase(type)) {
			Case elyxCase = new Case();
			elyxCase.setId(caseId);
			List<String> urls = s3Service.getCaseImageUploadUrls(elyxCase, 1);
			result = urls.get(0);
		} else if ("contri".equalsIgnoreCase(type)) {
			Contribution contri = new Contribution();
			contri.setCaseId(caseId);
			contri.setId(contributionId);
			result = s3Service.getContributionImageUploadUrl(contri);
		} 
		
		return result;
	}
	
	
	@RequestMapping(value = "/bucket", method = RequestMethod.GET)
	@ResponseBody
	public int createBucket(@RequestParam("buckeName") String bucketName) throws S3AccessException {
		S3Service s3Service = new S3Service(bucketName);
		return s3Service.createBucket(bucketName);
	}
	
	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	@ResponseBody
	public String getHTTPCallTest() throws ClientProtocolException, IOException {
		PresignedUrlForPutGenerator test = new PresignedUrlForPutGenerator(null);
		return test.executeGET();
	}	
	
	
	/*
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void test(@RequestParam("buckeName") String bucketName) throws S3AccessException {
		userService.updateNumberOfImages(2);
	}
*/
}
