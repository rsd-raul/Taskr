package com.software.achilles.tasked.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.controllers.TaskController;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    Activity mActivity;
    Bundle mBundle;

    // ------------------------- Constructor -------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.task_creation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
        mBundle = getArguments();

        // Initializing Buttons and Spinner (dialog)
        initializeSpinner();
//        initializeButtons();
    }


    private void initializeSpinner(){
        ArrayList<String> taskListsString = TaskController.getTaskListTitles();

        // Initialize the spinner name and populate the dialog with the possibilities
        Spinner spinner = (Spinner) mActivity.findViewById(R.id.spinner_task_lists);

        String defaultList = mBundle.getString(Constants.CURRENT_LIST + "", "Pick list");
        //TODO enviar al desplegar fragment y recibir de este lado
        // tal vez enviar la posicion en el viewpager y aqui rescatar el texto es más eficiente
        // a fin de cuentas ya tienes taskListString

        ArrayAdapter<String> taskListTitleAdapter = new ArrayAdapter<>
                (mActivity, android.R.layout.simple_dropdown_item_1line, taskListsString);

        spinner.setAdapter(taskListTitleAdapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
//              position corresponde 1:1 con TaskController.sTaskList, tener posicion =>lista
            }
        });
    }

    //TODO onClick para todos los botnes del fragment de create
    public void myClickMethod(View v) {
        switch(v.getId()) {
            case R.id.button_description:
                // Add place for interface
                // RETRIEVE and store information at "Save" like the Titlle.
                break;
            case R.id.button_label:
                // Launch dialog (multiple choice)
                // Store information
                // Reflect changes
                break;
            case R.id.button_location:
                // Launch picker
                // Store information
                // Reflect changes
                break;
            case R.id.button_time:
                // Launch picker/pickers
                // Store information
                // Reflect changes
                break;
            case R.id.checkbox_favourite:
                // Store information
                break;
        }
    }


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
