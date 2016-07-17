package com.software.achilles.tasked.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;

import javax.inject.Inject;

public class TaskDetailFAItem extends AbstractItem<TaskDetailFAItem, TaskDetailFAItem.ViewHolder>{

    // ------------------------- ATTRIBUTES --------------------------

    private int detailType;
    private String textView;

    // -------------------------- INJECTED ---------------------------

    TaskCreationPresenter taskCreationPresenter;
    Context context;

    // ------------------------- CONSTRUCTOR -------------------------

    @Inject
    public TaskDetailFAItem(TaskCreationPresenter taskCreationPresenter, Context context) {
        this.taskCreationPresenter = taskCreationPresenter;
        this.context = context;
    }

    // -------------------------- USE CASES --------------------------

    public TaskDetailFAItem withConfigure(int detailType, String textView){
        this.detailType = detailType;
        this.textView = textView;
        return this;
    }

    //The unique ID for this type of item //TODO What?
    @Override
    public int getType() {
        return R.id.list_detail;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.task_detail_fa_item;
    }

    //The logic to bind data to the view
    @Override
    public void bindView(final ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        // Change the text
        viewHolder.textView.setText(textView);

        // Change icon and color depending on the type set
        int drawRes, colRes;
        switch (detailType){
            case Constants.DETAIL_DESCRIPTION:
                drawRes = R.drawable.ic_description;
                colRes = R.color.md_black_1000;
                break;
            case Constants.DETAIL_ALARM:
                drawRes = R.drawable.ic_time_alarm;
                colRes = R.color.amberDateDark;
                break;
            case Constants.DETAIL_LABELS:
                drawRes = R.drawable.ic_label_outline;
                colRes = R.color.colorPrimaryDark;
                break;
            case Constants.DETAIL_LOCATION:
                drawRes = R.drawable.ic_place;
                colRes = R.color.tealLocationDark;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        int color = ContextCompat.getColor(context, colRes);
        viewHolder.detailIcon.setImageDrawable(ContextCompat.getDrawable(context, drawRes));
        viewHolder.detailIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);


        // Set the listeners for the views
        onClickListener = new FastAdapter.OnClickListener<TaskDetailFAItem>(){
            @Override
            public boolean onClick(View v, IAdapter<TaskDetailFAItem> adapter, TaskDetailFAItem item, int position) {
                taskCreationPresenter.detailOnClick(detailType, item, v);
                return false;
            }
        };
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageButton detailIcon;
        protected TextView textView;

        public ViewHolder(View view) {
            super(view);
            detailIcon = (ImageButton) view.findViewById(R.id.detailIcon);
            textView = (TextView) view.findViewById(R.id.detail_title);
        }
    }

    FastAdapter.OnClickListener<TaskDetailFAItem> onClickListener;

    // --------------------------- ON CLICK --------------------------

    @Override
    public FastAdapter.OnClickListener<TaskDetailFAItem> getOnItemClickListener() {
        return onClickListener;
    }

    public void setText(String text, View v){
        mFactory.create(v).textView.setText(text);
        taskCreationPresenter.setDescription(text);
    }
}
