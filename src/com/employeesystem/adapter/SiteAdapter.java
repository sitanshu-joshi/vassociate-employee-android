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
import com.employeesystem.fragment.AddSiteFragment;
import com.employeesystem.fragment.SiteFragment;
import com.employeesystem.model.SiteModel;
import com.employeesystem.util.Utils;

public class SiteAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<SiteModel> arrayList;
	private int pos;
	private SiteFragment fragment;

	public SiteAdapter(SiteFragment fragment, final ArrayList<SiteModel> arrayList) {
		this.context = fragment.getActivity();
		inflater = ((Activity) context).getLayoutInflater();
		this.arrayList = arrayList;
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

		if (Utils.getString(context, context.getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
			viewHolder.imageViewDelete.setVisibility(View.INVISIBLE);
			viewHolder.imageViewEdit.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.imageViewDelete.setVisibility(View.VISIBLE);
			viewHolder.imageViewEdit.setVisibility(View.VISIBLE);
		}
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).getSiteName());
		viewHolder.imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Bundle bundle = new Bundle();
				bundle.putParcelable(context.getString(R.string.key_site_model), arrayList.get(position));
				final AddSiteFragment addSiteFragment = new AddSiteFragment(fragment);
				addSiteFragment.setArguments(bundle);
				MainActivity.getInstance().addFragment(addSiteFragment, fragment);

			}
		});

		viewHolder.imageViewDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isConnectedToInternet(context)) {
					deleteEmployee("" + arrayList.get(position).getSiteId(), position);
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
		final String url = context.getString(R.string.url) + "deleteSite.php?deleteSiteId=" + Utils.replaceSpace(deleteEmployeeId);
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
