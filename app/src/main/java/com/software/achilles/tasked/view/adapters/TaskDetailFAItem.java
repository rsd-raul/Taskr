package com.software.achilles.tasked.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.helpers.LocalizationHelper;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import com.software.achilles.tasked.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class TaskDetailFAItem extends AbstractItem<TaskDetailFAItem, TaskDetailFAItem.ViewHolder>
        implements IExpandable<TaskDetailFAItem, IItem> {

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
        int color = ContextCompat.getColor(context, colRes);
        viewHolder.detailIcon.setImageDrawable(ContextCompat.getDrawable(context, drawRes));
        viewHolder.detailIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        // Set the expansion icon and its listener
        viewHolder.expandIcon.clearAnimation();
        ViewCompat.setRotation(viewHolder.expandIcon, isExpanded() ? 180 : 0);

        // Set the listeners for the views
        viewHolder.detailIcon.setOnClickListener(itemListener);
        viewHolder.textView.setOnClickListener(itemListener);
        viewHolder.expandIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.itemView.performClick();
            }
        });
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

    // -------------------------- SUB ITEMS --------------------------

    private List<IItem> mSubItems;

    @Override
    public List<IItem> getSubItems() {
        return mSubItems;
    }

    @Override
    public TaskDetailFAItem withSubItems(List<IItem> subItems) {
        this.mSubItems = subItems != null ? subItems : new ArrayList<IItem>();
        return this;
    }

    public TaskDetailFAItem addSubItem (IItem item){
        if(mSubItems == null)
            withSubItems(null);

        mSubItems.add(item);
        return this;
    }

    // -------------------------- EXPANSION --------------------------

    private boolean mExpanded = false;

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public TaskDetailFAItem withIsExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    @Override
    public boolean isAutoExpanding() {
        return true;
    }

    //TODO Playing with expansion
    public void expandTaskDetails(View v){
        ViewHolder viewHolder = getFactory().create(v);

        switch (detailType){
            case Constants.DETAIL_DESCRIPTION:
                break;
            case Constants.DETAIL_ALARM:
                break;
            case Constants.DETAIL_LABELS:
                break;
            case Constants.DETAIL_LOCATION:

                viewHolder.textView.setText(String.format("Updated at %s", LocalizationHelper.dateToTimeString(new Date())));
                break;
        }
    }

    // --------------------------- ON CLICK --------------------------

    @Override
    public FastAdapter.OnClickListener<TaskDetailFAItem> getOnItemClickListener() {
        return new FastAdapter.OnClickListener<TaskDetailFAItem>() {
            @Override
            public boolean onClick(View v, IAdapter<TaskDetailFAItem> adapter, TaskDetailFAItem item, int pos) {

                // If the detail has more info rotate the icon 180º
                boolean moreInfo = false;
                if (item.getSubItems() != null) {
                    int rotation = item.isExpanded() ? 180 : 0;

                    ViewCompat.animate(v.findViewById(R.id.expandButton)).rotation(rotation).start();

                    moreInfo = true;
                }

                if (!isExpanded())
                    item.expandTaskDetails(v);

                // And if the item has a custom onClickListener call it
//                return mOnClickListener != null ? mOnClickListener.onClick(v, adapter, item, pos) : moreInfo;
                return moreInfo;
            }
        };
    }

    private View.OnClickListener itemListener =
        new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "OnItemClick", Toast.LENGTH_SHORT).show();
            }
        };
}
