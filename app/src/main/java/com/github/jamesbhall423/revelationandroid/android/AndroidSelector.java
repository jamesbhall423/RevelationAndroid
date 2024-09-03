package com.github.jamesbhall423.revelationandroid.android;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;

import java.util.HashMap;
import java.util.List;

public class AndroidSelector implements ModelClickListener {
    private HashMap<Integer, SquareClickType> map = new HashMap<>();
    private RadioGroup view;
    public AndroidSelector(RadioGroup view, BoxModel model) {
        this.view = view;
        final List<SquareClickType> selectors = SquareClickType.clickTypes(model);
        for (SquareClickType clickType: selectors) {
            RadioButton text = new RadioButton(view.getContext());
            AndroidSelectionItem item = new AndroidSelectionItem(text,clickType);
            view.addView(text);
            map.put(text.getId(),clickType);
        }
    }
    public void doClick(int modelX, int modelY) {
        int checkedId = view.getCheckedRadioButtonId();
        if (checkedId!=-1) {
            map.get(checkedId).doClick(modelX,modelY);
        }
    }
}
