package com.software.achilles.tasked.model.helpers;

import android.content.res.Resources;

import com.software.achilles.tasked.R;
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
                + (task.getDue() == null ? "" : " - " + dateToText(task.getDue()));
    }

    // -------------------------- Properties -------------------------

    public static String dateToText(Date date){
        return DateFormat.getDateInstance().format(date);
    }
}
