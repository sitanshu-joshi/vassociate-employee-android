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
import com.employeesystem.adapter.SiteAdapter;
import com.employeesystem.model.SiteModel;
import com.employeesystem.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SiteFragment extends Fragment implements OnItemClickListener {
	private ListView listView;

	public SiteFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_employ_details, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final ImageView imageViewAddEmployee = (ImageView) view.findViewById(R.id.fragment_employee_details_imageview_add);
		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_employee_details_title);
		listView = (ListView) view.findViewById(R.id.fragment_employee_details_listview);
		textViewTitle.setText(getString(R.string.site_details));

		if (Utils.getString(getActivity(), getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
			imageViewAddEmployee.setVisibility(View.GONE);
		} else {
			imageViewAddEmployee.setVisibility(View.VISIBLE);
			// listView.setOnItemClickListener(null);
		}

		listView.setOnItemClickListener(this);
		if (Utils.isConnectedToInternet(getActivity())) {
			siteList();
		} else {
			Utils.showNoInternetConnectionDialog(getActivity());
		}

		imageViewAddEmployee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.getInstance().addFragment(new AddSiteFragment(SiteFragment.this), SiteFragment.this);
			}
		});
	}

	public void siteList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewSite.php";
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
							final Type listType = new TypeToken<List<SiteModel>>() {
							}.getType();
							final Gson gson = new Gson();
							final ArrayList<SiteModel> employeeList = (ArrayList<SiteModel>) gson.fromJson(jsonArray.toString(), listType);

							// final UserModel userModel = gson.fromJson(
							// jsonObject.toString(), UserModel.class);

							if (employeeList != null && !employeeList.isEmpty()) {
								final SiteAdapter adapter = new SiteAdapter(SiteFragment.this, employeeList);
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
				Utils.displayDialog(getActivity(), "Server error");
			}
		});
		queue.add(postRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final SiteModel siteModel = (SiteModel) parent.getItemAtPosition(position);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(getString(R.string.key_site_model), siteModel);
		bundle.putBoolean("view", true);
		final AddSiteFragment addSiteFragment = new AddSiteFragment(this);
		addSiteFragment.setArguments(bundle);
		MainActivity.getInstance().addFragment(addSiteFragment, this);
	}

}
