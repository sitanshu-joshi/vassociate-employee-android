package com.employeesystem.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.employeesystem.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class GalleryAdapter extends BaseAdapter {

	private ArrayList<String> arrayList;
	private LayoutInflater inflate;

	public GalleryAdapter(final Context context, final ArrayList<String> arrayList) {
		this.arrayList = arrayList;
		inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arrayList.get(arg0).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final Holder holder;
		if (convertView == null) {
			convertView = inflate.inflate(R.layout.row_gallery, null);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.row_gallery_imageview);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(arrayList.get(position), holder.imageView, getImageOptions());
		return convertView;
	}

	private class Holder {
		private ImageView imageView;
	}

	private void initImageLoader(Context context) {
		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50
																										// Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
																					// for
																					// release
																					// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	private DisplayImageOptions getImageOptions() {
		return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

}
