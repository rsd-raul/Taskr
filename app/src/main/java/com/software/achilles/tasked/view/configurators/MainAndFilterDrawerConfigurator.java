package com.software.achilles.tasked.view.configurators;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

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
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.BasicType;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Location;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.MainPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.Preferences;
import com.software.achilles.tasked.view.fragments.DashboardFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmResults;

@Singleton
public class MainAndFilterDrawerConfigurator {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private MainActivity mActivity;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    private AccountHeader mAccountHeader;
    public Drawer mMainDrawer, mFilterDrawer;
    private List<Integer> mTaskListIds, mLocationListIds, mOrderListIds,
            mLabelListIds = new ArrayList<>();
    private BadgeStyle mBadgeExpand, mBadgeCollapse;
    private boolean firstTime = true, mExpandedTaskList = false, mExpandedTaskListFilter = false,
            mExpandedLocationListFilter = false, mExpandedOrderListFilter = false;
    public boolean mExpandedLabelListFilter = true;
    private PrimaryDrawerItem mTaskListCollapsibleMain, mTaskCollapsible, mLabelCollapsible,
            mLocationCollapsible, mOrderCollapsible;

    // -------------------------- Injected ---------------------------

    MainPresenter mainPresenter;
    DataManager dataManager;
    DashboardPresenter dashboardPresenter;

    // ------------------------- Constructor -------------------------

    @Inject
    public MainAndFilterDrawerConfigurator(MainPresenter mainPresenter, DataManager dataManager, DashboardPresenter dashboardPresenter) {
        this.mainPresenter = mainPresenter;
        this.dataManager = dataManager;
        this.dashboardPresenter = dashboardPresenter;
    }

    public void configure(MainActivity activity){
        this.mActivity = activity;

        // Set ActionBar
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);

        // Setup Main Drawer and its behaviour
        initializeBadges();
        setupProfileHeader();
        setupMainDrawer();
        setupMainDrawerListener();

        // TODO esto esta puesto para quitar el null pointer exception, corregir

        setupMainList(dataManager.findAllTaskList());

        // Setup Filter Drawer and its behaviour
        setupFilterDrawer();
        setupFilterDrawerListener();
    }

    // ----------------------- Badges Creation -----------------------

    private void initializeBadges(){
        int color = ContextCompat.getColor(mActivity, R.color.secondaryText);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;

        Drawable expand = ContextCompat.getDrawable(mActivity, R.drawable.ic_expand_filled);
        expand.setColorFilter(color, mode);
        mBadgeExpand = new BadgeStyle().withBadgeBackground(expand);

        Drawable collapse = ContextCompat.getDrawable(mActivity, R.drawable.ic_collapse_filled);
        collapse.setColorFilter(color, mode);
        mBadgeCollapse = new BadgeStyle().withBadgeBackground(collapse);
    }

    // ---------------------- Navigation Drawer ----------------------

    private void setupProfileHeader() {
        // Create the AccountHeader
        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.header_background_default)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("John Doe")
                                .withEmail("jonnydoe@gmail.com")
                                .withIcon(R.drawable.person_image_empty),
                        new ProfileDrawerItem()
                                .withName("John Doe Work")
                                .withEmail("jonnydoework@gmail.com")
                                .withIcon(R.drawable.person_image_empty)
                        // TODO Add y Manage profiles - Añade lag?
