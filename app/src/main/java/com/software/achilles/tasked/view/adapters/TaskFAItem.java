package com.software.achilles.tasked.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.presenter.DashboardPresenter;
import com.software.achilles.tasked.util.Constants;
import com.software.achilles.tasked.util.helpers.DialogsHelper;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TaskFAItem extends AbstractItem<TaskFAItem, TaskFAItem.ViewHolder>{

    // ------------------------- Attributes --------------------------

    private Task task;

    // -------------------------- Injected ---------------------------

    DashboardPresenter dashboardPresenter;

    // ------------------------- Constructor -------------------------

    @Inject
    public TaskFAItem(DashboardPresenter dashboardPresenter) {
        this.dashboardPresenter = dashboardPresenter;
    }

    // -------------------------- Use Cases --------------------------

    public String getTitle(){
        return task.getTitle();
    }
    public Date getSortDueDate(){
        Date due = task.getDue();
        return due == null ? new Date() : due;
    }

    public TaskFAItem withTask(Task task){
        this.task = task;
        this.mIdentifier = task.getId();
        return this;
    }

    private int firstOrLast = 0;

    public void withFirstOrLast(int firstOrLast){
        this.firstOrLast = firstOrLast;
    }

    private boolean isFirst(){
        return firstOrLast == Constants.FIRST;
    }
    private boolean isLast(){
        return firstOrLast == Constants.LAST;
    }


    //The unique ID for this type of item // TODO What?
    @Override
    public int getType() {
        return R.id.taskLinearLayout;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.task_fa_item;
    }

    @Override
    public FastAdapter.OnClickListener<TaskFAItem> getOnItemClickListener() {
        return new FastAdapter.OnClickListener<TaskFAItem>(){
            @Override
            public boolean onClick(View v, IAdapter<TaskFAItem> adapter, TaskFAItem item, int position) {
                dashboardPresenter.itemOnClick(mIdentifier);
                return false;
            }
        };
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        // --------------------------- Margin ----------------------------

        // If the item is at the bottom or/and at the end, add some margin
        int top = isFirst() ? 24 : 0, bottom = isLast() ? 24 : 0;
        ((MarginLayoutParams) viewHolder.itemView.getLayoutParams()).setMargins(0, top, 0, bottom);

        // ------------------------ Check Control ------------------------

        // Checked if task is done
        viewHolder.doneCheck.setChecked(task.isCompleted());

        // On click update the status of the Task
        viewHolder.doneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardPresenter.taskModifier(Constants.DASH_DONE, task);
            }
        });

        // ------------------------ Text Control -------------------------

        viewHolder.textView.setText(task.getTitle());

        // ------------------------ Star Control -------------------------

        // Checked if task is favourite
        viewHolder.starCheck.setChecked(task.isStarred());

        // On click update the status of the Task
        viewHolder.starCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardPresenter.taskModifier(Constants.DASH_FAVE, task);
            }
        });

        // ------------------------ Date Control -------------------------

        // Get the context
        final Context context = viewHolder.itemView.getContext();

        final Date dueDate = task.getDue();
        ImageButton alarm = viewHolder.alarmImage;

        // Change icon and color if an alarm is set
        int drawRes = dueDate != null ? R.drawable.ic_time_alarm : R.drawable.ic_time_clean;
        alarm.setImageDrawable(ContextCompat.getDrawable(context, drawRes));

        int colRes = dueDate != null ? R.color.amberDateDark : R.color.task_modifier_icons_dash;
        int color = ContextCompat.getColor(context, colRes);
        alarm.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        // Launch Date Picker on touch and update the task
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogsHelper.buildDateTimePicker(mIdentifier, dueDate, dashboardPresenter);
            }
        });


    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView textView;
        protected CheckBox doneCheck;
        protected CheckBox starCheck;
        protected ImageButton alarmImage ;

        public ViewHolder(View view) {
            super(view);
            doneCheck = (CheckBox) view.findViewById(R.id.checkbox);
            textView = (TextView) view.findViewById(R.id.task_title);
            alarmImage = (ImageButton) view.findViewById(R.id.button_time);
            starCheck = (CheckBox) view.findViewById(R.id.checkbox_favourite);
        }
    }

    // --------------------------- FACTORY ---------------------------

    /**
     *  the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
     */
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    /**
     * return our ViewHolderFactory implementation here
     */
    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }
}
