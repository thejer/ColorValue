package com.google.developer.colorvalue.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Driver;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

import static android.content.ContentValues.TAG;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;
    private static final int PRACTICE_PENDING_INTENT_ID = 3417;


    private AsyncTask mNotificationBackgroundTask;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mNotificationBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = NotificationJobService.this;
                remindUserForPractice(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };
        mNotificationBackgroundTask.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if(mNotificationBackgroundTask != null) {
            mNotificationBackgroundTask.cancel(true);
        }
        return true;
    }

    public static void remindUserForPractice(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    context.getString(R.string.notification_channel_id),
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_info)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_info))
                        .setContentTitle(context.getString(R.string.time_to_practice))
                        .setContentText(context.getString(R.string.it_is_time_to_practice))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                context.getString(R.string.it_is_time_to_practice)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startMainActivity = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                PRACTICE_PENDING_INTENT_ID,
                startMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }


}