package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;
import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;

public class AndroidMenuItem implements SelectionItemUpdater {
    private ModelMenuItem modelItem;
    private MenuItem androidItem;
    private Activity context;
    public AndroidMenuItem(ModelMenuItem modelItem, MenuItem androidItem, Activity context) {
        this.androidItem = androidItem;
        this.modelItem = modelItem;
        modelItem.setUpdater(this);
        this.context = context;
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
}
