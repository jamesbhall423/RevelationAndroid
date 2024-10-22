package com.github.jamesbhall423.revelationandroid.model;


public abstract class SelectionItem {
    protected int number;
    protected BoxModel model;
    protected SelectionItemUpdater updater = null;
    protected boolean enabled = true;
    protected boolean alwaysActionable;
    private String actionName;
    public SelectionItem(String actionName,int number, BoxModel model, boolean alwaysActionable) {
        this.number = number;
        this.model = model;
        this.alwaysActionable = alwaysActionable;
        this.actionName = actionName;
    }
    public void setUpdater(SelectionItemUpdater updater) {
        this.updater = updater;
    }
    public int numLeft() {
        return number;
    }
    public String display() {
        if (number>0) return actionName+"-"+number;
        else return actionName;
    }
    public boolean enabled() {
        if (alwaysActionable) return true;
        else return model.responsive()&&enabled&&number>0;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (updater!=null) {
            updater.update();
        }
    }
    public void updateGraphics() {
        if (updater!=null) updater.update();
    }
    public void doClick(Object details) {
        if (enabled()) {
            if (executeAction(details)&&number>0) number--;
            if (updater!=null) {
                updater.update();
            }
        }
    }
    public String name() {
        return actionName;
    }
    @Override
    public String toString() {
        return display();
    }
    public abstract boolean executeAction(Object details);
}
