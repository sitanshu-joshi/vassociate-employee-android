package com.employeesystem.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;
import com.google.gson.Gson;

public class LoginFragment extends Fragment implements OnClickListener {
	private TextView btnLogin;
	private TextView btnAdmin;
	private TextView btnEmployee;
	private boolean isadmin = true;
	private EditText editTextUserName;
	private EditText editTextPassword;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_login, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		editTextUserName = (EditText) view.findViewById(R.id.fragment_login_editText_login);
		editTextPassword = (EditText) view.findViewById(R.id.fragment_login_editText_password);
		btnLogin = (TextView) view.findViewById(R.id.fragment_login_txt_login);
		btnAdmin = (TextView) view.findViewById(R.id.fragment_login_txt_admin);
		btnEmployee = (TextView) view.findViewById(R.id.fragment_login_txt_employee);
		final TextView textViewFeedBack = (TextView) view.findViewById(R.id.fragment_login_txt_feedback);
		btnLogin.setOnClickListener(this);
		textViewFeedBack.setOnClickListener(this);

	}

	private boolean isValid(final String username, final String password) {
		boolean isValid = false;
		if ((username.length() <= 0 && password.length() <= 0)) {
			Utils.displayDialog(getActivity(), "Please enter the all fields");
		} else if (username.length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the username");
		} else if (password.length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the password");
		} else {
			isValid = true;
		}
		return isValid;
	}

	// GET

	private void userLogin(final String username, final String password) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "login.php";
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					final Gson gson = new Gson();
					try {
						final Object obj = new JSONTokener(response).nextValue();
						if (obj instanceof JSONObject) {
							final JSONObject jsonObject = (JSONObject) obj;
							// final JSONObject jsonObjectError = new
							// JSONObject(response);
							final String error = jsonObject.optString("error");
							Utils.displayDialog(getActivity(), error);
						} else {
							final JSONArray jsonArray = (JSONArray) obj;
							final JSONObject jsonObject = jsonArray.getJSONObject(0);
							final UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);

							if (userModel != null) {
								if (userModel.getError() != null && !userModel.getError().isEmpty()) {
									Utils.displayDialog(getActivity(), userModel.getError());
								} else {
									Utils.storeString(getActivity(), getActivity().getString(R.string.key_user_id), userModel.getUserId());
									Utils.storeString(getActivity(), getActivity().getString(R.string.key_user_role), userModel.getUserRole());

									Utils.storeString(getActivity(), getActivity().getString(R.string.key_name), userModel.getName());

									Utils.storeString(getActivity(), getActivity().getString(R.string.key_email), userModel.getEmail());

									Utils.storeString(getActivity(), getActivity().getString(R.string.key_contact_number), userModel.getContactNumber());

									Utils.storeString(getActivity(), getActivity().getString(R.string.key_designation), userModel.getDesignation());
									MainActivity.getInstance().openFragment(new AssociateFragment());
								}
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Utils.showNoResponseDialog(getActivity());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Utils.dismissProgressDialog(progressDialog);
				Utils.displayDialog(getActivity(), "Server error");
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				final Map<String, String> params = new HashMap<String, String>();
				params.put("username", username);
				params.put("password", password);
				params.put("login", "login");
				return params;
			}
		};
		queue.add(postRequest);
	}

	@Override
	public void onClick(View v) {
		if (v == btnLogin) {
			if (isValid(editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim())) {
				if (Utils.isConnectedToInternet(getActivity())) {
					userLogin(editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim());
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
			}
		} else if (v == btnAdmin) {
			isadmin = true;
		} else if (v == btnEmployee) {
			isadmin = false;
		} else if (v.getId() == R.id.fragment_login_txt_feedback) {
			sendFeedback();
		}
	}

	private void sendFeedback() {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		String to = "gregorystelmire55@gmail.com";
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "mohitgohelce@gmail.com" });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \nThis is a new feedback.");
		startActivity(Intent.createChooser(emailIntent, "Send Feedback..."));
	}
}
