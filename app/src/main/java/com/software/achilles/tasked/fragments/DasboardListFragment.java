package com.software.achilles.tasked.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DasboardListFragment extends ListFragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

//    private ArrayAdapter<Task> adapter;
//    private ArrayList<Task> tasks;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> tasks;

    // ------------------------- Constructor -------------------------

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

//        tasks = TaskController.getInstance().getTasks();

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
//        Intent intent = new Intent(getActivity(), TaskActivity.class);
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
