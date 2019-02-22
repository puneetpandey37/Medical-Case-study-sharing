package com.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.Throwables;

/**
 * Generates presigned url for uploading file to Amazon S3.
 */
public class PresignedUrlForPutGenerator {

    private final S3Options s3Options;

    public PresignedUrlForPutGenerator(S3Options s3Options) {
        this.s3Options = s3Options;
    }

    public String generate(GenerationParams params) {
        String bucket = s3Options.getBucket();
        long millis = Calendar.getInstance().getTimeInMillis();
        Date expires = new Date(millis + params.getExpirationTimeMillis());
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, params.getResourceKey());
        generatePresignedUrlRequest
                .withMethod(HttpMethod.PUT)
                .withExpiration(expires)
                .withContentType(params.getContentType())
                .addRequestParameter("Content-Type", params.getContentType());
        // this parameter needed to make resource uploaded with presigned-url immediately public-available
        if(params.isPublicResource()) {
            generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        }

        URL result = createS3Client(s3Options).generatePresignedUrl(generatePresignedUrlRequest);
        try {
            return result.toURI().toString();
        } catch (URISyntaxException ex) {
            throw Throwables.failure(ex);
        }
    }

    public String generateGet(GenerationParams params) {
        String bucket = s3Options.getBucket();
        long millis = Calendar.getInstance().getTimeInMillis();
        Date expires = new Date(millis + params.getExpirationTimeMillis());
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, params.getResourceKey());
        generatePresignedUrlRequest
                .withMethod(HttpMethod.GET)
                .withExpiration(expires);

        URL result = createS3Client(s3Options).generatePresignedUrl(generatePresignedUrlRequest);
        try {
            return result.toURI().toString();
        } catch (URISyntaxException ex) {
            throw Throwables.failure(ex);
        }
    }

    private AmazonS3Client createS3Client(S3Options s3Options) {
        return new AmazonS3Client(
                new StaticCredentialsProvider(
                        new BasicAWSCredentials("AKIAJD3OQVNTI56LBDZA", "4M+j9gDkwoIHBCUY9Pferw4cRIJZasaVvI1oEqNw")));
    }
    
    public static void main(String[] args) throws ClientProtocolException, IOException {
		
//    	S3Options s3Options = new S3Options("mum3", "AKIAIY3B2EHXP6ZAPFDA", "SGcFADRpV1t0U9+pR9fbxifzW1YQQAvsQMxkOigj");
    	PresignedUrlForPutGenerator generator = new PresignedUrlForPutGenerator(null);
//    	GenerationParams params = new GenerationParams("puneettest.jpg", true);
//    	String url = generator.generate(params);
//    	System.out.println(url);
    	int status = generator.executePUT();
    	System.out.println(status);
	}
    
    public int executePUT() throws ClientProtocolException, IOException {
    	
    	CloseableHttpResponse response = null;
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpPut put = new HttpPut("https://esi1.s3.amazonaws.com/uploadmadiit?Expires=1475647972&AWSAccessKeyId=AKIAIY3B2EHXP6ZAPFDA&Signature=jvfY7WpnaPlcZp9xMdKNdMgHIC0%3D");
        put.setEntity(new FileEntity(new File("C:/Users/ppy/Downloads/testimage.jpg"), "binary/octet-stream"));
        put.setHeader("Content-Type","binary/octet-stream");
        try {
        		response = httpclient.execute(put);
		} catch (Exception e) {
		}
        return response.getStatusLine().getStatusCode();
    }
    
    public String executeGET() throws ClientProtocolException, IOException {
    	
	 String url = "http://localhost:8080/elyx-api/oauth/token?grant_type=password&client_id=32ewfeer33ewd32&client_secret=324rfvgq32rferve34ew&username=89743532325&password=873249";
	 String json_string = null;
    	CloseableHttpResponse response = null;
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpGet get = new HttpGet(url);
        try {
        		response = httpclient.execute(get);
        		response.getEntity();
        		json_string = EntityUtils.toString(response.getEntity());
        		System.out.println(response.getStatusLine().getStatusCode());
		} catch (Exception e) {
		}
        return json_string;
    }

}
