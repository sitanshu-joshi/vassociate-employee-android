package com.employeesystem.fragment;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;

@SuppressLint("ValidFragment")
public class AddEmployeeFragment extends Fragment {
	private EmployeeFragment employeeFragment;
	private UserModel userModel;
	private UserModel taskModel;

	public AddEmployeeFragment(final EmployeeFragment employeeFragment) {
		this.employeeFragment = employeeFragment;
	}

	public AddEmployeeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			if (getArguments().getParcelable(getString(R.string.key_employee_model)) != null) {
				userModel = getArguments().getParcelable(getString(R.string.key_employee_model));
			} else if (getArguments().getParcelable(getString(R.string.key_task_model)) != null) {
				taskModel = getArguments().getParcelable(getString(R.string.key_task_model));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_add_employees, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {

		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_create_task_title);
		final EditText editTextName = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_name);
		final EditText editTextUserName = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_username);
		final EditText editTextPassword = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_password);
		final EditText editTextEmail = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_email);
		final EditText editTextContact = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_contact);
		final EditText editTextDesignation = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_designation);
		final Spinner spinnercategory = (Spinner) view.findViewById(R.id.dialog_add_employees_spinner_type_user);
		final Button btnAdd = (Button) view.findViewById(R.id.dialog_add_employees_btn_add);
		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Administrator");
		arrayList.add("Employee");
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnercategory.setAdapter(adapter);
		final TextView txtEmployee = (TextView) view.findViewById(R.id.dialog_add_task_spinner_employee_name_txt);
		txtEmployee.setTextColor(editTextDesignation.getHintTextColors());
		if (userModel != null) {
			btnAdd.setText("Update");
			textViewTitle.setText("Update Employee Details");
			editTextName.setText("" + userModel.getName());
			editTextUserName.setText("" + userModel.getUserName());
			editTextPassword.setText("" + userModel.getUserPassword());
			editTextEmail.setText("" + userModel.getEmail());
			editTextContact.setText("" + userModel.getContactNumber());
			editTextDesignation.setText("" + userModel.getDesignation());

			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).equalsIgnoreCase("" + userModel.getUserRole())) {
					spinnercategory.setSelection(i);
				}
			}
		}
		if (taskModel != null) {
			final EditText editTextRole = (EditText) view.findViewById(R.id.dialog_add_employees_edittext_role);
			final LinearLayout empSpinnerLL = (LinearLayout) view.findViewById(R.id.dialog_add_task_spinner_employee_ll);
			btnAdd.setVisibility(View.GONE);
			textViewTitle.setText("Employee Details");
			final ScrollView scrollView = (ScrollView) view.findViewById(R.id.fragment_add_employees_scrollView);
			final TextView textView = (TextView) view.findViewById(R.id.fragment_add_employees_details);
			scrollView.setVisibility(View.GONE);
			textView.setText("Name           : " + taskModel.getName() + "\nUsername    : " + taskModel.getUserName() + "\nPassword    : " + taskModel.getUserPassword()
					+ "\nEmail            : " + taskModel.getEmail() + "\nContact        : " + taskModel.getContactNumber() + "\nDesignation : " + taskModel.getDesignation()
					+ "\nRole              : " + taskModel.getUserRole());
			editTextName.setText("" + taskModel.getName());
			editTextUserName.setText("" + taskModel.getUserName());
			editTextPassword.setText("" + taskModel.getUserPassword());
			editTextEmail.setText("" + taskModel.getEmail());
			editTextContact.setText("" + taskModel.getContactNumber());
			editTextDesignation.setText("" + taskModel.getDesignation());
			editTextRole.setText("" + taskModel.getUserRole());
			empSpinnerLL.setVisibility(View.GONE);
		}

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isConnectedToInternet(getActivity())) {
					if (userModel != null) {
						editEmployee(userModel.getUserId(), editTextName.getText().toString().trim(), editTextUserName.getText().toString().trim(), editTextPassword.getText()
								.toString().trim(), editTextEmail.getText().toString().trim(), editTextContact.getText().toString().trim(), editTextDesignation.getText()
								.toString().trim(), arrayList.get(spinnercategory.getSelectedItemPosition()));
					} else {
						addEmployee(editTextName.getText().toString().trim(), editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim(),
								editTextEmail.getText().toString().trim(), editTextContact.getText().toString().trim(), editTextDesignation.getText().toString().trim(),
								arrayList.get(spinnercategory.getSelectedItemPosition()));
					}
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
			}
		});

	}

	private void addEmployee(final String name, final String username, final String password, final String email, final String contact, final String designation,
			final String userRole) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "addEmployee.php?employeename=" + Utils.replaceSpace(name) + "&employeeusername=" + Utils.replaceSpace(username)
				+ "&employeepassword=" + Utils.replaceSpace(password) + "&employeeemail=" + Utils.replaceSpace(email) + "&employeecontact=" + Utils.replaceSpace(contact)
				+ "&employeedesignation=" + Utils.replaceSpace(designation) + "&employeeuserrole=" + Utils.replaceSpace(userRole);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response {"success":"Add Employee Successfully"}

				Utils.dismissProgressDialog(progressDialog);
				Utils.e(response);
				try {
					final JSONObject jsonObject = new JSONObject(response);
					final String error = jsonObject.optString("error");
					final String success = jsonObject.optString("success");
					// 03-18 15:04:04.793: E/TAG(7549): {"User Exists":" Please check the username or email"}
					final String userExists = jsonObject.optString("User Exists");
					if (userExists != null) {
						Utils.displayDialog(getActivity(), userExists.trim());
					} else {
						if (error != null && !error.isEmpty()) {
							Utils.displayDialog(getActivity(), error);
						} else {
							if (success != null) {
								getFragmentManager().popBackStack();
								employeeFragment.employeeList();
								Utils.displayDialog(getActivity(), success);
							}
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
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

	private void editEmployee(final String employeeId, final String name, final String username, final String password, final String email, final String contact,
			final String designation, final String userRole) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "editEmployee.php?editEmployeeId=" + Utils.replaceSpace(employeeId) + "&employeename=" + Utils.replaceSpace(name)
				+ "&employeeusername=" + Utils.replaceSpace(username) + "&employeepassword=" + Utils.replaceSpace(password) + "&employeeemail=" + Utils.replaceSpace(email)
				+ "&employeecontact=" + Utils.replaceSpace(contact) + "&employeedesignation=" + Utils.replaceSpace(designation) + "&employeeuserrole="
				+ Utils.replaceSpace(userRole);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response {"success":"Add Employee Successfully"}
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					try {
						final JSONObject jsonObject = new JSONObject(response);
						final String error = jsonObject.optString("error");
						final String success = jsonObject.optString("success");
						if (error != null && !error.isEmpty()) {
							Utils.displayDialog(getActivity(), error);
						} else {
							if (success != null) {
								if (Utils.isConnectedToInternet(getActivity())) {
									employeeFragment.employeeList();
								}
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
