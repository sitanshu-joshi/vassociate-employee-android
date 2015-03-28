package com.employeesystem.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employeesystem.R;
import com.employeesystem.adapter.EmployeeListAdapter;
import com.employeesystem.adapter.ImageAdapter;
import com.employeesystem.adapter.ImageAdapterView;
import com.employeesystem.adapter.SiteListAdapter;
import com.employeesystem.model.SiteModel;
import com.employeesystem.model.TaskModel;
import com.employeesystem.model.UserModel;
import com.employeesystem.util.Utils;
import com.employeesystem.volley.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint("ValidFragment")
public class AddTaskFragment extends Fragment implements OnClickListener {

	private Spinner spinnerEmployee;
	private Spinner spinnerSite;
	private ArrayList<UserModel> employeeList;
	private ArrayList<SiteModel> siteList;
	private TaskFragment taskFragment;
	private final int CAMERA_PHOTO_PICTURE = 1;
	private final int GALLERY_PHOTO_PICTURE = 2;
	private Uri fileUri;
	private File mediaFile;
	private String imagePath = "";
	private EditText editTextStartDate;
	private TaskModel taskModel;
	private EditText editTextStatus;
	private Button btnAdd;
	private ArrayList<String> b64 = new ArrayList<String>();
	private RelativeLayout relativeLayout;
	private RelativeLayout relativeLayoutView;
	private ArrayList<String> arrayListBitmaps;
	private ViewPager pager;
	private ImageAdapter adapter;
	private ImageButton previous;
	private ImageButton next;
	private ViewPager pagerView;
	private ImageAdapterView adapterView;
	private ImageButton previousView;
	private ImageButton nextView;
	private String isEdit;
	private String taskid;

	public AddTaskFragment(final TaskFragment taskFragment) {
		this.taskFragment = taskFragment;
	}

