package com.software.achilles.tasked.util.helpers;

import android.content.Context;
import android.content.res.Resources;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.domain.TaskList;
import com.software.achilles.tasked.view.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public abstract class LocalizationHelper {

    // -------------------------- To String --------------------------

    public static String TaskListToString(TaskList taskList, MainActivity activity) {

        // Access resources from Android in order to translate at sharing
        Resources resources = activity.getResources();
        String result = resources.getString(R.string.listName) + " " + taskList.getTitle() + "\n\n";
        result += resources.getString(R.string.tasks);

        // This is supposed to be more efficient that an extended for
        List<Task> tasks = taskList.getTasks();
        for (int i = 0; i < tasks.size(); i++)
            result += "\n" + TaskToString(tasks.get(i), activity);

        return result;
    }

    public static String  TaskToString(Task task, MainActivity activity){
        Resources resources = activity.getResources();
        return (task.isCompleted() ? resources.getString(R.string.task_done)+" - " : "") + task.getTitle()
                + (task.getDue() == null ? "" : " - " + dateToDateString(task.getDue()));
    }

    // -------------------------- Properties -------------------------

    public static String dateToDateString(Date date){
        return DateFormat.getDateInstance().format(date);
    }

    public static String dateToTimeString(Date date){
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }

    public static String dateToDateTimeString(Date date){
        return dateToTimeString(date) + " - " + dateToDateString(date);
    }


    public static String filterAndFormatLabels(List<Label> labels, Integer[] which, boolean all){
        String labelsStr =  "";

        if(all)
            for(Label aux : labels)
                labelsStr += "#" + aux.getTitle() + " ";
        else
            for(int aux : which)
                labelsStr += "#" + labels.get(aux).getTitle() + " ";

        return labelsStr;
    }

    public static boolean is24HourFormat(Context context){
        return android.text.format.DateFormat.is24HourFormat(context);
    }
}
