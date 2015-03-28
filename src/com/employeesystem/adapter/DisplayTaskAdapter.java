package com.employeesystem.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.employeesystem.activity.MainActivity;
import com.employeesystem.fragment.AddTaskFragment;
import com.employeesystem.fragment.TaskFragment;
import com.employeesystem.model.TaskModel;
import com.employeesystem.util.Utils;

public class DisplayTaskAdapter extends BaseAdapter implements Filterable {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<TaskModel> arrayList;
	private ArrayList<TaskModel> originalDataArrayList = null;
	private ItemFilter mFilter = new ItemFilter();
	private Fragment fragment;

	public DisplayTaskAdapter(final Fragment fragment, final ArrayList<TaskModel> employeeList) {
		this.context = fragment.getActivity();
		inflater = ((Activity) context).getLayoutInflater();
		this.arrayList = employeeList;
		originalDataArrayList = employeeList;
		this.fragment = fragment;
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
			convertView = inflater.inflate(R.layout.row_task_details, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.txtEmployeeName = (TextView) convertView.findViewById(R.id.row_employee_details_textview);
			viewHolder.imageViewEdit = (ImageView) convertView.findViewById(R.id.row_employee_details_imageview_edit);
			viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.row_employee_details_imageview_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).task_name);

		if (Utils.getString(context, context.getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
			viewHolder.imageViewDelete.setVisibility(View.GONE);
			// viewHolder.imageViewEdit.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageViewDelete.setVisibility(View.VISIBLE);
			// viewHolder.imageViewEdit.setVisibility(View.GONE);
		}

		viewHolder.imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// dialogAddEmployee(position);

				if (Utils.getString(context, context.getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
					if (Utils.isConnectedToInternet(context)) {
						dialogEditTask(position);
					} else {
						Utils.showNoInternetConnectionDialog(context);
					}
				} else {
					final Bundle bundle = new Bundle();
					bundle.putParcelable(context.getString(R.string.key_task_model), arrayList.get(position));
					bundle.putString(context.getString(R.string.key_is_edit), "isEdit");
					final AddTaskFragment addTaskFragment = new AddTaskFragment((TaskFragment) fragment);
					addTaskFragment.setArguments(bundle);
					MainActivity.getInstance().addFragment(addTaskFragment, fragment);
				}

			}
		});
		viewHolder.imageViewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// dialogAddEmployee(position);
				if (Utils.isConnectedToInternet(context)) {
					deleteTask(arrayList.get(position).task_id, position);
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

	private void editTask(final String editTaskid, final String taskStatus, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
		// final String taskId = editTaskid.replace(" ", "%20");
		// final String taStatus = taskStatus.replace(" ", "%20");
		final String url = context.getString(R.string.url) + "editEmployeeTaskStatus.php?editTaskid=" + Utils.replaceSpace(editTaskid) + "&taskStatus="
				+ Utils.replaceSpace(taskStatus);
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
							editTask(position, taskStatus);
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
				Utils.displayDialog(context, "Server error");
			}
		});
		queue.add(postRequest);
	}

	private void deleteTask(final String editTaskid, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
		// final String taskId = editTaskid.replace(" ", "%20");
		final String url = context.getString(R.string.url) + "deleteTask.php?deleteTaskId=" + Utils.replaceSpace(editTaskid);
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
							arrayList.remove(position);
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
				Utils.displayDialog(context, "Server error");
			}
		});
		queue.add(postRequest);
	}

	private void dialogEditTask(final int position) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		final LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View dialogView = li.inflate(R.layout.dialog_edit_task, null);
		dialog.setCancelable(true);
		dialog.setView(dialogView);

		final Spinner spinnercategory = (Spinner) dialogView.findViewById(R.id.dialog_edit_task_spinner);
		final ArrayList<String> taskList = new ArrayList<String>();
		taskList.add("Started");
		taskList.add("Completed");
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, taskList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnercategory.setAdapter(adapter);

		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).equalsIgnoreCase(arrayList.get(position).getTask_status())) {
				spinnercategory.setSelection(i);
			}
		}

		dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (Utils.isConnectedToInternet(context)) {
					editTask(arrayList.get(position).getTask_id(), spinnercategory.getSelectedItem().toString(), position);
				} else {
					Utils.showNoInternetConnectionDialog(context);
				}
			}
		});

		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		dialog.show();

	}

	private void editTask(final int item, final String taskStatus) {
		arrayList.get(item).setTask_status(taskStatus);
		notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			final String filterString = constraint.toString().toLowerCase();
			final FilterResults results = new FilterResults();
			final List<TaskModel> list = originalDataArrayList;
			final int count = list.size();
			final ArrayList<TaskModel> nlist = new ArrayList<TaskModel>(count);
			String filterableString;
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).getTask_status();
				if (filterableString.toLowerCase().contains(filterString)) {
					nlist.add(list.get(i));
				}
			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			arrayList = (ArrayList<TaskModel>) results.values;
			notifyDataSetChanged();
		}

	}

}
