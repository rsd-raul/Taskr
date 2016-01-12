package com.software.achilles.tasked;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.fragments.DashboardListFragment;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FloatingActionMenuConfigurator mFamConfigurator;
    private TaskController mTaskController;
    private Toolbar mToolbar;
    public ViewPager mViewPager;
    private AccountHeader mAccountHeader;
    private Drawer mDrawer;
    private List<Integer> taskListIds;

    // ------------------------- Constructor -------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TaskController
        mTaskController = TaskController.getInstance();

        // Set ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        // Setup tabs for Dashboard and make Scrollable
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
                                .withName("John Doe")
                                .withEmail("jonnydoe@gmail.com")
                                .withIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.person_image_empty)),
                        new ProfileDrawerItem()
                                .withName("John Doe Work")
                                .withEmail("jonnydoework@gmail.com")
                                .withIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.person_image_empty))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        //TODO onProfileChanged
                        return true;
                    }
                })
                .build();
    }

    private void setupDrawer(){

        // Setup the main components of the Navigation Drawer
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
        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(Constants.SETTINGS)
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true)
                .withSelectable(false);
        SecondaryDrawerItem contact = new SecondaryDrawerItem().withIdentifier(Constants.CONTACT)
                .withName(R.string.contact)
                .withIcon(R.drawable.ic_email)
                .withIconTintingEnabled(true)
                .withSelectable(false);
        PrimaryDrawerItem taskListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.taskList)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"",
                prefs.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", true)).apply();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        dashboard, snoozed, completed,
                        new DividerDrawerItem(),
                        glance, planner,
                        taskListCollapsable
                )
                .withActionBarDrawerToggle(true)
                .addStickyDrawerItems(
                        settings, contact
                )
                .build();

        // if Task List is
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean status = preferences.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", true);
        if(status)
            addTaskListToDrawer();

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int identifier = drawerItem.getIdentifier();
                switch (identifier) {

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

                    case Constants.SETTINGS:
                        // Launch the Preferences Fragment
                        Intent intentSettings = new Intent(getApplicationContext(), Preferences.class);
                        startActivity(intentSettings);
                        break;

                    case Constants.CONTACT:
                        // Create a custom intent for Emails
                        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                getResources().getString(R.string.mailto),
                                getResources().getString(R.string.developer_email), null));

                        //Populate the fields by default
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                                getResources().getString(R.string.subject_email));

                        // Create a dialog only for Email clients
                        if(intentEmail.resolveActivity(getPackageManager()) != null)
                            startActivity(Intent.createChooser(intentEmail,
                                    getResources().getString(R.string.send_email)));
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        switchCollapsableContentAndPreference();
                        break;

                    default:
                        int index = TaskController.getPositionById(identifier);
                        if(index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }

                if(identifier != Constants.COLLAPSABLE_TASK_LIST)
                    mDrawer.closeDrawer();
                return true;
            }
        });
    }

    private void switchCollapsableContentAndPreference(){
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean status = preferences.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", true);

        if(status)
            for (int i = 0; i < taskListIds.size(); i++)
                mDrawer.removeItem(taskListIds.get(i));
        else
            addTaskListToDrawer();

        preferences.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", !status).apply();
    }

    private void addTaskListToDrawer(){
        List<Integer>addedIds = new ArrayList<>();

        for(TaskList taskList : TaskController.sTaskLists) {
            mDrawer.addItem(
                    new SecondaryDrawerItem().withIdentifier(taskList.getId())
                            .withLevel(2)
                            .withName(taskList.getTitle())
                            .withIcon(R.drawable.ic_done_all)
                            .withIconTintingEnabled(true)
                            .withSelectable(false));
            addedIds.add(taskList.getId());
        }

        taskListIds = addedIds;
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

            case R.id.action_filter:
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

        if(fam.isOpened())
            fam.close(true);
        else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------
















    // ------------------------- Deprecated --------------------------

//    //TODO es necesario?
//    // A Runnable that we should execute when the navigation drawer finishes its closing animation
//    private Runnable mDeferredOnDrawerClosedRunnable;
//    private NavigationView mNavigationView;
//    private boolean mAccountBoxExpanded = false;
//    private DrawerLayout mDrawerLayout;
//    private void setupDrawerDEP(){
//        // Adapt to Lollipop and above as the Navigation Drawer is under the Status Bar
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            adaptNavigationDrawerIfStatusBarTransparent();
//
//        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // TODO es necesario esto??
//                // Run deferred action, if we have one
//                if (mDeferredOnDrawerClosedRunnable != null) {
//                    mDeferredOnDrawerClosedRunnable.run();
//                    mDeferredOnDrawerClosedRunnable = null;
//                }
////                // Once closed, if the AccountBox is opened, close it
////                if (mAccountBoxExpanded) {
////                    mAccountBoxExpanded = false;
////                    setupAccountBoxToggle();
////                }
////                onNavDrawerStateChanged(false, false);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
////                onNavDrawerStateChanged(true, false);
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
////                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
////                onNavDrawerSlide(slideOffset);
//            }
//        });
//
//    }
//
//    private void setupDrawerListenerDEP() {
//        mNavigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        menuItem.setChecked(true);
//                        mDrawerLayout.closeDrawers();
//
//                        // Intent?
//
//                        return true;
//                    }
//                });
//    }
//
//    // --------------------- Lollipop and above ----------------------
//
//    private void adaptNavigationDrawerIfStatusBarTransparent(){
//        // Setup StatusBar color so the Drawer can draw there instead
//        mDrawerLayout.setStatusBarBackgroundColor(
//                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//
//        // Retrieve the header in order to obtain the Views we need
//        View header = mNavigationView.getHeaderView(0);
//        View chosenAccountContentView = header.findViewById(R.id.chosen_account_content_view);
//        View chosenAccountView = header.findViewById(R.id.chosen_account_view);
//
//        // Retrieve current Navigation Drawer height and the StatusBar
//        int navDrawerHeight = getResources().getDimensionPixelSize(
//                R.dimen.navigation_drawer_chosen_account_height);
//        int statusBarHeight = getStatusBarHeight();
//
//        // Add top margin to the profile picture and more Height to the container
//        ViewGroup.MarginLayoutParams lp1= (ViewGroup.MarginLayoutParams)
//                chosenAccountContentView.getLayoutParams();
//        lp1.topMargin = statusBarHeight;
//        chosenAccountContentView.setLayoutParams(lp1);
//
//        ViewGroup.LayoutParams lp2 =
//                chosenAccountView.getLayoutParams();
//        lp2.height = navDrawerHeight + statusBarHeight;
//        chosenAccountView.setLayoutParams(lp2);
//    }
//
//    // ----------------------------- Util ----------------------------
//
//    private int getStatusBarHeight() {
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        return (resourceId > 0) ? getResources().getDimensionPixelSize(resourceId) : 0 ;
//    }
}