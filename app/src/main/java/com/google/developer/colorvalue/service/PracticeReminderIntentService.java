package com.google.developer.colorvalue.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class PracticeReminderIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String TAG = PracticeReminderIntentService.class.getSimpleName();

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss_notification";
    static final String ACTION_PRACTICE_REMINDER = "practice_reminder";

    public PracticeReminderIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            String action = intent.getAction();
            if (ACTION_DISMISS_NOTIFICATION.equals(action)){
                NotificationJobService.clearAllNotifications(this);
            }else if (ACTION_PRACTICE_REMINDER.equals(action)){
                NotificationJobService.remindUserForPractice(this);
            }
        }
    }
}
