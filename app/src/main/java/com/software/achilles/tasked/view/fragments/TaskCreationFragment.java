package com.software.achilles.tasked.view.fragments;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.helpers.ClickListenerHelper;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.adapters.TaskDetailAdapter;
import com.software.achilles.tasked.view.listeners.OnText_EditTextListener;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

public class TaskCreationFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private MainActivity mMainActivity;
    ClickListenerHelper<IItem> mClickListenerHelper;
    private Spinner mSpinner;
    private FloatingActionButton mFabSaveAndVoice;
    private static EditText mTitle;
    private ImageButton mDescription, mReminder, mLocation, mLabels, mFavourite;

    // -------------------------- Injected ---------------------------

    @Inject
    TaskCreationPresenter taskCreationPresenter;
    @Inject
    DataManager dataManager;
    @Inject
    FastItemAdapter<IItem> fastAdapter;
    @Inject
    Provider<TaskDetailAdapter> taskDetailAdapterProvider;

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
        mMainActivity = ((MainActivity) getActivity());

        // Initialize presenter
        taskCreationPresenter.attachView(this);

        // Get the list the user is at if it's coming from Dashboard
        int listIndex = 0;
        try {
            listIndex = getArguments().getInt("listIndex", 0);
        }catch (NullPointerException e){ /* Do nothing, 0 by default */ }

        // Retrieve the recycler view and set the manager
        RecyclerView recyclerView = (RecyclerView) mMainActivity.findViewById(R.id.recycler_task_creation);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        // Configure the FastAdapter and set it on the RecyclerView
        recyclerView.setAdapter(fastAdapter);

        mClickListenerHelper = new ClickListenerHelper<>(fastAdapter);
        fastAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {
            @Override
            public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                return fastAdapter.getTypeInstance(viewType).getViewHolder(parent);
            }

            @Override
            public RecyclerView.ViewHolder onPostCreateViewHolder(final RecyclerView.ViewHolder viewHolder) {
                //we do this for our ImageItem.ViewHolder
                if (viewHolder instanceof TaskDetailAdapter.ViewHolder) {
                    //if we click on the imageLovedContainer
                    mClickListenerHelper.listen(viewHolder, ((TaskDetailAdapter.ViewHolder) viewHolder).itemView, new ClickListenerHelper.OnClickListener<IItem>() {
                        @Override
                        public void onClick(View v, int position, IItem item) {
//                            if (item instanceof TaskDetailAdapter) {
//                                fastAdapter.toggleExpandable(position);
//                                if (((TaskDetailAdapter) item).getSubItems() != null) {
                                    if (!((TaskDetailAdapter) item).isExpanded()) {
                                        ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(180).start();
                                    } else {
                                        ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(0).start();
                                    }
//                                }
//                            }
                        }
                    });
                }

                return viewHolder;
            }
        });

        // REVIEW We are removing before adding, maybe is better not to
        fastAdapter.removeItemRange(0, fastAdapter.getItemCount());

        // TODO AQUI ESTAMOS, toca popular los campos en funcion de la tarea
        fastAdapter.add(taskDetailAdapterProvider.get()
                .withConfigure(Constants.DETAIL_DESCRIPTION, "Random placeholder description test testing")
                .addSubItem(taskDetailAdapterProvider.get()
                        .withConfigure(Constants.DETAIL_LOCATION, "Parchment Square 152A, Cork")));
        fastAdapter.add(taskDetailAdapterProvider.get()
                .withConfigure(Constants.DETAIL_LOCATION, "Parchment Square 152A, Cork")
                .addSubItem(taskDetailAdapterProvider.get()
                        .withConfigure(Constants.DETAIL_LABELS, "Food - Persona - More Food")));
        fastAdapter.add(taskDetailAdapterProvider.get()
                .withConfigure(Constants.DETAIL_LABELS, "Food - Persona - More Food"));
        fastAdapter.add(taskDetailAdapterProvider.get()
                .withConfigure(Constants.DETAIL_ALARM, "12:40 - Sunday 3, July"));


        // Setup the fragment composing the ViewPager and the Tabs to control it
        taskCreationPresenter.setupLayout(listIndex);
    }

    public void setupLayout(List<String> taskListNames, int listIndex){
        mFabSaveAndVoice = (FloatingActionButton) mMainActivity.findViewById(R.id.saveAndVoiceFAB);
        mSpinner = (Spinner) mMainActivity.findViewById(R.id.spinner_task_list);
        mDescription = (ImageButton) mMainActivity.findViewById(R.id.button_description);
        mReminder = (ImageButton) mMainActivity.findViewById(R.id.button_time);
        mLocation = (ImageButton) mMainActivity.findViewById(R.id.button_location);
        mLabels = (ImageButton) mMainActivity.findViewById(R.id.button_label);
        mFavourite = (ImageButton) mMainActivity.findViewById(R.id.button_favourite);
        mTitle = (EditText) mMainActivity.findViewById(R.id.textInput);

        setupModifiersColors();
        setupModifiersListeners();

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

        // Populate Spinner
        mSpinner.setAdapter(new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, taskListNames));
        mSpinner.setSelection(listIndex);
    }

    private void setupModifiersColors(){
        PorterDuffColorFilter filter = getColorFilter(R.color.task_modifier_icons);
        mDescription.setColorFilter(filter);
        mReminder.setColorFilter(filter);
        mLocation.setColorFilter(filter);
        mLabels.setColorFilter(filter);
        mFavourite.setColorFilter(filter);
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

    private void setupModifiersListeners(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCreationPresenter.modifiersOnClick(v);
            }};

        mDescription.setOnClickListener(listener);
        mReminder.setOnClickListener(listener);
        mLocation.setOnClickListener(listener);
        mLabels.setOnClickListener(listener);
        mFavourite.setOnClickListener(listener);
    }

    /**
     * This method will toggle the characteristics of the FAB between Save and Voice input
     *
     * @param save  Will set the FAB to Save if true, to Voice if not.
     */
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

        dataManager.setTemporalTaskListPosition(mSpinner.getSelectedItemPosition());

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
            taskCreationPresenter.modifiersOnClick(mFavourite);

        setupModifiersColors();
        mTitle.setText(R.string.blank);
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
