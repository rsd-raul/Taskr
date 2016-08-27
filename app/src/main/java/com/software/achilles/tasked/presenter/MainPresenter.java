package com.software.achilles.tasked.presenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.helpers.DialogsHelper;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainPresenter implements Presenter<MainActivity> {

// REVIEW - was Dependency Cycle - TaskCreationFragment.isDataPresent() supplies functionality
//    @Inject
//    TaskCreationPresenter taskCreationPresenter;

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
//        return instance;
    }

//    public static void destroyPresenter() {
//        if(instance == null)
//            return;
//
//        instance.mActivity = null;
//        instance = null;
//
////      Un-subscribe from the thread?
////        if (subscription != null) subscription.unsubscribe();
//    }

    /**
     * Closes the Add Task layout if there is no data present, if there is, it asks the user
     * for confirmation.
     */
    public void backToBack(){

        // If the user haven't typed anything, close the interface
        if(!TaskCreationFragment.isDataPresent()){
            mActivity.removeAddTask();
        }else{
            // Else, ask for confirmation
            Dialog dialog = new AlertDialog.Builder(mActivity)
                    // For simple dialogs we don't use a Title (Google Guidelines)
                    .setMessage(mActivity.getString(R.string.discard_changes))

                    // Only on discard the removeAddTask is triggered
                    .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mActivity.removeAddTask();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .show();

            // Dialog width customization (BUG > LOLLIPOP)  REVIEW - light BUG with dialog size xD
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                return;

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = 850;
            dialog.getWindow().setAttributes(lp);
        }
    }

    public void deployEditLayout(long itemId){
        mActivity.setEditItem(itemId);

        // REVIEW Esto es warro no, lo siguiente
        mActivity.mFamConfigurator.famVisibility(false);
        deployLayout(Constants.ADD_TASK);
    }

    public void deployLayout(int key){
        switch (key) {
            case Constants.ADD_TASK:
                mActivity.deployAddTask();
                break;
            case Constants.ADD_LABEL:
            case Constants.ADD_TASK_LIST:
                DialogsHelper.buildAndShowInputDialog(key, mActivity, dataManager);
                break;
        }
    }
}
