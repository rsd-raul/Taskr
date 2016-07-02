package com.software.achilles.tasked.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import javax.inject.Inject;

public class TaskDetailAdapter extends AbstractItem<TaskDetailAdapter, TaskDetailAdapter.ViewHolder> {

    // ------------------------- Attributes --------------------------

    // -------------------------- Injected ---------------------------

    TaskCreationPresenter taskCreationPresenter;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskDetailAdapter(TaskCreationPresenter taskCreationPresenter) {
        this.taskCreationPresenter = taskCreationPresenter;
    }

    // -------------------------- Use Cases --------------------------
//
//    public TaskDetailAdapter withTask(Task task){
//        this.task = task;
//        return this;
//    }

    //The unique ID for this type of item // TODO What?
    @Override
    public int getType() {
        return R.id.taskDetailLinearLayout;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_detail;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageButton detailIcon;
        protected TextView textView;
        protected ImageButton expandIcon;

        public ViewHolder(View view) {
            super(view);
            detailIcon = (ImageButton) view.findViewById(R.id.detailIcon);
            textView = (TextView) view.findViewById(R.id.detail_title);
            expandIcon = (ImageButton) view.findViewById(R.id.expandButton);
        }
    }
}
