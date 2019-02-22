package com.elyx.cloud.s3.access;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.Throwables;
import com.elyx.cloud.s3.model.GenerationParams;
import com.elyx.cloud.s3.model.S3Options;
import com.elyx.cloud.s3.util.PresignedURLUtility;

public class PresignedS3URLService {

	private final S3Options s3Options;
	private PresignedURLUtility presignedURLUtility;

	public PresignedS3URLService(S3Options s3Options) {
		this.s3Options = s3Options;
		presignedURLUtility = new PresignedURLUtility(s3Options);
	}

	public String getPresignedDownloadS3URL(String path,
			String filename) {
		
		String url = "https://s3-ap-southeast-1.amazonaws.com/";
		path = url+s3Options.getBucket()+"/"+path;
		return presignedURLUtility.makeRequest("GET", path, filename);
	}

	public String getPresignedUploadS3URL(GenerationParams params) {

		String bucket = s3Options.getBucket();
		long millis = Calendar.getInstance().getTimeInMillis();
		Date expires = new Date(millis + 1000000l);
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				bucket, params.getResourceKey());
		generatePresignedUrlRequest.withMethod(HttpMethod.PUT)
				.withExpiration(expires)
				.withContentType(params.getContentType())
				.addRequestParameter("Content-Type", params.getContentType());
		if (params.isPublicResource()) {
			generatePresignedUrlRequest.addRequestParameter(
					Headers.S3_CANNED_ACL,
					CannedAccessControlList.Private.toString());
		}

		URL result = presignedURLUtility.createS3Client(s3Options)
				.generatePresignedUrl(generatePresignedUrlRequest);
		try {
			return result.toURI().toString();
		} catch (URISyntaxException ex) {
			throw Throwables.failure(ex);
		}
	}

}
