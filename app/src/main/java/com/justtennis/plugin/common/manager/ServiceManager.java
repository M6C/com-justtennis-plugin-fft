package com.justtennis.plugin.common.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.justtennis.plugin.common.fragment.LoginFragment;
import com.justtennis.plugin.common.tool.FragmentTool;
import com.justtennis.plugin.fb.fragment.FBPublishFragment;
import com.justtennis.plugin.fb.service.FBServiceLogin;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.fragment.FindCompetitionFragment;
import com.justtennis.plugin.fft.fragment.FindPlayerFragment;
import com.justtennis.plugin.fft.fragment.MillesimeMatchFragment;
import com.justtennis.plugin.fft.fragment.RankingMatchFragment;
import com.justtennis.plugin.fft.service.FFTServiceLogin;
import com.justtennis.plugin.shared.service.IServiceLogin;
import com.justtennis.plugin.yout.fragment.YoutFindVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

	public enum SERVICE {
		LOGIN("Login", false, false), FFT("FFT", true, true), FB("Other", true, false);

		final String label;
		private boolean visible;
		private boolean saveLogin;

		SERVICE(String label, boolean visible, boolean saveLogin) {
			this.label = label;
			this.visible = visible;
			this.saveLogin = saveLogin;
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
	private static final List<SERVICE> serviceVisible = getServiceVisible();
	private SERVICE service = SERVICE.LOGIN;

	private ServiceManager() {
	}

	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}

	public static List<String> getServiceLabel() {
		List<String> ret = new ArrayList<>();
		for (ServiceManager.SERVICE service : serviceVisible) {
			ret.add(service.label);
		}
		return ret;
	}

	private static List<ServiceManager.SERVICE> getServiceVisible() {
		List<ServiceManager.SERVICE> ret = new ArrayList<>();
		for (ServiceManager.SERVICE service : ServiceManager.SERVICE.values()) {
			if (service.visible) {
				ret.add(service);
			}
		}
		return ret;
	}

	public IServiceLogin getServiceLogin(Context context) {
		switch (service) {
			case FB:
				return FBServiceLogin.newInstance(context);
			case FFT:
				return FFTServiceLogin.newInstance(context);
			case LOGIN:
			default:
				return null;
		}
	}

	public boolean doSaveLogin() {
		return service.saveLogin;
	}

	public void setService(SERVICE service) {
		this.service = service;
	}

	public void setService(String label) {
		setService(SERVICE.findByLabel(label));
	}

	public void setService(int position) {
		setService(serviceVisible.get(position));
	}

	public SERVICE getService() {
		return service;
	}

	public void initializeFragment(FragmentActivity activity, Bundle extra) {
		Fragment fragment = null;
		switch (service) {
			case FB:
				fragment = FBPublishFragment.newInstance();
				break;
			case FFT:
				fragment = MillesimeMatchFragment.newInstance();
				break;
			case LOGIN:
				fragment = LoginFragment.newInstance();
			default:
		}
		if (fragment != null) {
			if (extra != null) {
				fragment.setArguments(extra);
			}
			FragmentTool.replaceFragment(activity, fragment);

			NavigationView navigationView = activity.findViewById(R.id.nav_view);
			if (navigationView != null) {
				initializeNavigation(navigationView);
			}
		}
	}

	public void initializeNavigation(NavigationView navigationView) {
		clearNavigationMenu(navigationView);
		switch (service) {
			case FB:
				navigationView.inflateMenu(R.menu.activity_fb_drawer);
				navigationView.getMenu().getItem(0).setChecked(true);
				break;
			case FFT:
				navigationView.inflateMenu(R.menu.activity_main_drawer);
				navigationView.getMenu().getItem(0).setChecked(true);
				break;
			case LOGIN:
				navigationView.inflateMenu(R.menu.activity_login_drawer);
				navigationView.getMenu().getItem(0).setChecked(true);
				break;
			default:
		}
	}

	public boolean onNavigationItemSelected(@NonNull FragmentActivity activity, @NonNull NavigationView navigationView, @NonNull MenuItem item) {
		switch (service) {
			case FB:
				return onFBNavigationItemSelected(activity, navigationView, item);
			case FFT:
				return onFFTNavigationItemSelected(activity, navigationView, item);
			case LOGIN:
				return onLoginNavigationItemSelected(activity, navigationView, item);
			default:
				return false;
		}
	}

	private boolean onFBNavigationItemSelected(@NonNull FragmentActivity activity, @NonNull NavigationView navigationView, @NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Menu menu = navigationView.getMenu();

		if (isMenuItem(id, menu, R.id.nav_fb_publish)) {
			FragmentTool.replaceFragment(activity, FBPublishFragment.newInstance());
		} else if (id == R.id.nav_fb_redirect) {
			String url = "https://www.facebook.com";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			activity.startActivity(i);
		} else if (id == R.id.nav_fb_disconnect) {
			ServiceManager.getInstance().setService(SERVICE.LOGIN);
			ServiceManager.getInstance().initializeFragment(activity, new Bundle());
		}

		DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private boolean onFFTNavigationItemSelected(@NonNull FragmentActivity activity, @NonNull NavigationView navigationView, @NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Menu menu = navigationView.getMenu();

		if (isMenuItem(id, menu, R.id.nav_millesime_match)) {
			FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance());
		} else if (isMenuItem(id, menu, R.id.nav_ranking_match)) {
			FragmentTool.replaceFragment(activity, RankingMatchFragment.newInstance());
		} else if (isMenuItem(id, menu, R.id.nav_slideshow)) {
			FragmentTool.replaceFragment(activity, FindPlayerFragment.newInstance());
		} else if (id == R.id.nav_manage) {
			FragmentTool.replaceFragment(activity, FindCompetitionFragment.newInstance());
		} else if (id == R.id.nav_fft) {
			String url = "https://mon-espace-tennis.fft.fr";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			activity.startActivity(i);
		} else if (id == R.id.nav_disconnect) {
			ServiceManager.getInstance().setService(SERVICE.LOGIN);
			ServiceManager.getInstance().initializeFragment(activity, new Bundle());
		}

		DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private boolean onLoginNavigationItemSelected(@NonNull FragmentActivity activity, @NonNull NavigationView navigationView, @NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Menu menu = navigationView.getMenu();

		if (isMenuItem(id, menu, R.id.nav_login)) {
			FragmentTool.replaceFragment(activity, LoginFragment.newInstance());
		} else if (isMenuItem(id, menu, R.id.nav_youtube)) {
			FragmentTool.replaceFragment(activity, YoutFindVideoFragment.newInstance());
		}

		DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void clearNavigationMenu(NavigationView navigationView) {
		Menu menu = navigationView.getMenu();
		for(int i = menu.size(); i>0 ; i--) {
			menu.removeItem(menu.getItem(i-1).getItemId());
		}
	}

	private boolean isMenuItem(int id, Menu menu, int p) {
		return id == p && !menu.findItem(p).isChecked();
	}
}