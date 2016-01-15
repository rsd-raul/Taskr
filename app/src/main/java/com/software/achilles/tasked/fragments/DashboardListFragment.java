package com.software.achilles.tasked.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.domain.Task;
import com.software.achilles.tasked.domain.TaskList;
import com.software.achilles.tasked.util.Constants;
import java.util.List;

public class DashboardListFragment extends Fragment {

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

//    private ArrayAdapter<Task> adapter;
    private TaskList taskList;

    // ------------------------- Constructor -------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Retrieve the TaskList from the Activity
        taskList = (TaskList) getArguments().getSerializable(Constants.TASK_LIST + "");

        // Recupero las listas
        List<Task> tasks = taskList.getTasks();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        setupRecyclerView(recyclerView, tasks);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Task> tasks) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(
                getActivity(), tasks));
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Task> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(R.id.task_title);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position).getTitle();
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Task> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_task, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position).getTitle();
            holder.mTextView.setText(mValues.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, CheeseDetailActivity.class);
//                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
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