//                        new ProfileSettingDrawerItem()
//                                .withIdentifier(Constants.ADD_ACCOUNT)
//                                .withName("Add Account")
//                                .withIcon(R.drawable.ic_add)
//                                .withIconTinted(true),
//                        new ProfileSettingDrawerItem()
//                                .withIdentifier(Constants.SETTINGS_ACCOUNTS)
//                                .withName("Manage Account")
//                                .withIcon(R.drawable.ic_settings_nut)
//                                .withIconTinted(true)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        // TODO Account handling
                        return true;
                    }
                })
                .build();
    }

    private void setupMainDrawer(){

        // Create main items
        int color = R.color.colorPrimary;
        final PrimaryDrawerItem dashboard = new PrimaryDrawerItem().withIdentifier(Constants.DASHBOARD)
                .withIcon(R.drawable.ic_dashboard)
                .withName(R.string.dashboard)
                .withIconColorRes(color)
                .withIconTintingEnabled(true);
        color = R.color.amberDate;
        PrimaryDrawerItem snoozed = new PrimaryDrawerItem().withIdentifier(Constants.SNOOZED)
                .withIcon(R.drawable.ic_time_clean)
                .withName(R.string.snoozed)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true);
        color = R.color.colorSuccess;
        PrimaryDrawerItem completed = new PrimaryDrawerItem().withIdentifier(Constants.COMPLETED)
                .withIcon(R.drawable.ic_done)
                .withName(R.string.completed)
                .withIconColorRes(color)
                .withSelectedIconColorRes(color)
                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true);

        // Create secondary items
        PrimaryDrawerItem glance = new PrimaryDrawerItem().withIdentifier(Constants.GLANCE)
                .withName(R.string.glance)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem planner = new PrimaryDrawerItem().withIdentifier(Constants.PLANNER)
                .withName(R.string.planner)
                .withIcon(R.drawable.ic_view_carousel)
                .withIconTintingEnabled(true);

        // Create the footer items
        PrimaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(Constants.SETTINGS)
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true)
                .withSelectable(false);
        PrimaryDrawerItem contact = new SecondaryDrawerItem().withIdentifier(Constants.CONTACT)
                .withName(R.string.contact)
                .withIcon(R.drawable.ic_email)
                .withIconTintingEnabled(true)
                .withSelectable(false);

        // Create Main Drawer
        mMainDrawer = new DrawerBuilder()
                .withActivity(mActivity)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(mAccountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        dashboard, snoozed, completed,
                        new DividerDrawerItem(),
                        glance, planner,
                        new DividerDrawerItem().withIdentifier(Constants.LIST_SEPARATOR)
//                        , mTaskListCollapsibleMain
                )
                .addStickyDrawerItems(
                        settings, contact
                )
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        mainPresenter.backToBack();
                        return true;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if(mActivity.mCurrentFragmentKey == Constants.DASHBOARD)
                            mMainDrawer.setSelection(dashboard, false);

                        // drawerViews.getWidth():
                        // MainDrawer = 912
                        // FilterDrawer = 750

                        // If the opened one is filterDrawer and is the first time
                        if (drawerView.getWidth() < 800 && mFilterDrawer != null && firstTime) {
                            // We want the labels opened by default ONLY
                            addLabelsToFilterDrawer(dataManager.findAllLabels());
                            firstTime = false;
                            mLabelCollapsible.withBadgeStyle(mBadgeCollapse);
                            mFilterDrawer.updateItem(mLabelCollapsible);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // If filterDrawer closed and the orderListFilter is opened, close it.
                        if (drawerView.getWidth() < 800 && mFilterDrawer != null && mExpandedOrderListFilter)
                            toggleExpandableFilters(Constants.COLLAPSIBLE_ORDER_LIST, true, true);
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }
                })
                .build();
