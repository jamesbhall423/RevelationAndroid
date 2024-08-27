package com.github.jamesbhall423.revelationandroid.android;

import android.view.Menu;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;

import java.util.List;

public class MenuCreator {
    public MenuCreator(Menu menu, CMap map, BoxModel model) {
        List<ModelMenuItem> items = ModelMenuItem.createMenu(map,model.player(),model);
        for (ModelMenuItem item: items) {
            MenuItem displayItem = menu.add(item.display());
            new AndroidMenuItem(item,displayItem);
        }
    }
}
