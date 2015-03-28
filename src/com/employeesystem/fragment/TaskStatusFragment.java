package com.employeesystem.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.adapter.TaskStatusAdapter;
import com.employeesystem.model.TaskModel;
import com.employeesystem.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskStatusFragment extends Fragment {
	private ListView listView;
	private TextView txtDate;
	private ArrayList<TaskModel> employeeList;
	private TaskStatusAdapter taskStatusAdapter;
	private Calendar cMod;
	private TextView txtNodata;
	private Spinner spinner;

	public TaskStatusFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_create_task, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final ImageView imageViewAddTask = (ImageView) view.findViewById(R.id.fragment_create_task_imageview_add);
		final ImageView imageViewAddCalender = (ImageView) view.findViewById(R.id.fragment_create_task_imageview_calender);
		listView = (ListView) view.findViewById(R.id.fragment_create_task_listview);
		txtDate = (TextView) view.findViewById(R.id.fragment_create_task_date);
		txtNodata = (TextView) view.findViewById(R.id.fragment_create_task_empty);
		spinner = (Spinner) view.findViewById(R.id.fragment_create_task_spinner);
		listView.setEmptyView(txtNodata);
		imageViewAddTask.setVisibility(View.GONE);
		final Calendar c = Calendar.getInstance();
		final int mYear = c.get(Calendar.YEAR);
		final int mMonth = c.get(Calendar.MONTH);
		final int mDay = c.get(Calendar.DAY_OF_MONTH);

		final ArrayList<String> taskList = new ArrayList<String>();
		taskList.add(getActivity().getString(R.string.pending));
		taskList.add(getActivity().getString(R.string.started));
		taskList.add(getActivity().getString(R.string.completed));

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, taskList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		if (Utils.isConnectedToInternet(getActivity())) {
			taskDateList(mDay + "-" + (mMonth + 1) + "-" + mYear);
			// taskList();
		} else {
			Utils.showNoInternetConnectionDialog(getActivity());
		}
		// txtDate.setText("Date: " + mDay + "-" + (mMonth + 1) + "-" + mYear);
		// txtDate.setText("All Tasks");
		txtDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

		imageViewAddCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cMod != null) {
					final DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, cMod.get(Calendar.YEAR), cMod.get(Calendar.MONTH), cMod
							.get(Calendar.DAY_OF_MONTH));
					dpd.show();
				} else {
					final DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
					dpd.show();

				}
			}
		});

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (taskStatusAdapter != null) {
					taskStatusAdapter.getFilter().filter(taskList.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			if (view.isShown()) {
				// editTextStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				if (employeeList != null) {
					employeeList.clear();
				}
				if (taskStatusAdapter != null) {
					taskStatusAdapter.notifyDataSetChanged();
				}
				if (Utils.isConnectedToInternet(getActivity())) {
					taskDateList(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
				txtDate.setText("Date: " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
				cMod = Calendar.getInstance();
				cMod.set(year, monthOfYear, dayOfMonth);
			}
		}
	};

	public void taskList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		String url = "";
		url = getString(R.string.url) + "viewTask.php";
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					final Gson gson = new Gson();
					try {
						// {"error":"no task assign"}
						final Object obj = new JSONTokener(response).nextValue();

						if (obj instanceof JSONObject) {

							final JSONObject jsonObject = new JSONObject(response);
							final String jsonObjectError = jsonObject.optString("error");

							if (jsonObjectError != null && !jsonObjectError.isEmpty()) {
								Utils.displayDialog(getActivity(), jsonObjectError);
								listView.setVisibility(View.GONE);
								txtNodata.setVisibility(View.VISIBLE);
							}
							employeeList = new ArrayList<TaskModel>();
							for (int i = 1; i <= jsonObject.length(); i++) {
								final TaskModel taskModel = (TaskModel) gson.fromJson(jsonObject.getJSONObject("" + i).toString(), TaskModel.class);
								employeeList.add(taskModel);
							}
							employeeList.trimToSize();
							if (employeeList != null && !employeeList.isEmpty()) {
								taskStatusAdapter = new TaskStatusAdapter(getActivity(), employeeList);
								listView.setAdapter(taskStatusAdapter);
								listView.setVisibility(View.VISIBLE);
								txtNodata.setVisibility(View.GONE);
								taskStatusAdapter.getFilter().filter(spinner.getSelectedItem().toString());
							}

						} else {

							final JSONArray jsonArray = new JSONArray(response);
							final Type listType = new TypeToken<List<TaskModel>>() {
							}.getType();
							employeeList = (ArrayList<TaskModel>) gson.fromJson(jsonArray.toString(), listType);
							if (employeeList != null && !employeeList.isEmpty()) {
								taskStatusAdapter = new TaskStatusAdapter(getActivity(), employeeList);
								listView.setAdapter(taskStatusAdapter);
								listView.setVisibility(View.VISIBLE);
								txtNodata.setVisibility(View.GONE);
								taskStatusAdapter.getFilter().filter(spinner.getSelectedItem().toString());
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
				Utils.displayDialog(getActivity(), error.getMessage());
			}
		});
		queue.add(postRequest);
	}

	public void taskDateList(final String date) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewTaskDate.php?taskDate=" + Utils.changeDateTimeFormat(date, Utils.DATE_DISPLAY_FORMAT, Utils.DATE_DATABASE_FORMAT);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				Utils.e(response);
				final Gson gson = new Gson();
				try {
					// {"error":"no task assign"}
					final Object obj = new JSONTokener(response).nextValue();

					if (obj instanceof JSONObject) {

						final JSONObject jsonObject = new JSONObject(response);
						final String jsonObjectString = jsonObject.getString("error");

						if (jsonObjectString != null && !jsonObjectString.isEmpty()) {
							Utils.displayDialog(getActivity(), jsonObjectString);
							listView.setVisibility(View.GONE);
							txtNodata.setVisibility(View.VISIBLE);
						}
					} else {
						final JSONArray jsonArray = new JSONArray(response);
						final Type listType = new TypeToken<List<TaskModel>>() {
						}.getType();
						employeeList = (ArrayList<TaskModel>) gson.fromJson(jsonArray.toString(), listType);
						if (employeeList != null && !employeeList.isEmpty()) {
							taskStatusAdapter = new TaskStatusAdapter(getActivity(), employeeList);
							listView.setAdapter(taskStatusAdapter);
							listView.setVisibility(View.VISIBLE);
							txtNodata.setVisibility(View.GONE);
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
				Utils.displayDialog(getActivity(), error.getMessage());
			}
		});
		queue.add(postRequest);
	}

}
