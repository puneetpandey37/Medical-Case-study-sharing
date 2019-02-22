package com.elyx.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.stereotype.Component;

@Component
public class SmsGatewayUtil {

	private String key = "Af41e44c63a61c0ad5ce0faf28d0b1ac3";
	private String sender = "SIDEMO";
	private final String USER_AGENT = "Mozilla/5.0";

	public void sendMessage(String mobileNo, int otp, String messageType)
			throws IOException {
		String otpMessage = "is One Time Password (OTP) of your Elyx request. Please treat this as confedential "
				+ "and do not share this password with anyone - ELYX. ";

		if ("OTP".equalsIgnoreCase(messageType)) {
			otpMessage = otp + " " + otpMessage;
		}
		/*String url = "http://alerts.sinfini.com/api/web2sms.php?"
				+ "workingkey=" + key + "&" + "to=" + mobileNo + "&"
				+ "sender=" + sender + "&" + "message=" + otpMessage;*/
		mobileNo = 91+mobileNo;
		String url = "http://203.212.70.200/smpp/sendsms?username= octal &password=octal@123&to="+ mobileNo 
				+ "&from=ELYXMD&udh=&text="+ otpMessage;
		url = URIUtil.encodeQuery(url);
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		System.out.println(response);
		System.out.println(mobileNo);
		System.out.println(url);
		in.close();
	}
}
