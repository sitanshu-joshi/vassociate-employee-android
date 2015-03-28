package com.employeesystem.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.adapter.EmployeeAdapter;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EmployeeFragment extends Fragment implements OnItemClickListener {
	private ListView listView;

	public EmployeeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_employ_details, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final ImageView imageViewAddEmployee = (ImageView) view.findViewById(R.id.fragment_employee_details_imageview_add);
		listView = (ListView) view.findViewById(R.id.fragment_employee_details_listview);
		listView.setOnItemClickListener(this);

		if (Utils.isConnectedToInternet(getActivity())) {
			employeeList();
		} else {
			Utils.showNoInternetConnectionDialog(getActivity());
		}

		imageViewAddEmployee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final AddEmployeeFragment addEmployeeFragment = new AddEmployeeFragment(EmployeeFragment.this);
				getFragmentManager().beginTransaction().add(R.id.container, addEmployeeFragment, addEmployeeFragment.getClass().getSimpleName()).hide(EmployeeFragment.this)
						.addToBackStack(addEmployeeFragment.getClass().getSimpleName()).commit();
			}
		});
	}

	public void employeeList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewEmployee.php";
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					try {

						final Object obj = new JSONTokener(response).nextValue();
						if (obj instanceof JSONObject) {
							final JSONObject jsonObject = (JSONObject) obj;
							// final JSONObject jsonObjectError = new JSONObject(response);
							final String error = jsonObject.optString("error");
							Utils.displayDialog(getActivity(), error);
						} else {
							final JSONArray jsonArray = (JSONArray) obj;
							final Type listType = new TypeToken<List<UserModel>>() {
							}.getType();
							final Gson gson = new Gson();
							final ArrayList<UserModel> employeeList = (ArrayList<UserModel>) gson.fromJson(jsonArray.toString(), listType);

							// final UserModel userModel = gson.fromJson(
							// jsonObject.toString(), UserModel.class);

							if (employeeList != null && !employeeList.isEmpty()) {
								if (employeeList.get(0).getError() != null && !employeeList.get(0).getError().isEmpty()) {
									Utils.displayDialog(getActivity(), employeeList.get(0).getError());
								} else {
									final EmployeeAdapter adapter = new EmployeeAdapter(EmployeeFragment.this, employeeList);
									listView.setAdapter(adapter);
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
		});
		queue.add(postRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		final UserModel taskModel = (UserModel) listView.getItemAtPosition(position);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(getActivity().getString(R.string.key_task_model), taskModel);
		final AddEmployeeFragment addTaskFragment = new AddEmployeeFragment(EmployeeFragment.this);
		addTaskFragment.setArguments(bundle);
		MainActivity.getInstance().addFragment(addTaskFragment, EmployeeFragment.this);
	}

	// private void dialogAddEmployee() {
	// final Dialog dialogView = new Dialog(getActivity());
	// dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialogView.setContentView(R.layout.dialog_add_employees);
	// final EditText editTextName = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_name);
	// final EditText editTextUserName = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_username);
	// final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_password);
	// final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_email);
	// final EditText editTextContact = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_contact);
	// final EditText editTextDesignation = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_designation);
	// final Spinner spinnercategory = (Spinner) dialogView.findViewById(R.id.dialog_add_employees_spinner_type_user);
	// final Button btnAdd = (Button) dialogView.findViewById(R.id.dialog_add_employees_btn_add);
	// final ArrayList<String> arrayList = new ArrayList<String>();
	// arrayList.add("Administrator");
	// arrayList.add("Employee");
	// final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList);
	// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// spinnercategory.setAdapter(adapter);
	//
	// spinnercategory.setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	// pos = position;
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	//
	// }
	// });
	//
	// btnAdd.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// addEmployee(dialogView, editTextName.getText().toString().trim(), editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim(),
	// editTextEmail.getText().toString().trim(), editTextContact.getText().toString().trim(), editTextDesignation.getText().toString().trim(), arrayList.get(pos));
	// }
	// });
	//
	// dialogView.show();
	// }

	// employeename=name,
	// employeeusername=login user name ,
	// employeepassword= login password ,
	// employeeemail= mail ,
	// employeecontact = contact ,
	// employeedesignation= designation ,
	// employeeuserrole = Administrator or Employee

}
