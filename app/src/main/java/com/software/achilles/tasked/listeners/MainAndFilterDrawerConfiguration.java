package com.software.achilles.tasked.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.software.achilles.tasked.MainActivity;
import com.software.achilles.tasked.Preferences;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.BasicType;
import com.software.achilles.tasked.domain.FavoriteLocation;
import com.software.achilles.tasked.domain.Label;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainAndFilterDrawerConfiguration {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private Activity mActivity;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private AccountHeader mAccountHeader;
    public Drawer mMainDrawer, mFilterDrawer;
    private List<Integer> mTaskListIds, mLabelListIds, mLocationListIds, mOrderListIds;
    private PrimaryDrawerItem mTaskListCollapsableMain;
    private BadgeStyle mBadgeExpand, mBadgeCollapse;
    private boolean firstTime = true;
    private boolean mExpandedTaskList = false;
    private boolean mExpandedTaskListFilter = false;
    private boolean mExpandedLabelListFilter = true;
    private boolean mExpandedLocationListFilter = false;
    private boolean mExpandedOrderListFilter = false;
    private PrimaryDrawerItem mTaskCollapsable, mLabelCollapsable,
            mLocationCollapsable, mOrderCollapsable;

    // ------------------------- Constructor -------------------------

    public MainAndFilterDrawerConfiguration(MainActivity activity, boolean filter) {
        this.mActivity = activity;

        // Set ActionBar
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);

        // Setup Main Drawer and its behaviour
        initializeBadges();
        setupProfileHeader();
        setupMainDrawer();
        setupMainDrawerListener();
        setupMainList(TaskController.sTaskLists);

        if(!filter)
            return;

        // ------------------------ Dashboard Only -----------------------

        mViewPager = (ViewPager) mActivity.findViewById(R.id.viewpager);

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
        PrimaryDrawerItem dashboard = new PrimaryDrawerItem().withIdentifier(Constants.DASHBOARD)
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

        // Create Filter Drawer
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
//                        , mTaskListCollapsableMain
                )
                .addStickyDrawerItems(
                        settings, contact
                )
