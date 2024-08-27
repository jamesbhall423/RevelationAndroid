package com.github.jamesbhall423.revelationandroid.android;

import android.widget.ListView;
import android.widget.TextView;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;

import java.util.List;

public class AndroidSelector implements ModelClickListener {
    private ListView view;
    public AndroidSelector(ListView view, BoxModel model) {
        this.view = view;
        List<SquareClickType> selectors = SquareClickType.clickTypes(model);
        for (SquareClickType clickType: selectors) {
            TextView text = new TextView(view.getContext());
            AndroidSelectionItem item = new AndroidSelectionItem(text,clickType);
            view.addFooterView(text,clickType,true);
        }
    }
    public void doClick(int modelX, int modelY) {
        if (view.getSelectedItem()!=null) ((SquareClickType)view.getSelectedItem()).doClick(modelX,modelY);
    }
}
