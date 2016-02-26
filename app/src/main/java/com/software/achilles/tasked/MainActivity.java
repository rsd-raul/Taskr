package com.software.achilles.tasked;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.*;
import com.software.achilles.tasked.fragments.DashboardListFragment;
import com.software.achilles.tasked.fragments.TaskCreationFragment;
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
    public View mAddTaskView;
    public TaskCreationFragment mTaskCreationFragment;

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

        // TODO only for testing
//        deployAddTask();
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

        // TODO 1 de 2 - Quick jump to the desired list if too many lists present (necessary?)
//        if(taskLists.size() > 5)
//            adapter.addFragment(new DashboardSearchFragment(), "Search");

        // Populate each of the pages of the ViewPager
        for (int index = 0 ; index < taskLists.size(); index++){
            // Pick the fragment the page is going to show
            DashboardListFragment dashboardListFragment = new DashboardListFragment();

            // Introduce the TaskList corresponding to that fragment
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TASK_LIST+"", index);
            dashboardListFragment.setArguments(bundle);

            // Add the fragment and it's bundle to the adapter
            adapter.addFragment(dashboardListFragment, taskLists.get(index).getTitle());
        }

        mViewPager.setAdapter(adapter);

        // TODO 2 de 2 - Quick jump to the desired list if too many lists present (necessary?)
//        mViewPager.setCurrentItem(1);     // show the first list by default, not the quick search
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

    public void taskCustomization(View view){
        switch (view.getId()){
            // Close interface
            case R.id.button_close:
                dialogForTaskRemoval();
                break;
            // Task customization buttons
            default:
                mTaskCreationFragment.taskCustomization(view);
                break;

        }
    }

    private void dialogForTaskRemoval(){

        // If the user haven't typed anything, close the interface
        if(!mTaskCreationFragment.isDataPresent()){
            removeAddTask();
            return;
        }

        // Else, ask for confirmation
        Dialog dialog = new AlertDialog.Builder(this)
                // For simple dialogs we don't use a Title (Google Guidelines)
                .setMessage(getString(R.string.discard_changes))

                // Only on discard the removeAddTask is triggered
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeAddTask();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();

        // Dialog width customization
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = 800;
        dialog.getWindow().setAttributes(lp);
    }

    public void deployAddTask(){

        // Hide the fam
        mFamConfigurator.famVisibility(false);

        // If the layout was created, show it and restart the fields
        if (mAddTaskView != null) {
            mAddTaskView.setVisibility(View.VISIBLE);
            mTaskCreationFragment.resetFields();
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        mTaskCreationFragment = new TaskCreationFragment();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bottom_fragment_container, mTaskCreationFragment).commit();

        // Color the status bar in order to adapt the color to the shadow deployed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        // Block filter and navigation drawers
        mDrawersConfigurator.blockDrawers(true);
    }

    public void removeAddTask(){

        // Show the FAM
        mFamConfigurator.famVisibility(true);

        // Hide the container with the layout
        mAddTaskView = findViewById(R.id.bottom_fragment_container);
        mAddTaskView.setVisibility(View.GONE);

        // Color the status bar in order to adapt the color to the shadow deployed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
            window.setStatusBarColor(color);
        }

        // Unblock filter and navigation drawers
        mDrawersConfigurator.blockDrawers(false);
    }

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