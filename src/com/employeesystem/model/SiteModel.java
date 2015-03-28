package com.employeesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SiteModel implements Parcelable {

	@SerializedName("site_id")
	private String siteId;
	@SerializedName("site_name")
	private String siteName;
	@SerializedName("site_desc")
	private String siteDesc;
	@SerializedName("site_address")
	private String siteAddress;

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getClient_phone() {
		return client_phone;
	}

	public void setClient_phone(String client_phone) {
		this.client_phone = client_phone;
	}

	public String getSite_address_2() {
		return site_address_2;
	}

	public void setSite_address_2(String site_address_2) {
		this.site_address_2 = site_address_2;
	}

	@SerializedName("client_name")
	private String client_name;

	@SerializedName("client_phone")
	private String client_phone;

	@SerializedName("site_address_2")
	private String site_address_2;
	@SerializedName("contact_number")
	private String contactNumber;

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The siteId
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * 
	 * @param siteId
	 *            The site_id
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * 
	 * @return The siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * 
	 * @param siteName
	 *            The site_name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * 
	 * @return The siteDesc
	 */
	public String getSiteDesc() {
		return siteDesc;
	}

	/**
	 * 
	 * @param siteDesc
	 *            The site_desc
	 */
	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}

	/**
	 * 
	 * @return The siteAddress
	 */
	public String getSiteAddress() {
		return siteAddress;
	}

	/**
	 * 
	 * @param siteAddress
	 *            The site_address
	 */
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public SiteModel() {
	}

	protected SiteModel(Parcel in) {
		siteId = in.readString();
		siteName = in.readString();
		siteDesc = in.readString();
		siteAddress = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(siteId);
		dest.writeString(siteName);
		dest.writeString(siteDesc);
		dest.writeString(siteAddress);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<SiteModel> CREATOR = new Parcelable.Creator<SiteModel>() {
		@Override
		public SiteModel createFromParcel(Parcel in) {
			return new SiteModel(in);
		}

		@Override
		public SiteModel[] newArray(int size) {
			return new SiteModel[size];
		}
	};
}
