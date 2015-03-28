package com.employeesystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.employeesystem.R;

public class AssociateAdapter extends BaseAdapter {

	private Context context;
	private int[] ids;
	private String[] names;

	public AssociateAdapter(Context c, int[] icons, String[] names) {
		context = c;
		ids = icons;
		this.names = names;
	}

	@Override
	public int getCount() {
		return ids.length;
	}

	@Override
	public Object getItem(int position) {
		return ids[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_associate, null);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.row_pick_photo_imgView);
			holder.txtView = (TextView) convertView.findViewById(R.id.row_associate_tv);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.imageView.setImageResource(ids[position]);
		holder.txtView.setText(names[position]);
		return convertView;
	}

	private class Holder {
		ImageView imageView;
		TextView txtView;
	}

}
