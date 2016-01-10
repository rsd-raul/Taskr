package com.software.achilles.tasked.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.domain.Task;

import java.util.ArrayList;

public class DashboardListFragment extends ListFragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private ArrayAdapter<Task> adapter;
    private ArrayList<Task> tasks;

    // ------------------------- Constructor -------------------------


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        tasks = TaskController.getInstance().getTasks();
        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tasks);
        setListAdapter(this.adapter);
    }

    // -------------------------- Use Cases --------------------------

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
//
//        if(TaskController.firstTime)
//            return;
//
//        //  Intent creation
//        Intent intent = new Intent(getActivity(), TaskEdit.class);
//
//        //  Retrieve and save data in the bundle
//        Task selectedTask = tasks.get(position);
//        intent.putExtra("selectedTask", selectedTask);
//        intent.putExtra("selectedPosition", position);
//
//        //  Start activity
//        startActivity(intent);
    }

    public void refreshList(){
        this.adapter.notifyDataSetChanged();
    }
}
