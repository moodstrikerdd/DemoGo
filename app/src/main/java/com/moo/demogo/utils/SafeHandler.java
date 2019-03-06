package com.moo.demogo.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class SafeHandler extends Handler {
    private WeakReference<Activity> activityWeakReference;

    public SafeHandler(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = activityWeakReference.get();

    }
}
