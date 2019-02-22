package com.elyx.cloud.s3.access;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Region;
import com.elyx.cloud.exception.S3AccessException;
import com.elyx.cloud.s3.model.S3MetaData;
import com.elyx.cloud.s3.model.S3Options;

public class AmazonS3Service {

	private AmazonS3Client as3Client = null;

	/*public static void main(String[] args) throws S3AccessException {

		S3Options s3Options = new S3Options("elyx-s3-02", "AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw");
		AmazonS3Service amazonS3Service = new AmazonS3Service(
				"s3-ap-southeast-1.amazonaws.com", s3Options);
		amazonS3Service.createBucket("elyx-s3-02",
				"https://s3-ap-southeast-1.amazonaws.com");
	}*/

	public AmazonS3Service(String hostname, S3Options s3Options) {

		as3Client = new AmazonS3Client(new BasicAWSCredentials(
				s3Options.getAccessId(), s3Options.getSecretKey()));
		as3Client.setEndpoint(hostname);
	}

	public int createBucket(String bucketName, String podUrl)
			throws S3AccessException {
		try {
			CreateBucketRequest cbReq = new CreateBucketRequest(bucketName,
					getS3RegionFromUrl(podUrl));
			Bucket createdBucket = as3Client.createBucket(cbReq);
			if (createdBucket != null) {
				BucketCrossOriginConfiguration corsConfiguration = new BucketCrossOriginConfiguration();

				CORSRule rule1 = new CORSRule()
						.withId("CORSRule")
						.withAllowedMethods(
								Arrays.asList(new CORSRule.AllowedMethods[] { CORSRule.AllowedMethods.GET, 
										CORSRule.AllowedMethods.PUT, 
										CORSRule.AllowedMethods.POST }))
						.withAllowedOrigins(
								Arrays.asList(new String[] { "*" + podUrl }))
						.withMaxAgeSeconds(30000)
						.withAllowedHeaders(Arrays.asList(new String[] { "*" }));

				corsConfiguration.setRules(Arrays
						.asList(new CORSRule[] { rule1 }));
				as3Client.setBucketCrossOriginConfiguration(bucketName,
						corsConfiguration);
				return 200;
			} else {
				return -1;
			}
		} catch (Throwable thr) {
			throw new S3AccessException("error while creating bucket", thr);
		}
	}

	public int updateBucket(String bucketName, String podUrl, String location)
			throws S3AccessException {
		try {
			BucketCrossOriginConfiguration corsConfiguration = new BucketCrossOriginConfiguration();

			CORSRule rule1 = new CORSRule()
					.withId("CORSRule")
					.withAllowedMethods(
							Arrays.asList(new CORSRule.AllowedMethods[] { CORSRule.AllowedMethods.GET }))
					.withAllowedOrigins(
							Arrays.asList(new String[] { "*" + podUrl }))
					.withMaxAgeSeconds(30000)
					.withAllowedHeaders(Arrays.asList(new String[] { "*" }));

			corsConfiguration.setRules(Arrays.asList(new CORSRule[] { rule1 }));
			as3Client.setBucketCrossOriginConfiguration(bucketName,
					corsConfiguration);
			return 200;
		} catch (Throwable thr) {
			throw new S3AccessException("error while updating bucket", thr);
		}
	}

	public int deleteBucket(String bucket) throws S3AccessException {
		try {
			as3Client.deleteBucket(bucket);
			return 200;
		} catch (Throwable thr) {
			throw new S3AccessException("error while deleting bucket", thr);
		}
	}

	private Region getS3RegionFromUrl(String podUrl)
			throws MalformedURLException {

		URL pUrl = new URL(podUrl);
		Region finalRegion = Region.US_Standard;
		String endPoint = pUrl.getHost();
		if (endPoint.startsWith(S3MetaData.S3_ENPOINT_PREFIX)
				&& endPoint.endsWith(S3MetaData.S3_ENDPOINT_SUFFIX)) {
			int regionCodeStartIndex = S3MetaData.S3_ENPOINT_PREFIX.length();
			int regionCodeEndIndex = endPoint
					.lastIndexOf(S3MetaData.S3_ENDPOINT_SUFFIX);

			String initialRegionCode = endPoint.substring(regionCodeStartIndex,
					regionCodeEndIndex);
			if (!initialRegionCode.isEmpty()) {
				initialRegionCode = initialRegionCode.substring(1);
				if (initialRegionCode.contentEquals(S3MetaData.AP_REGION_CODE)) {
					initialRegionCode = S3MetaData.AP_LOCATION_CONSTRAINT_CODE;
				}
				finalRegion = Region.fromValue(initialRegionCode);
			}
			return finalRegion;
		}
		throw new IllegalArgumentException(
				"invalid or null region code submitted");
	}
}
