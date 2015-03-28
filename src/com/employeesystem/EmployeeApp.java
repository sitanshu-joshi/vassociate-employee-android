package com.employeesystem;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.app.Application;

import com.employeesystem.volley.VolleyHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EmployeeApp extends Application {
	private static EmployeeApp mInstance;
	private VolleyHelper mVolleyHelper;
	private DefaultHttpClient httpClient;
	private ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		httpClient = createHttpClient();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000) // 1.5 Mb
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()) // Not
																												// necessary
																												// in
																												// common
				.build();
		// Initialize ImageLoader with configuration.
		imageLoader = ImageLoader.getInstance();

		imageLoader.init(config);
	}

	public static EmployeeApp getInstance() {
		return mInstance;
	};

	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	private DefaultHttpClient createHttpClient() {
		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		final HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);

		final ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		final DefaultHttpClient client = new DefaultHttpClient(cm, params);

		return client;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

}
