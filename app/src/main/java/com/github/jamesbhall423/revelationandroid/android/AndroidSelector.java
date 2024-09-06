package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AndroidSelector implements ModelClickListener {
    private HashMap<Integer, SquareClickType> map = new HashMap<>();
    private List<AndroidSelectionItem> list = new ArrayList<>();
    private RadioGroup view;
    public AndroidSelector(RadioGroup view, BoxModel model, Activity context) {
        this.view = view;
        final List<SquareClickType> selectors = SquareClickType.clickTypes(model);
        for (SquareClickType clickType: selectors) {
            RadioButton text = new RadioButton(view.getContext());
            list.add(new AndroidSelectionItem(text,clickType, context));
            view.addView(text);
            map.put(text.getId(),clickType);
        }
    }
    public void updateItems() {
        for (AndroidSelectionItem item: list) item.update();
    }
    public void doClick(int modelX, int modelY) {
        int checkedId = view.getCheckedRadioButtonId();
        if (checkedId!=-1) {
            map.get(checkedId).doClick(modelX,modelY);
        }
    }
}
