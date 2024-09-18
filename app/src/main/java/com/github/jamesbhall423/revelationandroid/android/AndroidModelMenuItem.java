package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;

public class AndroidModelMenuItem implements AndroidMenuItem {
    private ModelMenuItem modelItem;
    private MenuItem androidItem;
    private Activity context;
    public AndroidModelMenuItem(ModelMenuItem modelItem, MenuItem androidItem, Activity context) {
        this.androidItem = androidItem;
        this.modelItem = modelItem;
        this.context = context;
        modelItem.setUpdater(this);
        update();
    }

    @Override
    public void update() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                androidItem.setTitle(modelItem.display());
                androidItem.setEnabled(modelItem.enabled());
            }
        });
    }
    public void doClick() {
        modelItem.doClick();
    }
}
