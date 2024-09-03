package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.jamesbhall423.revelationandroid.R;
import com.github.jamesbhall423.revelationandroid.io.ConnectionCreator;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.io.InetBuffer;
import com.github.jamesbhall423.revelationandroid.io.InetReceiver;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import android.util.Log;
import android.widget.RadioGroup;

public class GameActivity extends Activity {
    private static final int IN_PORT=4111;
    public static final String IP_OTHER = "IP_OTHER";
    private AndroidSquare[][] board;
    private BoxModel model;
    private GridLayout displayBoard;
    private LinearLayout display;
    private AndroidMenu androidMenu;
    private AndroidSelector selector;
    private boolean host;
    private CMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String ip_extra = intent.getStringExtra(IP_OTHER);
        Thread loader = new Thread(new Runnable() {
            @Override
            public void run() {
                loadIP(ip_extra);
            }
        });
        loader.start();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu==null||map==null||model==null) return false;
        androidMenu = new AndroidMenu(menu,map,model);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (androidMenu==null) return false;
        androidMenu.optionSelected(item);
        return true;
    }

    private void loadIP(String ip_extra) {
        try {
            final BoxModel model = ConnectionCreator.createClient(ip_extra);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDetails(model,false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setDetails(BoxModel model, boolean host) {
        this.host = host;
        this.map = model.cmap();
        RadioGroup selectorView = new RadioGroup(this);
        selector = new AndroidSelector(selectorView,model);
        displayBoard = new GridLayout(this);
        displayBoard.setColumnCount(model.displayWidth());
        displayBoard.setRowCount(model.displayHeight());
        displayBoard.setOrientation(GridLayout.HORIZONTAL);
        displayBoard.setUseDefaultMargins(true);
        display = new LinearLayout(this);
        display.setOrientation(LinearLayout.VERTICAL);
        board = new AndroidSquare[model.displayHeight()][model.displayWidth()];
        for (int y = 0; y < model.displayHeight(); y++) for (int x = 0; x < model.displayWidth(); x++) {
            board[y][x] = new AndroidSquare(this,model.getDisplaySquare(x,y),selector,model.displayHeight());
            displayBoard.addView(board[y][x]);
        }
        display.addView(selectorView);
        display.addView(displayBoard);
        setContentView(display);
        display.invalidate();
    }
}
