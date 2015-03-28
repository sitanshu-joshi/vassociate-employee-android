package com.employeesystem.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.employeesystem.R;
import com.employeesystem.model.SiteModel;

public class SiteListAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private ArrayList<SiteModel> arrayList;

	public SiteListAdapter(Context context, final ArrayList<SiteModel> arrayList) {
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
			convertView = inflater.inflate(R.layout.row_employee_list, null);
			// configure view holder
			viewHolder = new ViewHolder();
			viewHolder.txtSiteName = (TextView) convertView.findViewById(R.id.row_employee_details_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtSiteName.setText("" + arrayList.get(position).getSiteName());

		return convertView;
	}

	public class ViewHolder {
		private TextView txtSiteName;
	}

}
