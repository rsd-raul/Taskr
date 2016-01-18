package com.software.achilles.tasked.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

public class DashboardPagerFragment extends Fragment {


    public ViewPager mViewPager;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard, container, false);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            mActivity = (MainActivity) context;
        }else{
            Log.e("Tasked", "Problemas en DashboadrPagerFragment");
        }

        // Setup the fragment composing the ViewPager
        mViewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(TaskController.sTaskLists);



        // Setup tabs for Dashboard and make Scrollable
        mActivity.tabLayout.setupWithViewPager(mViewPager);
        mActivity.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    // -------------------------- View Pager -------------------------

    private void setupViewPager(ArrayList<TaskList> taskLists) {
        Adapter adapter = new Adapter(mActivity.getSupportFragmentManager());

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

}
