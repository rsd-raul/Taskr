package com.software.achilles.tasked.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.software.achilles.tasked.App;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;

import javax.inject.Inject;

public class TaskDetailAdapter extends AbstractItem<TaskDetailAdapter, TaskDetailAdapter.ViewHolder> {

    // ------------------------- Attributes --------------------------

    private int detailType;
    private String textView;

    // -------------------------- Injected ---------------------------

    TaskCreationPresenter taskCreationPresenter;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskDetailAdapter(TaskCreationPresenter taskCreationPresenter) {
        this.taskCreationPresenter = taskCreationPresenter;
    }

    // -------------------------- Use Cases --------------------------

    public TaskDetailAdapter withConfigure(int detailType, String textView){
        this.detailType = detailType;
        this.textView = textView;
        return this;
    }

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

        // Get the context
        Context context = App.getInstance();

        // Change the text
        viewHolder.textView.setText(textView);

        // Change icon and color depending on the type set
        int drawRes = -1;
        int colRes = -1;
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
        }

        viewHolder.detailIcon.setImageDrawable(ContextCompat.getDrawable(context, drawRes));
        int color = ContextCompat.getColor(context, colRes);
        viewHolder.detailIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
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
