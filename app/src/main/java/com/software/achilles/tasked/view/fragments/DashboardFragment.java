package com.software.achilles.tasked.view.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.view.adapters.PagerAdapter;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.util.Constants;
import javax.inject.Inject;
import javax.inject.Provider;
import io.realm.RealmResults;

public class DashboardFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    public static ViewPager mViewPager;

    // -------------------------- Injected ---------------------------

    @Inject
    DashboardPresenter dashboardPresenter;
    @Inject
    Provider<DashboardListFragment> dashboardListFragmentProvider;
    
    // ------------------------- Constructor -------------------------

    @Inject
    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        return inflater.inflate(R.layout.dashboard_viewpager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize presenter     // FIXME Testing lazy to solve the null pointer
        dashboardPresenter.attachView(this);

        // Setup the fragment composing the ViewPager and the Tabs to control it
        dashboardPresenter.setupLayout();
    }

    //    @Override
//    public void onDestroy() {
//        DashboardPresenter.destroyPresenter();
//        super.onDestroy();
//    }

    private PagerAdapter adapter;

    // -------------------------- View Pager -------------------------

    public void setupViewPager(RealmResults<TaskList> taskLists) {
        MainActivity mMainActivity = ((MainActivity) getActivity());
        mViewPager = (ViewPager) mMainActivity.findViewById(R.id.viewpager);

        adapter = new PagerAdapter(getChildFragmentManager());

        // FIXME 1 de 2 - Quick jump to the desired list if too many lists present (necessary?)
//        if(taskLists.size() > 5)
//            adapter.addFragment(new DashboardSearchFragment(), "Search");

        // Populate each of the pages of the ViewPager
        for (int index = 0 ; index < taskLists.size(); index++){
            // Pick the fragment the page is going to show
            DashboardListFragment dashboardListFragment = dashboardListFragmentProvider.get();

            // Introduce the TaskList corresponding to that fragment
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TASK_LIST+"", index);
            dashboardListFragment.setArguments(bundle);

            // Add the fragment and it's bundle to the adapter
            adapter.addFragment(dashboardListFragment, taskLists.get(index).getTitle());
        }
        mViewPager.setAdapter(adapter);
        // FIXME 2 de 2 - Quick jump to the desired list if too many lists present (necessary?)
//        mViewPager.setCurrentItem(1);     // show the first list by default, not the quick search
    }

    // -------------------------- Tab Layout -------------------------

    public void setupTabLayout(int taskListsSize) {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);

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

    // --------------------------- USE CASES -------------------------

    public void notifyItemChange(){
        ((DashboardListFragment) adapter.getItem(mViewPager.getCurrentItem())).notifyChange();
    }

    public void reorderLists(int identifier){
        int size = adapter.getCount();

        for (int i = 0; i < size; i++)
            ((DashboardListFragment) adapter.getItem(i)).changeSortMode(identifier, true);
    }

    // --------------------------- FILTERING -------------------------

    public void filterAllLists(){
        int position = mViewPager.getCurrentItem();
        ((DashboardListFragment) adapter.getItem(position)).notifyFilter(position);
        if(position > 0)
            ((DashboardListFragment) adapter.getItem(position - 1)).notifyFilter(position);
        if(position < adapter.getCount()-1)
            ((DashboardListFragment) adapter.getItem(position + 1)).notifyFilter(position);
    }
}
