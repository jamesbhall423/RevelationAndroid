package com.github.jamesbhall423.revelationandroid.model;

import java.util.ArrayList;
import java.util.List;

public class ModelMenuItem extends SelectionItem {
    private CAction action;
    public ModelMenuItem(CAction action, int number, BoxModel model, boolean alwaysActionable) {
        super(action.getName(),number,model,alwaysActionable);
        this.action = action;
    }
    public boolean executeAction(Object details) {
        int player = model.player();
        int startTime = model.getTime();
        model.distribute(action.create(player, startTime));
        model.distribute(CAction.TURN.create(player, startTime));
        return true;
    }
    public CAction getAction() {
        return action;
    }
    public void doClick() {
        // View needs to update itself after calling this method
        doClick(null);
    }
    public static List<ModelMenuItem> createMenu(CMap map, int pl, BoxModel model) {
        List<ModelMenuItem> items = new ArrayList<>();
        Player player = map.players[pl];
        items.add(new ModelMenuItem(CAction.EXIT, -1, model,true));
        //items.add(new ModelMenuItem(CAction.SHOW_REVERTS, player.showReverts, model,false));
        items.add(new ModelMenuItem(CAction.DECLARE, player.declareVictories, model,false));
        return items;
    }
}
