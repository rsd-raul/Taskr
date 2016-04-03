package com.software.achilles.tasked.presenter;

import android.support.annotation.NonNull;
import android.text.InputType;
import com.afollestad.materialdialogs.MaterialDialog;
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
                buildAndShowInputDialog(R.string.addLabel, R.color.colorAccent, key);
                break;
            case Constants.ADD_TASK_LIST:
                buildAndShowInputDialog(R.string.addList, R.color.colorPrimary, key);
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
    private void buildAndShowInputDialog(int titRes, int titColRes, final int uniqueId) {
        new MaterialDialog.Builder(mActivity)
            .cancelable(false)

            // Dialog content
            .title(titRes)
            .content(R.string.ask_for_title)
            .positiveText(R.string.save)
            .negativeText(R.string.cancel)

            // Colors
            .titleColorRes(titColRes)
            .negativeColorRes(titColRes)
            .positiveColorRes(titColRes)

            // Input customization
            .inputRangeRes(2, 24, titColRes)
            .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT)
            .input(R.string.title, R.string.blank, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    if (uniqueId == Constants.ADD_TASK_LIST) {
                        TaskList newTaskList = new TaskList(input.toString(), null);

                        DataManager.getInstance().saveTaskList(newTaskList);

                        // Update the Tabs
                        mActivity.setupViewPagerAndTabs(true);

                        // Update the filterDrawer and the MainDrawer
                        mActivity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_TASK_LIST);
                    }
                    if (uniqueId == Constants.ADD_LABEL) {
                        int[] clr = new int[]{R.color.colorAccent, R.color.colorPrimary,
                                R.color.tealLocation, R.color.amberDate, R.color.md_black_1000,
                                R.color.md_orange_500};

                        // TODO manually pick the color for the Label instead of randomly
                        Label newLabel = new Label(input.toString(), clr[new Random().nextInt(5)]);

                        // Save the label
                        DataManager.getInstance().saveLabel(newLabel);

                        // If label list on filterDrawer is closed... Do nothing
                        if (!mActivity.mDrawersConfigurator.mExpandedLabelListFilter)
                            return;

                        // If label list is expanded update the lists (or wait for opening drawer?)
                        mActivity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_LABEL_LIST);
                    }
                }
            }).show();
    }
}
