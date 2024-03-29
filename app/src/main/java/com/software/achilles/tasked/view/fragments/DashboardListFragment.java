package com.software.achilles.tasked.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.helpers.PreferencesHelper;
import com.software.achilles.tasked.view.adapters.TaskFAItem;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

public class DashboardListFragment extends Fragment {

    @Inject
    DataManager dataManager;
    @Inject
    FastItemAdapter<IItem> fastAdapter;
    @Inject
    Provider<TaskFAItem> taskAdapterProvider;
    @Inject
    DashboardPresenter dashboardPresenter;

    // ------------------------- Constructor -------------------------

    @Inject
    public DashboardListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState) {

        // Retrieve the recycler view and set the manager
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Configure the FastAdapter and set it on the RecyclerView
        recyclerView.setAdapter(fastAdapter);

        // Sort the list based on user preference
        changeSortMode(PreferencesHelper.getShaPrefInt(this.getContext(), PreferencesHelper.Keys.ORDER, PreferencesHelper.Value.ORDER, true), false);

        // Retrieve the TaskList index from the Activity and obtain its tasks
        int posOnPager = getArguments().getInt(Constants.TASK_LIST + "");

        needsChild = (posOnPager == Constants.COMPLETED_FILTER ||
                posOnPager == Constants.SNOOZED_FILTER);
        dashboardPresenter.attachChildView(needsChild ? this : null);

        // Populate our list
        populateAdapter(dashboardPresenter.getFilteredTasksByTaskListPosition(posOnPager));

        return recyclerView;
    }

    public static boolean needsChild;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    public void populateAdapter(List<Task> tasks){
        // REVIEW Is this the proper way to restart the adapter to remove duplicates?
//        fastAdapter.removeItemRange(0, fastAdapter.getItemCount());
        fastAdapter.clear();

        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            TaskFAItem item = taskAdapterProvider.get().withTask(tasks.get(i));

            // Control the extra margins
            if(i == 0)
                item.withFirstOrLast(Constants.FIRST);
            else if(i == size-1)
                item.withFirstOrLast(Constants.LAST);

            fastAdapter.add(item);
        }
    }

    // -------------------------- USE CASE ---------------------------

    public void notifyChange(){
        fastAdapter.notifyAdapterDataSetChanged();
    }

    public void notifyFilter(int position){
        populateAdapter(dashboardPresenter.getFilteredTasksByTaskListPosition(position));
    }

    // --------------------------- SORTING ---------------------------

    private @SortingStrategy int sortingStrategy;

    public void changeSortMode(int sortingStrategy, boolean sortNow){

        try{
            PreferencesHelper.setShaPrefInteger(this.getContext(), PreferencesHelper.Keys.ORDER, sortingStrategy, true);
        }catch (Exception e){ /* Nothing to see here */ }

        this.sortingStrategy = sortingStrategy;
        fastAdapter.getItemAdapter().withComparator(getComparator(), sortNow);
    }

    @Nullable
    private Comparator<IItem> getComparator() {
        switch (sortingStrategy) {
            case Constants.CUSTOM_ORDER:
                return new CustomOrderComparatorAscending();
            case Constants.ALPHABETICAL:
                return new AlphabetComparatorAscending();
            case Constants.DUE_DATE:
                return new DueDateComparatorAscending();
            case Constants.NONE:
                return null;
            default:
                throw new RuntimeException("This sortingStrategy is not supported.");
        }
    }

    private class AlphabetComparatorAscending implements Comparator<IItem>, Serializable {
        @Override
        public int compare(IItem lhs, IItem rhs) {
            return ((TaskFAItem)lhs).getTitle().compareTo(((TaskFAItem)rhs).getTitle());
        }
    }
    private class DueDateComparatorAscending implements Comparator<IItem>, Serializable {
        @Override
        public int compare(IItem lhs, IItem rhs) {
            return ((TaskFAItem)lhs).getSortDueDate().compareTo(((TaskFAItem)rhs).getSortDueDate());
        }
    }
    private class CustomOrderComparatorAscending implements Comparator<IItem>, Serializable {
        @Override
        public int compare(IItem lhs, IItem rhs) {
            return Long.compare(lhs.getIdentifier(), rhs.getIdentifier());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Constants.CUSTOM_ORDER, Constants.ALPHABETICAL, Constants.DUE_DATE})
    public @interface SortingStrategy { }
}