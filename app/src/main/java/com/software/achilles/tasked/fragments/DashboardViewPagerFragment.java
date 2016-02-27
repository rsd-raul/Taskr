package com.software.achilles.tasked.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.achilles.tasked.MainActivity;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.adapters.Adapter;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;

public class DashboardViewPagerFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    public ViewPager mViewPager;
    private MainActivity mMainActivity;

    // ------------------------- Constructor -------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        return inflater.inflate(R.layout.dashboard_viewpager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize MainActivity
        mMainActivity = ((MainActivity) getActivity());

        // Setup the fragment composing the ViewPager and the Tabs to control it - NEW THREAD
//        threadManager(new Runnable() {
//            public void run() {
                setupViewPager(TaskController.sTaskLists);
                setupTabLayout(TaskController.sTaskLists.size());
//            }
//        });
    }

    // TODO Investigar sobre threads y como manejarlos, sigue dando "Skipped X frames!"
    private void threadManager(Runnable runnable){
        // Launch new thread
        new Thread(runnable).start();
    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ArrayList<TaskList> taskLists) {
        mViewPager = (ViewPager) mMainActivity.findViewById(R.id.viewpager);
        mMainActivity.mViewPager = mViewPager;
        Adapter adapter = new Adapter(getActivity().getSupportFragmentManager());

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
}
