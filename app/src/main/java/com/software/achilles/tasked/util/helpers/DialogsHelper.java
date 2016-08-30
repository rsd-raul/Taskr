package com.software.achilles.tasked.util.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions.Picker;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker.RecurrenceOption;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.view.MainActivity;
import com.software.achilles.tasked.view.fragments.DashboardListFragment;
import com.software.achilles.tasked.view.fragments.TaskCreationFragment;
import com.software.achilles.tasked.view.pickers.SublimePickerFragment;
import com.software.achilles.tasked.view.pickers.SublimePickerFragment.SublimeCallback;

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

        int titColRes = R.color.colorPrimary;

        new MaterialDialog.Builder(activity)
                .title(R.string.select_labels)
                .items(labels)
                .itemsCallbackMultiChoice(selected, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        // Build the string representation of the labels
                        String labelsStr = LocalisationHelper.filterAndFormatLabels(items, which, false);

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

                .titleColorRes(titColRes)
                .negativeColorRes(titColRes)
                .positiveColorRes(titColRes)
                .widgetColorRes(titColRes)
                .show();
    }

    // ---------------------------- INPUT ----------------------------

    public static void buildDescriptionDialog(String text, final Activity activity, final TaskCreationPresenter taskCreationPresenter){
        String hint = activity.getResources().getString(R.string.description);
        String existent = text != null ? text : "";

        int titColRes = R.color.md_black_1000;

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

                .titleColorRes(titColRes)
                .negativeColorRes(titColRes)
                .positiveColorRes(titColRes)
                .widgetColorRes(titColRes)
                .build();

        EditText input = dialog.getInputEditText();
        if(input != null)
            input.setSingleLine(false);

        dialog.show();
    }

    public static void buildLocationTypeDialog(final FragmentActivity activity,
                                               final String location,
                                               final TaskCreationPresenter presenter,
                                               final double[] bounds){
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(activity);
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.as_note)
                .icon(R.drawable.ic_description)
                .iconPaddingDp(6)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.as_place)
                .icon(R.drawable.ic_place)
                .iconPaddingDp(6)
                .backgroundColor(Color.WHITE)
                .build());

        new MaterialDialog.Builder(activity)
                .title(R.string.set_location)
                .autoDismiss(true)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                        switch (which){
                            case 0:
                                buildLocationInputDialog(location, activity, presenter);
                                dialog.dismiss();
                                break;
                            case 1:
                                buildPlacePicker(activity, bounds);
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }

    public static void buildLocationInputDialog(final String currentLocation,
                                                final FragmentActivity activity,
                                                final TaskCreationPresenter presenter){
        int titRes = R.string.addLocation;
        int titColRes = R.color.tealLocation;
        String title = activity.getResources().getString(R.string.location);

        new MaterialDialog.Builder(activity)

                // Dialog content
                .title(titRes)
                .content(R.string.ask_for_location)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)

                // Colors
                .titleColorRes(titColRes)
                .negativeColorRes(titColRes)
                .positiveColorRes(titColRes)
                .widgetColorRes(titColRes)

                // Input customization
                .inputRangeRes(1, 60, titColRes)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(title, currentLocation, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        presenter.processNoteAsLocation(input.toString());
                    }
                }).show();
    }

    /**
     * This method is responsible of the creation of a dialog, dialog that includes a text
     * input and it's responsible of adding a list or a label to the database, including the
     * update of the interface.
     *
     * @param uniqueId  If the type of Dialog we want (Add task list or add label)
     */
    public static void buildAndShowInputDialog(final int uniqueId, final MainActivity activity, final DataManager dataManager) {
        int titRes, titColRes;

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

    // ------------------------ PLACE PICKER -------------------------

    public static void buildPlacePicker(FragmentActivity activity, double[] bounds){

        Toast.makeText(activity, R.string.launching_place_picker, Toast.LENGTH_SHORT).show();

        try{
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            if(bounds != null) {
                LatLng first = new LatLng(bounds[0], bounds[1]);
                LatLng last = new LatLng(bounds[2], bounds[3]);
                builder.setLatLngBounds(new LatLngBounds(first, last));
            }

            Intent intent = builder.build(activity);
            activity.startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesNotAvailableException e1){
            Log.e("DialogHelper", "launchPlacePicker exception", e1);
            Toast.makeText(activity, R.string.GooglePlayServicesUnavailable, Toast.LENGTH_SHORT).show();

        } catch (GooglePlayServicesRepairableException e2){
            Log.e("DialogHelper", "launchPlacePicker exception", e2);
            Toast.makeText(activity, R.string.PlacePickerFailed, Toast.LENGTH_SHORT).show();

        }
    }

    // ----------------------- SUBLIME PICKER ------------------------

    private static SublimeOptions getDateTimePicker(Picker picker, boolean allowRange){
        return getBaseOptionsSublime(picker, true, true, false, allowRange);
    }

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

    public static void buildDateTimePicker(final long id, Date date,
                                   final DashboardPresenter dashboardPresenter, Context context){
        Fragment fragment = dashboardPresenter.getView();
        if(fragment == null || fragment.getFragmentManager() == null)
            fragment = dashboardPresenter.getChildView();
        dateTimePickerFactory(id, date, fragment, null, dashboardPresenter, context);
    }

    public static void buildDateTimePicker(Fragment fragment, Date date,
                                           final TaskCreationPresenter taskCreationPresenter){
        dateTimePickerFactory(-1, date, fragment, taskCreationPresenter, null, null);
    }

    private static void dateTimePickerFactory(final long id, Date date, Fragment fragment,
                                              final TaskCreationPresenter taskCreationPresenter,
                                              final DashboardPresenter dashboardPresenter, Context context){
        final boolean dashboard = dashboardPresenter != null;
        final boolean taskCreation = taskCreationPresenter != null;

        // Get a Date & Time picker starting in time and without range
        SublimeOptions options = getDateTimePicker(Picker.TIME_PICKER, false);

        // Based on the user Locale, get the format
        Context cntxt = fragment.getContext();
        if (cntxt == null)
            cntxt = context;
        boolean is24 = LocalisationHelper.is24HourFormat(cntxt);

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
                cal.set(Calendar.MINUTE, minute);

                Date date = cal.getTime();

                if(taskCreation) {
                    String result = LocalisationHelper.dateToDateTimeString(date);
                    taskCreationPresenter.setDueDate(result, date);
                }else if (dashboard){
                    dashboardPresenter.setDueDate(id, date);
                    if(DashboardListFragment.needsChild)
                        dashboardPresenter.notifyItemChangeChild();
                    else
                        dashboardPresenter.notifyItemChange();
                }
            }
        };

        // Build the final picker
        DialogsHelper.buildSublimePicker(fragment.getFragmentManager(),callback ,options);
    }
}
