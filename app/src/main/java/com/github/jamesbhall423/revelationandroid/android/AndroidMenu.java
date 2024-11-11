package com.github.jamesbhall423.revelationandroid.android;


import android.view.Menu;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidMenu {
    private Map<MenuItem,AndroidMenuItem> correlator = new HashMap<>();
    private List<AndroidMenuItem> list = new ArrayList<>();
    public AndroidMenu(Menu menu, BoxModel model, GameActivity context) {
        List<ModelMenuItem> items = model.menu();
        for (ModelMenuItem item: items)if (item.getAction()!= CAction.EXIT) {
            MenuItem displayItem = menu.add(item.display());
            AndroidMenuItem next = new AndroidModelMenuItem(item,displayItem, context);
            list.add(next);
            correlator.put(displayItem,next);
        }
        addTextDisplayViewer(menu, context,"notifications",GameActivity.DISPLAY_NOTIFICATIONS);
        addTextDisplayViewer(menu, context,"instructions",GameActivity.DISPLAY_INSTRUCTIONS);
        addTextDisplayViewer(menu, context,"game",GameActivity.DISPLAY_MAIN);
        addExitMenuItem(menu,context);
    }
    private void addTextDisplayViewer(Menu menu, GameActivity context, String name, int displayRef) {
        MenuItem next = menu.add(name);
        TextDisplayViewer viewer = new TextDisplayViewer(next,context,name,displayRef);
        correlator.put(next,viewer);
        list.add(viewer);

    }
    private void addExitMenuItem(Menu menu, GameActivity context) {
        MenuItem next = menu.add("exit game");
        AndroidMenuItem item = new ExitMenuItem(context);
        correlator.put(next,item);
        list.add(item);
    }
    public void updateItems() {
        for (AndroidMenuItem item: list) item.update();
    }
    public void optionSelected(MenuItem item) {
        correlator.get(item).doClick();
    }
}
