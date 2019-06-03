package com.example.nelli.posters2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.nelli.posters2.events.EditableEventsAdapter;
import com.example.nelli.posters2.events.EditableEventsFragment;
import com.example.nelli.posters2.infrastructures.EditableInfrastructuresAdapter;
import com.example.nelli.posters2.infrastructures.EditableInfrastrcuturesFragment;
import com.example.nelli.posters2.managers.ManagersFragment;
import com.example.nelli.posters2.requests.EventsResponse;
import com.example.nelli.posters2.requests.InfrastructuresResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EditableEventsFragment.OnListFragmentInteractionListener,
        EditableInfrastrcuturesFragment.OnListFragmentInteractionListener, CreateEventFragment.OnFragmentInteractionListener,
        CreateInfrastructureFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        ManagersFragment.OnListFragmentInteractionListener {

    private int DATE_PICKER = 0;
    private CreateEventFragment cardFragment;
    private CreateInfrastructureFragment infrastructureCardFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sh = getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header)).setText(sh.getString("FullName", ""));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, new EditableInfrastrcuturesFragment(), "Infrastructures").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startCreateEventFragment(EditableEventsAdapter adapter) {
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        cardFragment = new CreateEventFragment();
        cardFragment.setAdapter(adapter);
        fragmentManager.beginTransaction().add(R.id.card_fragment, cardFragment, "CreateEvent").commit();
    }

    @Override
    public void startCreateEventFragment(EditableEventsAdapter adapter, EventsResponse event) {
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        cardFragment = new CreateEventFragment();
        cardFragment.setAdapter(adapter, event);
        fragmentManager.beginTransaction().add(R.id.card_fragment, cardFragment, "CreateEvent").commit();
    }

    @Override
    public void closeCreateEventFragment() {
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(cardFragment).commit();
    }

    @Override
    public void startCreateInfrastructureFragment(EditableInfrastructuresAdapter adapter) {
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        infrastructureCardFragment = new CreateInfrastructureFragment();
        infrastructureCardFragment.setAdapter(adapter);
        fragmentManager.beginTransaction().add(R.id.card_fragment, infrastructureCardFragment, "CreateInfrastructure").commit();
    }

    @Override
    public void startCreateInfrastructureFragment(EditableInfrastructuresAdapter adapter, InfrastructuresResponse event) {
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        infrastructureCardFragment = new CreateInfrastructureFragment();
        infrastructureCardFragment.setAdapter(adapter, event);
        fragmentManager.beginTransaction().add(R.id.card_fragment, infrastructureCardFragment, "CreateInfrastructure").commit();
    }

    @Override
    public void closeCreateInfrastructureFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(infrastructureCardFragment).commit();
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.GONE);
    }

    @Override
    public void closeProfile() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(profileFragment).commit();
        ((CardView) findViewById(R.id.card_view)).setVisibility(View.GONE);
    }

    @Override
    public void startDatePicker() {
        showDialog(DATE_PICKER);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            default:
                final int year, month, day;
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = simpleDateFormat.format(calendar.getTime());
                        dateToFragment(dateString);
                    }
                }, year, month, day);
        }
    }

    private void dateToFragment(String dateString) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.card_fragment) instanceof CreateEventFragment) {
            ((CreateEventFragment) fragmentManager.findFragmentById(R.id.card_fragment)).setDate(dateString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ((CardView) findViewById(R.id.card_view)).setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            profileFragment = new ProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.card_fragment, profileFragment, "Profile").commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_infrastructure) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new EditableInfrastrcuturesFragment(), "Infrastructures").commit();
            setTitle("Инфраструктуры");
        } else if (id == R.id.nav_event) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new EditableEventsFragment(), "Events").commit();
            setTitle("События");
        } else if (id == R.id.nav_manager) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new ManagersFragment(), "Managers").commit();
            setTitle("Менеджеры");
        } else if (id == R.id.nav_exit) {
            goToLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changeName(String newName) {
        ((TextView) ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.nav_header)).setText(newName);
    }

    public void goToLogin() {
        SharedPreferences sh = getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.remove("Token");
        editor.remove("FullName");
        editor.remove("UserType");
        editor.commit();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