	public AddTaskFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_add_task, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		final TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_create_task_title);
		final ImageView btnImageView = (ImageView) view.findViewById(R.id.dialog_add_task_img_task);
		final EditText editTextName = (EditText) view.findViewById(R.id.dialog_add_task_edittext_name);
		final EditText editTextDescription = (EditText) view.findViewById(R.id.dialog_add_task_edittext_desc);
		final EditText editTextSiteName = (EditText) view.findViewById(R.id.dialog_add_task_edittext_site);
		final EditText editTextEmployee = (EditText) view.findViewById(R.id.dialog_add_task_edittext_employee);
		relativeLayout = (RelativeLayout) view.findViewById(R.id.dialog_add_task_viewpager_rl);
		pager = (ViewPager) view.findViewById(R.id.dialog_add_task_viewpager);
		previous = (ImageButton) view.findViewById(R.id.fragment_home_page_featured_items_previous);
		next = (ImageButton) view.findViewById(R.id.fragment_home_page_featured_items_next);
		relativeLayoutView = (RelativeLayout) view.findViewById(R.id.dialog_add_task_viewpager_rl_view);
		pagerView = (ViewPager) view.findViewById(R.id.dialog_add_task_viewpager_view);
		previousView = (ImageButton) view.findViewById(R.id.fragment_home_page_featured_items_previous_view);
		nextView = (ImageButton) view.findViewById(R.id.fragment_home_page_featured_items_next_view);
		previous.setOnClickListener(this);
		next.setOnClickListener(this);
		previousView.setOnClickListener(this);
		nextView.setOnClickListener(this);
		editTextStartDate = (EditText) view.findViewById(R.id.dialog_add_task_edittext_start_date);
		editTextStatus = (EditText) view.findViewById(R.id.dialog_add_task_edittext_status);
		spinnerEmployee = (Spinner) view.findViewById(R.id.dialog_add_task_spinner_employee_name);
		spinnerSite = (Spinner) view.findViewById(R.id.dialog_add_task_spinner_site_name);
		btnAdd = (Button) view.findViewById(R.id.dialog_add_task_btn_add);
		final TextView txtEmployee = (TextView) view.findViewById(R.id.dialog_add_task_spinner_employee_name_txt);
		txtEmployee.setTextColor(editTextStatus.getHintTextColors());
		final ScrollView scrollView = (ScrollView) view.findViewById(R.id.fragment_create_task_scrollView);
		scrollView.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.v("PARENT", "PARENT TOUCH");
				view.findViewById(R.id.dialog_add_task_edittext_desc).getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		editTextDescription.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.v("CHILD", "CHILD TOUCH");
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		if (getArguments() != null) {
			final LinearLayout llEmployee = (LinearLayout) view.findViewById(R.id.dialog_add_task_spinner_employee_ll);
			final LinearLayout llSite = (LinearLayout) view.findViewById(R.id.dialog_add_task_spinner_employee_ll1);
			final TextView textView = (TextView) view.findViewById(R.id.fragment_create_task_details);

			taskModel = getArguments().getParcelable(getString(R.string.key_task_model));
			isEdit = getArguments().getString(getString(R.string.key_is_edit));
			if (isEdit != null) {
				textViewTitle.setText("Edit Task");
				btnAdd.setText("Update");
				editTextName.setText("" + taskModel.getTask_name());
				editTextDescription.setText("" + taskModel.getTask_desc());
				editTextStartDate.setText("" + Utils.changeDateTimeFormat(taskModel.getStart_date(), Utils.DATE_DATABASE_FORMAT, Utils.DATE_DISPLAY_FORMAT));
				taskid = taskModel.getTask_id();
				if (taskModel.getImageUrl() != null && taskModel.getImageUrl().size() > 0) {
					adapterView = new ImageAdapterView(getActivity(), (ArrayList<String>) taskModel.getImageUrl(), false);
					pager.setAdapter(adapterView);
					pager.setVisibility(View.VISIBLE);
					relativeLayout.setVisibility(View.VISIBLE);

				} else {
					pagerView.setVisibility(View.GONE);
					relativeLayout.setVisibility(View.GONE);

				}
				if (Utils.isConnectedToInternet(getActivity())) {
					employeeList();
					siteList();
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
			} else {
				llEmployee.setVisibility(View.GONE);
				btnImageView.setVisibility(View.GONE);
				relativeLayoutView.setVisibility(View.VISIBLE);
				btnAdd.setVisibility(View.GONE);
				llSite.setVisibility(View.GONE);
				scrollView.setVisibility(View.GONE);
				editTextSiteName.setVisibility(View.VISIBLE);
				editTextEmployee.setVisibility(View.VISIBLE);
				textViewTitle.setText(getString(R.string.view_task));
				textView.setText("Name                    : " + Utils.getString(taskModel.getTask_name()) + "\nDesription            : "
						+ Utils.getString(taskModel.getTask_desc()) + "\nStatus                   : " + Utils.getString(taskModel.getTask_status()) + "\nEmployee name : "
						+ Utils.getString(taskModel.getName()) + "\nSite name            : " + Utils.getString(taskModel.getSite_name()) + "\nStarted date        : "
						+ Utils.getString(taskModel.getStart_date()));
				if (taskModel.getImageUrl() != null && taskModel.getImageUrl().size() > 0) {
					adapterView = new ImageAdapterView(getActivity(), (ArrayList<String>) taskModel.getImageUrl(), true);
					pagerView.setAdapter(adapterView);
					pagerView.setVisibility(View.VISIBLE);
				} else {
					pagerView.setVisibility(View.GONE);
				}

			}
			// textView.setText("Name: " + taskModel.getTask_name() + "\nDesription: " + taskModel.getTask_desc() + "\nStart date: "
			// + Utils.changeDateTimeFormat(taskModel.getStart_date(), Utils.DATE_DATABASE_FORMAT, Utils.DATE_DISPLAY_FORMAT) + "\nStatus: " + taskModel.getTask_status());
			// editTextName.setText("" + taskModel.getTask_name());
			// editTextDescription.setText("" + taskModel.getTask_desc());
			// editTextStartDate.setText("" + Utils.changeDateTimeFormat(taskModel.getStart_date(), Utils.DATE_DATABASE_FORMAT, Utils.DATE_DISPLAY_FORMAT));
			// editTextStatus.setText("" + taskModel.getTask_status());
			// editTextStatus.setVisibility(View.VISIBLE);
			// editTextName.setFocusable(false);
			// editTextDescription.setFocusable(false);
			// editTextStartDate.setFocusable(false);
			// editTextSiteName.setFocusable(false);
			// editTextStatus.setFocusable(false);
			// editTextEmployee.setFocusable(false);
			// editTextSiteName.setText(""+taskModel.get)
			// }
		} else {
			if (Utils.isConnectedToInternet(getActivity())) {
				employeeList();
				siteList();
			} else {
				Utils.showNoInternetConnectionDialog(getActivity());
			}
		}
		if (Utils.getString(getActivity(), getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
			btnAdd.setVisibility(View.VISIBLE);
			if (editTextStatus.getText().toString().trim().equalsIgnoreCase("Pending")) {
				btnAdd.setText("Start Task");
			} else if (editTextStatus.getText().toString().trim().equalsIgnoreCase("In Progress")) {
				btnAdd.setText("Complete Task");
			} else if (editTextStatus.getText().toString().trim().equalsIgnoreCase("Completed")) {
				btnAdd.setVisibility(View.GONE);
			}
		}
		final Calendar c = Calendar.getInstance();
		final int mYear = c.get(Calendar.YEAR);
		final int mMonth = c.get(Calendar.MONTH);
		final int mDay = c.get(Calendar.DAY_OF_MONTH);

		editTextStartDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
				dpd.show();
			}
		});
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isConnectedToInternet(getActivity())) {

					if (Utils.getString(getActivity(), getString(R.string.key_user_role)).equalsIgnoreCase("Employee") && taskModel != null) {
						if (editTextStatus.getText().toString().trim().equalsIgnoreCase("Pending")) {
							editTask(taskModel.getTask_id(), "In Progress");
						} else if (editTextStatus.getText().toString().trim().equalsIgnoreCase("In Progress")) {
							editTask(taskModel.getTask_id(), "Completed");
						}
					} else {
						if (employeeList != null && !employeeList.isEmpty()) {
							if (Utils.isConnectedToInternet(getActivity())) {
								if (isValid(editTextName, editTextDescription, editTextStartDate, editTextStatus)) {
									if (isEdit != null) {
										deleteEmployee(getActivity(), editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim(), editTextStartDate
												.getText().toString().trim(), employeeList.get(spinnerEmployee.getSelectedItemPosition()).getUserId(),
												"" + Utils.getString(getActivity(), getString(R.string.key_user_id)), editTextStatus.getText().toString().trim(),
												siteList.get(spinnerSite.getSelectedItemPosition()).getSiteId(), true);
									} else {
										deleteEmployee(getActivity(), editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim(), editTextStartDate
												.getText().toString().trim(), employeeList.get(spinnerEmployee.getSelectedItemPosition()).getUserId(),
												"" + Utils.getString(getActivity(), getString(R.string.key_user_id)), editTextStatus.getText().toString().trim(),
												siteList.get(spinnerSite.getSelectedItemPosition()).getSiteId(), false);
									}
								}
							} else {
								Utils.showNoInternetConnectionDialog(getActivity());
							}

						}
					}
				} else {
					Utils.showNoInternetConnectionDialog(getActivity());
				}
			}
		});

		btnImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEdit != null) {
					if (arrayListBitmaps == null) {
						if (pager.getAdapter().getCount() < 10) {
							displayPhotoDialog();
						} else {
							Utils.displayDialog(getActivity(), "Max. limit for photos reached.");
						}
					} else {
						if (pager.getAdapter().getCount() + arrayListBitmaps.size() < 10) {
							displayPhotoDialog();
						} else {
							Utils.displayDialog(getActivity(), "Max. limit for photos reached.");
						}
					}
				} else {
					if (arrayListBitmaps == null) {
						displayPhotoDialog();
					} else {
						if (arrayListBitmaps.size() < 10) {
							displayPhotoDialog();
						} else {
							Utils.displayDialog(getActivity(), "Max. limit for photos reached.");
						}
					}
				}
			}
		});
	}

	private boolean isValid(final EditText editTextName, final EditText editTextDescription, final EditText editTextStartDate, final EditText editTextStatus) {
		boolean isValid = false;

		if (editTextName.getText().toString().trim().length() <= 0 && editTextDescription.getText().toString().trim().length() <= 0
				&& editTextDescription.getText().toString().trim().length() <= 0 && editTextStatus.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the all the fields");
		} else if (editTextName.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the task name");
		} else if (editTextDescription.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the task description");
		} else if (editTextStartDate.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the task startdate");
		} else if (editTextStatus.getText().toString().trim().length() <= 0) {
			Utils.displayDialog(getActivity(), "Please enter the task status");
		}
		// else if (arrayListBitmaps == null) {
		// Utils.displayDialog(getActivity(), "Please add atleast one image");
		// }
		else {
			isValid = true;
		}
		return isValid;

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			if (view.isShown()) {
				editTextStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
			}
		}
	};

	private void employeeList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewEmployee.php";
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					final Gson gson = new Gson();
					try {
						final JSONArray jsonArray = new JSONArray(response);
						final Type listType = new TypeToken<List<UserModel>>() {
						}.getType();
						employeeList = (ArrayList<UserModel>) gson.fromJson(jsonArray.toString(), listType);

						// final UserModel userModel = gson.fromJson(
						// jsonObject.toString(), UserModel.class);

						if (employeeList != null && !employeeList.isEmpty()) {
							if (employeeList.get(0).getError() != null && !employeeList.get(0).getError().isEmpty()) {
								Utils.displayDialog(getActivity(), employeeList.get(0).getError());
							} else {
								final EmployeeListAdapter adapter = new EmployeeListAdapter(getActivity(), employeeList);
								spinnerEmployee.setAdapter(adapter);

								if (isEdit != null) {
									for (int i = 0; i < employeeList.size(); i++) {
										if (employeeList.get(i).getName() != null && taskModel.getName() != null) {
											if (taskModel.getName().equalsIgnoreCase("" + employeeList.get(i).getName())) {
												spinnerEmployee.setSelection(i);
											}
										}
									}
								}

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

	private void siteList() {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getString(R.string.url) + "viewSite.php";
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);
				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					final Gson gson = new Gson();
					try {
						final JSONArray jsonArray = new JSONArray(response);
						final Type listType = new TypeToken<List<SiteModel>>() {
						}.getType();
						siteList = (ArrayList<SiteModel>) gson.fromJson(jsonArray.toString(), listType);

						// final UserModel userModel = gson.fromJson(
						// jsonObject.toString(), UserModel.class);

						if (siteList != null && !siteList.isEmpty()) {
							final SiteListAdapter adapter = new SiteListAdapter(getActivity(), siteList);
							spinnerSite.setAdapter(adapter);
							if (isEdit != null) {
								for (int i = 0; i < siteList.size(); i++) {
									if (siteList.get(i).getSiteName() != null && taskModel.getSite_name() != null) {
										if (taskModel.getSite_name().equalsIgnoreCase("" + siteList.get(i).getSiteName())) {
											spinnerSite.setSelection(i);
										}
									}
								}
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

	// /** Create a File for saving an image or video */
	private File getOutputMediaFile() {
		// File mediaStorageDir = new
		// File(Environment.getExternalStorageDirectory(),
		// getString(R.string.app_name));
		final File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.employee));
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		final String timeStamp = "" + System.currentTimeMillis();
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMAGE_" + timeStamp + ".jpg");
		try {
			mediaFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mediaFile;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GALLERY_PHOTO_PICTURE) {
				try {
					imagePath = Utils.getPath(getActivity(), data.getData());
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
					// cameraUri = Uri.parse("file:///" +
					// tempImageUri);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
					// added lines
					// added lines
					byte[] b = baos.toByteArray();
					b64.add(Base64.encodeToString(b, Base64.DEFAULT));
					if (arrayListBitmaps == null)
						arrayListBitmaps = new ArrayList<String>();
					if (bitmap != null) {
						arrayListBitmaps.add(imagePath);
						// imageViewPic.setVisibility(View.VISIBLE);
						// imageViewPic.setImageBitmap(bitmap);
					}
					bitmap.recycle();
					bitmap = null;
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			} else if (requestCode == CAMERA_PHOTO_PICTURE) {
				try {
					if (fileUri != null) {
						imagePath = fileUri.getPath();
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
						// cameraUri = Uri.parse("file:///" +
						// tempImageUri);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
						// added lines
						// added lines
						byte[] b = baos.toByteArray();
						b64.add(Base64.encodeToString(b, Base64.DEFAULT));
						if (arrayListBitmaps == null)
							arrayListBitmaps = new ArrayList<String>();
						if (bitmap != null) {
							arrayListBitmaps.add(imagePath);
							// imageViewPic.setVisibility(View.VISIBLE);
							// imageViewPic.setImageBitmap(bitmap);

						}
						bitmap.recycle();
						bitmap = null;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			if (arrayListBitmaps != null && arrayListBitmaps.size() > 0) {
				if (arrayListBitmaps.size() == 1) {
					relativeLayout.setVisibility(View.VISIBLE);
					adapter = new ImageAdapter(getActivity(), arrayListBitmaps);
					pager.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				pager.setCurrentItem(adapter.getCount());
			}
		}
	}

	private void displayPhotoDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Photo");
		builder.setItems(R.array.array_photo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = Uri.fromFile(getOutputMediaFile());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, CAMERA_PHOTO_PICTURE);
					break;
				case 1:
					Intent pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
					pictureActionIntent.setType("image/*");
					pictureActionIntent.putExtra("return-data", true);
					startActivityForResult(pictureActionIntent, GALLERY_PHOTO_PICTURE);
					break;

				}
			}
		});
		builder.show();
	}

	private void editTask(final String editTaskid, final String taskStatus) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		final String url = getActivity().getString(R.string.url) + "editEmployeeTaskStatus.php?editTaskid=" + Utils.replaceSpace(editTaskid) + "&taskStatus="
				+ Utils.replaceSpace(taskStatus);
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
								if (editTextStatus.getText().toString().trim().equalsIgnoreCase("Pending")) {
									editTextStatus.setText("In Progress");
									btnAdd.setText("Complete Task");
								} else if (editTextStatus.getText().toString().trim().equalsIgnoreCase("In Progress")) {
									editTextStatus.setText("Completed");
									btnAdd.setVisibility(View.GONE);
								}
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

	private void deleteEmployee(final Context context, final String taskname, final String taskdesc, final String taskstartdate, final String addEmployeeId,
			final String addAdminId, final String taskstatus, final String addSiteId, final boolean isEdit) {
		final RequestQueue queue = Volley.newRequestQueue(getActivity());
		String url = "";
		if (isEdit) {
			url = getString(R.string.url) + "editTaskEncode64.php";
		} else {
			url = getString(R.string.url) + "addTaskEncode64.php";
		}
		final ProgressDialog progressDialog = Utils.showProgressDialog(getActivity(), "", "Loading...");
		final StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// response
				Utils.dismissProgressDialog(progressDialog);

				if (response != null && !response.isEmpty()) {
					Utils.e(response);
					try {
						final Object obj = new JSONTokener(response).nextValue();
						if (obj instanceof JSONObject) {
							final JSONObject jsonObject = (JSONObject) obj;
							final String error = jsonObject.optString("error");
							if (!TextUtils.isEmpty(error)) {
								Utils.displayDialog(getActivity(), error);
							}
							final String success = jsonObject.optString("success");
							if (!TextUtils.isEmpty(success)) {
								getFragmentManager().popBackStack();
								if (taskFragment != null) {
									taskFragment.taskDateList();
								}
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
				Log.e("error", error.toString());
				Utils.dismissProgressDialog(progressDialog);
				Utils.displayDialog(getActivity(), "Server error");
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				final Map<String, String> params = new HashMap<String, String>();
				params.put("taskname", taskname);
				params.put("taskdesc", taskdesc);
				params.put("taskstartdate", Utils.changeDateTimeFormat(taskstartdate, Utils.DATE_DISPLAY_FORMAT, Utils.DATE_DATABASE_FORMAT));
				params.put("addEmployeeId", addEmployeeId);
				params.put("addAdminId", addAdminId);
				params.put("taskstatus", taskstatus);
				params.put("addsiteId", addSiteId);
				int offset = 0;
				if (isEdit) {
					params.put("taskid", taskid);
					offset = pager.getAdapter().getCount();
				}
				for (int i = 0; i < b64.size(); i++) {
					params.put("taskimg" + (offset + i + 1), arrayListBitmaps.get(i).substring(arrayListBitmaps.get(i).length() - 3, arrayListBitmaps.get(i).length()) + b64.get(i));
				}
				return params;
			}
		};
		postRequest.setRetryPolicy(new DefaultRetryPolicy(60000000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 2f));

		queue.add(postRequest);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == previous) {
			if (adapter != null && pager.getCurrentItem() > 0) {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		} else if (v == next) {
			if (adapter != null && pager.getCurrentItem() < adapter.getCount()) {
				pager.setCurrentItem(pager.getCurrentItem() + 1);
			}
		} else if (v == previousView) {
			if (adapterView != null && pagerView.getCurrentItem() > 0) {
				pagerView.setCurrentItem(pagerView.getCurrentItem() - 1);
			}
		} else if (v == nextView) {
			if (adapterView != null && pagerView.getCurrentItem() < adapterView.getCount()) {
				pagerView.setCurrentItem(pagerView.getCurrentItem() + 1);
			}
		}
	}
}
