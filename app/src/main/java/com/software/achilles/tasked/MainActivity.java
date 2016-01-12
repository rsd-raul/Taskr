package com.software.achilles.tasked;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
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
    private Toolbar toolbar;
    private boolean mAccountBoxExpanded = false;

    // TODO es necesario esto??
    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;
    private AccountHeader mAccountHeader;
    private Drawer mDrawer;




    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Navigation Drawer button
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        // Initialize Navigation Drawer variables
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
//
        // Setup Navigation , behavior and first item to checked
        setupHeader();
        setupDrawer();
//        mNavigationView.getMenu().getItem(0).setChecked(true);
//        setupDrawerListener();

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    // ---------------------- Navigation Drawer ----------------------


    private void setupHeader() {
        // Create the AccountHeader
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.default_image_navigation)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Mike Penz")
                                .withEmail("mikepenz@gmail.com")
                                .withIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.person_image_empty)),
                        new ProfileDrawerItem()
                                .withName("Mike Penz Work")
                                .withEmail("mikepenzwork@gmail.com")
                                .withIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.person_image_empty))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        return true;
                    }
                })
                .build();
    }

    private void setupDrawer(){

        // Settup the main components of the Navigation Drawer
        PrimaryDrawerItem dashboard = new PrimaryDrawerItem().withIdentifier(Constants.DASHBOARD)
                .withName(R.string.dashboard)
                .withIcon(R.drawable.ic_dashboard)
                .withIconColorRes(R.color.colorPrimary)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem snoozed = new PrimaryDrawerItem().withIdentifier(Constants.SNOOZED)
                .withName(R.string.snoozed)
                .withIcon(R.drawable.ic_time_clean)
                .withIconColorRes(R.color.amberDate)
                .withSelectedTextColorRes(R.color.amberDate)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem completed = new PrimaryDrawerItem().withIdentifier(Constants.COMPLETED)
                .withName(R.string.completed)
                .withIcon(R.drawable.ic_done)
                .withIconColorRes(R.color.colorSuccess)
                .withSelectedTextColorRes(R.color.colorSuccess)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem glance = new PrimaryDrawerItem().withIdentifier(Constants.GLANCE)
                .withName(R.string.glance)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem planner = new PrimaryDrawerItem().withIdentifier(Constants.PLANNER)
                .withName(R.string.planner)
                .withIcon(R.drawable.ic_view_carousel)
                .withIconTintingEnabled(true);
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(Constants.SETTIGS)
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true);


        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        dashboard, snoozed, completed,
                        new DividerDrawerItem(),
                        glance, planner,
                        new DividerDrawerItem()
                )
                .withActionBarDrawerToggle(true)
                .addStickyDrawerItems(
                        settings
                )
                .build();

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                switch (drawerItem.getIdentifier()) {

                    case Constants.DASHBOARD:
                        break;
                    case Constants.SNOOZED:
                        break;
                    case Constants.COMPLETED:
                        break;
                    case Constants.GLANCE:
                        break;
                    case Constants.PLANNER:
                        break;
                    case Constants.SETTIGS:
                        Intent intent = new Intent(getApplicationContext(), Preferences.class);
                        startActivity(intent);
                        break;
                }

                mDrawer.closeDrawer();
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

//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                break;

            case R.id.action_filter:
                break;

            case R.id.action_search:
                break;

//            case R.id.action_settings:
//                Intent intent = new Intent(this, Preferences.class);
//                startActivity(intent);
//                break;

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

    // ----------------------------- Util ----------------------------

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return (resourceId > 0) ? getResources().getDimensionPixelSize(resourceId) : 0 ;
    }















    // ------------------------- Deprecated --------------------------

    private void setupDrawerDEP(){
        // Adapt to Lollipop and above as the Navigation Drawer is under the Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            adaptNavigationDrawerIfStatusBarTransparent();

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO es necesario esto??
                // Run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
//                // Once closed, if the AccountBox is opened, close it
//                if (mAccountBoxExpanded) {
//                    mAccountBoxExpanded = false;
//                    setupAccountBoxToggle();
//                }
//                onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
//                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                onNavDrawerSlide(slideOffset);
            }
        });

    }

    private void setupDrawerListenerDEP() {
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

    // --------------------- Lollipop and above ----------------------

    private void adaptNavigationDrawerIfStatusBarTransparent(){
        // Setup StatusBar color so the Drawer can draw there instead
        mDrawerLayout.setStatusBarBackgroundColor(
                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        // Retrieve the header in order to obtain the Views we need
        View header = mNavigationView.getHeaderView(0);
        View chosenAccountContentView = header.findViewById(R.id.chosen_account_content_view);
        View chosenAccountView = header.findViewById(R.id.chosen_account_view);

        // Retrieve current Navigation Drawer height and the StatusBar
        int navDrawerHeight = getResources().getDimensionPixelSize(
                R.dimen.navigation_drawer_chosen_account_height);
        int statusBarHeight = getStatusBarHeight();

        // Add top margin to the profile picture and more Height to the container
        ViewGroup.MarginLayoutParams lp1= (ViewGroup.MarginLayoutParams)
                chosenAccountContentView.getLayoutParams();
        lp1.topMargin = statusBarHeight;
        chosenAccountContentView.setLayoutParams(lp1);

        ViewGroup.LayoutParams lp2 =
                chosenAccountView.getLayoutParams();
        lp2.height = navDrawerHeight + statusBarHeight;
        chosenAccountView.setLayoutParams(lp2);
    }
}