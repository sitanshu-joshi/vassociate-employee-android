package com.employeesystem.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.model.QueryModel;
import com.employeesystem.util.Utils;

@SuppressLint("ValidFragment")
public class AddQueryFragment extends Fragment {

	// private int pos;
	private QueryFragment queryFragment;
	private QueryModel queryModel;

	public AddQueryFragment(final QueryFragment queryFragment) {
		this.queryFragment = queryFragment;
	}

	public AddQueryFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			queryModel = getArguments().getParcelable(getString(R.string.key_query_model));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_add_query, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_create_query_title);
		final EditText editTextClientName = (EditText) view.findViewById(R.id.fragment_add_query_edittext_client_name);
		final EditText editTextClientAddress = (EditText) view.findViewById(R.id.fragment_add_query_edittext_client_address);
		final EditText editTextClientPhone = (EditText) view.findViewById(R.id.fragment_add_query_edittext_client_phone);
		final Button btnAdd = (Button) view.findViewById(R.id.fragment_add_query_btn_add);

		if (queryModel != null) {
			textViewTitle.setText("View Query Details");
			btnAdd.setVisibility(View.GONE);
			editTextClientName.setText("" + queryModel.getClient_Name());
			editTextClientAddress.setText("" + queryModel.getClient_Address());
			editTextClientPhone.setText("" + queryModel.getClient_Phone());
		}
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isConnectedToInternet(getActivity())) {
					if (isValid(editTextClientName, editTextClientAddress, editTextClientPhone)) {
						// addTasks(editTextName.getText().toString().trim(),
						// editTextDescription.getText().toString().trim(),
						// editTextStartDate.getText().toString().trim(),
						// employeeList.get(pos).getUserId(), "" +
						// Utils.getString(getActivity(),
						// getString(R.string.key_user_id)),
						// editTextStatus.getText().toString().trim());

						// addTasks(editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim(), editTextStartDate.getText().toString().trim(),
						// employeeList.get(spinnercategory.getSelectedItemPosition()).getUserId(), "" + Utils.getString(getActivity(), getString(R.string.key_user_id)),
						// editTextStatus.getText().toString().trim());
						addQuery(editTextClientName.getText().toString().trim(), editTextClientAddress.getText().toString().trim(), editTextClientPhone.getText().toString().trim());
					}
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
			}
		});
	}

	private boolean isValid(final EditText editTextName, final EditText editTextDescription, final EditText editTextStartDate) {
		boolean isValid = false;

		if (editTextName.getText().toString().trim().length() <= 0 && editTextDescription.getText().toString().trim().length() <= 0
				&& editTextDescription.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the all the fields");
		} else if (editTextName.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the client name");
		} else if (editTextDescription.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the client address");
		} else if (editTextStartDate.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the client phone");
		} else {
			isValid = true;
		}
		return isValid;

	}

	private void addQuery(final String clientname, final String clientaddress, final String clientphone) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "addClientQuery.php?clientname=" + Utils.replaceSpace(clientname) + "&clientaddress=" + Utils.replaceSpace(clientaddress)
				+ "&clientphone=" + Utils.replaceSpace(clientphone);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response {"success":"Add Employee Successfully"}

				Utils.dismissProgressDialog(progressDialog);
				Utils.e(response);
				if (response != null && !response.isEmpty()) {
					try {
						final JSONObject jsonObject = new JSONObject(response);
						final String error = jsonObject.optString("error");
						final String success = jsonObject.optString("success");
						if (error != null && !error.isEmpty()) {
							Utils.displayDialog(getActivity(), error);
						} else {
							if (success != null) {
								queryFragment.taskList();
								getFragmentManager().popBackStack();
								Utils.displayDialog(getActivity(), success);
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
		});
		queue.add(postRequest);
	}

}
