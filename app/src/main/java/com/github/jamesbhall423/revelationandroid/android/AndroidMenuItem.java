package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;
import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;

public interface AndroidMenuItem extends SelectionItemUpdater {
    public void update();
    public void doClick();
}
