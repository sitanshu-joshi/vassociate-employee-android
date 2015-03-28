package com.employeesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserModel implements Parcelable {

	@SerializedName("user_Id")
	private String userId;
	@SerializedName("user_Name")
	private String userName;
	@SerializedName("user_Password")
	private String userPassword;
	private String name;
	private String email;
	@SerializedName("contact_number")
	private String contactNumber;
	private String designation;
	@SerializedName("user_role")
	private String userRole;
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * 
	 * @return The userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 *            The user_Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return The userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 *            The user_Name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return The userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * 
	 * @param userPassword
	 *            The user_Password
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 *            The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return The contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * 
	 * @param contactNumber
	 *            The contact_number
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * 
	 * @return The designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * 
	 * @param designation
	 *            The designation
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * 
	 * @return The userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * 
	 * @param userRole
	 *            The user_role
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public UserModel() {
	}

	protected UserModel(Parcel in) {
		userId = in.readString();
		userName = in.readString();
		userPassword = in.readString();
		name = in.readString();
		email = in.readString();
		contactNumber = in.readString();
		designation = in.readString();
		userRole = in.readString();
		error = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userId);
		dest.writeString(userName);
		dest.writeString(userPassword);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(contactNumber);
		dest.writeString(designation);
		dest.writeString(userRole);
		dest.writeString(error);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
		@Override
		public UserModel createFromParcel(Parcel in) {
			return new UserModel(in);
		}

		@Override
		public UserModel[] newArray(int size) {
			return new UserModel[size];
		}
	};
}
