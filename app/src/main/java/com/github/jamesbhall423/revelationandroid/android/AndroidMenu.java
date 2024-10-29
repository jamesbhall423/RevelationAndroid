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
        for (ModelMenuItem item: items) if (item.getAction()!= CAction.EXIT) {
            MenuItem displayItem = menu.add(item.display());
            AndroidMenuItem next = new AndroidModelMenuItem(item,displayItem, context);
            list.add(next);
            correlator.put(displayItem,next);
        }
        addTextDisplayViewer(menu,model,context,"notifications",GameActivity.DISPLAY_NOTIFICATIONS);
        addTextDisplayViewer(menu,model,context,"instructions",GameActivity.DISPLAY_INSTRUCTIONS);
        addTextDisplayViewer(menu,model,context,"game",GameActivity.DISPLAY_MAIN);
    }
    private void addTextDisplayViewer(Menu menu, BoxModel model, GameActivity context, String name, int displayRef) {
        MenuItem next = menu.add(name);
        TextDisplayViewer viewer = new TextDisplayViewer(next,context,name,displayRef);
        correlator.put(next,viewer);
        list.add(viewer);

    }
    public void updateItems() {
        for (AndroidMenuItem item: list) item.update();
    }
    public void optionSelected(MenuItem item) {
        correlator.get(item).doClick();
    }
}
