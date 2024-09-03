package com.github.jamesbhall423.revelationandroid.android;

import android.widget.RadioButton;
import android.widget.TextView;

import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;


public class AndroidSelectionItem implements SelectionItemUpdater {
    private RadioButton view;
    private SquareClickType clickType;
    public AndroidSelectionItem(RadioButton view, SquareClickType clickType) {
        this.view = view;
        this.clickType = clickType;
        clickType.setUpdater(this);
        update();
    }

    @Override
    public void update() {
        view.setText(clickType.display());
        view.setEnabled(clickType.enabled());
    }
}
