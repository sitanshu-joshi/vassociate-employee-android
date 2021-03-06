package com.employeesystem.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import android.content.Context;

import com.employeesystem.EmployeeApp;

/**
 * This class contains util methods for all web services of application.
 * 
 * @author
 * @version 1.0
 * @date
 */

public class WebServiceUtil {

	public String postMethod(Context context, String url, List<NameValuePair> nameValuePairs) {

		final EmployeeApp imanApplication = (EmployeeApp) context.getApplicationContext();
		final HttpPost httpPost = new HttpPost(url);

		HttpResponse httpResponse = null;

		try {
			// Add your data
			if (nameValuePairs != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}

			// Execute HTTP Post Request

			httpResponse = imanApplication.getHttpClient().execute(httpPost);
			String response = convertInputStreamToString(httpResponse);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String putMethod(Context context, String url, List<NameValuePair> nameValuePairs) {

		final EmployeeApp imanApplication = (EmployeeApp) context.getApplicationContext();
		final HttpPut httpPut = new HttpPut(url);

		HttpResponse httpResponse = null;

		try {
			// Add your data
			httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			httpResponse = imanApplication.getHttpClient().execute(httpPut);
			String response = convertInputStreamToString(httpResponse);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String getMethod(Context context, String url) {
		final EmployeeApp imanApplication = (EmployeeApp) context.getApplicationContext();
		final HttpGet httpGet = new HttpGet(url);

		HttpResponse httpResponse = null;

		try {
			// Add your data

			httpResponse = imanApplication.getHttpClient().execute(httpGet);
			String response = convertInputStreamToString(httpResponse);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String deleteMethod(Context context, String url) {
		final EmployeeApp imanApplication = (EmployeeApp) context.getApplicationContext();
		final HttpDelete httpDelete = new HttpDelete(url);

		HttpResponse httpResponse = null;

		try {
			// Add your data

			httpResponse = imanApplication.getHttpClient().execute(httpDelete);
			String response = convertInputStreamToString(httpResponse);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	// To convert inputStream into String
	public String convertInputStreamToString(HttpResponse response) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			final StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			final String LineSeparator = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + LineSeparator);
			}
			return stringBuffer.toString();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

}
