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
import com.employeesystem.model.QueryModel;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;

public class QueryAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<QueryModel> arrayList;
	private int pos;

	public QueryAdapter(Context context, final ArrayList<QueryModel> arrayList) {
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
			convertView = inflater.inflate(R.layout.row_query, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.txtEmployeeName = (TextView) convertView.findViewById(R.id.row_query_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtEmployeeName.setText("" + arrayList.get(position).getClient_Name());

		return convertView;
	}

	public class ViewHolder {
		private TextView txtEmployeeName;
	}

}
