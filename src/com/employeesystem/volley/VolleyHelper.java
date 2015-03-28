package com.employeesystem.volley;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {

	private static VolleyHelper mInstance;

	public static final String TAG = "VolleyHelper";

	private RequestQueue mRequestQueue;
	private DefaultHttpClient defaultHttpClient;

	public VolleyHelper(Context context) {
		mInstance = this;

		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		final HttpParams params = new BasicHttpParams();
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		int timeoutSocket = 20000;
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, true);

		final ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		defaultHttpClient = new DefaultHttpClient();

		defaultHttpClient = new DefaultHttpClient(cm, params);

		final HttpStack httpStack = new HttpClientStack(defaultHttpClient);

		mRequestQueue = Volley.newRequestQueue(context, httpStack);

	}

	public DefaultHttpClient getDefaultHttpClient() {
		return defaultHttpClient;
	}

	public static VolleyHelper getInstance() {
		return mInstance;
	}

	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public void cancelPendingRequests() {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(TAG);
		}
	}

}
