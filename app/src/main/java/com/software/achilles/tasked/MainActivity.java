package com.software.achilles.tasked;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.BasicType;
import com.software.achilles.tasked.domain.FavoriteLocation;
import com.software.achilles.tasked.domain.Label;
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
    private Drawer mFilterDrawer;
    private List<Integer> mTaskListIds;
    private List<Integer> mLabelListIds;
    private List<Integer> mLocationListIds;
    private PrimaryDrawerItem mTaskListCollapsable;
    private BadgeStyle mBadgeStyleExpand;
    private BadgeStyle mBadgeStyleCollapse;
    private boolean mExpandedTaskListFilter = false;
    private boolean mExpandedLabelListFilter = false;
    private boolean mExpandedLocationListFilter = false;

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

        // Setup Navigation , behavior and first item to checked
        initializeBadges();
        setupDrawer();
        setupFilterDrawer();
        // TODO a peticion de filter mejor? recuerda problemas de drawer
        // TODO si ejecuto aqui me cargo la sombra transparente para profile en la otra
        // SI LO PONGO ARRIBA SE CORRIJE, PERO QUEDA FEO TELA

        // Configure the fab menu and its children.
        mFamConfigurator = new FloatingActionMenuConfigurator(this);

        // Setup the fragment composing the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager, TaskController.sTaskLists);

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

    private void initializeBadges(){
        int color = ContextCompat.getColor(this, R.color.secondaryText);

        Drawable expand = ContextCompat.getDrawable(this, R.drawable.ic_expand_filled);
        expand.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleExpand = new BadgeStyle().withBadgeBackground(expand);

        Drawable collapse = ContextCompat.getDrawable(this, R.drawable.ic_collapse_filled);
        collapse.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        mBadgeStyleCollapse = new BadgeStyle().withBadgeBackground(collapse);
    }

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

        setupHeader();

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

        mTaskListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.taskList)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"",
                prefs.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS+"", true)).apply();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        dashboard, snoozed, completed,
                        new DividerDrawerItem(),
                        glance, planner,
                        new DividerDrawerItem(),
                        mTaskListCollapsable
                )
                .addStickyDrawerItems(
                        settings, contact
                )
                .build();

        setupDrawerListener();
    }

    private void setupDrawerListener(){
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
                        if (intentEmail.resolveActivity(getPackageManager()) != null)
                            startActivity(Intent.createChooser(intentEmail,
                                    getResources().getString(R.string.send_email)));
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        switchExpandableTaskListContentAndPreference();
                        break;

                    case Constants.ADD_TASK_LIST:
                        break;

                    default:
                        int index = TaskController.getPositionById(identifier);
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }

                if (identifier != Constants.COLLAPSABLE_TASK_LIST)
                    mDrawer.closeDrawer();
                return true;
            }
        });

        setupExpandableTaskList();
    }

    private void setupExpandableTaskList(){
        // if Task List is expanded populate the Navigation Drawer
        if(getAndOrSwitch(false)) {
            addTaskListToMainDrawer(TaskController.sTaskLists);
            adaptTaskListExpandable(false);
        }else
            adaptTaskListExpandable(true);
    }

    private boolean getAndOrSwitch(boolean switchValue){
        // Get the preference that indicates if the user wants the Task List collapsed or not
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean status = preferences.getBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS + "", true);

        // If switch (click on Task List case), switch the values to reflect the change
        if(switchValue)
            preferences.edit().putBoolean(Constants.COLLAPSABLE_TASK_LIST_STATUS + "", !status).apply();

        return status;
    }

    private void switchExpandableTaskListContentAndPreference(){
        // Get current status for the Task List and in this case switch it in the preferences
        Boolean status = getAndOrSwitch(true);

        // If it opened, remove all items, if closed, populate the drawer
        if(status) {
            adaptTaskListExpandable(true);
            for (int i = 0; i < mTaskListIds.size(); i++)
                mDrawer.removeItem(mTaskListIds.get(i));
        } else{
            adaptTaskListExpandable(false);
            addTaskListToMainDrawer(TaskController.sTaskLists);
        }
    }

    private void adaptTaskListExpandable(boolean expand){
        // Switch the badge between expand and collapse icons
        if(expand)
            mTaskListCollapsable.withBadgeStyle(mBadgeStyleExpand);
        else
            mTaskListCollapsable.withBadgeStyle(mBadgeStyleCollapse);

        mDrawer.updateItem(mTaskListCollapsable);
    }

    private void setupFilterDrawer(){

        SectionDrawerItem mainSection = new SectionDrawerItem()
                .withName(R.string.main_filters);
        SectionDrawerItem listSection = new SectionDrawerItem()
                .withName(R.string.list_filters);

        PrimaryDrawerItem starred = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_flag)
                .withIconTintingEnabled(true)
                .withIconColorRes(R.color.colorAccent)
                .withSelectedTextColorRes(R.color.colorAccent)
                .withIdentifier(Constants.STARRED)
                .withSelectable(true)
                .withBadge("");

        PrimaryDrawerItem today = new PrimaryDrawerItem()
                .withName(R.string.dueToday)
                .withIcon(R.drawable.ic_calendar_today)
                .withIconColorRes(R.color.amberDate)
                .withSelectedTextColorRes(R.color.amberDate)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_TODAY)
                .withSelectable(true)
                .withBadge("");

        PrimaryDrawerItem thisWeek = new PrimaryDrawerItem()
                .withName(R.string.dueThisWeek)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconColorRes(R.color.colorPrimary)
                .withSelectedTextColorRes(R.color.colorPrimary)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_THIS_WEEK)
                .withSelectable(true)
                .withBadge("");

        PrimaryDrawerItem taskListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.taskList)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("");

        PrimaryDrawerItem labelListCollapsable = new PrimaryDrawerItem()
                .withName(R.string.labelList)
                .withIcon(R.drawable.ic_label_outline)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_LABEL_LIST)
                .withSelectable(false)
                .withBadge("");

        PrimaryDrawerItem labelLocationCollapsable = new PrimaryDrawerItem()
                .withName(R.string.locationList)
                .withIcon(R.drawable.ic_place)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSABLE_LOCATION_LIST)
                .withSelectable(false)
                .withBadge("");

        mFilterDrawer = new DrawerBuilder()
                .withActivity(this)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(false)
                .addDrawerItems(
                        mainSection,
                        starred, today, thisWeek,
                        listSection,
                        taskListCollapsable, labelListCollapsable, labelLocationCollapsable
                )
                .withDrawerGravity(Gravity.END)
