package com.justtennis.plugin.fft;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.justtennis.plugin.fft.fragment.FindPlayerFragment;
import com.justtennis.plugin.fft.fragment.MillesimeMatchFragment;
import com.justtennis.plugin.fft.fragment.RankingMatchFragment;
import com.justtennis.plugin.fft.preference.FFTSharedPref;
import com.justtennis.plugin.fft.tool.FragmentTool;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = (savedInstanceState != null) ? savedInstanceState : getIntent().getExtras();
        if (bundle != null) {
            // Something to do
        }
        navigationView.getMenu().getItem(0).setChecked(true);
        FragmentTool.replaceFragment(this, MillesimeMatchFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Menu menu = navigationView.getMenu();

        if (id == R.id.nav_millesime_match && !menu.findItem(R.id.nav_millesime_match).isChecked()) {
            FragmentTool.replaceFragment(this, MillesimeMatchFragment.newInstance());
        } else if (id == R.id.nav_ranking_match && !menu.findItem(R.id.nav_ranking_match).isChecked()) {
            FragmentTool.replaceFragment(this, RankingMatchFragment.newInstance());
        } else if (id == R.id.nav_slideshow) {
            FragmentTool.replaceFragment(this, FindPlayerFragment.newInstance());
//        } else if (id == R.id.nav_manage) {
//
        } else if (id == R.id.nav_fft) {
            String url = "https://mon-espace-tennis.fft.fr";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_disconnect) {
            Context context = getApplicationContext();
            FFTSharedPref.cleanSecurity(context);
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
