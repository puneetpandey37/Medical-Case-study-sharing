package com.elyx.cloud.s3.util;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.Throwables;
import com.elyx.cloud.s3.model.GenerationParams;
import com.elyx.cloud.s3.model.S3Options;

/**
 * Generates presigned url for uploading file to Amazon S3.
 */
public class PresignedURLUtility {

	private final S3Options s3Options;
	protected String dateString;
	protected boolean virtual = false;
	protected boolean noObject = false;
	protected long byteCount = 0;
	final int BUFFER_SIZE = 65536;
	protected String authString;

	public PresignedURLUtility(S3Options s3Options) {
		this.s3Options = s3Options;
	}


	/**
	 * @param params
	 * @return Presigned URL for file upload
	 */
	public String generateGet(GenerationParams params) {
		String bucket = s3Options.getBucket();
		long millis = Calendar.getInstance().getTimeInMillis();
		Date expires = new Date(millis + params.getExpirationTimeMillis());
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				bucket, params.getResourceKey());
		generatePresignedUrlRequest.withMethod(HttpMethod.GET).withExpiration(
				expires);

		URL result = createS3Client(s3Options).generatePresignedUrl(
				generatePresignedUrlRequest);
		try {
			return result.toURI().toString();
		} catch (URISyntaxException ex) {
			throw Throwables.failure(ex);
		}
	}

	public AmazonS3Client createS3Client(S3Options s3Options) {
		return new AmazonS3Client(new StaticCredentialsProvider(
				new BasicAWSCredentials(s3Options.getAccessId(),
						s3Options.getSecretKey())));
	}

	
	public String makeRequest(String method, String path, String filename) {
		SortedMap<String, String> headers = new TreeMap<String, String>();
		return getS3URL(method, path, headers, false);
	}

	protected String getS3URL(String method, String path,
			SortedMap<String, String> headers, boolean query) {
		// expiration time in minutes
		return getS3URL(method, path, headers, 10000000);
	}

	protected String getS3URL(String method, String path,
			SortedMap<String, String> headers, int expires) {
		String url = null;
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z"); 
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		dateString = format.format(now); 
		HashMap<String, String> map = parseUrl(path);
		if (virtual) {
			url = map.get("VirtualHostedURLencoded");
		} else {
			url = map.get("PathFormURLencoded");
		}
		String bucket = map.get("Bucket");
		String object = urlEncode(map.get("Key"), true);
		String queries = map.get("Query");
		url = url + QSA(method, bucket, object, headers, queries, expires)
				+ queries.replaceAll("[?]", "&");
		return url;
	}

	//This uses Amazon V2 algorithm to generate the presigned URL
	protected String QSA(String method, String bucket, String key,
			SortedMap<String, String> headers, String queries, int expires) {
		Date now = new Date();
		Long expirationTime = now.getTime() / 1000L + 60L * expires;
		String signature = signature(method, bucket, key, headers, queries,
				expirationTime.toString());
		return "?AWSAccessKeyId=" + s3Options.getAccessId() + "&Expires="
				+ expirationTime.toString() + "&Signature="
				+ urlEncode(signature, true);
	}

	protected String signature(String method, String bucket, String key,
			SortedMap<String, String> headers, String queries, String date) {
		try {

			String path = "/";
			if (!bucket.equals("")) {
				path = path + bucket + "/";
			}
			if (!key.equals("")) {
				path = path + key;
			}
			String stringToSign = stringToSign(method, date, headers, queries,
					path);
			return computeSignature(stringToSign);
		} catch (GeneralSecurityException ex) {
			Logger.getLogger(PresignedURLUtility.class.getName()).log(Level.ERROR,
					null, ex);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(PresignedURLUtility.class.getName()).log(Level.ERROR,
					null, ex);
		}
		return null;
	}

	protected String stringToSign(String method, String expires,
			SortedMap<String, String> headers, String queries, String path) {
		String contentType = "";
		String contentMD5 = "";
		if (headers.containsKey("content-type")) {
			contentType = headers.get("content-type");
		}
		if (headers.containsKey("Content-Type")) {
			contentType = headers.get("Content-Type");
		}
		if (headers.containsKey("content-md5")) {
			contentMD5 = headers.get("content-md5");
		}
		String stringToSign = method + "\n" + contentMD5 + "\n" + contentType
				+ "\n" + expires + "\n"
				+ canonicalizeAmzHeaders(method, headers)
				+ canonicalizeResource(path, queries);
		return stringToSign;
	}

	protected String canonicalizeAmzHeaders(String method,
			SortedMap<String, String> headers) {
		String result = "";
		for (String key : headers.keySet()) {
			if (key.startsWith("x-amz-")) {
				result = result + key.trim() + ":" + headers.get(key).trim();
				result = result.replaceAll("[ ]*\n[ ]*", " ") + "\n";
			}
		}
		return result;
	}

	protected String canonicalizeResource(String path, String queries) { // New
		return path + subresource(queries);
	}

	protected String subresource(String query) {
		String result = "";
		String[] resources = { "acl", "cors", "lifecycle", "location",
				"logging", "notification", "partNumber", "policy",
				"requestPayment", "torrent", "uploadId", "uploads",
				"versionId", "versioning", "versions", "website" };
		for (String subres : resources) {
			if (query.contains(subres)) {
				int start = query.indexOf(subres);
				int end = query.indexOf("&", start);
				if (end != -1) {
					result = result
							+ query.substring(query.indexOf(subres),
									query.indexOf("&", start));
				} else {
					result = result + query.substring(query.indexOf(subres));
				}
			}
		}
		if (!result.isEmpty()) {
			result = "?" + result;
		}
		return result;
	}

	protected String computeSignature(String baseString)
			throws GeneralSecurityException, UnsupportedEncodingException {
		String algorithm = "HmacSHA1";
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(s3Options.getSecretKey().getBytes(), algorithm));
		byte[] signature = Base64.encodeBase64(mac.doFinal(baseString
				.getBytes("UTF-8")));
		return new String(signature);
	}

	public HashMap<String, String> parseUrl(String url) {
		HashMap<String, String> result = new HashMap<String, String>();
		int endProtocol = url.indexOf(':') + 3;
		String protocol = url.substring(0, endProtocol);
		String[] parts = url.substring(endProtocol).split("/", 3);
		String hostname = parts[0];
		String bucket = parts[1];
		String object = "";
		String queries = "";
		if (bucket.equals("") || bucket.startsWith("?")) {
			noObject = true;
			if (bucket.startsWith("?")) {
				queries = bucket.substring(bucket.indexOf("?"));
			}
			bucket = "";
		} else {
			object = parts[2];
			if (object.contains("?")) {
				queries = object.substring(object.indexOf("?"));
				object = object.substring(0, object.indexOf("?"));
			}
			if (object.equals("")) {
				noObject = true;
			} else {
				object = urlDecode(object);
				noObject = false;
			}
		}
		result.put("Protocol", protocol);
		result.put("Location", protocol + hostname + "/");
		result.put("Endpoint", hostname);
		result.put("Bucket", bucket);
		result.put("Key", object);
		result.put("Query", queries);
		result.put("VirtualHostedURL", protocol + bucket + "." + hostname + "/"
				+ object);
		result.put("VirtualHostedURLencoded", protocol + bucket + "."
				+ hostname + "/" + urlEncode(object, true));
		if (bucket.equals("")) {
			result.put("PathFormURL", protocol + hostname + "/");
			result.put("PathFormURLencoded", protocol + hostname + "/");
		} else {
			result.put("PathFormURL", protocol + hostname + "/" + bucket + "/"
					+ object);
			result.put("PathFormURLencoded", protocol + hostname + "/" + bucket
					+ "/" + urlEncode(object, true));
		}
		return result;
	}

	protected static String urlDecode(String value) {
		if (value == null) {
			return "";
		}
		try {
			String decoded = URLDecoder.decode(value, "UTF-8");
			return decoded;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected static String urlEncode(String value, boolean path) {
		if (value == null) {
			return "";
		}
		try {
			String encoded = URLEncoder.encode(value, "UTF-8")
					.replace("+", "%20").replace("*", "%2A")
					.replace("%7E", "~");
			if (path) {
				encoded = encoded.replace("%2F", "/");
			}
			return encoded;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
}
