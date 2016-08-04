package com.software.achilles.tasked.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.view.adapters.TaskFAItem;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.model.managers.DataManager;
import com.software.achilles.tasked.util.Constants;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

public class DashboardListFragment extends Fragment {

    @Inject
    DataManager dataManager;
    @Inject
    FastItemAdapter<IItem> fastAdapter;
    @Inject
    Provider<TaskFAItem> taskAdapterProvider;

    // ------------------------- Constructor -------------------------

    @Inject
    public DashboardListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState) {

        // Retrieve the TaskList index from the Activity and obtain its tasks
        int posOnPager = getArguments().getInt(Constants.TASK_LIST + "");

        // Retrieve the recycler view and set the manager
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));


        // Configure the FastAdapter and set it on the RecyclerView
        recyclerView.setAdapter(fastAdapter);

        // REVIEW Is this the proper way to restart the adapter to remove duplicates?
        fastAdapter.removeItemRange(0, fastAdapter.getItemCount());

        // Populate our list
        List<Task> tasks = dataManager.findAllTasksByTaskListPosition(posOnPager);
        for (int i = 0; i < tasks.size(); i++) {
            TaskFAItem item = taskAdapterProvider.get().withTask(tasks.get(i));

            // Control the extra margins
            if(i == 0)
                item.withFirstOrLast(Constants.FIRST);
            else if(i == tasks.size()-1)
                item.withFirstOrLast(Constants.LAST);

            fastAdapter.add(item);
        }

        return recyclerView;
    }

    public void notifyChange(){
        fastAdapter.notifyAdapterDataSetChanged();
    }

//    // ---------------------- Internal Adapter -----------------------
//
//    public static class TaskRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        // --------------------------- Values ----------------------------
//
//        // ------------------------- Attributes --------------------------
//
//        @Inject
//        DashboardPresenter dashboardPresenter;
//
//        private final TypedValue mTypedValue = new TypedValue();
//        private int mBackground;
//        private List<Task> mListOfTasks;
//        private Context mContext;
//
//        // ------------------------- Constructor -------------------------
//
//        @Inject
//        public TaskRecyclerViewAdapter(){
//        }
//
//        public void setup (Context context, List<Task> items) {
//            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//            this.mContext = context;
//            mBackground = mTypedValue.resourceId;
//            mListOfTasks = items;
//        }
//
//        // ------------------------- View Holder -------------------------
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            // We inflate the layout that will be each one of the Items (Tasks) on the recycler
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.task_fa_item, parent, false);
//            view.setBackgroundResource(mBackground);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//
//            // FIXME 1 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
//            if (position == 0)
//                ((ViewGroup.MarginLayoutParams) holder.mLinear.getLayoutParams()).setMargins(0, 24, 0, 0);
//            else if (position == (mListOfTasks.size() - 1))
//                ((ViewGroup.MarginLayoutParams) holder.mLinear.getLayoutParams()).setMargins(0, 0, 0, 24);
//            else
//                ((ViewGroup.MarginLayoutParams) holder.mLinear.getLayoutParams()).setMargins(0, 0, 0, 0);
//
//            final Task task = mListOfTasks.get(position);
//            holder.mBoundString = task.getTitle();
//
//            // ------------------------ View Control -------------------------
//
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dashboardPresenter.taskModifier(Constants.DASH_TASK, task);
//                }
//            });
//
//            // ------------------------ Check Control ------------------------
//
//            CheckBox checkDone = holder.mCheckDone;
//
//            // Checked if task is done
//            checkDone.setChecked(task.isCompleted());
//
//            // On click update the status of the Task
//            checkDone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dashboardPresenter.taskModifier(Constants.DASH_DONE, task);
//                }
//            });
//
//            // ------------------------ Text Control -------------------------
//
//            holder.mTextView.setText(task.getTitle());
//
//            // ------------------------ Star Control -------------------------
//
//            CheckBox checkStar = holder.mCheckStar;
//
//            // Checked if task is favourite
//            checkStar.setChecked(task.isStarred());
//
//            // On click update the status of the Task
//            checkStar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dashboardPresenter.taskModifier(Constants.DASH_FAVE, task);
//                }
//            });
//
//            // ------------------------ Date Control -------------------------
//
//            Date dueDate = task.getDue();
//            ImageButton alarm = holder.mAlarm;
//
//            // Change icon and color if an alarm is set
//            int drawRes = dueDate != null ? R.drawable.ic_time_alarm : R.drawable.ic_time_clean;
//            alarm.setImageDrawable(ContextCompat.getDrawable(mContext, drawRes));
//
//            int colRes = dueDate != null ? R.color.amberDateDark : R.color.task_modifier_icons_dash;
//            int color = ContextCompat.getColor(mContext, colRes);
//            alarm.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//
//            // Launch Date Picker on touch and update the task
//            alarm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Alarm clicked", Snackbar.LENGTH_LONG).show();
//                }
//            });
//
//        }
//
//        // -------------------------- Use Cases --------------------------
//
//        @Override
//        public int getItemCount() {
//            return mListOfTasks.size();
//        }
//
//
//    }
//
//
//
//
//
//    // -------------------- Internal ViewHolder ----------------------
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public String mBoundString;
//
//        private View mView;
//        private CheckBox mCheckDone;
//        private TextView mTextView;
//        //            public final ImageButton mPlace;
//        private ImageButton mAlarm;
//        private CheckBox mCheckStar;
//
//        // FIXME 2 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
//        public final LinearLayout mLinear;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mCheckDone = (CheckBox) view.findViewById(R.id.checkbox);
//            mTextView = (TextView) view.findViewById(R.id.task_title);
////                mPlace = (ImageButton) view.findViewById(R.id.button_location);
//            mAlarm = (ImageButton) view.findViewById(R.id.button_time);
//            mCheckStar = (CheckBox) view.findViewById(R.id.checkbox_favourite);
//
//            // FIXME 3 of 3 - This adds the 8dp margin to the top of the list... But it's not properly done
//            mLinear = (LinearLayout) view.findViewById(R.id.taskLinearLayout);
//        }
//
//
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mTextView.getText();
//        }
//    }
}