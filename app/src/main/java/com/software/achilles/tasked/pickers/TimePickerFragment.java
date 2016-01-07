package com.software.achilles.tasked.pickers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
                                implements TimePickerDialog.OnTimeSetListener {

    private Date time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve the task due date if exists
        Bundle bundle = getArguments();

        // If there is no due date, set the current time as the default values for the picker
        if(bundle == null || bundle.isEmpty()) {
            time = new Date(Calendar.getInstance().getTimeInMillis());
        } else
            time = (Date) bundle.getSerializable("date");

        int hour = time.getHours();
        int minute = time.getMinutes();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Date dueDate = time;

        // Update the Controller with the new time
        dueDate.setHours(hourOfDay);
        dueDate.setMinutes(minute);

        // Launch the Date Picker
        showDatePickerDialog(dueDate);
    }

    private void showDatePickerDialog(Date date) {

        // Creation of the datePicker
        DatePickerFragment newFragment =  new DatePickerFragment();

        // Saving the time in a bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("dueDateWithTime", date);

        // Sending the bundle to the fragment
        newFragment.setArguments(bundle);

        // Showing the picker
        newFragment.show(getFragmentManager(), "datePicker");
    }

}