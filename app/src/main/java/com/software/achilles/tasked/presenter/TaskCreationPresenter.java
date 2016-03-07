package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import java.util.ArrayList;
import java.util.List;

public class TaskCreationPresenter implements Presenter<TaskCreationFragment, TaskCreationPresenter> {

    // --------------------------- Values ----------------------------

    private int mKeyConstant = Constants.ADD_TASK;

    // ------------------------- Attributes --------------------------

    private TaskCreationFragment mFragment;
    private static TaskCreationPresenter instance;

    // ------------------------- Constructor -------------------------

    public static TaskCreationPresenter getInstance() {
        if(instance == null)
            instance = new TaskCreationPresenter();
        return instance;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public TaskCreationPresenter attachView(TaskCreationFragment view) {
        mFragment = view;
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mFragment = null;
        instance = null;

//      Un-subscribe from the thread?
//        if (subscription != null) subscription.unsubscribe();
    }

    // ---------------------------- Layout ---------------------------

    public void setupLayout(){
        // TODO esto va aqui?
        DataManager dataManager = new DataManager();
        List<String> taskListTitles = new ArrayList<>();

        // Get data for setting the ViewPager
        ArrayList<TaskList> taskList = dataManager.findAllTaskList();

        // Format that data for the spinner
        for (int i = 0; i < taskList.size(); i++)
            taskListTitles.add(taskList.get(i).getTitle());

        // Setup the layout
        mFragment.setupLayout(taskListTitles);
    }
}