//      TODO 1 de 2 - Descomentando esto tienes drawer solo en el click y puedes cerrarlo a mano :D
//                .withOnDrawerListener(new Drawer.OnDrawerListener() {
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
//
//                    @Override
//                    public void onDrawerSlide(View drawerView, float slideOffset) {
//                    }
//                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                        // drawerViews.getWidth():
                            // MainDrawer = 912
                            // FilterDrawer = 750

                        // TRUE is the opened one is filterDrawer
                        if (drawerView.getWidth() < 800 && mFilterDrawer != null && firstTime) {
                                // We want the labels opened by default ONLY
                                addLabelsToFilterDrawer(TaskController.sLabels);
                                firstTime = false;
                                mLabelCollapsable.withBadgeStyle(mBadgeCollapse);
                                mFilterDrawer.updateItem(mLabelCollapsable);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // TRUE is the opened one is filterDrawer
                        if (drawerView.getWidth() < 800 && mFilterDrawer != null && mExpandedOrderListFilter)
                            toggleExpandableFilters(Constants.COLLAPSIBLE_ORDER_LIST, true, true);
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) { }
                })
                .build();
    }

    private void setupMainDrawerListener(){

        // Setup the listener for the Main Drawer
        mMainDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
                        Intent intSettings = new Intent(mActivity.getApplicationContext(),
                                Preferences.class);
                        mActivity.startActivity(intSettings);
                        break;

                    case Constants.CONTACT:
                        contactByEmail();
                        break;

                    case Constants.COLLAPSIBLE_TASK_LIST:
                        // Populate or remove the Task lists
                        switchExpandableTaskListContent();
                        break;

                    case Constants.ADD_TASK_LIST:
                        break;

                    default:
                        // Calculate the position according to the Task List identifier.
                        int index = TaskController.getTaskListPositionById(identifier);

                        // Set the view pager on the correct list
                        if (index != -1)
                            mViewPager.setCurrentItem(index, true);
                        break;
                }
                // Do not close the drawer at Task List click
                if (identifier != Constants.COLLAPSIBLE_TASK_LIST)
                    mMainDrawer.closeDrawer();
                return true;
            }
        });
    }

    private void contactByEmail(){
        Resources resources= mActivity.getResources();

        // Create a custom intent for Emails
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                resources.getString(R.string.mailto),
                resources.getString(R.string.developer_email), null));

        //Populate the fields by default
        intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                resources.getString(R.string.subject_email));

        // Create a dialog only for Email clients
        if (intentEmail.resolveActivity(mActivity.getPackageManager()) != null)
            mActivity.startActivity(Intent.createChooser(intentEmail,
                    resources.getString(R.string.send_email)));
    }

    private void setupMainList(ArrayList<TaskList> taskLists){

        // If there is more than 2 List of TaskList add the collapsable and its badge
        if(taskLists.size() > 2) {
            // Create expandable and collapsible item
            mTaskListCollapsableMain = new PrimaryDrawerItem()
                    .withName(R.string.task_list_quick_access)
                    .withIcon(R.drawable.ic_list_bullet)
                    .withIconTintingEnabled(true)
                    .withIdentifier(Constants.COLLAPSIBLE_TASK_LIST)
                    .withSelectable(false)
                    .withBadge("");

            mMainDrawer.addItem(mTaskListCollapsableMain);

            toggleTaskListExpandable(true);

        // If there is 2 list or less, show them directly. (if only one, include "add list" button)
        } else
            addTaskListToMainDrawer(taskLists, (taskLists.size() < 2), 1);
    }

    private void switchExpandableTaskListContent(){
        // Get current status for the Task List and in this case switch it.
        Boolean status = mExpandedTaskList;
        mExpandedTaskList = !mExpandedTaskList;

        // If it opened, remove all items, if closed, populate the drawer
        if(status) {
            toggleTaskListExpandable(true);
            removeTaskListFromMainDrawer(mTaskListIds);
        } else{
            toggleTaskListExpandable(false);
            addTaskListToMainDrawer(TaskController.sTaskLists, true, 2);
        }
    }

    private void toggleTaskListExpandable(boolean expand){
        // Switch the badge between expand and collapse icons
        mTaskListCollapsableMain.withBadgeStyle( expand ? mBadgeExpand : mBadgeCollapse);
        mMainDrawer.updateItem(mTaskListCollapsableMain);
    }

    // ------------------------ Filter Drawer ------------------------

    private void setupFilterDrawer(){

        // Create clear filter item
        int color = R.color.colorAccent;
        PrimaryDrawerItem clear = new PrimaryDrawerItem()
                .withName(R.string.clear_filter)
                .withSelectedTextColorRes(color)
                .withIdentifier(Constants.CLEAR)
                .withSelectable(true);

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
                .withSelectable(false);
        color = R.color.amberDate;
        PrimaryDrawerItem today = new PrimaryDrawerItem()
                .withName(R.string.dueToday)
                .withIcon(R.drawable.ic_calendar_today)
                .withIconColorRes(color)
//                .withSelectedIconColorRes(color)
//                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_TODAY)
                .withSelectable(false);
        color = R.color.colorPrimary;
        PrimaryDrawerItem thisWeek = new PrimaryDrawerItem()
                .withName(R.string.dueThisWeek)
                .withIcon(R.drawable.ic_calendar_list)
                .withIconColorRes(color)
//                .withSelectedIconColorRes(color)
//                .withSelectedTextColorRes(color)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.DUE_THIS_WEEK)
                .withSelectable(false);

        // Create expandable and collapsable items
        mTaskCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_task_list)
                .withIcon(R.drawable.ic_list_bullet)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_TASK_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);
        mLabelCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_label)
                .withIcon(R.drawable.ic_label_outline)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_LABEL_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);
        mLocationCollapsable = new PrimaryDrawerItem()
                .withName(R.string.by_location)
                .withIcon(R.drawable.ic_map)
                .withIconTintingEnabled(true)
                .withIdentifier(Constants.COLLAPSIBLE_LOCATION_LIST)
                .withSelectable(false)
                .withBadge("")
                .withBadgeStyle(mBadgeExpand);

        // Create expandable and collapsible item to order list
        mOrderCollapsable = new PrimaryDrawerItem()
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
                        mLabelCollapsable, mLocationCollapsable
                        , mTaskCollapsable    // TODO si filtras sobre la lista es redundante
                )
                .addStickyDrawerItems(
                        mOrderCollapsable
                )
                .withDrawerWidthRes(R.dimen.filter_drawer_width)
                .withDrawerGravity(Gravity.END)
                .append(mMainDrawer);

