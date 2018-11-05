package com.justtennis.plugin.fft.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.justtennis.plugin.fb.fragment.FBPublishFragment;
import com.justtennis.plugin.fb.service.FBServiceLogin;
import com.justtennis.plugin.fft.LoginActivity;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.fragment.FindPlayerFragment;
import com.justtennis.plugin.fft.fragment.MillesimeMatchFragment;
import com.justtennis.plugin.fft.fragment.RankingMatchFragment;
import com.justtennis.plugin.fft.service.FFTServiceLogin;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.shared.preference.LoginSharedPref;
import com.justtennis.plugin.shared.service.IServiceLogin;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

	public enum SERVICE {
		FFT("FFT"), FB("Other");

		final String label;

		SERVICE(String label) {
			this.label = label;
		}

		public static SERVICE findByLabel(String label) {
			for(SERVICE service : SERVICE.values()) {
				if (label.equalsIgnoreCase(service.label)) {
					return service;
				}
			}
			return SERVICE.FFT;
		}
	}

	private static ServiceManager instance;
	private SERVICE service;

	private ServiceManager() {}

	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}

	public static List<String> getServiceLabel() {
		List<String> ret = new ArrayList<>();
		for (ServiceManager.SERVICE service : ServiceManager.SERVICE.values()) {
			ret.add(service.label);
		}
		return ret;
	}


	public IServiceLogin getServiceLogin(Context context) {
		switch (service) {
			case FB:
				return FBServiceLogin.newInstance(context);
			case FFT:
			default:
				return FFTServiceLogin.newInstance(context);
		}
	}

	public boolean doSaveLogin() {
		return service != SERVICE.FB;
	}

	public void setService(String label) {
		this.service = SERVICE.findByLabel(label);
	}

	public void setService(int position) {
		this.service = SERVICE.values()[position];
	}

	public SERVICE getService() {
		return service;
	}

	public void initializeFragment(FragmentActivity activity) {
		switch (service) {
			case FB:
				FragmentTool.replaceFragment(activity, FBPublishFragment.newInstance());
				break;
			case FFT:
			default:
				FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance());
		}
	}

	public void initializeNavigation(NavigationView navigationView) {
		switch (service) {
			case FB:
				break;
			case FFT:
			default:
				navigationView.inflateMenu(R.menu.activity_main_drawer);
				navigationView.getMenu().getItem(0).setChecked(true);
		}
	}

	public boolean onNavigationItemSelected(@NonNull FragmentActivity activity, @NonNull NavigationView navigationView, @NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Menu menu = navigationView.getMenu();

		if (isMenuItem(id, menu, R.id.nav_millesime_match)) {
			FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance());
		} else if (isMenuItem(id, menu, R.id.nav_ranking_match)) {
			FragmentTool.replaceFragment(activity, RankingMatchFragment.newInstance());
		} else if (isMenuItem(id, menu, R.id.nav_slideshow)) {
			FragmentTool.replaceFragment(activity, FindPlayerFragment.newInstance());
//        } else if (id == R.id.nav_manage) {
//
		} else if (id == R.id.nav_fft) {
			String url = "https://mon-espace-tennis.fft.fr";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			activity.startActivity(i);
		} else if (id == R.id.nav_disconnect) {
			Context context = activity.getApplicationContext();
			LoginSharedPref.cleanSecurity(context);
			activity.startActivity(new Intent(context, LoginActivity.class));
			activity.finish();
		}

		DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private boolean isMenuItem(int id, Menu menu, int p) {
		return id == p && !menu.findItem(p).isChecked();
	}
}