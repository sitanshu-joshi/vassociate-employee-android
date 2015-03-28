package com.employeesystem.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.model.SiteModel;
import com.employeesystem.util.Utils;

@SuppressLint("ValidFragment")
public class AddSiteFragment extends Fragment {

	private SiteFragment siteFragment;
	private SiteModel siteModel;
	private boolean viewSite = false;

	public AddSiteFragment(final SiteFragment siteFragment) {
		this.siteFragment = siteFragment;
	}

	public AddSiteFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			siteModel = getArguments().getParcelable(getString(R.string.key_site_model));
			viewSite = getArguments().getBoolean("view");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_add_site, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {

		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_create_site_title);
		final EditText editTextName = (EditText) view.findViewById(R.id.dialog_add_site_edittext_name);
		final EditText editTextDescription = (EditText) view.findViewById(R.id.dialog_add_site_edittext_desc);
		final EditText editTextAddress = (EditText) view.findViewById(R.id.dialog_add_site_edittext_site_address);
		final EditText editTextAddress2 = (EditText) view.findViewById(R.id.dialog_add_site_edittext_site_address2);
		final EditText editTextClientName = (EditText) view.findViewById(R.id.dialog_add_site_edittext_client_name);
		final EditText editTextClientPhone = (EditText) view.findViewById(R.id.dialog_add_site_edittext_client_phone);
		final Button btnAdd = (Button) view.findViewById(R.id.dialog_add_site_btn_add);

		if (Utils.getString(getActivity(), getString(R.string.key_user_role)).equalsIgnoreCase("Employee") || viewSite) {
			editTextAddress.setFocusable(false);
			editTextDescription.setFocusable(false);
			editTextName.setFocusable(false);
			editTextAddress.setEnabled(false);
			editTextDescription.setEnabled(false);
			editTextAddress2.setEnabled(false);
			editTextClientName.setEnabled(false);
			editTextClientPhone.setEnabled(false);
			editTextName.setEnabled(false);
			btnAdd.setVisibility(View.GONE);
		}