//      TODO 2 de 2 - descomentando esto tienes drawer solo en el click y puedes cerrarlo a mano :D
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

                switch (drawerItem.getIdentifier()) {

                    case Constants.STARRED:
                        break;
                    case Constants.DUE_TODAY:
                        break;
                    case Constants.DUE_THIS_WEEK:
                        break;

                    case Constants.COLLAPSIBLE_TASK_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_TASK_LIST,
                                mExpandedTaskListFilter, false);
                        break;
                    case Constants.COLLAPSIBLE_LABEL_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_LABEL_LIST,
                                mExpandedLabelListFilter, false);
                        break;
                    case Constants.COLLAPSIBLE_LOCATION_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_LOCATION_LIST,
                                mExpandedLocationListFilter, false);
                        break;
                    case Constants.COLLAPSIBLE_ORDER_LIST:
                        toggleExpandableFilters(Constants.COLLAPSIBLE_ORDER_LIST,
                                mExpandedOrderListFilter, true);
                        break;

                    default:
//                        int index = TaskController.getTaskListPositionById(identifier);
//                        if (index != -1)
//                            mViewPager.setCurrentItem(index, true);
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
                toToggleCollapsible = mTaskCollapsable;
                mExpandedTaskListFilter = !expanded;        // if(expanded) ? False : True
                if (!expanded)
                    addTaskListToFilterDrawer(TaskController.sTaskLists);
                break;
            case Constants.COLLAPSIBLE_LABEL_LIST:
                toRemoveItems = mLabelListIds;
                toToggleCollapsible = mLabelCollapsable;
                mExpandedLabelListFilter = !expanded;       // if(expanded) ? False : True
                if (!expanded)
                    addLabelsToFilterDrawer(TaskController.sLabels);
                break;
            case Constants.COLLAPSIBLE_LOCATION_LIST:
                toRemoveItems = mLocationListIds;
                toToggleCollapsible = mLocationCollapsable;
                mExpandedLocationListFilter = !expanded;    // if(expanded) ? False : True
                if (!expanded)
                    addLocationsFilterToDrawer(TaskController.sFavouriteLocations);
                break;
            case Constants.COLLAPSIBLE_ORDER_LIST:
                toRemoveItems = mOrderListIds;
                toToggleCollapsible = mOrderCollapsable;
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

    private void addTaskListToFilterDrawer(ArrayList<TaskList> listTaskList){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mFilterDrawer,
                false, R.drawable.ic_stop, Constants.COLLAPSIBLE_TASK_LIST, 2);
    }
    private void addTaskListToMainDrawer(ArrayList<TaskList> listTaskList, boolean add, int level){
        addItemListToDrawer(new ArrayList<BasicType>(listTaskList), mMainDrawer,
                add, R.drawable.ic_stop,
                (level==1) ? Constants.LIST_SEPARATOR : Constants.COLLAPSIBLE_TASK_LIST, level);
    }
    private void addLabelsToFilterDrawer(ArrayList<Label> listLabels){
        addItemListToDrawer(new ArrayList<BasicType>(listLabels), mFilterDrawer,
                false, R.drawable.ic_label_filled, Constants.COLLAPSIBLE_LABEL_LIST, 2);
    }
    private void addLocationsFilterToDrawer(ArrayList<FavoriteLocation> favLocations){
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
        switch (identifier){
            case Constants.COLLAPSIBLE_LOCATION_LIST:
                color = R.color.tealLocation;
                break;
            case Constants.COLLAPSIBLE_TASK_LIST:
                color = (main) ? R.color.secondaryText : R.color.colorPrimary;
                break;
        }

        // Add the Task Lists to the drawer by order at the right position
        for (int i = 0; i < itemLists.size(); i++) {

            // In case of a Label we customize the color according to each label
            if (identifier == Constants.COLLAPSIBLE_LABEL_LIST)
                    color = ((Label) itemLists.get(i)).getColorRes();

            // Construct the Item to add on the Drawer
            IDrawerItem itemToAdd = new SecondaryDrawerItem()
                    .withIdentifier(itemLists.get(i).getId())
                    .withLevel(level)
                    .withName(itemLists.get(i).getTitle())
                    .withIcon(iconRes)
                    .withIconColorRes(color)
                    .withIconTintingEnabled(true)
                    .withSelectable(false);
            drawer.addItemAtPosition(itemToAdd, position);

            // Move the pointer for an ordered insertion and save the id for posterior deletion
            position++;
            addedIds.add(itemLists.get(i).getId());
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
//        drawer.getRecyclerView().getChildAdapterPosition(mLabelCollapsable.generateView(getApplication()));
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
}