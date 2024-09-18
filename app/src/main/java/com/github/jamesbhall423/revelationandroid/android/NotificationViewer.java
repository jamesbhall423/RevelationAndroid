package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CAction;

import java.util.List;

public class NotificationViewer implements AndroidMenuItem {
    private GameActivity displayer;
    private BoxModel model;
    private MenuItem androidItem;
    private boolean isShowing = false;
    public NotificationViewer(BoxModel model, MenuItem androidItem, GameActivity displayer) {
        this.displayer = displayer;
        this.model = model;
        this.androidItem = androidItem;
        androidItem.setEnabled(true);
        update();
    }
    @Override
    public void update() {
        final String title = isShowing ? "hide notifications" : "show notifications";
        displayer.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                androidItem.setTitle(title);
            }
        });
    }

    @Override
    public void doClick() {
        isShowing = !isShowing;
        if (isShowing) {
            List<CAction> notifications = model.notifications();
            displayer.showNotifications(notifications);
        } else {
            displayer.hideNotifications();
        }
        update();
    }
}
