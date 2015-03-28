package com.employeesystem.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.employeesystem.R;
import com.employeesystem.fragment.AssociateFragment;
import com.employeesystem.fragment.LoginFragment;
import com.employeesystem.util.Utils;

public class MainActivity extends Activity {
	private static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_main);
		if (Utils.getString(this, getString(R.string.key_user_id)) != null && !Utils.getString(this, getString(R.string.key_user_id)).equalsIgnoreCase("")) {
			getFragmentManager().beginTransaction().replace(R.id.container, new AssociateFragment(), AssociateFragment.class.getSimpleName()).commit();
		} else {
			getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment(), LoginFragment.class.getSimpleName()).commit();
		}
	}

	public void closeFragment(final Fragment fragment) {
		getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).replace(R.id.container, fragment, fragment.getClass().getSimpleName())
				.commit();
	}

	public void openFragment(final Fragment fragment) {
		getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.container, fragment, fragment.getClass().getSimpleName())
				.commit();
	}

	public void addFragment(final Fragment fragment, final Fragment fragment2) {
		getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.container, fragment, fragment.getClass().getSimpleName())
				.addToBackStack(fragment.getClass().getSimpleName()).hide(fragment2).commit();
	}

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}

	}
}
