package com.github.jamesbhall423.revelationandroid.android.mapmaker;


import android.view.Menu;
import android.view.MenuItem;

import com.github.jamesbhall423.revelationandroid.android.ModelClickListener;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

public class MapmakerBar implements ModelClickListener {
    private BoxModel model;
    private MapMaker context;
    private Map<MenuItem, SquareChanger> correlator = new HashMap<>();
    private SquareChanger squareChanger;
    private MenuItem selected;
    public MapmakerBar(Toolbar toolbar, BoxModel model, final MapMaker context) {
        this.model = model;
        this.context = context;
        Menu menu = toolbar.getMenu();
        menu.add("EXIT");
        addRoadMenuItem(menu,"ROAD LEFT", SquareModel.LEFT);
        addRoadMenuItem(menu,"ROAD RIGHT", SquareModel.RIGHT);
        addRoadMenuItem(menu,"ROAD UP", SquareModel.UP);
        addRoadMenuItem(menu,"ROAD DOWN", SquareModel.DOWN);
        for (TypeChanger typeChanger: TypeChanger.types()) {
            correlator.put(menu.add("TYPE "+typeChanger.type().toString().toUpperCase()),new BasicSquareChanger(typeChanger));
        }
        addPlayerMenuItem(menu,"PLAYER EMPTY", 0);
        addPlayerMenuItem(menu, "PLAYER 1 HORIZONTAL",1);
        addPlayerMenuItem(menu, "PLAYER 2 VERTICAL", -1);
        correlator.put(menu.add("TELEPORTER"),new TeleporterChanger(context,model));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (selected!=null) selected.setTitle(selected.getTitle().toString().toUpperCase());
                selected = item;
                if (item.getTitle().toString().equals("EXIT")) context.saveAndExit();
                else if (correlator.containsKey(item)) {
                    squareChanger = correlator.get(item);
                }
                item.setTitle(selected.getTitle().toString().toLowerCase());
                return true;
            }
        });
    }

    private void addPlayerMenuItem(Menu menu, String name, int value) {
        correlator.put(menu.add(name),new BasicSquareChanger(new PlayerContentsChanger(value)));
    }

    private void addRoadMenuItem(Menu menu, String name, int value) {
        correlator.put(menu.add(name),new RoadChanger(model,value));
    }

    @Override
    public void doClick(int modelX, int modelY) {
        if (squareChanger!=null) {
            squareChanger.alterSquare(model.getModelSquare(modelX,modelY));
        }
    }
}
