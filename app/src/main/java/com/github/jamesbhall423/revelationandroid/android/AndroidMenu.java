package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidMenu {
    private Map<MenuItem,AndroidMenuItem> correlator = new HashMap<>();
    private List<AndroidMenuItem> list = new ArrayList<>();
    public AndroidMenu(Menu menu, CMap map, BoxModel model, GameActivity context) {
        List<ModelMenuItem> items = model.menu();
        for (ModelMenuItem item: items) {
            MenuItem displayItem = menu.add(item.display());
            AndroidMenuItem next = new AndroidModelMenuItem(item,displayItem, context);
            list.add(next);
            correlator.put(displayItem,next);
        }
        MenuItem notificationDisplay = menu.add("");
        NotificationViewer notificationViewer = new NotificationViewer(model,notificationDisplay,context);
        correlator.put(notificationDisplay,notificationViewer);
        list.add(notificationViewer);
    }
    public void updateItems() {
        for (AndroidMenuItem item: list) item.update();
    }
    public void optionSelected(MenuItem item) {
        correlator.get(item).doClick();
    }
}
