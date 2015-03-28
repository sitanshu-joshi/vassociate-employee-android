package com.employeesystem.adapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.employeesystem.EmployeeApp;
import com.employeesystem.R;
import com.employeesystem.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageAdapterView extends PagerAdapter {
	private ArrayList<String> arrayList;
	private LayoutInflater inflater;
	private Context context;
	private boolean isVisible = true;

	public ImageAdapterView(final Context context, final ArrayList<String> arrayList, boolean b) {
		this.arrayList = arrayList;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isVisible = b;
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
		final ProgressBar progress = (ProgressBar) view.findViewById(R.id.row_view_pager_progress);
		final Button btnSave = (Button) view.findViewById(R.id.row_view_pager_btn_save);
		if (isVisible) {
			btnSave.setVisibility(View.VISIBLE);
		} else {
			btnSave.setVisibility(View.GONE);
		}
		EmployeeApp application = (EmployeeApp) context.getApplicationContext();
		application.getImageLoader().loadImage(arrayList.get(position), new ImageLoadingListener() {

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				imgProduct.setImageBitmap(arg2);
				progress.setVisibility(View.GONE);

			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub

				progress.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.VISIBLE);
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Bitmap bitmap = ((BitmapDrawable) imgProduct.getDrawable()).getBitmap();
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					// final File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "EmployeeMangement");
					// dir.mkdirs();
					// File f = new File(dir, arrayList.get(position).substring(arrayList.get(position).lastIndexOf("/"), arrayList.get(position).length()));
					// f.createNewFile();
					// FileOutputStream fo = new FileOutputStream(f);
					// fo.write(bytes.toByteArray());
					// fo.close();
					MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
							arrayList.get(position).substring(arrayList.get(position).lastIndexOf("/"), arrayList.get(position).length()), "Employee Management");

					Utils.displayDialog(context, "Image saved to gallery.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		((ViewPager) container).addView(view);
		return view;
	}
}
