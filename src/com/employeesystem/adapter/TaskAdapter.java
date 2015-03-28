package com.employeesystem.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class TaskAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<UserModel> arrayList;
	private int pos;

	public TaskAdapter(Context context, final ArrayList<UserModel> arrayList) {
		this.context = context;
		inflater = ((Activity) context).getLayoutInflater();
		this.arrayList = arrayList;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return arrayList.get(position).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_employee_details, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.txtEmployeeName = (TextView) convertView.findViewById(R.id.row_employee_details_textview);
			viewHolder.imageViewEdit = (ImageView) convertView.findViewById(R.id.row_employee_details_imageview_edit);
			viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.row_employee_details_imageview_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).getName());
		viewHolder.imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isConnectedToInternet(context)) {
					dialogAddEmployee(position);
				} else {
					Utils.showNoInternetConnectionDialog(context);
				}
			}
		});

		viewHolder.imageViewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isConnectedToInternet(context)) {
					deleteEmployee("" + arrayList.get(position).getUserId(), position);
				} else {
					Utils.showNoInternetConnectionDialog(context);
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		private TextView txtEmployeeName;
		private ImageView imageViewEdit;
		private ImageView imageViewDelete;
	}

	private void dialogAddEmployee(final int posItem) {
		final Dialog dialogView = new Dialog(context);
		dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogView.setContentView(R.layout.dialog_add_employees);
		final EditText editTextName = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_name);
		final EditText editTextUserName = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_username);
		final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_password);
		final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_email);
		final EditText editTextContact = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_contact);
		final EditText editTextDesignation = (EditText) dialogView.findViewById(R.id.dialog_add_employees_edittext_designation);
		final Spinner spinnercategory = (Spinner) dialogView.findViewById(R.id.dialog_add_employees_spinner_type_user);
		final Button btnAdd = (Button) dialogView.findViewById(R.id.dialog_add_employees_btn_add);
		final ArrayList<String> arrayListType = new ArrayList<String>();
		arrayListType.add("Administrator");
		arrayListType.add("Employee");
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, arrayListType);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnercategory.setAdapter(adapter);

		editTextName.setText("" + arrayList.get(posItem).getName());
		editTextUserName.setText("" + arrayList.get(posItem).getUserName());
		editTextPassword.setText("" + arrayList.get(posItem).getUserPassword());
		editTextEmail.setText("" + arrayList.get(posItem).getEmail());
		editTextContact.setText("" + arrayList.get(posItem).getContactNumber());
		editTextDesignation.setText("" + arrayList.get(posItem).getDesignation());

		spinnercategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				pos = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		btnAdd.setText("Update");
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editEmployee(dialogView, arrayList.get(posItem).getUserId(), editTextName.getText().toString().trim(), editTextUserName.getText().toString().trim(),
						editTextPassword.getText().toString().trim(), editTextEmail.getText().toString().trim(), editTextContact.getText().toString().trim(), editTextDesignation
								.getText().toString().trim(), arrayListType.get(pos), posItem);
			}
		});

		dialogView.show();
	}

	private void deleteEmployee(final String deleteEmployeeId, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
		final String url = context.getString(R.string.url) + "deleteEmployee.php?deleteEmployeeId=" + Utils.replaceSpace(deleteEmployeeId);
		final ProgressDialog progressDialog = Utils.showProgressDialog(context, "", "Loading...");
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
					if (error != null && !error.isEmpty()) {
						Utils.displayDialog(context, error);
					} else {
						if (success != null) {
							deleteItem(position);
							Utils.displayDialog(context, success);
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
				Utils.e(error.getMessage());
				Utils.displayDialog(context, error.getMessage());
			}
		});
		queue.add(postRequest);
	}

	private void editEmployee(final Dialog dialog, final String employeeId, final String name, final String username, final String password, final String email,
			final String contact, final String designation, final String userRole, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
		final String url = context.getString(R.string.url) + "editEmployee.php?editEmployeeId=" + Utils.replaceSpace(employeeId) + "&employeename=" + Utils.replaceSpace(name)
				+ "&employeeusername=" + Utils.replaceSpace(username) + "&employeepassword=" + Utils.replaceSpace(password) + "&employeeemail=" + Utils.replaceSpace(email)
				+ "&employeecontact=" + contact + "&employeedesignation=" + designation + "&employeeuserrole=" + Utils.replaceSpace(userRole);
		final ProgressDialog progressDialog = Utils.showProgressDialog(context, "", "Loading...");
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
					if (error != null && !error.isEmpty()) {
						Utils.displayDialog(context, error);
					} else {
						if (success != null) {
							dialog.dismiss();
							arrayList.get(position).setName(name);
							arrayList.get(position).setUserName(username);
							arrayList.get(position).setUserPassword(password);
							arrayList.get(position).setEmail(email);
							arrayList.get(position).setContactNumber(contact);
							arrayList.get(position).setDesignation(designation);
							arrayList.get(position).setUserRole(userRole);
							notifyDataSetChanged();
							Utils.displayDialog(context, success);
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
				Utils.e(error.getMessage());
				Utils.displayDialog(context, error.getMessage());
			}
		});
		queue.add(postRequest);
	}

	private void deleteItem(final int item) {
		arrayList.remove(item);
		notifyDataSetChanged();
	}
}
