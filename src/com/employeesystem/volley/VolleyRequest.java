package com.employeesystem.volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.employeesystem.util.Utils;

public class VolleyRequest extends Request<String> {

	private static VolleyOnResponseListener volleyOnResponseListener;
	private String message;
	private Map<String, String> params;
	private HttpEntity entity;
	private ProgressDialog progressDialog;

	public VolleyRequest(int method, String url, VolleyOnResponseListener onResponseListener) {
		super(method, url, errorListener);
		Utils.e(url);
		volleyOnResponseListener = onResponseListener;
	}

	@Override
	protected void deliverResponse(String response) {
		if (volleyOnResponseListener != null) {
			try {
				final JSONObject jsMain = new JSONObject(response);
				Utils.e(response);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				volleyOnResponseListener.onSuccess(response, message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		return super.parseNetworkError(volleyError);

	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (params != null) {
			return params;
		} else {
			return super.getParams();
		}

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return null;
	}

	@Override
	public String getBodyContentType() {
		if (entity != null) {
			return entity.getContentType().getValue();
		} else {
			return super.getBodyContentType();
		}

	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		if (entity != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				entity.writeTo(bos);
			} catch (IOException e) {
				VolleyLog.e("IOException writing to ByteArrayOutputStream");
			}
			return bos.toByteArray();
		} else {
			return super.getBody();
		}

	}



	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	private static Response.ErrorListener errorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Utils.e(String.format("%s", error));
			if (error.getCause() instanceof TimeoutException || error.getCause() instanceof SocketTimeoutException) {
				volleyOnResponseListener.onFailure("Timeout");
			} else if (error.getCause() instanceof UnknownHostException || error.getCause() instanceof NoConnectionError) {
				volleyOnResponseListener.onFailure("Unable to connect server");
			} else {
				volleyOnResponseListener.onFailure("Some error occur...Please try again later");
			}
		}
	};

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
}
