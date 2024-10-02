package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import android.view.View;
import android.widget.EditText;

import com.github.jamesbhall423.revelationandroid.model.Player;

public class PlayerRecorder {
    private EditText scans;
    private EditText reverts;
    private EditText declares;
    private Player player;
    public PlayerRecorder(Player player, View scans, View reverts, View declares) {
        this.player = player;
        this.reverts = (EditText) reverts;
        this.declares = (EditText) declares;
        this.scans = (EditText) scans;
        this.reverts.setText(Integer.valueOf(player.numReverts).toString());
        this.scans.setText(Integer.valueOf(player.scans).toString());
        this.declares.setText(Integer.valueOf(player.declareVictories).toString());

    }
    public void record() {
        player.declareVictories = Integer.parseInt(declares.getText().toString());
        player.scans = Integer.parseInt(scans.getText().toString());
        player.numReverts = Integer.parseInt(reverts.getText().toString());
    }
}
