package com.software.achilles.tasked.presenter;

import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.helpers.DialogsHelper;
import com.software.achilles.tasked.view.MainActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainPresenter implements Presenter<MainActivity> {

    // -------------------------- Injected ---------------------------

    DataManager dataManager;
    
    // ------------------------- Constructor -------------------------

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // ------------------------- Attributes --------------------------

    private MainActivity mActivity;

    // ------------------------- Life Cycle --------------------------

    @Override
    public void attachView(MainActivity view) {
        mActivity = view;
    }


    /**
     * Closes the Add Task layout if there is no data present, if there is, it asks the user
     * for confirmation.
     */
    public void backToBack(){
            mActivity.deployOrRemoveTaskCreation(false);
    }

    public void deployEditLayout(long itemId){
        mActivity.setEditItem(itemId);

        mActivity.mFamConfigurator.famVisibility(false);
        deployLayout(Constants.ADD_TASK);
    }

    public void deployLayout(int key){
        switch (key) {
            case Constants.ADD_TASK:
                mActivity.deployOrRemoveTaskCreation(true);
                break;
            case Constants.ADD_LABEL:
            case Constants.ADD_TASK_LIST:
                DialogsHelper.buildAndShowInputDialog(key, mActivity, dataManager);
                break;
        }
    }
}
