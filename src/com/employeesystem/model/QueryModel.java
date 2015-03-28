package com.employeesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QueryModel implements Parcelable {

	private String Client_ID;
	private String Client_Name;
	private String Client_Address;
	private String Client_Phone;

	public String getClient_ID() {
		return Client_ID;
	}

	public void setClient_ID(String client_ID) {
		Client_ID = client_ID;
	}

	public String getClient_Name() {
		return Client_Name;
	}

	public void setClient_Name(String client_Name) {
		Client_Name = client_Name;
	}

	public String getClient_Address() {
		return Client_Address;
	}

	public void setClient_Address(String client_Address) {
		Client_Address = client_Address;
	}

	public String getClient_Phone() {
		return Client_Phone;
	}

	public void setClient_Phone(String client_Phone) {
		Client_Phone = client_Phone;
	}

	public QueryModel() {
	}

	protected QueryModel(Parcel in) {
		Client_ID = in.readString();
		Client_Name = in.readString();
		Client_Address = in.readString();
		Client_Phone = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Client_ID);
		dest.writeString(Client_Name);
		dest.writeString(Client_Address);
		dest.writeString(Client_Phone);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<QueryModel> CREATOR = new Parcelable.Creator<QueryModel>() {
		@Override
		public QueryModel createFromParcel(Parcel in) {
			return new QueryModel(in);
		}

		@Override
		public QueryModel[] newArray(int size) {
			return new QueryModel[size];
		}
	};
}