package com.software.achilles.tasked.view.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Task;
import com.software.achilles.tasked.view.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class AndroidTimerService extends Service {

    // --------------------------- Values ----------------------------

    public static final long INTERVAL = 60 * 1000; // 1 minute

    // ------------------------- Attributes --------------------------

    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    // ------------------------- Constructor -------------------------

    @Override
    public void onCreate() {

        // We get a convenient moment to start the service, "convenient" as "close to hh:mm:00"
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
        cal.set(Calendar.SECOND, 0);

        final Date convenient = cal.getTime();

        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();

        // Launch scheduler on the set interval
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), convenient, INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ----------------------- Internal Class ------------------------

    class TimeDisplayTimerTask extends TimerTask {

        // --------------------------- Values ----------------------------

        NotificationService ss = new NotificationService(getApplicationContext());

        // ------------------------- Constructor -------------------------

        @Override
        public void run() {

            // Launch on another thread not to mess with the main app
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    List<Task> tasksOnMinute = AndroidTimerService.getTasksOnRange();

                    // If there is any task due
                    if(tasksOnMinute.size() !=0 ){
                        String textToShow = getString(R.string.task_due) + tasksOnMinute.size();

                        String tasks="";
                        for(Task task : tasksOnMinute)
                            tasks += task.getTitle() + " / ";

                        if(!tasksOnMinute.isEmpty())
                            tasks = tasks.replace(" / ", "");

                        NotificationService.showNotification(textToShow, tasks,
                                            MainActivity.class, R.drawable.ic_time_alarm);
                    }
                }
            });
        }
    }

    private static List<Task> getAllTasks(Date min, Date max){
        return Realm.getDefaultInstance().where(Task.class)
                .between("due", min, max).or().equalTo("due", max)
                .findAll().where()
                .equalTo("completed", false).findAll();
    }

    public static List<Task> getTasksOnRange(){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        Date min = cal.getTime();
        cal.add(Calendar.MINUTE, 1);
        Date max = cal.getTime();

        return getAllTasks(min, max);
    }
}