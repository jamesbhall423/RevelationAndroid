package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.widget.RadioButton;

import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;


public class AndroidSelectionItem implements SelectionItemUpdater {
    private RadioButton view;
    private SquareClickType clickType;
    private Activity context;
    public AndroidSelectionItem(RadioButton view, SquareClickType clickType, Activity context) {
        this.view = view;
        this.clickType = clickType;
        clickType.setUpdater(this);
        this.context = context;
        update();
    }

    @Override
    public void update() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(clickType.display());
                view.setEnabled(clickType.enabled());
            }
        });

    }
}