		if (siteModel != null) {
			if (viewSite) {
				textViewTitle.setText("Site Details");
				btnAdd.setVisibility(View.VISIBLE);
				btnAdd.setText("Call Site");
				final ScrollView scrollView = (ScrollView) view.findViewById(R.id.fragment_add_site_scrollView);
				final TextView textView = (TextView) view.findViewById(R.id.fragment_add_site_details);
				final Button btnAdd2 = (Button) view.findViewById(R.id.dialog_add_site_btn_add2);
				btnAdd2.setText("Call Site");
				btnAdd2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							Intent intentToLoadDetails = new Intent(Intent.ACTION_DIAL);
							String telURI = "tel:" + siteModel.getClient_phone();
							intentToLoadDetails.setData(Uri.parse(telURI));
							startActivity(intentToLoadDetails);
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				});
				scrollView.setVisibility(View.GONE);
				textView.setText("Site Name            : " + siteModel.getSiteName() + "\nSite Description  : " + siteModel.getSiteDesc() + "\nSite Address       : " + siteModel.getSiteAddress()
						+ "\nSite Address 2    : " + siteModel.getSite_address_2() + "\nClient Name        : " + siteModel.getClient_name() + "\nClient Phone        : " + siteModel.getClient_phone());
			} else {
				textViewTitle.setText("Update Site");
				btnAdd.setText("Update");
			}
			editTextName.setText("" + siteModel.getSiteName());
			editTextDescription.setText("" + siteModel.getSiteDesc());
			editTextAddress.setText("" + siteModel.getSiteAddress());
			editTextAddress2.setText("" + siteModel.getSite_address_2());
			editTextClientName.setText("" + siteModel.getClient_name());
			editTextClientPhone.setText("" + siteModel.getClient_phone());
		}
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (viewSite) {
					try {
						Intent intentToLoadDetails = new Intent(Intent.ACTION_DIAL);
						String telURI = "tel:" + editTextClientPhone.getText().toString();
						intentToLoadDetails.setData(Uri.parse(telURI));
						startActivity(intentToLoadDetails);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else {

					if (Utils.isConnectedToInternet(getActivity())) {
						if (isValid(editTextName, editTextDescription, editTextAddress, editTextAddress2, editTextClientName, editTextClientPhone)) {
							if (siteModel != null) {
								editSite(siteModel.getSiteId(), editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim(), editTextAddress
										.getText().toString().trim(), editTextAddress2.getText().toString().trim(), editTextClientName.getText().toString(), editTextClientPhone
										.getText().toString());
							} else {
								addTasks(editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim(), editTextAddress.getText().toString().trim(),
										editTextAddress2.getText().toString().trim(), editTextClientName.getText().toString(), editTextClientPhone.getText().toString());
							}
						}
					} else {
						Utils.showNoInternetConnectionDialog(getActivity());
					}
				}
			}
		});
	}

	private boolean isValid(final EditText editTextName, final EditText editTextDescription, final EditText editTextAddress, EditText editTextAddress2,
			EditText editTextClientName, EditText editTextClientPhone) {
		boolean isValid = false;

		if (editTextName.getText().toString().trim().length() <= 0 && editTextDescription.getText().toString().trim().length() <= 0
				&& editTextAddress.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the all the fields.");
		} else if (editTextName.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the site name.");
		} else if (editTextDescription.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the site description.");
		} else if (editTextAddress.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the site address.");
		} else if (editTextClientName.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the client name.");
		} else if (editTextClientPhone.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the clinet phone.");
		} else {
			isValid = true;
		}
		return isValid;

	}

	private void addTasks(final String sitename, final String sitedesc, final String siteaddress, final String siteaddress2, final String clientName, final String clientNumber) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		// Sitename,sitedesc,siteaddress
		final String url = getString(R.string.url) + "addSite.php?sitename=" + Utils.replaceSpace(sitename) + "&sitedesc=" + Utils.replaceSpace(sitedesc) + "&siteaddress="
				+ Utils.replaceSpace(siteaddress) + "&siteaddress2=" + Utils.replaceSpace(siteaddress2) + "&clientname=" + Utils.replaceSpace(clientName) + "&clientphone="
				+ Utils.replaceSpace(clientNumber);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response {"success":"Add Employee Successfully"}

				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					try {
						final JSONObject jsonObject = new JSONObject(response);
						final String error = jsonObject.optString("error");
						final String success = jsonObject.optString("success");
						if (error != null && !error.isEmpty()) {
							Utils.displayDialog(getActivity(), error);
						} else {
							if (success != null) {
								siteFragment.siteList();
								getFragmentManager().popBackStack();
								Utils.displayDialog(getActivity(), success);
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Utils.showNoResponseDialog(getActivity());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Utils.dismissProgressDialog(progressDialog);
				Utils.displayDialog(getActivity(), "Server error");
			}
		});
		queue.add(postRequest);
	}

	private void editSite(final String id, final String sitename, final String sitedesc, final String siteaddress, final String siteaddress2, final String clientName,
			final String clientNumber) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		// EditSiteList=site id,sitename,sitedesc,siteaddress
		// http://employeemanager.co.nf/editSite.php?editSiteList=1&sitename=Google&sitedesc=Google&siteaddress=Google&editSite=Edit
		final String url = getString(R.string.url) + "editSite.php?editSiteList=" + Utils.replaceSpace(id) + "&sitename=" + Utils.replaceSpace(sitename) + "&sitedesc="
				+ Utils.replaceSpace(sitedesc) + "&siteaddress=" + Utils.replaceSpace(siteaddress) + "&siteaddress2=" + Utils.replaceSpace(siteaddress2) + "&clientname="
				+ Utils.replaceSpace(clientName) + "&clientphone=" + Utils.replaceSpace(clientNumber);
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response {"success":"Add Employee Successfully"}

				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					try {
						final JSONObject jsonObject = new JSONObject(response);
						final String error = jsonObject.optString("error");
						final String success = jsonObject.optString("success");
						if (error != null && !error.isEmpty()) {
							Utils.displayDialog(getActivity(), error);
						} else {
							if (success != null) {
								siteFragment.siteList();
								MainActivity.getInstance().onBackPressed();
								Utils.displayDialog(getActivity(), success);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Utils.showNoResponseDialog(getActivity());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Utils.dismissProgressDialog(progressDialog);
				Utils.displayDialog(getActivity(), "Server error");
			}
		});
		queue.add(postRequest);
	}
}
