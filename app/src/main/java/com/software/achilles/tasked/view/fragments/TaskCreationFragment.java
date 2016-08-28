package com.software.achilles.tasked.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.adapters.TaskDetailFAItem;
import com.software.achilles.tasked.view.listeners.OnText_EditTextListener;

import javax.inject.Inject;
import javax.inject.Provider;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private FragmentActivity mMainActivity;

    private TextView mTaskList;
    private FloatingActionButton mFabSaveAndVoice;
    private static EditText mTitle;
    private ImageButton mDescription, mReminder, mLocation, mLabels, mFavourite;

    // -------------------------- Injected ---------------------------

    @Inject
    TaskCreationPresenter taskCreationPresenter;
    @Inject
    DataManager dataManager;
    @Inject
    public FastItemAdapter<IItem> fastAdapter;
    @Inject
    Provider<TaskDetailFAItem> taskDetailAdapterProvider;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskCreationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        return inflater.inflate(R.layout.task_creation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize MainActivity
        mMainActivity = getActivity();

        // Initialize presenter
        taskCreationPresenter.attachView(this);

        // Get the list the user is at, if it's coming from Dashboard
        int listIndex;
        long taskId;
        try {listIndex = getArguments().getInt(Constants.LIST_INDEX, 0);} catch(Exception e) {listIndex = 0;}
        try {taskId = getArguments().getLong(Constants.TASK_ID, -1);} catch(Exception e) {taskId = -1;}

        // Retrieve the recycler view, set the Manager and the FastAdapter
        RecyclerView recyclerView = (RecyclerView) mMainActivity.findViewById(R.id.recycler_task_creation);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(fastAdapter);

        // Setup the fragment composing the ViewPager and the Tabs to control it
        taskCreationPresenter.setupLayout(taskId, listIndex);
    }

    public void deleteItem(int index){
        fastAdapter.remove(index);
    }

    public void createItem(int detailType, String text){
        fastAdapter.add(taskDetailAdapterProvider.get().withConfigure(detailType, text));
    }

    // REVIEW Las probabilidades de que esto sea una warrada son altas xD
    public void editItem(int index, int detailType, String text){
        fastAdapter.remove(index);
        fastAdapter.add(index, taskDetailAdapterProvider.get().withConfigure(detailType, text));
    }

    public void setupLayout(boolean edit){
        mFabSaveAndVoice = (FloatingActionButton) mMainActivity.findViewById(R.id.saveAndVoiceFAB);
        mTaskList = (TextView) mMainActivity.findViewById(R.id.text_task_list);
        mDescription = (ImageButton) mMainActivity.findViewById(R.id.button_description);
        mReminder = (ImageButton) mMainActivity.findViewById(R.id.button_time);
        mLocation = (ImageButton) mMainActivity.findViewById(R.id.button_location);
        mLabels = (ImageButton) mMainActivity.findViewById(R.id.button_label);
        mFavourite = (ImageButton) mMainActivity.findViewById(R.id.button_favourite);
        mTitle = (EditText) mMainActivity.findViewById(R.id.textInput);

        // Setup the fab and its listeners
        Runnable positive = new Runnable() {
            @Override
            public void run() {
                setupSaveOrVoice(true);
            }
        };
        Runnable negative = new Runnable() {
            @Override
            public void run() {
                setupSaveOrVoice(false);
            }
        };
        mTitle.addTextChangedListener(new OnText_EditTextListener(positive, negative));

        // Focus keyboard o the title
        mTitle.requestFocus();



        if(!edit)
            setupModifiersColors();
        setupModifiersListeners();
    }

    private void setupModifiersColors() {
        PorterDuffColorFilter filter = getColorFilter(R.color.task_modifier_icons);

        mDescription.setColorFilter(filter);
        mReminder.setColorFilter(filter);
        mLocation.setColorFilter(filter);
        mLabels.setColorFilter(filter);
        mFavourite.setColorFilter(filter);
    }

    private void setupModifiersListeners(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) { taskCreationPresenter.itemOnClick(v.getId()); }};

        mDescription.setOnClickListener(listener);
        mReminder.setOnClickListener(listener);
        mLocation.setOnClickListener(listener);
        mLabels.setOnClickListener(listener);
        mFavourite.setOnClickListener(listener);
        mTaskList.setOnClickListener(listener);
    }

    public void colorModifierButton(int modifierId, boolean active){
        // If we are going to turn the button ON we don't need a color
        int stock = active? -1 : R.color.task_modifier_icons;

        // Switch ON or OFF by adjusting the color
        switch (modifierId){
            case R.id.button_description:
                mDescription.setColorFilter(getColorFilter(active ? R.color.md_black_1000 : stock));
                break;
            case R.id.button_time:
                mReminder.setColorFilter(getColorFilter(active ? R.color.amberDate : stock));
                break;
            case R.id.button_location:
                mLocation.setColorFilter(getColorFilter(active ? R.color.tealLocation : stock));
                break;
            case R.id.button_label:
                mLabels.setColorFilter(getColorFilter(active ? R.color.colorPrimary : stock));
                break;
            case R.id.button_favourite:
                // Change the icon for ON and OFF
                Drawable star = ContextCompat.getDrawable(getContext(),
                        active ? R.drawable.ic_star_filled : R.drawable.ic_star_clear);

                // Set the drawable
                mFavourite.setImageDrawable(star);

                // Color the drawable if ON
                if(active)
                    mFavourite.clearColorFilter();
                else
                    mFavourite.setColorFilter(getColorFilter(stock));
                break;
        }
    }

    private PorterDuffColorFilter getColorFilter(int colorRes){
        int color = ContextCompat.getColor(mMainActivity, colorRes);
        return new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setTaskListTextView(String value, int index){
        mTaskList.setText(value);
        dataManager.setTemporalTaskListPosition(index);
    }

    public void setTaskNameTextView(String value){
        mTitle.setText(value);
    }
    
    /**
     * This method will toggle the characteristics of the FAB between Save and Voice input
     *
     * @param save  Will set the FAB to Save if true, to Voice if not.
     */
    public void setupSaveOrVoice(boolean save){
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
                    taskCreationPresenter.saveTask(true);
                    return false;
                }};
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save and go back to the Dashboard/Glance
                    Toast.makeText(mMainActivity, "Save and go back", Toast.LENGTH_SHORT).show();
                    taskCreationPresenter.saveTask(false);
                }};

        }else{
            dra = R.drawable.ic_voice_microphone;
            col = R.color.colorAccent;

            // Define listeners
            longClickListener = null;
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected()){
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        getActivity().startActivityForResult(intent, Constants.VOICE_RECOGNITION_REQUEST);
                    }else
                        Toast.makeText(mMainActivity, R.string.please_connect_internet, Toast.LENGTH_LONG).show();
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

    public  boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) mMainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net!=null && net.isAvailable() && net.isConnected();
    }

    /**
     * Before saving, this method will populate the parameters in the temporal Task that are not
     * populated in real time.
     *
     * @return      The temporal task once updated
     */
    public Task populateAndGetTemporal(){
        Task result = dataManager.getTemporalTask();

        result.setTitle(mTitle.getText().toString());
//        result.setNotes(mNotes.toString());


        return result;
    }

//    @Override
//    public void onDestroy() {
//        TaskCreationPresenter.destroyPresenter();
//        super.onDestroy();
//    }


    // FIXME mTitle pasado a static para poder acceder desde MainPresenter
    // si el usuario a introducido algun dato => TRUE, de lo contrario => FALSE
    public static boolean isDataPresent(){

        return !mTitle.getText().toString().equals("");
    }

    public void resetFields(){
        // FIXME Favourite needs to be set to unchecked
        if(dataManager.getTemporalTask().isStarred())
            taskCreationPresenter.itemOnClick(mFavourite.getId());

        setupModifiersColors();
        mTitle.setText(R.string.blank);
    }
}
