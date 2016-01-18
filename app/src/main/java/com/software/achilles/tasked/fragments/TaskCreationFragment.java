package com.software.achilles.tasked.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.software.achilles.tasked.R;

import java.util.Date;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    // ------------------------- Constructor -------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.task_creation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Time button initializing
//        Button taskTimeBT = (Button) getView().findViewById(R.id.time);
//        taskTimeBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showTimePickerDialog();
//            }
//        });

//        // On lollipop assign a margin to separate the view from the ActionBar
//        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP)
//            return;
//
//        RelativeLayout relative = (RelativeLayout) getActivity().findViewById(R.id.relative);
//        MarginLayoutParams params = (MarginLayoutParams) relative.getLayoutParams();
//        params.setMargins(0, 50, 0, 0);

    }

    // ------------------------ Time & Date --------------------------

    private void showTimePickerDialog() {

//        // Creation of the timePicker
//        DialogFragment newFragment =  new TimePickerFragment();
//
//        if(TaskController.taskDate != null) {
//            // Saving the time in a bundle
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("date", TaskController.taskDate);
//
//            // Sending the bundle to the fragment
//            newFragment.setArguments(bundle);
//        }
//
//        // Showing the picker
//        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static void dateToButton(Activity activity){
//        Button taskTimeBT = (Button) activity.findViewById(R.id.time);
//        Date dueDate = TaskController.taskDate;
//
//        if(dueDate!=null) {
//            taskTimeBT.setText(Task.dateToText(dueDate));
//        }else
//            taskTimeBT.setText(R.string.setDate);
    }

}
