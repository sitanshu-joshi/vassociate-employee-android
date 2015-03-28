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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.adapter.QueryAdapter;
import com.employeesystem.model.QueryModel;
import com.employeesystem.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryFragment extends Fragment implements OnItemClickListener {
	private ListView listView;

	public QueryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_create_task, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_create_task_title);
		final ImageView imageViewAddTask = (ImageView) view.findViewById(R.id.fragment_create_task_imageview_add);
		listView = (ListView) view.findViewById(R.id.fragment_create_task_listview);
		textViewTitle.setText("View Query");
		if (Utils.isConnectedToInternet(getActivity())) {
			taskList();
		} else {
			Utils.showNoInternetConnectionDialog(getActivity());
		}

		listView.setOnItemClickListener(this);
		imageViewAddTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// dialogAddEmployee();
				// getFragmentManager().beginTransaction().add(R.id.container, new AddQueryFragment(QueryFragment.this),
				// QueryFragment.class.getSimpleName()).hide(QueryFragment.this)
				// .addToBackStack(QueryFragment.class.getSimpleName()).commit();

				MainActivity.getInstance().addFragment(new AddQueryFragment(QueryFragment.this), QueryFragment.this);

			}
		});
	}

	public void taskList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewClientQuery.php";
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
							final String jsonObjectString = jsonObject.getString("error");

							if (jsonObjectString != null && !jsonObjectString.isEmpty()) {
								Utils.displayDialog(getActivity(), jsonObjectString);
							}
						} else {
							final JSONArray jsonArray = new JSONArray(response);
							final Type listType = new TypeToken<List<QueryModel>>() {
							}.getType();
							final ArrayList<QueryModel> employeeList = (ArrayList<QueryModel>) gson.fromJson(jsonArray.toString(), listType);
							if (employeeList != null && !employeeList.isEmpty()) {
								final QueryAdapter adapter = new QueryAdapter(getActivity(), employeeList);
								listView.setAdapter(adapter);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final QueryModel queryModel = (QueryModel) listView.getItemAtPosition(position);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(getActivity().getString(R.string.key_query_model), queryModel);
		final AddQueryFragment addQueryFragment = new AddQueryFragment(QueryFragment.this);
		addQueryFragment.setArguments(bundle);
		MainActivity.getInstance().addFragment(addQueryFragment, QueryFragment.this);
	}

}
