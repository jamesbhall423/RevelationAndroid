package com.github.jamesbhall423.revelationandroid.android;

import android.view.MenuItem;

public class TextDisplayViewer implements AndroidMenuItem {
    private GameActivity displayer;
    private MenuItem androidItem;
    private String name;
    private int displayRef;
    public TextDisplayViewer(MenuItem androidItem, GameActivity displayer, String name, int displayRef) {
        this.displayer = displayer;
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
