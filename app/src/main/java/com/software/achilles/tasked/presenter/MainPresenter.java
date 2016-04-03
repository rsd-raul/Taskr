package com.software.achilles.tasked.presenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;

import java.util.Random;

public class MainPresenter implements Presenter<MainActivity, MainPresenter> {

    private MainActivity mActivity;
    private static MainPresenter instance;

    // ------------------------- Constructor -------------------------

    public static MainPresenter getInstance() {
        if(instance == null)
            instance = new MainPresenter();
        return instance;
    }

    // ------------------------- Life Cycle --------------------------

    @Override
    public MainPresenter attachView(MainActivity view) {
        mActivity = view;
        return instance;
    }

    public static void destroyPresenter() {
        if(instance == null)
            return;

        instance.mActivity = null;
        instance = null;

//      Un-subscribe from the thread?
//        if (subscription != null) subscription.unsubscribe();
    }

    public void backToBack(){
        mActivity.removeAddTask();
    }

    public void deployLayout(int key){
        switch (key) {
            case Constants.ADD_TASK:
                mActivity.deployAddTask();
                break;
            case Constants.ADD_LABEL:
                buildAndShowInputDialog(R.string.addLabel, key);
                break;
            case Constants.ADD_TASK_LIST:
                buildAndShowInputDialog(R.string.addList, key);
                break;
        }
    }

    /**
     * This method is responsible of the creation of a dialog, dialog that includes a text
     * input and it's responsible of adding a list or a label to the database, including the
     * update of the interface.
     *
     * @param titRes    The resource that represents the title for the dialog
     * @param uniqueId  If the type of Dialog we want (Add task list or add label)
     */
    private void buildAndShowInputDialog(int titRes, final int uniqueId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(mActivity.getString(titRes));

        final View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_view, null);

        builder.setView(dialogView);
        builder.setPositiveButton(mActivity.getString(R.string.save),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = ((EditText) dialogView.findViewById(R.id.input)).getText().toString();

                if(uniqueId == Constants.ADD_TASK_LIST){
                    TaskList newTaskList = new TaskList(text, null);

                    DataManager.getInstance().saveTaskList(newTaskList);

                    // Update the Tabs


                    // Update the filterDrawer and the MainDrawer
                    mActivity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_TASK_LIST);
                }
                if(uniqueId == Constants.ADD_LABEL) {
                    int[] color = new int[]{R.color.colorAccent, R.color.colorPrimary,
                            R.color.tealLocation, R.color.amberDate, R.color.md_black_1000,
                            R.color.md_orange_500};

                    // TODO manually pick the color for the Label instead of randomly
                    Label newLabel = new Label(text, color[new Random().nextInt(5)]);

                    DataManager.getInstance().saveLabel(newLabel);

                    // If label list on filterDrawer is closed... Do nothing
                    if (!mActivity.mDrawersConfigurator.mExpandedLabelListFilter)
                        return;

                    // If label list is expanded update the lists (or wait for opening drawer?)
                    mActivity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_LABEL_LIST);
                }
            }
        });
        builder.setNegativeButton(mActivity.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
