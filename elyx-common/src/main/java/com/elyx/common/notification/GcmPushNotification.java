package com.elyx.common.notification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.elyx.model.user.User;

public class GcmPushNotification {

	private static final String API_KEY = "AIzaSyAIs-3nqij4JCmmBJktNkEO2mw-ZG_Yeio";
	
	public String notifyUser(User user, String message, String caseId) {
		
		String gcmId = user.getGcmId();
		String response = null;
		JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("message", message);
        jData.put("caseid", caseId);
        jGcmData.put("to", gcmId);
        jGcmData.put("data", jData);
        
        try {
        	 URL url = new URL("https://android.googleapis.com/gcm/send");
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestProperty("Authorization", "key=" + API_KEY);
             conn.setRequestProperty("Content-Type", "application/json");
             conn.setRequestMethod("POST");
             conn.setDoOutput(true);

             // Send GCM message content.
             OutputStream outputStream = conn.getOutputStream();
             outputStream.write(jGcmData.toString().getBytes());

             // Read GCM response.
             InputStream inputStream = conn.getInputStream();
             response = IOUtils.toString(inputStream);
             
        } catch(IOException exception) {
        	exception.printStackTrace();
        }
        
       return response;
	}
}
