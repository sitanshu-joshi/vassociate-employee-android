package com.employeesystem.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.employeesystem.R;
import com.employeesystem.activity.MainActivity;
import com.employeesystem.adapter.AssociateAdapter;
import com.employeesystem.util.Utils;

public class AssociateFragment extends Fragment {
	private int[] ids;
	private String[] names;

	public AssociateFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_associate, null);
		initComp(view);
		return view;
	}

	private void initComp(final View view) {
		if (Utils.getString(getActivity(), getActivity().getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
			ids = new int[] { R.drawable.task, R.drawable.site };
			names = new String[] { "Task", "Site" };

		} else {
			ids = new int[] { R.drawable.task, R.drawable.employee, R.drawable.site, R.drawable.tasklist };
			names = new String[] { "Task", "Employee", "Site", "Task Status" };
		}
		final GridView gridView = (GridView) view.findViewById(R.id.fragment_associate_gridview);
		final Button btnLogout = (Button) view.findViewById(R.id.fragment_associate_btn_logout);

		final AssociateAdapter associateAdapter = new AssociateAdapter(getActivity(), ids, names);
		gridView.setAdapter(associateAdapter);

		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Utils.storeString(getActivity(), getString(R.string.key_user_id), "");
				MainActivity.getInstance().openFragment(new LoginFragment());
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (Utils.getString(getActivity(), getActivity().getString(R.string.key_user_role)).equalsIgnoreCase("Employee")) {
					switch (position) {
					case 1:
						MainActivity.getInstance().addFragment(new SiteFragment(), AssociateFragment.this);
						break;
					case 0:
						MainActivity.getInstance().addFragment(new TaskFragment(), AssociateFragment.this);
						break;
					}

				} else {
					switch (position) {
					case 1:
						MainActivity.getInstance().addFragment(new EmployeeFragment(), AssociateFragment.this);
						break;
					case 0:
						MainActivity.getInstance().addFragment(new TaskFragment(), AssociateFragment.this);
						break;
					case 2:
						MainActivity.getInstance().addFragment(new SiteFragment(), AssociateFragment.this);
						break;
					case 3:
						MainActivity.getInstance().addFragment(new TaskStatusFragment(), AssociateFragment.this);
						break;

					}

				}

			}
		});
	}
}
