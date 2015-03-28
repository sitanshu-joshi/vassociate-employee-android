package com.employeesystem.fragment;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.employeesystem.R;
import com.employeesystem.util.Utils;

public class WSNewsFeedCityId {

	private Context context;

	private String message;
	private String status;

	public WSNewsFeedCityId(Context context) {
		this.context = context;
	}

	// To execute request-response
	public String execute(List<NameValuePair> nameValuePairs) {
		// final String url = context.getString(R.string.base_url) +
		// context.getString(R.string.login);
		final String response = new WebServiceUtil().postMethod(context, context.getString(R.string.url) + "addTaskEncode64.php", nameValuePairs);
		return parseJSON(response);
	}

	// To parse response data
	private String parseJSON(String response) {
		String id = "";
		Utils.e(response);

		return response;

	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

}
