package com.employeesystem.fragment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.ParseException;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.employeesystem.R;

public class WSPostComment {

	private Context context;

	private String message;
	private String status;

	public WSPostComment(Context context) {
		this.context = context;
	}

	// To execute request-response
	// http://localhost:8080/Troyage/ws/city/post/{postId}/comment

	public void execute(final String file, final String taskname, final String taskdesc, final String taskstartdate, final String addEmployeeId, final String addAdminId,
			final String taskstatus, final String addSiteId, Bitmap bitmap) {
		// http://localhost:8080/Troyage/ws/city/post/{cityId}
		final String baseURL = context.getString(R.string.url) + "addTask.php?taskname=" + taskname + "&taskdesc=" + taskdesc + "&taskstartdate=03/03/2015" + "&addEmployeeId="
				+ addEmployeeId + "&addAdminId=" + addAdminId + "&taskstatus=" + taskstatus + "&addsiteId=" + addSiteId;
		final String response = postMultiPartMethod(context, baseURL, file, taskname, taskdesc, taskstartdate, addEmployeeId, addAdminId, taskstatus, addSiteId, bitmap);
		parseJSON(response);
	}

	@SuppressWarnings("deprecation")
	public String postMultiPartMethod(Context context, String url, final String file, final String taskname, final String taskdesc, final String taskstartdate,
			final String addEmployeeId, final String addAdminId, final String taskstatus, final String addSiteId, Bitmap bitmap) {
		// final HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader("Content-type", "multipart/form-data");
		//
		// try {
		// @SuppressWarnings("deprecation")
		// final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		// entity.addPart("taskname", new StringBody(taskname));
		// entity.addPart("taskdesc", new StringBody(taskdesc));
		// entity.addPart("taskstartdate", new StringBody("03/03/2015"));
		// entity.addPart("addEmployeeId", new StringBody(addEmployeeId));
		// entity.addPart("addAdminId", new StringBody(addAdminId));
		// entity.addPart("taskstatus", new StringBody(taskstatus));
		// entity.addPart("addsiteId", new StringBody(addSiteId));
		// //
		// if (file != null && !file.equalsIgnoreCase("")) {
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// bitmap.compress(CompressFormat.PNG, 75, bos);
		// byte[] data = bos.toByteArray();
		// ByteArrayBody bab = new ByteArrayBody(data, "image/jpeg");
		//
		// entity.addPart("taskimg[0]", bab);
		// // entity.addPart("taskimg[0]", new FileBody(new File(file), "image/jpeg"));
		// }
		// httpPost.setEntity(entity);
		// // Execute HTTP Post Request
		// EmployeeApp imanApplication = (EmployeeApp) context.getApplicationContext();
		// final HttpResponse httpResponse = imanApplication.getHttpClient().execute(httpPost);
		// final String response = new WebServiceUtil().convertInputStreamToString(httpResponse);

		try {
			// final String post = "taskname=" + taskname + "&taskdesc=" + taskdesc + "&taskstartdate=03/03/2015" + "&addEmployeeId=" + addEmployeeId + "&addAdminId=" + addAdminId
			// + "&taskstatus=" + taskstatus + "&addsiteId=" + addSiteId;
			String response = multipartRequest(url, "", file, "taskimg[0]");
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		// } catch (Exception e) {
		// e.printStackTrace();
		// return "";
		// }

	}

	@SuppressWarnings("deprecation")
	// To parse response data
	private void parseJSON(String response) {
		try {
			if (response != null && !response.isEmpty()) {
				Log.e("response", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public String multipartRequest(String urlTo, String post, String filepath, String filefield) throws ParseException, IOException {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;

		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";

		String result = "";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);

			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			// String[] posts = post.split("&");
			// int max = posts.length;
			// for (int i = 0; i < max; i++) {
			// outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			// String[] kv = posts[i].split("=");
			// outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
			// outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
			// outputStream.writeBytes(lineEnd);
			// outputStream.writeBytes(kv[1]);
			// outputStream.writeBytes(lineEnd);
			// }

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			inputStream = connection.getInputStream();
			result = this.convertStreamToString(inputStream);

			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();

			return result;
		} catch (Exception e) {
			Log.e("MultipartRequest", "Multipart Form Upload Error");
			e.printStackTrace();
			return "error";
		}
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void put(String targetURL, File file, String username, String password) throws Exception {

		String BOUNDRY = "==================================";
		HttpURLConnection conn = null;

		try {

			// These strings are sent in the request body. They provide information about the file being uploaded
			String contentDisposition = "Content-Disposition: form-data; name=\"userfile\"; filename=\"" + file.getName() + "\"";
			String contentType = "Content-Type: application/octet-stream";

			// This is the standard format for a multipart request
			StringBuffer requestBody = new StringBuffer();
			requestBody.append("--");
			requestBody.append(BOUNDRY);
			requestBody.append('\n');
			requestBody.append(contentDisposition);
			requestBody.append('\n');
			requestBody.append(contentType);
			requestBody.append('\n');
			requestBody.append('\n');
			requestBody.append(new String(getBytesFromFile(file)));
			requestBody.append("--");
			requestBody.append(BOUNDRY);
			requestBody.append("--");

			// Make a connect to the server
			URL url = new URL(targetURL);
			conn = (HttpURLConnection) url.openConnection();

			// Put the authentication details in the request
			// if (username != null) {
			// String usernamePassword = username + ":" + password;
			// String encodedUsernamePassword = Base64.encodeBytes(usernamePassword.getBytes());
			// conn.setRequestProperty("Authorization", "Basic " + encodedUsernamePassword);
			// }

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDRY);

			// Send the body
			DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
			dataOS.writeBytes(requestBody.toString());
			dataOS.flush();
			dataOS.close();

			// Ensure we got the HTTP 200 response code
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				throw new Exception(String.format("Received the response code %d from the URL %s", responseCode, url));
			}

			// Read the response
			InputStream is = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(bytes)) != -1) {
				baos.write(bytes, 0, bytesRead);
			}
			byte[] bytesReceived = baos.toByteArray();
			baos.close();

			is.close();
			String response = new String(bytesReceived);

			// TODO: Do something here to handle the 'response' string

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, Math.min(bytes.length - offset, 512 * 1024))) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

}
