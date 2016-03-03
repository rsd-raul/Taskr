package com.software.achilles.tasked.view.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Date;

public class DatePickerFragment extends DialogFragment
                                implements DatePickerDialog.OnDateSetListener {
    private Date date = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve the task due date if exists
        Bundle bundle = getArguments();
        if(bundle != null)
            date = (Date) bundle.getSerializable("dueDateWithTime");

        int year = date.getYear() + 1900;   // Correcting the Util.Date fails
        int month = date.getMonth();
        int day = date.getDate();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

//        // Update the controller
//        Date dueDate = date;
//        dueDate.setYear(year - 1900);   // Correcting Util.Date fails
//        dueDate.setMonth(month);
//        dueDate.setDate(day);
//        TaskController.taskDate = dueDate;
//
//        // Reflect the changes
//        TopFragment.dateToButton(getActivity());
    }
}