package com.github.jamesbhall423.revelationandroid.android;

import android.view.Menu;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidMenu {
    private Map<MenuItem,ModelMenuItem> correlator = new HashMap<>();
    public AndroidMenu(Menu menu, CMap map, BoxModel model) {
        List<ModelMenuItem> items = ModelMenuItem.createMenu(map,model.player(),model);
        for (ModelMenuItem item: items) {
            MenuItem displayItem = menu.add(item.display());
            new AndroidMenuItem(item,displayItem);
            correlator.put(displayItem,item);
        }
    }
    public void optionSelected(MenuItem item) {
        correlator.get(item).doClick();
    }
}