//      REVIEW 1 de 2 - Descomentando esto tienes filterDrawer solo en el click y puedes cerrarlo a mano :D
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
//                        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                                DrawerLayout.LOCK_MODE_UNLOCKED);
//                    }
//
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
//                    }
    }

    private void setupMainDrawerListener(){

        // Setup the listener for the Main Drawer
        mMainDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                int identifier = (int) drawerItem.getIdentifier();
                switch (identifier) {
                    case Constants.DASHBOARD:
                    case Constants.SNOOZED:
                    case Constants.COMPLETED:
                    case Constants.GLANCE:
                    case Constants.PLANNER:
                        mActivity.setFragment(identifier);
                        break;

                    case Constants.ADD_TASK_LIST:
                        mainPresenter.deployLayout(Constants.ADD_TASK_LIST);
                        break;

                    case Constants.SETTINGS:
                        // Launch the Preferences Fragment
                        Intent intSettings = new Intent(mActivity.getApplicationContext(),
                                Preferences.class);
                        mActivity.startActivity(intSettings);
                        break;
                    case Constants.CONTACT:
                        // Launch intent to contact the dev via Email
                        contactByEmail();
                        break;
                    case Constants.COLLAPSIBLE_TASK_LIST:
                        // Populate or remove the Task lists
                        switchExpandableTaskListContent();
                        break;

                    default:
                        viewPagerToList(identifier);
                        break;
                }
                // Do not close the drawer at Task List Expandable click
                if (identifier != Constants.COLLAPSIBLE_TASK_LIST)
                    mMainDrawer.closeDrawer();
                return true;
            }
        });
    }

    private void viewPagerToList(int identifier){
        // Redirect to Dashboard in case the user is in other place
        mActivity.setFragment(Constants.DASHBOARD);

        // Calculate the position according to the Task List identifier.
        int index = dataManager.findTaskListPositionById(identifier);

        // Set the view pager on the correct list if there is a correct list
        if (index != -1)
            DashboardFragment.mViewPager.setCurrentItem(index, true);
    }

    private void contactByEmail(){
        Resources resources= mActivity.getResources();

        // Create a custom intent for Emails    ShareCompat.IntentBuilder not utilisable
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                resources.getString(R.string.developer_email), null));

        //Populate the fields by default
        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                resources.getString(R.string.subject_email));

        // Create a dialog only for Email clients & Avoid ActivityNotFoundException
        if (intentEmail.resolveActivity(mActivity.getPackageManager()) != null)
            mActivity.startActivity(Intent.createChooser(intentEmail,
                    resources.getString(R.string.send_email)));
    }

    private void setupMainList(RealmResults<TaskList> taskLists){

        // If there is 2 list or less, show them directly. (if only one, include "add list" button)
        if(taskLists.size() <= 2) {
            addTaskListToMainDrawer(taskLists, (taskLists.size() < 2), 1);
            return;
        }

        // Else (if there is more than 2 List of TaskList) add the collapsible and its badge
        mTaskListCollapsibleMain = new PrimaryDrawerItem()
                .withName(R.string.task_list_quick_access)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("");

        mMainDrawer.addItem(mTaskListCollapsibleMain);

        toggleTaskListExpandable(true);
    }

    private void switchExpandableTaskListContent(){
        // Get current status for the Task List and in this case switch it.
        mExpandedTaskList = !mExpandedTaskList;

        // If it opened, remove all items, if closed, populate the drawer
        if(!mExpandedTaskList) {
            toggleTaskListExpandable(true);
            removeTaskListFromMainDrawer(mTaskListIds);
        } else{
            toggleTaskListExpandable(false);
            addTaskListToMainDrawer(dataManager.findAllTaskList(), true, 2);
        }
    }

    private void toggleTaskListExpandable(boolean expand){
        // Switch the badge between expand and collapse icons
        mTaskListCollapsibleMain.withBadgeStyle( expand ? mBadgeExpand : mBadgeCollapse);
        mMainDrawer.updateItem(mTaskListCollapsibleMain);
    }

    // ------------------------ Filter Drawer ------------------------

    private void setupFilterDrawer(){

        // Create clear filter item
        int color = R.color.colorAccent;
        PrimaryDrawerItem clear = new PrimaryDrawerItem()
                .withName(R.string.clear_filter)
                .withIcon(R.drawable.ic_cancel)
                .withIconTintingEnabled(true)
                .withIconColorRes(R.color.transparent)
                .withTextColorRes(color)
                .withIdentifier(Constants.CLEAR_FILTER)
                .withSelectable(false);

        // Create main items
        color = R.color.colorAccent;
        PrimaryDrawerItem starred = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_flag)
                .withIconTintingEnabled(true)
                .withIconColorRes(color)
