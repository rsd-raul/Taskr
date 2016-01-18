package com.software.achilles.tasked.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.domain.Task;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardListFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    // ------------------------- Constructor -------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO si en un futuro no usas TaskList pasar a List<Task> directamente
        // Retrieve the TaskList from the Activity
        TaskList taskList = getArguments().getParcelable(Constants.TASK_LIST+"");

        List<Task> tasks;
        // Retrieve the Tasks from the task
        if(taskList!=null)
            tasks = taskList.getTasks();
        else {
            tasks = new ArrayList<>();
            Log.e("DashboardListFragment", "taskList was null and was initialized");
        }

        // Setup the recycler view with the list of Tasks
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        setupRecyclerView(recyclerView, tasks);

        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Task> tasks) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), tasks));
    }


    // ---------------------- Internal Adapter -----------------------

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        // --------------------------- Values ----------------------------

        // ------------------------- Attributes --------------------------

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Task> mListOfTasks;
        private Context mContext;

        // ------------------------- Constructor -------------------------

        public SimpleStringRecyclerViewAdapter(Context context, List<Task> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            this.mContext = context;
            mBackground = mTypedValue.resourceId;
            mListOfTasks = items;
        }

        // ------------------------- View Holder -------------------------

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // We inflate the layout that will be each one of the Items (Tasks) on the recycler
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_task, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // TODO 1 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
//            if(position == 0)
//                ((ViewGroup.MarginLayoutParams) holder.mLinear.getLayoutParams()).setMargins(0,24,0,0);
//            if(position == mListOfTasks.size()-1)
//                ((ViewGroup.MarginLayoutParams) holder.mLinear.getLayoutParams()).setMargins(0,0,0,24);

            final Task task = mListOfTasks.get(position);
            holder.mBoundString = task.getTitle();

            // ------------------------ View Control -------------------------

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, CheeseDetailActivity.class);
//                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                    context.startActivity(intent);
                    Snackbar.make(view, "Clicked: " + task.getId(), Snackbar.LENGTH_LONG).show();
                }
            });

            // ------------------------ Check Control ------------------------

            CheckBox checkDone = holder.mCheckDone;

            // Checked if task is done
            if (task.getFinished())
                checkDone.setChecked(true);

            // On click update the status of the Task
            checkDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Status clicked", Snackbar.LENGTH_LONG).show();
                }
            });

            // ------------------------ Text Control -------------------------

            holder.mTextView.setText(task.getTitle());

            // ------------------------ Star Control -------------------------

            CheckBox checkStar = holder.mCheckStar;

            // Checked if task is favourite
            if (task.getStarred())
                checkStar.setChecked(true);

            // On click update the status of the Task
            checkStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Star clicked", Snackbar.LENGTH_LONG).show();
                }
            });

            // ------------------------ Date Control -------------------------

            Date dueDate = task.getDueDate();
            ImageButton alarm = holder.mAlarm;

            // Change icon and color if an alarm is set
            if (dueDate != null) {
                alarm.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_time_alarm));
                int dateColor = ContextCompat.getColor(mContext, R.color.amberDateDark);
                alarm.getDrawable().setColorFilter(dateColor, PorterDuff.Mode.SRC_IN);
            }

            // Launch Date Picker on touch and update the task
            alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Alarm clicked", Snackbar.LENGTH_LONG).show();
                }
            });

        }

        // -------------------------- Use Cases --------------------------

        @Override
        public int getItemCount() {
            return mListOfTasks.size();
        }

        public String getValueAt(int position) {
            return mListOfTasks.get(position).getTitle();
        }


        // -------------------- Internal ViewHolder ----------------------

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final CheckBox mCheckDone;
            public final TextView mTextView;
//            public final ImageButton mPlace;
            public final ImageButton mAlarm;
            public final CheckBox mCheckStar;

            // TODO 2 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
            public final LinearLayout mLinear;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mCheckDone = (CheckBox) view.findViewById(R.id.checkbox);
                mTextView = (TextView) view.findViewById(R.id.task_title);
//                mPlace = (ImageButton) view.findViewById(R.id.button_location);
                mAlarm = (ImageButton) view.findViewById(R.id.button_time);
                mCheckStar = (CheckBox) view.findViewById(R.id.checkbox_favourite);

                // TODO 3 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
                mLinear = (LinearLayout) view.findViewById(R.id.taskLinearLayout);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }
    }











//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//
//        sTasks = TaskController.getInstance().getTasks();
//        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, sTasks);
//        setListAdapter(this.adapter);
//    }

    // -------------------------- Use Cases --------------------------

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id){
//
//        if(TaskController.firstTime)
//            return;
//
//        //  Intent creation
//        Intent intent = new Intent(getActivity(), TaskEdit.class);
//
//        //  Retrieve and save data in the bundle
//        Task selectedTask = sTasks.get(position);
//        intent.putExtra("selectedTask", selectedTask);
//        intent.putExtra("selectedPosition", position);
//
//        //  Start activity
//        startActivity(intent);
//    }

//    public void refreshList(){
//        this.adapter.notifyDataSetChanged();
//    }
}