//                .build();
                .append(mDrawer);
                // TODO esto hace que los drawer se dibujen en un mismo plano, pero no vale para
                // activar usando SOLO el boton, así que tendrias que descomentar lo de abajo


        // TODO esto a lo mejor es lo suyo que sea una opcion
//        mFilterDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //  TODO comprobar si las alertas por memoria es por esto

        setupFilterDrawerListener();
    }

    private void setupFilterDrawerListener(){
        mFilterDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int identifier = drawerItem.getIdentifier();
                switch (identifier) {

                    case Constants.STARRED:
                        break;
                    case Constants.DUE_TODAY:
                        break;
                    case Constants.DUE_THIS_WEEK:
                        break;

                    case Constants.COLLAPSABLE_TASK_LIST:
                        if(mExpandedTaskListFilter) {
                            for (int i = 0; i < mTaskListIds.size(); i++)
                                mFilterDrawer.removeItem(mTaskListIds.get(i));
                            mExpandedTaskListFilter = false;
                        } else {
                            addTaskListToFilterDrawer(TaskController.sTaskLists);
                            mExpandedTaskListFilter = true;
                        }
                        break;

                    case Constants.COLLAPSABLE_LABEL_LIST:
                        if(mExpandedLabelListFilter) {
                            Log.d("myApp", mLabelListIds + "Aaaaaaaaaaaaaa");
                            for (int i = 0; i < mLabelListIds.size(); i++)
                                mFilterDrawer.removeItem(mLabelListIds.get(i));
                            mExpandedLabelListFilter = false;
                        } else {
                            addLabelsToFilterDrawer(TaskController.sLabels);
                            mExpandedLabelListFilter = true;
                        }
                        break;

                    case Constants.COLLAPSABLE_LOCATION_LIST:
                        if(mExpandedLocationListFilter) {
                            for (int i = 0; i < mLocationListIds.size(); i++)
                                mFilterDrawer.removeItem(mLocationListIds.get(i));
                            mExpandedLocationListFilter = false;
                        } else {
                            addLocationsFilterToDrawer(TaskController.sFavouriteLocations);
                            mExpandedLocationListFilter = true;
                        }
                        break;

                    default:
                        int index = TaskController.getPositionById(identifier);
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }

                return true;
            }
        });

        setupExpandableTaskList();
    }

    private void addTaskListToFilterDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mFilterDrawer,
                false, R.drawable.ic_done_all, Constants.COLLAPSABLE_TASK_LIST);
    }
    private void addTaskListToMainDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mDrawer,
                true, R.drawable.ic_done_all, Constants.COLLAPSABLE_TASK_LIST);
    }
    private void addLabelsToFilterDrawer(ArrayList<Label> listLabels){
        addItemListToDrawer(new ArrayList<BasicType>(listLabels), mFilterDrawer,
                false, R.drawable.ic_done_all, Constants.COLLAPSABLE_LABEL_LIST);
    }
    private void addLocationsFilterToDrawer(ArrayList<FavoriteLocation> favLocations){
        addItemListToDrawer(new ArrayList<BasicType>(favLocations), mFilterDrawer,
                false, R.drawable.ic_done_all, Constants.COLLAPSABLE_LOCATION_LIST);
    }

    private void addItemListToDrawer(ArrayList<BasicType> taskLists, Drawer drawer,
                                     boolean main, int iconRes, int identifier){
        List<Integer>addedIds = new ArrayList<>();

        // Get the position for the item in the drawer in order to add its children
        Integer position = drawer.getPosition(identifier)+1;

        // Add the Task Lists to the drawer by order at the right position
        for (int i = 0; i < taskLists.size(); i++) {
            drawer.addItemAtPosition(
                    new SecondaryDrawerItem().withIdentifier(taskLists.get(i).getId())
                            .withLevel(2)
                            .withName(taskLists.get(i).getTitle())
                            .withIcon(iconRes)
                            .withIconTintingEnabled(true)
                            .withSelectable(false),
                    position);
            position++;
            addedIds.add(taskLists.get(i).getId());
        }

        // If on the main drawer, add a new "add Task List" item for convenience and its ID
        if(main) {
            mDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
                    .withLevel(2)
                    .withName(R.string.addList)
                    .withIcon(R.drawable.ic_add)
                    .withIconTintingEnabled(true)
                    .withSelectable(false));
            addedIds.add(Constants.ADD_TASK_LIST);
        }

        // Save the id to control the Navigation Drawer more properly
        switch (identifier){
            case Constants.COLLAPSABLE_TASK_LIST:
                mTaskListIds = addedIds;
                break;
            case Constants.COLLAPSABLE_LABEL_LIST:
                mLabelListIds = addedIds;
                break;
            case Constants.COLLAPSABLE_LOCATION_LIST:
                mLocationListIds = addedIds;
                break;
        }
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ViewPager viewPager, ArrayList<TaskList> taskLists) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

