package com.software.achilles.tasked.domain;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.listeners.FloatingActionMenuConfigurator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskList extends BasicType implements Parcelable {

    // --------------------------- Values ----------------------------



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //TODO el bug esta aqui, pero puede que TaskList no tenga que ser Parcelable si recuperamos las cosas de la BD
//        dest.writeInt(this.getId());
//        dest.writeString(this.getTitle());
//        dest.writeList(this.tasks);
    }

//    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//        public TaskList createFromParcel(Parcel in) {
//            return new TaskList(in.readInt(), in.readString(), in.readList(new ArrayList(), TaskList.CREATOR);
//        }
//
//        public TaskList[] newArray(int size) {
//            return new TaskList[size];
//        }
//    };

    // ------------------------- Attributes --------------------------

    private List<Task> tasks;

    // ------------------------- Constructor -------------------------

    public TaskList(int id, String title, List<Task> tasks) {
        setId(id);
        setTitle(title);
        this.tasks = tasks;
    }



    // ---------------------- Getters & Setters ----------------------


    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // -------------------------- To String --------------------------

    @Override
    public String toString() {

        // Access resources from Android in order to translate at sharing
        Resources resources = FloatingActionMenuConfigurator.activity.getResources();
        String result = resources.getString(R.string.listName) + " " + getTitle() + "\n\n";
        result += resources.getString(R.string.tasks);

        // This is supposed to be more efficient that an extended for
        List<Task> tasks = getTasks();
        for (int i = 0; i < tasks.size(); i++)
            result += "\n" + tasks.get(i).toString();

        return result;
    }

    // ------------------------ Other methods ------------------------
}
