package com.employeesystem.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.model.TaskModel;
import com.employeesystem.util.Utils;

public class TaskStatusAdapter extends BaseAdapter implements Filterable {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<TaskModel> arrayList;
	private ArrayList<TaskModel> originalDataArrayList = null;
	private ItemFilter mFilter = new ItemFilter();

	public TaskStatusAdapter(Context context, final ArrayList<TaskModel> employeeList) {
		this.context = context;
		inflater = ((Activity) context).getLayoutInflater();
		this.arrayList = employeeList;
		originalDataArrayList = employeeList;
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
			convertView = inflater.inflate(R.layout.row_task_stutas, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.txtEmployeeName = (TextView) convertView.findViewById(R.id.row_employee_details_textview_name);
			viewHolder.textViewStatus = (TextView) convertView.findViewById(R.id.row_employee_details_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).task_name);
		viewHolder.textViewStatus.setText("" + arrayList.get(position).task_status);
		return convertView;
	}

	public class ViewHolder {
		private TextView txtEmployeeName;
		private TextView textViewStatus;
	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}

	private void editTask(final String editTaskid, final String taskStatus, final int position) {
		final RequestQueue queue = Volley.newRequestQueue(context);
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
				Utils.e(error.getMessage());
				Utils.displayDialog(context, error.getMessage());
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
