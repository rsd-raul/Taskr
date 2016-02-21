package com.software.achilles.tasked;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.*;
import com.software.achilles.tasked.fragments.DashboardListFragment;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.listeners.MainAndFilterDrawerConfiguration;
import com.software.achilles.tasked.util.Constants;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FloatingActionMenuConfigurator mFamConfigurator;
    private MainAndFilterDrawerConfiguration mDrawersConfigurator;

    private TaskController mTaskController;
    public Toolbar mToolbar;
    public ViewPager mViewPager;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Set ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Configure the drawers, both main and filter
        mDrawersConfigurator = new MainAndFilterDrawerConfiguration(this, true);

        final MainActivity aux = this;
        // Configure the fab menu and its children. - NEW THREAD
        threadManager(new Runnable() {
            public void run() {
                mFamConfigurator = new FloatingActionMenuConfigurator(aux);
            }
        });

        // Setup the fragment composing the ViewPager and the Tabs to control it - NEW THREAD
        threadManager(new Runnable() {
            public void run() {
                setupViewPager(TaskController.sTaskLists);
                setupTabLayout(TaskController.sTaskLists.size());
            }
        });
    }

    // TODO Investigar sobre threads y como manejarlos, sigue dando "Skipped X frames!"
    private void threadManager(Runnable runnable){
        // Launch new thread
        new Thread(runnable).start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ArrayList<TaskList> taskLists) {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        Adapter adapter = new Adapter(getSupportFragmentManager());

        // TODO quick jump to the desired list if too many lists present
//        if(taskLists.size() > 5)
//            adapter.addFragment(new DashboardSearchFragment(), "Search");

        // Populate each of the pages of the ViewPager
        for (TaskList taskList : taskLists) {
            // Pick the fragment the page is going to show
            DashboardListFragment dashboardListFragment = new DashboardListFragment();

            // Introduce the TaskList corresponding to that fragment
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TASK_LIST+"", taskList);
            dashboardListFragment.setArguments(bundle);

            // Add the fragment and it's bundle to the adapter
            adapter.addFragment(dashboardListFragment, taskList.getTitle());
        }
        mViewPager.setAdapter(adapter);
    }

    private void setupTabLayout(int taskListsSize) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Setup tabs for Dashboard if there is more than one TaskList, make them Scrollable/Fixed
        if(taskListsSize > 1) {
            tabLayout.setupWithViewPager(mViewPager);
            if(taskListsSize > 2)
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            else
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else
            tabLayout.setVisibility(View.GONE);
    }

    // -------------------------- Landscape --------------------------

    // ------------------------ Actionbar Menu -----------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        // TODO Filtering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the final search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Text has changed, apply filtering
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_filter:
                mDrawersConfigurator.mFilterDrawer.openDrawer();
                break;

            case R.id.action_search:
                // The calculations and filtering are done in "onCreateOptionsMenu"
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

        if(fam.isOpened() || mDrawersConfigurator.mMainDrawer.isDrawerOpen()) {
            fam.close(true);
            mDrawersConfigurator.mMainDrawer.closeDrawer();

        // Add mDrawersConfigurator.mFilterDrawer != null && if not only on dashboard
        } else if(mDrawersConfigurator.mFilterDrawer.isDrawerOpen()) {
            mDrawersConfigurator.mFilterDrawer.closeDrawer();

        } else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------

    // ------------------------- Deprecated --------------------------

}