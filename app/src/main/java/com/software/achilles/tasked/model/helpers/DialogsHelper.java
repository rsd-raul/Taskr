package com.software.achilles.tasked.model.helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.adapters.TaskDetailFAItem;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;

import java.util.List;
import java.util.Random;

public abstract class DialogsHelper {

    public static void buildChoiceFromList(List<String> items, int defaultList, final TaskCreationFragment fragment){
        new MaterialDialog.Builder(fragment.getActivity())
                .title(R.string.select_task_list)
                .items(items)
                .itemsCallbackSingleChoice(defaultList, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        fragment.setTaskListTextView(text.toString(), which);
                        return true;
                    }
                })
                .show();
    }

    public static void buildChoiceFromListMulti(final List<String> all, Integer[] selected, final TaskDetailFAItem item, final FragmentActivity activity, final View view){
        new MaterialDialog.Builder(activity)
                .title(R.string.select_labels)
                .items(all)
                .itemsCallbackMultiChoice(selected, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        // Build the string representation of the labels
                        String labels = which.length > 0 ? "" : activity.getString(R.string.select_labels);
                        for(int aux : which)
                            labels += "#" + all.get(aux) + " ";

                        item.setLabels(which, labels, view);
                        return true;
                    }
                })
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .show();
    }

    public static void buildDescriptionDialog(String text, final Activity activity, final TaskCreationPresenter taskCreationPresenter){
        String hint = activity.getResources().getString(R.string.description);
        String existent = text != null ? text : "";

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                // Dialog content
                .title(R.string.description)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .content(R.string.ask_for_description)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        | InputType.TYPE_CLASS_TEXT)
                .input(hint, existent, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        taskCreationPresenter.setDescription(input.toString());
                        taskCreationPresenter.addOrEditItem(input.toString(), Constants.DETAIL_DESCRIPTION);

                        taskCreationPresenter.modifiersColor(input.length() != 0, Constants.DETAIL_DESCRIPTION);
//                        item.setDescription(input.toString(), view);
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        taskCreationPresenter.modifiersColor(false, Constants.DETAIL_DESCRIPTION);
                    }
                }).build();

        EditText input = dialog.getInputEditText();
        if(input != null)
            input.setSingleLine(false);

        dialog.show();
    }

    public static void buildDescriptionDialog(String text, final TaskDetailFAItem item, final Activity activity, final View view){
        String hint = activity.getResources().getString(R.string.description);
        String existent = text != null ? text : "";

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                // Dialog content
                .title(R.string.description)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .content(R.string.ask_for_description)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        | InputType.TYPE_CLASS_TEXT)
                .input(hint, existent, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        item.setDescription(input.toString(), view);
                    }
                }).build();

        EditText input = dialog.getInputEditText();
        if(input != null)
            input.setSingleLine(false);

        dialog.show();
    }


    /**
     * This method is responsible of the creation of a dialog, dialog that includes a text
     * input and it's responsible of adding a list or a label to the database, including the
     * update of the interface.
     *
     * @param uniqueId  If the type of Dialog we want (Add task list or add label)
     */
    public static void buildAndShowInputDialog(final int uniqueId, final MainActivity activity, final DataManager dataManager) {
        int titRes, titColRes ;

        switch (uniqueId){
            case Constants.ADD_LABEL:
                titRes = R.string.addLabel;
                titColRes = R.color.colorAccent;
                break;
            case Constants.ADD_TASK_LIST:
                titRes = R.string.addList;
                titColRes = R.color.colorPrimary;
                break;
            default:
                throw new MaterialDialog.NotImplementedException("Not implemented");
        }

        new MaterialDialog.Builder(activity)

            // Dialog content
            .title(titRes)
            .content(R.string.ask_for_title)
            .positiveText(R.string.save)
            .negativeText(R.string.cancel)

            // Colors
            .titleColorRes(titColRes)
            .negativeColorRes(titColRes)
            .positiveColorRes(titColRes)
            .widgetColorRes(titColRes)

            // Input customization
            .inputRangeRes(1, 24, titColRes)
            .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT)
            .input(R.string.title, R.string.blank, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    if (uniqueId == Constants.ADD_TASK_LIST) {
                        TaskList newTaskList = new TaskList(input.toString(), null);

                        dataManager.saveTaskList(newTaskList);

                        // Update the Tabs
                        activity.setupViewPagerAndTabs(true);

                        // Update the filterDrawer and the MainDrawer
                        activity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_TASK_LIST);
                    }
                    if (uniqueId == Constants.ADD_LABEL) {
                        int[] clr = new int[]{R.color.colorAccent, R.color.colorPrimary,
                                R.color.tealLocation, R.color.amberDate, R.color.md_black_1000,
                                R.color.md_orange_500};

                        // FIXME manually pick the color for the Label instead of randomly
                        Label newLabel = new Label(input.toString(), clr[new Random().nextInt(5)]);

                        // Save the label
                        dataManager.saveLabel(newLabel);

                        // If label list on filterDrawer is closed... Do nothing
                        if (!activity.mDrawersConfigurator.mExpandedLabelListFilter)
                            return;

                        // If label list is expanded update the lists (or wait for opening drawer?)
                        activity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_LABEL_LIST);
                    }
                }
            }).show();
    }
}
