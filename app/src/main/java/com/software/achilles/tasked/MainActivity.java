package com.software.achilles.tasked;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    public MainAndFilterDrawerConfiguration mDrawersConfigurator;

    private TaskController mTaskController;
    public Toolbar mToolbar;
    public ViewPager mViewPager;
    public TabLayout tabLayout;

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
        mToolbar.setNavigationIcon(R.drawable.ic_menu);

        // Configure the fab menu and its children.
        mFamConfigurator = new FloatingActionMenuConfigurator(this);

        // Configure the drawers, both main and filter
        mDrawersConfigurator = new MainAndFilterDrawerConfiguration(this, true);

        // Setup the fragment composing the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(TaskController.sTaskLists);

        // Setup tabs for Dashboard and make Scrollable
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ArrayList<TaskList> taskLists) {
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
                if(TaskController.addTaskOpen)
                    retractAddTaskLayout();
                else
                    mDrawersConfigurator.mMainDrawer.openDrawer();
                break;

            case R.id.action_filter:
                mDrawersConfigurator.mFilterDrawer.openDrawer();
                break;

            case R.id.action_search:
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

        // If the addTaskFragment is open, destroy it.
        } else if(TaskController.addTaskOpen){
            retractAddTaskLayout();
        } else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------

    // ------------------------- Deprecated --------------------------

    public void deployAddTaskLayout(){

        // Instead of removing the ViewPager we disable it and its children
        enableDisableViewGroup(mViewPager, false);

        // Insert the fragment by replacing any existing fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new TaskCreationFragment())
                .commit();

        // Hide the TabLayout
        tabLayout.setVisibility(View.GONE);

        // Clear the previous menu from the Toolbar and add the new one
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_create_task);

        // Change the icon and notify the controller the view has changed
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        TaskController.addTaskOpen = true;
        mToolbar.setTitle("");

        mFamConfigurator.hideMenu();
    }

    public void retractAddTaskLayout(){
        // Instead of removing the ViewPager we disable it and its children
        enableDisableViewGroup(mViewPager, true);

        // Insert the fragment by replacing any existing fragment
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        frameLayout.removeAllViews();

        // Hide the TabLayout
        tabLayout.setVisibility(View.VISIBLE);

        // Clear the previous menu from the Toolbar and add the new one
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_dashboard);

        // Change the icon and notify the controller the view has changed
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        TaskController.addTaskOpen = false;
        mToolbar.setTitle(R.string.dashboard);

        mFamConfigurator.showMenu();
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();

        // Enable or disable all the childs for the ViewGroup
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);

            // Recursive call in order to disable or enable all the children for a nested viewGroup
            if (view instanceof ViewGroup)
                enableDisableViewGroup((ViewGroup) view, enabled);
        }
    }

}