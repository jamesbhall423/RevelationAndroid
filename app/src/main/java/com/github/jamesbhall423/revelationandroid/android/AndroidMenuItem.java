package com.github.jamesbhall423.revelationandroid.android;

import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;
import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;

public class AndroidMenuItem implements SelectionItemUpdater {
    private ModelMenuItem modelItem;
    private MenuItem androidItem;
    public AndroidMenuItem(ModelMenuItem modelItem, MenuItem androidItem) {
        this.androidItem = androidItem;
        this.modelItem = modelItem;
        modelItem.setUpdater(this);
        update();
    }

    @Override
    public void update() {
        androidItem.setTitle(modelItem.display());
        androidItem.setEnabled(modelItem.enabled());
    }
}
