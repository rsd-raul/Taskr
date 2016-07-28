package com.software.achilles.tasked.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.presenter.TaskCreationPresenter;
import javax.inject.Inject;

public class TaskDetailFAItem extends AbstractItem<TaskDetailFAItem, TaskDetailFAItem.ViewHolder>{

    // ------------------------- ATTRIBUTES --------------------------

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

    public TaskDetailFAItem withConfigure(int detailType, String textView){
        this.mIdentifier = detailType;
        this.textView = textView;
        return this;
    }

    // --------------------------- SETTERS ---------------------------

//    public void setDescription(String description, View v){
////        taskCreationPresenter.setDescription(description);
//
//        String text = description.length() > 0 ? description :
//                                              context.getString(R.string.ask_for_description);
//        getFactory().create(v).textView.setText(text);
//    }
//
//    public void setLabels(Integer[] labelsIndex, String labelsString, View v){
////        taskCreationPresenter.setLabels(labelsIndex);
//
//        getFactory().create(v).textView.setText(labelsString);
//    }

    // ------------------------- VIEW HOLDER -------------------------

    //The unique ID for this type of item //TODO What?
    @Override
    public int getType() {
        return R.id.list_detail;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.task_detail_fa_item;
    }

    @Override
    public FastAdapter.OnClickListener<TaskDetailFAItem> getOnItemClickListener() {
        return new FastAdapter.OnClickListener<TaskDetailFAItem>(){
            @Override
            public boolean onClick(View v, IAdapter<TaskDetailFAItem> adapter, TaskDetailFAItem item, int position) {
                taskCreationPresenter.itemOnClick(((int) mIdentifier));
                return false;
            }
        };
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
        switch (((int) mIdentifier)){
            case R.id.button_description:
                drawRes = R.drawable.ic_description;
                colRes = R.color.md_black_1000;
                break;
            case R.id.button_time:
                drawRes = R.drawable.ic_time_alarm;
                colRes = R.color.amberDateDark;
                break;
            case R.id.button_label:
                drawRes = R.drawable.ic_label_outline;
                colRes = R.color.colorPrimaryDark;
                break;
            case R.id.button_location:
                drawRes = R.drawable.ic_place;
                colRes = R.color.tealLocationDark;
                break;
            default:
                Log.e("TaskDetailItem", "Unsupported item type: " + ((int) mIdentifier));
                throw new UnsupportedOperationException();
        }

        int color = ContextCompat.getColor(context, colRes);
        viewHolder.detailIcon.setImageDrawable(ContextCompat.getDrawable(context, drawRes));
        viewHolder.detailIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageButton detailIcon;
        protected TextView textView;

        public ViewHolder(View view) {
            super(view);
            detailIcon = (ImageButton) view.findViewById(R.id.detailIcon);
            textView = (TextView) view.findViewById(R.id.detail_title);
        }
    }

    // --------------------------- FACTORY ---------------------------

    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }
}
