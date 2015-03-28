package com.employeesystem.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TaskModel implements Parcelable {
	@SerializedName("task_id")
	public String task_id;
	@SerializedName("task_admin_id")
	public String task_admin_id;
	@SerializedName("task_name")
	public String task_name;
	@SerializedName("task_desc")
	public String task_desc;

	private String name;
	private String site_name;

	// @SerializedName("task_image")
	// public String task_image;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	@SerializedName("image_url")
	private List<String> imageUrl = new ArrayList<String>();

	public List<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	@SerializedName("start_date")
	public String start_date;
	@SerializedName("task_status")
	public String task_status;

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_admin_id() {
		return task_admin_id;
	}

	public void setTask_admin_id(String task_admin_id) {
		this.task_admin_id = task_admin_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTask_desc() {
		return task_desc;
	}

	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}

	protected TaskModel(Parcel in) {
		task_id = in.readString();
		task_admin_id = in.readString();
		task_name = in.readString();
		task_desc = in.readString();
		name = in.readString();
		site_name = in.readString();
		if (in.readByte() == 0x01) {
			imageUrl = new ArrayList<String>();
			in.readList(imageUrl, String.class.getClassLoader());
		} else {
			imageUrl = null;
		}
		start_date = in.readString();
		task_status = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(task_id);
		dest.writeString(task_admin_id);
		dest.writeString(task_name);
		dest.writeString(task_desc);
		dest.writeString(name);
		dest.writeString(site_name);
		if (imageUrl == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(imageUrl);
		}
		dest.writeString(start_date);
		dest.writeString(task_status);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<TaskModel> CREATOR = new Parcelable.Creator<TaskModel>() {
		@Override
		public TaskModel createFromParcel(Parcel in) {
			return new TaskModel(in);
		}

		@Override
		public TaskModel[] newArray(int size) {
			return new TaskModel[size];
		}
	};
}