package com.software.achilles.tasked.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.R;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private MainActivity mMainActivity;
    private TaskCreationPresenter mPresenter;

    // ------------------------- Constructor -------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        return inflater.inflate(R.layout.task_creation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize MainActivity
        mMainActivity = ((MainActivity) getActivity());

        // Initialize presenter
        mPresenter = TaskCreationPresenter.getInstance().attachView(this);

//        // Time button initializing
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

    @Override
    public void onDestroy() {
        mPresenter.destroyPresenter();
        super.onDestroy();
    }

    public boolean isDataPresent(){
        // TODO si el usuario a introducido algun dato => TRUE, de lo contrario => FALSE

        return true;
    }

    public void resetFields(){
        // TODO esto es llamado cuando se despliega el layout tras borrarse, basicamente, todo a 0
    }

    // ------------------------ Time & Date --------------------------

    // TODO Show information back to the user and also persist that information
    public void taskCustomization(View view){
        switch (view.getId()){
//            case R.id.button_close:
//                ((MainActivity)getActivity()).removeAddTask();
//                break;
            case R.id.button_description:
                // Deploy description
                break;
            case R.id.button_time:
                // Show picker, then deploy result if any
                break;
            case R.id.button_location:
                // Show picker, then deploy result if any
                break;
            case R.id.button_label:
                // Show picker, then deploy result if any
                break;
            case R.id.checkbox_favourite:
                break;
        }
    }

    private void dialogForTaskRemoval(){

        // If the user haven't typed anything, close the interface
        if(!isDataPresent()){
            mMainActivity.removeAddTask();
            return;
        }

        // Else, ask for confirmation
        Dialog dialog = new AlertDialog.Builder(mMainActivity)
                // For simple dialogs we don't use a Title (Google Guidelines)
                .setMessage(getString(R.string.discard_changes))

                        // Only on discard the removeAddTask is triggered
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mMainActivity.removeAddTask();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();

        // Dialog width customization (BUG > LOLLIPOP)  TODO light BUG with dialog size xD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return;

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = 850;
        dialog.getWindow().setAttributes(lp);
    }

//    private void showTimePickerDialog() {
//
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
//    }

//    public static void dateToButton(Activity activity){
//        Button taskTimeBT = (Button) activity.findViewById(R.id.time);
//        Date dueDate = TaskController.taskDate;
//
//        if(dueDate!=null) {
//            taskTimeBT.setText(Task.dateToText(dueDate));
//        }else
//            taskTimeBT.setText(R.string.setDate);
//    }

}
