package com.employeesystem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.employeesystem.EmployeeApp;
import com.employeesystem.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageAdapter extends PagerAdapter {
	private ArrayList<String> arrayList;
	private LayoutInflater inflater;
	private Context context;

	public ImageAdapter(final Context context, final ArrayList<String> arrayList) {
		this.arrayList = arrayList;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(final View container, final int position) {
		final View view = inflater.inflate(R.layout.row_viewpager, null);

		final ImageView imgProduct = (ImageView) view.findViewById(R.id.row_view_pager_iv);

		EmployeeApp application = (EmployeeApp) context.getApplicationContext();
		application.getImageLoader().loadImage("file://" + arrayList.get(position), new ImageLoadingListener() {

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				imgProduct.setImageBitmap(arg2);

			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub

			}
		});
		((ViewPager) container).addView(view);
		return view;
	}

}
