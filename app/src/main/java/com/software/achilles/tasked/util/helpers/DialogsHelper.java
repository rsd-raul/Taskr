package com.software.achilles.tasked.util.helpers;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions.Picker;
import com.software.achilles.tasked.view.pickers.SublimePickerFragment;
import com.software.achilles.tasked.view.pickers.SublimePickerFragment.SublimeCallback;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker.RecurrenceOption;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmResults;

public abstract class DialogsHelper {

    // ---------------------------- SELECT ---------------------------

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

    public static void buildLabelDialogMulti(final RealmResults<Label> items, Integer[] selected, final Activity activity, final TaskCreationPresenter taskCreationPresenter){
        final List<String> labels = new ArrayList<>();
        for (int i = 0; i < items.size(); i++)
            labels.add(items.get(i).getTitle());

        new MaterialDialog.Builder(activity)
                .title(R.string.select_labels)
                .items(labels)
                .itemsCallbackMultiChoice(selected, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        // Build the string representation of the labels
                        String labelsStr = LocalizationHelper.filterAndFormatLabels(items, which, false);

                        // From the list of labels, get the ones selected
                        RealmList<Label> filtered = new RealmList<>();
                        for(Integer index : which)
                            filtered.add(items.get(index));

                        taskCreationPresenter.setLabels(labelsStr, filtered);
                        return true;
                    }
                })
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .show();
    }

    // ---------------------------- INPUT ----------------------------

    public static void buildDescriptionDialog(String text, final Activity activity, final TaskCreationPresenter taskCreationPresenter){
        String hint = activity.getResources().getString(R.string.description);
        String existent = text != null ? text : "";

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                // Dialog content
                .title(hint)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .content(R.string.ask_for_description)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        | InputType.TYPE_CLASS_TEXT)
                .input(hint, existent, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        taskCreationPresenter.setDescription(input.toString());
                    }
                })
                .build();

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

                    switch (uniqueId){

                        case Constants.ADD_TASK_LIST:
                            TaskList newTaskList = new TaskList(input.toString(), null);

                            dataManager.saveTaskList(newTaskList);

                            // Update the Tabs
                            activity.setupViewPagerAndTabs(true);

                            // Update the filterDrawer and the MainDrawer
                            activity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_TASK_LIST);
                            break;

                        case Constants.ADD_LABEL:
                            int[] clr = new int[]{R.color.colorAccent, R.color.colorPrimary,
                                    R.color.tealLocation, R.color.amberDate, R.color.md_black_1000,
                                    R.color.md_orange_500};

                            // TODO manually pick the color for the Label instead of randomly
                            Label newLabel = new Label(input.toString(), clr[new Random().nextInt(5)]);

                            // Save the label
                            dataManager.saveLabel(newLabel);

                            // If label list on filterDrawer is closed... Do nothing
                            if (!activity.mDrawersConfigurator.mExpandedLabelListFilter)
                                return;

                            // If label list is expanded update the lists (or wait for opening drawer?)
                            activity.mDrawersConfigurator.includeTheNew(Constants.COLLAPSIBLE_LABEL_LIST);
                            break;

                    }
                }
            }).show();
    }

    // ----------------------- SUBLIME PICKER ------------------------

//    private static SublimeOptions getFullPicker(Picker picker){
//        return getBaseOptionsSublime(picker, true, true, true, true);
//    }
//
//    private static SublimeOptions getTimePicker(){
//        return getBaseOptionsSublime(Constants.TIME_PICKER, false, true, false, false);
//    }
//
//    private static SublimeOptions getDatePicker(boolean allowRange){
//        return getBaseOptionsSublime(Constants.DATE_PICKER, true, false, false, allowRange);
//    }

    private static SublimeOptions getDateTimePicker(Picker picker, boolean allowRange){
        return getBaseOptionsSublime(picker, true, true, false, allowRange);
    }

//    private static SublimeOptions getRecurrenceOptions(){
//        return getBaseOptionsSublime(Constants.REPEAT_PICKER, false, false, true, false);
//    }

    private static SublimeOptions getBaseOptionsSublime(Picker picker,
                                                        boolean date, boolean time,
                                                        boolean repeat, boolean range){

        SublimeOptions options = new SublimeOptions();

        // Select the start screen
        options.setPickerToShow(picker);

        // Select the extras that will be available
        int displayOptions = 0;
        if (date)
            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        if (time)
            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        if (repeat)
            displayOptions |= SublimeOptions.ACTIVATE_RECURRENCE_PICKER;
        options.setCanPickDateRange(range);

        options.setDisplayOptions(displayOptions);

        // If 'displayOptions' is zero, the chosen options are not valid
        if(displayOptions == 0)
            throw new UnsupportedOperationException("Display options empty");

        return options;
    }

    private static SublimeOptions customizeOptionsSublime(SublimeOptions options, Date date, boolean is24){
        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);
        options.setDateParams(startCal, endCal);*/

        // Initialize the calendar and if the task is valid, set the calendar to that value
        Calendar cal = Calendar.getInstance();
        if(date != null)
            cal.setTime(date);

        // Set the default or the selected date
        options.setDateParams(cal);
        options.setTimeParams(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), is24);

        return options;
    }

    private static void buildSublimePicker(FragmentManager fragmentManager,
                                           SublimeCallback callback,
                                           SublimeOptions options){

        SublimePickerFragment pickerFrag = new SublimePickerFragment().withCallback(callback);

        // Valid options
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SUBLIME_OPTIONS, options);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(fragmentManager, Constants.SUBLIME_PICKER);
    }

    public static void buildDateTimePicker(Fragment fragment, Date date,
                                           final TaskCreationPresenter taskCreationPresenter){

        // Get a Date & Time picker starting in time and without range
        SublimeOptions options = getDateTimePicker(Picker.TIME_PICKER, false);

        // Based on the user Locale, get the format
        boolean is24 = LocalizationHelper.is24HourFormat(fragment.getContext());

        // Set the date if any, or default
        options = DialogsHelper.customizeOptionsSublime(options, date, is24);

        SublimeCallback callback = new SublimeCallback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                                int hourOfDay, int minute,
                                                RecurrenceOption recurrenceOption,
                                                String recurrenceRule) {
                Calendar cal = selectedDate.getFirstDate();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, hourOfDay);

                Date date = cal.getTime();

                String result = LocalizationHelper.dateToDateTimeString(date);

                taskCreationPresenter.setDueDate(result, date);
            }
        };


        // Build the final picker
        DialogsHelper.buildSublimePicker(fragment.getFragmentManager(),callback ,options);
    }
}
