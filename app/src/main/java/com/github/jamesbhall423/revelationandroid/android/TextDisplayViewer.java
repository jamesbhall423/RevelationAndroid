package com.github.jamesbhall423.revelationandroid.android;

import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CAction;

import java.util.List;

public class TextDisplayViewer implements AndroidMenuItem {
    private GameActivity displayer;
    private BoxModel model;
    private MenuItem androidItem;
    private boolean isShowing = false;
    private String name;
    private int displayRef;
    public TextDisplayViewer(BoxModel model, MenuItem androidItem, GameActivity displayer, String name, int displayRef) {
        this.displayer = displayer;
        this.model = model;
        this.androidItem = androidItem;
        androidItem.setEnabled(true);
        this.name = name;
        this.displayRef=displayRef;
        update();
    }
    @Override
    public void update() {
        final String title = "display "+name;
        displayer.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                androidItem.setTitle(title);
            }
        });
    }

    @Override
    public void doClick() {
        displayer.showDisplay(displayRef);
        update();
    }
    public void setEnabled(boolean enabled) {
        androidItem.setEnabled(enabled);
    }
}