//                .withSelectedIconColorRes(color)
//                .withSelectedTextColorRes(color)
                .withIdentifier(Constants.STARRED)
                .withSelectable(true);
        color = R.color.amberDate;
        PrimaryDrawerItem today = new PrimaryDrawerItem()
                .withName(R.string.dueToday)
                .withIcon(R.drawable.ic_calendar_today)
                .withIconColorRes(color)
//                .withSelectedIconColorRes(color)
//                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_TODAY)
                .withSelectable(true);
        color = R.color.colorPrimary;
        PrimaryDrawerItem thisWeek = new PrimaryDrawerItem()
                .withName(R.string.dueThisWeek)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconColorRes(color)
//                .withSelectedIconColorRes(color)
//                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_THIS_WEEK)
                .withSelectable(true);

        // Create expandable and collapsible items
        mLabelCollapsible = new PrimaryDrawerItem()
                .withName(R.string.by_label)
                .withIcon(R.drawable.ic_label_outline)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_LABEL_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);
        mLocationCollapsible = new PrimaryDrawerItem()
                .withName(R.string.by_location)
                .withIcon(R.drawable.ic_map)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_LOCATION_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);
        mTaskCollapsible = new PrimaryDrawerItem()
                .withName(R.string.by_task_list)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);

        // Create expandable and collapsible item to order list
        mOrderCollapsible = new PrimaryDrawerItem()
                .withName(R.string.reorder_items)
                .withIcon(R.drawable.ic_order_list)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_ORDER_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeCollapse);

        // Create Filter Drawer
        mFilterDrawer = new DrawerBuilder()
                .withActivity(mActivity)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(false)
                .addDrawerItems(
                        clear,
                        new SectionDrawerItem().withName(R.string.main_filters).withDivider(false),
                        starred, today, thisWeek,
                        new SectionDrawerItem().withName(R.string.list_filters),
                        mLabelCollapsible, mLocationCollapsible
                        , mTaskCollapsible
                        // TODO si filtras sobre la lista es redundante, eso o lo que haces es llevar
                        // a el usuario a la lista y activar el resto de filtros... Pueeeeede ser util
                )
                .addStickyDrawerItems(
                        mOrderCollapsible
                )
                .withSelectedItem(Constants.CUSTOM_ORDER)
                .withDrawerWidthRes(R.dimen.filter_drawer_width)
                .withDrawerGravity(Gravity.END)
                .withMultiSelect(true)
                .append(mMainDrawer);

//      REVIEW 2 de 2 - descomentando esto tienes drawer solo en el click y puedes cerrarlo a mano :D
//        mFilterDrawer.getDrawerLayout().setDrawerLockMode(
//                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

