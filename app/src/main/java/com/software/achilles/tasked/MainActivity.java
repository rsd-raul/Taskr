package com.software.achilles.tasked;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.fragments.DashboardListFragment;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.util.Constants;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FloatingActionMenuConfigurator mFamConfigurator;
    private DrawerLayout mDrawerLayout;
    private TaskController mTaskController;
    private NavigationView mNavigationView;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Set ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Navigation Drawer button
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize Navigation Drawer variables
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        // Setup Navigation , behavior and first item to checked

        setupDrawer();
        mNavigationView.getMenu().getItem(0).setChecked(true);
        setupDrawerListener();

        // Configure the fab menu and its children.
        mFamConfigurator = new FloatingActionMenuConfigurator(this);

        // Setup the fragment composing the ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Setup tabs for Dashboard and make Scrollable
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    // ---------------------- Navigation Drawer ----------------------

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setupDrawer(){

        // Setup StatusBar color so the Drawer can draw there instead
        mDrawerLayout.setStatusBarBackgroundColor(
                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        //
        View header = mNavigationView.getHeaderView(0);

        final View chosenAccountContentView = header.findViewById(R.id.chosen_account_content_view);
        final View chosenAccountView = header.findViewById(R.id.chosen_account_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

//        mNavigationView.
        Log.d("myApp", chosenAccountContentView+"");
        Log.d("myApp", chosenAccountView+"");
    }

    private void setupDrawerListener() {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        // Intent?

                        return true;
                    }
                });
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

//        adapter.addFragment(new DashboardSearchFragment(), "Search");
        // TODO quick jump to the desired list


        // Populate each of the pages of the ViewPager
        for (TaskList taskList : TaskController.sTaskLists) {
            // Pick the fragment the page is going to show
            DashboardListFragment dashboardListFragment = new DashboardListFragment();

            // Introduce the TaskList corresponding to that fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TASK_LIST + "", taskList);
            dashboardListFragment.setArguments(bundle);

            // Add the fragment and it's bundle to the adapter
            adapter.addFragment(dashboardListFragment, taskList.getTitle());
        }

        viewPager.setAdapter(adapter);
    }

    // -------------------------- Landscape --------------------------

    // ------------------------ Actionbar Menu -----------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_filter:
                break;

            case R.id.action_search:
                break;

            case R.id.action_settings:
                Intent intent = new Intent(this, Preferences.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------- Use Cases --------------------------

    // --------------------- Add Task Interface ----------------------

    // --------------------------- Details ---------------------------

    @Override
    public void onBackPressed() {
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        if(fam.isOpened())
            fam.close(true);
        else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------

}