//        adapter.addFragment(new DashboardSearchFragment(), "Search");
        // TODO quick jump to the desired list


        // Populate each of the pages of the ViewPager
        for (TaskList taskList : taskLists) {
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
                if(mFilterDrawer == null)
                    setupFilterDrawer();

                mFilterDrawer.openDrawer();
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

        if(fam.isOpened() || mDrawer.isDrawerOpen()) {
            fam.close(true);
            mDrawer.closeDrawer();
        }else if(mFilterDrawer != null && mFilterDrawer.isDrawerOpen())
            mFilterDrawer.closeDrawer();
        else
            super.onBackPressed();
    }

    // ------------------------ Notifications ------------------------

    // ------------------------- Preferences -------------------------










    // ------------------------- Deprecated --------------------------

//    private void addTaskListToDrawer(ArrayList<TaskList> taskLists){
//        List<Integer>addedIds = new ArrayList<>();
//
//        // Add the Task Lists to the drawer by order and at the end
//        for(TaskList taskList : taskLists) {
//            mDrawer.addItem(
//                    new SecondaryDrawerItem().withIdentifier(taskList.getId())
//                            .withLevel(2)
//                            .withName(taskList.getTitle())
//                            .withIcon(R.drawable.ic_done_all)
//                            .withIconTintingEnabled(true)
//                            .withSelectable(false));
//            addedIds.add(taskList.getId());
//        }
//
//        // Add a new add Task List item for convenience, also add its ID to control it.
//        mDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
//                .withLevel(2)
//                .withName(R.string.addList)
//                .withIcon(R.drawable.ic_add)
//                .withIconTintingEnabled(true)
//                .withSelectable(false));
//        addedIds.add(Constants.ADD_TASK_LIST);
//
//        // Save the id to control the Navigation Drawer more properly
//        mTaskListIds = addedIds;
//    }
}