//        // We want the labels opened by default
//        addLabelsToFilterDrawer(TaskController.sLabels);
//        mExpandedLabelListFilter = true;
    }

    private void setupFilterDrawerListener(){

        // Setup the listener for the Filter Drawer
        mFilterDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                int identifier = (int) drawerItem.getIdentifier();
                switch (identifier) {

                    case Constants.CLEAR_FILTER:
                        dashboardPresenter.clearFilter();
                        mFilterDrawer.deselect();
                        break;
                    case Constants.STARRED:
                    case Constants.DUE_TODAY:
                    case Constants.DUE_THIS_WEEK:
                        dashboardPresenter.filterByMain(identifier);
                        break;

                    case Constants.COLLAPSIBLE_TASK_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_TASK_LIST,
                                mExpandedTaskListFilter, false);

                        dashboardPresenter.clearGroupFilter(identifier);
                        break;
                    case Constants.COLLAPSIBLE_LABEL_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_LABEL_LIST,
                                mExpandedLabelListFilter, false);

                        dashboardPresenter.clearGroupFilter(identifier);
                        break;
                    case Constants.COLLAPSIBLE_LOCATION_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_LOCATION_LIST,
                                mExpandedLocationListFilter, false);

                        dashboardPresenter.clearGroupFilter(identifier);
                        break;
                    case Constants.COLLAPSIBLE_ORDER_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_ORDER_LIST,
                                mExpandedOrderListFilter, true);
                        break;

                    case Constants.CUSTOM_ORDER:
                    case Constants.ALPHABETICAL:
                    case Constants.DUE_DATE:
                        dashboardPresenter.reorderLists(identifier);
                        break;

                    default:
                        dashboardPresenter.filterByGrouped(identifier);
                        break;
                }
                return true;
            }
        });
    }

    private void toggleExpandableFilters(int identifier, boolean expanded, boolean footer){
        PrimaryDrawerItem toToggleCollapsible = new PrimaryDrawerItem();
        List<Integer> toRemoveItems = null;

        switch (identifier) {
            case Constants.COLLAPSIBLE_TASK_LIST:
                toRemoveItems = mTaskListIds;
                toToggleCollapsible = mTaskCollapsible;
                mExpandedTaskListFilter = !expanded;        // if(expanded) ? False : True
                if (!expanded)
                    addTaskListToFilterDrawer(dataManager.findAllTaskList());
                break;
            case Constants.COLLAPSIBLE_LABEL_LIST:
                toRemoveItems = mLabelListIds;
                toToggleCollapsible = mLabelCollapsible;
                mExpandedLabelListFilter = !expanded;       // if(expanded) ? False : True
                if (!expanded)
                    addLabelsToFilterDrawer(dataManager.findAllLabels());
                break;
            case Constants.COLLAPSIBLE_LOCATION_LIST:
                toRemoveItems = mLocationListIds;
                toToggleCollapsible = mLocationCollapsible;
                mExpandedLocationListFilter = !expanded;    // if(expanded) ? False : True
                if (!expanded)
                    addLocationsFilterToDrawer(dataManager.findAllLocations());
                break;
            case Constants.COLLAPSIBLE_ORDER_LIST:
                toRemoveItems = mOrderListIds;
                toToggleCollapsible = mOrderCollapsible;
                mExpandedOrderListFilter = !expanded;       // if(expanded) ? False : True
                if (!expanded)
                    addOrderFilterToDrawer();
                break;
        }

        if (expanded)
            removeItemListFromFilterDrawer(toRemoveItems, footer);

        // Behaves like a XNOR, when expanded is true it collapses, if footer behaves opposite
        toToggleCollapsible.withBadgeStyle(expanded == footer ? mBadgeCollapse : mBadgeExpand);

        if(footer)
            mFilterDrawer.updateStickyFooterItem(toToggleCollapsible);
        else
            mFilterDrawer.updateItem(toToggleCollapsible);
    }

    // ------------------ Remove Items From Drawer -------------------

    private void removeItemListFromFilterDrawer(List<Integer> mItemListIds, boolean sticky){
        for (int i = 0; i < mItemListIds.size(); i++) {
            if (sticky)
                mFilterDrawer.removeStickyFooterItemAtPosition(0);
            else
                mFilterDrawer.removeItem(mItemListIds.get(i));
        }
    }

    private void removeTaskListFromMainDrawer(List<Integer> mItemListIds){
        for (int i = 0; i < mItemListIds.size(); i++)
            mMainDrawer.removeItem(mItemListIds.get(i));
    }

    // --------------------- Add Items To Drawer ---------------------

    private void addTaskListToFilterDrawer(RealmResults<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mFilterDrawer,
                false, R.drawable.ic_stop, Constants.COLLAPSIBLE_TASK_LIST, 2);
    }
    private void addTaskListToMainDrawer(RealmResults<TaskList> listTaskList, boolean add, int level){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mMainDrawer,
                add, R.drawable.ic_stop,
                (level==1) ? Constants.LIST_SEPARATOR : Constants.COLLAPSIBLE_TASK_LIST, level);
    }

    /**
     * This method updates both filter and main Drawer in order to reflect changes such as
     * a new list or label being added.
     *
     * @param uniqueId  Refers to the ID on the drawer
     */
    @SuppressWarnings("all")
    // mExpandedLabelListFilter and mExpandedTaskListFilter change dinamically.
    public void includeTheNew(int uniqueId){

        switch (uniqueId){
            case Constants.COLLAPSIBLE_LABEL_LIST:
                // If labels are not expanded but set to (first time) or labels closed, exit method
                if(firstTime || !mExpandedLabelListFilter)
                    return;

                // If the label list is expanded, close it
                toggleExpandableFilters(uniqueId, mExpandedLabelListFilter, false);
                break;

            case Constants.COLLAPSIBLE_TASK_LIST:

                // On the main drawer, if the TaskList list is opened, close it
                if(mExpandedTaskList)
                    switchExpandableTaskListContent();

                // On the filter drawer, if the TaskList list is closed, exit method
                if(!mExpandedTaskListFilter)
                    return;

                // If the TaskList list is expanded, close it
                toggleExpandableFilters(uniqueId, mExpandedTaskListFilter, false);
                break;
        }

        // This expands again showing the changes
        toggleExpandableFilters(uniqueId, false, false);
    }

    private void addLabelsToFilterDrawer(RealmResults<Label> listLabels){
        addItemListToDrawer(new ArrayList<BasicType>(listLabels), mFilterDrawer,
                false, R.drawable.ic_label_filled, Constants.COLLAPSIBLE_LABEL_LIST, 2);
    }
    private void addLocationsFilterToDrawer(RealmResults<Location> favLocations){
        addItemListToDrawer(new ArrayList<BasicType>(favLocations), mFilterDrawer,
                false, R.drawable.ic_place, Constants.COLLAPSIBLE_LOCATION_LIST, 2);
    }

    private void addItemListToDrawer(ArrayList<BasicType> itemLists, final Drawer drawer,
                                     boolean main, int iconRes, int identifier, int level){
        List<Integer>addedIds = new ArrayList<>();

        // Get the position for the item in the drawer, in order to add its children (+1)
        Integer position = drawer.getPosition(identifier) +1;

        // Retrieve the color only once, or use the default
        int color = R.color.secondaryText;
        if (identifier == Constants.COLLAPSIBLE_LOCATION_LIST)
            color = R.color.tealLocation;
        else if(identifier == Constants.COLLAPSIBLE_TASK_LIST && main)
            color = R.color.colorPrimary;

        boolean isLabel = identifier == Constants.COLLAPSIBLE_LABEL_LIST;

        // Add the Task Lists to the drawer by order at the right position
        for (int i = 0; i < itemLists.size(); i++) {

            // In case of a Label we customize the color according to each label
            if (isLabel)
                color = ((Label) itemLists.get(i)).getColorRes();

            // Dynamic items on main are not selectable
            boolean selectable = !main;

            // Construct the Item to add on the Drawer
            IDrawerItem itemToAdd = new SecondaryDrawerItem()
                    .withIdentifier((int) itemLists.get(i).getId())
                    .withLevel(level)
                    .withName(itemLists.get(i).getTitle())
                    .withIcon(iconRes)
                    .withIconColorRes(color)
                    .withIconTintingEnabled(true)
                    .withSelectable(selectable);
            drawer.addItemAtPosition(itemToAdd, position);

            // Move the pointer for an ordered insertion and save the id for posterior deletion
            position++;
            addedIds.add((int) itemLists.get(i).getId());
        }

        // If on the main drawer, add a new "add Task List" item for convenience and its ID
        if(main) {
            mMainDrawer.addItem(new SecondaryDrawerItem().withIdentifier(Constants.ADD_TASK_LIST)
                    .withLevel(level)
                    .withName(R.string.addList)
                    .withIcon(R.drawable.ic_add)
                    .withIconTintingEnabled(true)
                    .withSelectable(false));
            addedIds.add(Constants.ADD_TASK_LIST);
        }

        // Save the id to control the Navigation Drawer more properly
        switch (identifier){
            case Constants.COLLAPSIBLE_TASK_LIST:
                mTaskListIds = addedIds;
                break;
            case Constants.COLLAPSIBLE_LABEL_LIST:
                mLabelListIds = addedIds;
                break;
            case Constants.COLLAPSIBLE_LOCATION_LIST:
                mLocationListIds = addedIds;
                break;
        }

        // TODO OnClick in "expandable" scroll to the header of the list
//        drawer.getRecyclerView().scrollToPosition(6);
//        drawer.getRecyclerView().getLayoutManager().scrollToPosition(identifier);    //   ESTO DABA EL APAÑO, pero no funciona para By task list
//        drawer.getRecyclerView().getChildAdapterPosition(mLabelCollapsible.generateView(getApplication()));
//        drawer.getRecyclerView().
//        drawer.getRecyclerView().getLayoutManager().sc
    }

    private void addOrderFilterToDrawer() {
        List<Integer>addedIds = new ArrayList<>();

        int idAlphabetical = Constants.ALPHABETICAL;
        int idDueDate = Constants.DUE_DATE;
        int idCustomOrder = Constants.CUSTOM_ORDER;

        // Create order items
        PrimaryDrawerItem alphabetical = new PrimaryDrawerItem()
                .withName(R.string.alphabetically)
                .withIcon(R.drawable.ic_order_alphabetical)
                .withIconTintingEnabled(true)
                .withIdentifier(idAlphabetical)
                .withSelectable(false);
        PrimaryDrawerItem dueDate = new PrimaryDrawerItem()
                .withName(R.string.due_date)
                .withIcon(R.drawable.ic_time_alarm)
                .withIconTintingEnabled(true)
                .withIdentifier(idDueDate)
                .withSelectable(false);
        PrimaryDrawerItem customOrder = new PrimaryDrawerItem()
                .withName(R.string.custom_order)
                .withIcon(R.drawable.ic_menu)
                .withIconTintingEnabled(true)
                .withIdentifier(idCustomOrder)
                .withSelectable(false);

        // Get the position for the item in the drawer, in order to add its children (+1)
        Integer position = mFilterDrawer.getPosition(Constants.COLLAPSIBLE_ORDER_LIST)+1;

        // Add the Task Lists to the drawer by order
        mFilterDrawer.addStickyFooterItemAtPosition(customOrder, position);
        mFilterDrawer.addStickyFooterItemAtPosition(dueDate, position);
        mFilterDrawer.addStickyFooterItemAtPosition(alphabetical, position);

        // Add the identifiers in a list to safely delete them
        addedIds.add(idAlphabetical);
        addedIds.add(idDueDate);
        addedIds.add(idCustomOrder);
        mOrderListIds = addedIds;
    }

    public void blockDrawers(boolean toggle) {
        int mode = toggle ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;

        mFilterDrawer.getDrawerLayout().setDrawerLockMode(mode);
        mMainDrawer.getDrawerLayout().setDrawerLockMode(mode);
    }

    public void blockFilterDrawer(){
        mFilterDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void customizeActionBar(int drawer_item_id) {
        ActionBar actionBar = mActivity.getSupportActionBar();
        if(actionBar == null)
            return;

        // Retrieve the toolbar in case it's the first time
        if(mTabLayout == null)
            mTabLayout = (TabLayout) mActivity.findViewById(R.id.tabs);

        int title = R.string.blank;

        switch (drawer_item_id){
            case Constants.DASHBOARD:
                mTabLayout.setVisibility(View.VISIBLE);

                actionBar.setDisplayHomeAsUpEnabled(false);
                mMainDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

                title = R.string.dashboard;
                break;
            case Constants.ADD_TASK:
                mTabLayout.setVisibility(View.GONE);

                mMainDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel);
                actionBar.setDisplayHomeAsUpEnabled(true);

                break;
            case Constants.SNOOZED:

                title = R.string.snoozed;
                break;
            case Constants.COMPLETED:

                title = R.string.completed;
                break;
            case Constants.GLANCE:

                title = R.string.glance;
                break;
//            case Constants.PLANNER:       // Planner es completamente diferente
//
//                title = R.string.planner;
//                break;
        }
        actionBar.invalidateOptionsMenu();
        actionBar.setTitle(title);
    }
}