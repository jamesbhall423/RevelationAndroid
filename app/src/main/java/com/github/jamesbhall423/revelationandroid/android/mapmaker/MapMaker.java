package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.jamesbhall423.revelationandroid.R;
import com.github.jamesbhall423.revelationandroid.android.AndroidSquare;
import com.github.jamesbhall423.revelationandroid.io.RevelationOutputStream;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.Player;
import com.github.jamesbhall423.revelationandroid.model.SquareState;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.github.jamesbhall423.revelationandroid.android.GameActivity.GAME_FILE;

public class MapMaker extends AppCompatActivity implements BoxViewUpdater {

    private static final JSONSerializer serializer = JSONSerializer.REVELATION_SERIALIZER;
    public static final int DEFAULT_WIDTH = 8;
    public static final int DEFAULT_HEIGHT = 8;

    private GridLayout displayBoard;
    private LinearLayout display;
    private Toolbar toolbar;
    private MapmakerBar bar;
    private AndroidSquare[][] board;
    private BoxModel model;
    private String gameFile;
    private PlayerRecorder player1Recorder;
    private PlayerRecorder player2Recorder;
    private Player[] players;
    private boolean saved = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapmaker);
        Intent intent = getIntent();
        gameFile =  intent.getStringExtra(GAME_FILE);
        if (new File(gameFile).exists()) {
            try {
                CMap map = CMap.read(gameFile,serializer);
                model = new BoxModel(map, 0, null);
                players = map.players;
            }  catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            model = new BoxModel(DEFAULT_WIDTH,DEFAULT_HEIGHT,this);
            players = new Player[2];
            players[0] = new Player(0);
            players[1] = new Player(1);
        }
        model.registerUpdater(this);
        displayBoard = new GridLayout(this);
        displayBoard.setColumnCount(model.displayWidth());
        displayBoard.setRowCount(model.displayHeight());
        displayBoard.setOrientation(GridLayout.HORIZONTAL);
        displayBoard.setUseDefaultMargins(true);
        display = findViewById(R.id.main_layout);
        display.setOrientation(LinearLayout.VERTICAL);
        board = new AndroidSquare[model.displayHeight()][model.displayWidth()];
        toolbar = findViewById(R.id.toolbar);
        bar = new MapmakerBar(toolbar,model,this);
        for (int y = 0; y < model.displayHeight(); y++) for (int x = 0; x < model.displayWidth(); x++) {
            board[y][x] = new AndroidSquare(this,null,model.getDisplaySquare(x,y),bar,model.displayHeight());
            displayBoard.addView(board[y][x]);
            board[y][x].showTrueValue();
        }
        display.addView(displayBoard);
        player1Recorder = new PlayerRecorder(players[0],findViewById(R.id.ScansP1),findViewById(R.id.RevertsP1),findViewById(R.id.DeclaresP1));
        player2Recorder = new PlayerRecorder(players[1],findViewById(R.id.ScansP2),findViewById(R.id.RevertsP2),findViewById(R.id.DeclaresP2));
    }
    @Override
    public void updateAllSquares() {

    }

    @Override
    public void updateGlobal() {

    }

    @Override
    public void updateEndStatus() {

    }
    private CMap map() {
        SquareState[][] states = new SquareState[model.modelHeight()][model.modelWidth()];
        for (int y = 0; y < states.length; y++) for (int x = 0; x < states[y].length; x++) states[y][x]=model.getModelSquare(x,y).getState();
        return new CMap(states,players);
    }
    public void saveAndExit() {
        if (save()) {
            saved = true;
            System.exit(0);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (!saved) save();
    }
    public boolean save() {
        player1Recorder.record();
        player2Recorder.record();
        try {
            File writeTo = new File(gameFile);
            RevelationOutputStream out = new RevelationOutputStream(new FileOutputStream(writeTo),serializer,false);
            out.writeCMap(map());
            out.close();
            return true;
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            Toast.makeText(this,"Save Failed",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
