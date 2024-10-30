package com.github.jamesbhall423.revelationandroid.android;

import androidx.appcompat.app.AppCompatActivity;

public class ExitMenuItem implements AndroidMenuItem {
    private GameActivity context;
    public ExitMenuItem(GameActivity context) {
        this.context = context;
    }
    @Override
    public void update() {

    }

    @Override
    public void doClick() {
        context.backToStart();
    }
}
