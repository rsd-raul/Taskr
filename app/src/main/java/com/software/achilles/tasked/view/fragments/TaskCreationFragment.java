package com.software.achilles.tasked.view.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.software.achilles.tasked.model.managers.ThreadManager;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.extras.FloatingActionMenuBehavior;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.R;

import java.util.List;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private MainActivity mMainActivity;
    private TaskCreationPresenter mPresenter;

    private Spinner mSpinner;
    private FloatingActionButton mFabSaveAndVoice;
    private ImageButton mDescription, mReminder, mLocation, mLabels;
    private CheckBox mFavourite;

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

        // Setup the fragment composing the ViewPager and the Tabs to control it - NEW THREAD
        ThreadManager.launchIfPossible(new Runnable() {
            public void run() {
                mPresenter.setupLayout();
            }
        });
    }

    public void setupLayout(List<String> taskListNames){
        mFabSaveAndVoice = (FloatingActionButton) mMainActivity.findViewById(R.id.saveAndVoiceFAB);
        mSpinner = (Spinner) mMainActivity.findViewById(R.id.spinner_task_list);
        mDescription = (ImageButton) mMainActivity.findViewById(R.id.button_description);
        mReminder = (ImageButton) mMainActivity.findViewById(R.id.button_time);
        mLocation = (ImageButton) mMainActivity.findViewById(R.id.button_location);
        mLabels = (ImageButton) mMainActivity.findViewById(R.id.button_label);
        mFavourite = (CheckBox) mMainActivity.findViewById(R.id.checkbox_favourite);

        setupColors();
        setupListeners();

        // Setup the fab and its listeners
        setupSaveOrVoice(false);

        // Populate Spinner
        mSpinner.setAdapter(new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, taskListNames));
    }

    private void setupColors(){
        int color = ContextCompat.getColor(mMainActivity, R.color.secondaryText);
        PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);

        mDescription.setColorFilter(filter);
        mReminder.setColorFilter(filter);
        mLocation.setColorFilter(filter);
        mLabels.setColorFilter(filter);
        mFavourite.setHintTextColor(color);
        mFavourite.setHighlightColor(color);
    }

    private void setupListeners(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button_description:
                        Toast.makeText(mMainActivity, "Button id: " + v.getId(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_time:
                        Toast.makeText(mMainActivity, "Button id: " + v.getId(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_location:
                        Toast.makeText(mMainActivity, "Button id: " + v.getId(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_label:
                        Toast.makeText(mMainActivity, "Button id: " + v.getId(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.checkbox_favourite:
                        Toast.makeText(mMainActivity, "Button id: " + v.getId(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }};

        mDescription.setOnClickListener(listener);
        mReminder.setOnClickListener(listener);
        mLocation.setOnClickListener(listener);
        mLabels.setOnClickListener(listener);
        mFavourite.setOnClickListener(listener);
    }

    private void setupSaveOrVoice(boolean save){
        View.OnLongClickListener longClickListener;
        View.OnClickListener clickListener;
        int dra, col;

        if(save) {
            dra = R.drawable.ic_save;
            col = R.color.colorSuccess;

            // Define listeners
            longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Save and reset fields (except spinner)
                    Toast.makeText(mMainActivity, "Save and reset", Toast.LENGTH_SHORT).show();
                    return false;
                }};
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save and go back to the Dashboard/Glance
                    Toast.makeText(mMainActivity, "Save and go back", Toast.LENGTH_SHORT).show();
                }};

        }else{
            dra = R.drawable.ic_voice_microphone;
            col = R.color.colorAccent;

            // Define listeners
            longClickListener = null;
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch the microphone and write in the title field
                    Toast.makeText(mMainActivity, "Micro to title", Toast.LENGTH_SHORT).show();
                }};
        }

        mFabSaveAndVoice.setImageDrawable(ContextCompat.getDrawable(mMainActivity, dra));
        mFabSaveAndVoice.setBackgroundTintList(ContextCompat.getColorStateList(mMainActivity, col));

        mFabSaveAndVoice.setOnClickListener(clickListener);
        mFabSaveAndVoice.setOnLongClickListener(longClickListener);

        ScaleAnimation expandAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        expandAnimation.setDuration(150);
        expandAnimation.setInterpolator(new AccelerateInterpolator());

        mFabSaveAndVoice.startAnimation(expandAnimation);
    }
    

    @Override
    public void onDestroy() {
        TaskCreationPresenter.destroyPresenter();
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
