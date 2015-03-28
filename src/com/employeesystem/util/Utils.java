package com.employeesystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.employeesystem.R;

public class Utils {

	private static final String TAG = "TAG";
	public static final String DATE_DISPLAY_FORMAT = "dd-MM-yyyy";
	public static final String DATE_DATABASE_FORMAT = "yyyy-MM-dd";

	public static final void e(String msg) {
		Log.e(TAG, msg);
	}

	public static final void v(String msg) {
		Log.v(TAG, msg);
	}

	public static final void w(String msg) {
		Log.w(TAG, msg);
	}

	public static final void d(String msg) {
		Log.d(TAG, msg);
	}

	public static final void i(String msg) {
		Log.i(TAG, msg);
	}

	public static String changeDateTimeFormat(String strDate, String srcFormat, String destFormat) {
		Date date = new Date();
		String strFormatedDate = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(srcFormat);
			date = formatter.parse(strDate);
			strFormatedDate = new SimpleDateFormat(destFormat).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strFormatedDate;
	}

	public static void displayDialog(final Context context, final String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	public static void storeString(final Context context, final String key, final String value) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		final Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void storeBoolean(final Context context, final String key, final Boolean value) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		final Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void storeInt(final Context context, final String key, final int value) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		final Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(final Context context, final String key) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);
	}

	public static String getString(final Context context, final String key) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}

	public static boolean getBoolean(final Context context, final String key) {
		final SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
		return preferences.getBoolean(key, false);
	}

	// To check internet is available or not
	public static boolean isConnectedToInternet(Context c) {
		ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static final ProgressDialog showProgressDialog(Context context, String title, String msg) {
		final ProgressDialog progressDialog = ProgressDialog.show(context, title, msg);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		return progressDialog;
	}

	public static final void dismissProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public static final void showAlertDialog(Context context, String title, String message) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle(title);
		dialogBuilder.setMessage(message);
		dialogBuilder.setNeutralButton(context.getString(android.R.string.ok), null);
		dialogBuilder.create().show();
	}

	public static void showNoInternetConnectionDialog(Context context) {
		showAlertDialog(context, context.getString(R.string.app_name), context.getString(R.string.no_internet));
	}

	public static void showNoResponseDialog(Context context) {
		showAlertDialog(context, context.getString(R.string.app_name), context.getString(R.string.no_response_from_server));
	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static String replaceQuote(String str) {
		return str.replaceAll("\"", "\\\"");
	}

	public static String replaceBackQuote(String str) {
		return str.replaceAll("\\\"", "\"");
	}

	public static String replaceSpace(final String str) {
		return str.replace(" ", "%20");
	}

	public static String getString(final String str) {
		if (str != null && !str.equalsIgnoreCase("")) {
			return str;
		}
		return "";
	}

}
