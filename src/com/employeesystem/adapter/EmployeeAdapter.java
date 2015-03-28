package com.employeesystem.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.fragment.AddEmployeeFragment;
import com.employeesystem.fragment.EmployeeFragment;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;

public class EmployeeAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<UserModel> arrayList;
	private EmployeeFragment employeeFragment;

	public EmployeeAdapter(final EmployeeFragment employeeFragment, final ArrayList<UserModel> arrayList) {
		this.context = employeeFragment.getActivity();
		inflater = ((Activity) context).getLayoutInflater();
		this.arrayList = arrayList;
		this.employeeFragment = employeeFragment;
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
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).getName() + " - " + arrayList.get(position).getUserRole());
		viewHolder.imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// dialogAddEmployee(position);
				final Bundle bundle = new Bundle();
				bundle.putParcelable(context.getString(R.string.key_employee_model), arrayList.get(position));
				final AddEmployeeFragment addEmployeeFragment = new AddEmployeeFragment(employeeFragment);
				addEmployeeFragment.setArguments(bundle);
				MainActivity.getInstance().addFragment(addEmployeeFragment, employeeFragment);
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

	private void deleteEmployee(final String deleteEmployeeId, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
		final String url = context.getString(R.string.url) + "deleteEmployee.php?deleteEmployeeId=" + deleteEmployeeId;
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

	private void deleteItem(final int item) {
		arrayList.remove(item);
		notifyDataSetChanged();
	}
}
