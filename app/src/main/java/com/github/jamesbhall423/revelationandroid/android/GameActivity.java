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
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayGlobal;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.io.ConnectionCreator;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
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
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements BoxViewUpdater {
    private static final int IN_PORT=4111;
    public static final String IP_OTHER = "IP_OTHER";
    private AndroidSquare[][] board;
    private BoxModel model;
    private GridLayout displayBoard;
    private LinearLayout display;
    private AndroidMenu androidMenu;
    private AndroidSelector selector;
    private Toolbar toolbar;
    private boolean host;
    private CMap map;
    private String miniTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (menu==null||map==null||model==null) return false;
//        System.out.println("Creating options menu");
//        androidMenu = new AndroidMenu(menu,map,model);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (androidMenu==null) return false;
//        androidMenu.optionSelected(item);
//        return true;
//    }

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
        this.model = model;
        model.registerUpdater(this);
        RadioGroup selectorView = new RadioGroup(this);
        selector = new AndroidSelector(selectorView,model,this);
        displayBoard = new GridLayout(this);
        displayBoard.setColumnCount(model.displayWidth());
        displayBoard.setRowCount(model.displayHeight());
        displayBoard.setOrientation(GridLayout.HORIZONTAL);
        displayBoard.setUseDefaultMargins(true);
        display = findViewById(R.id.main_layout);
        display.setOrientation(LinearLayout.VERTICAL);
        board = new AndroidSquare[model.displayHeight()][model.displayWidth()];
        for (int y = 0; y < model.displayHeight(); y++) for (int x = 0; x < model.displayWidth(); x++) {
            board[y][x] = new AndroidSquare(this,model.getDisplaySquare(x,y),selector,model.displayHeight());
            displayBoard.addView(board[y][x]);
        }
        display.addView(selectorView);
        display.addView(displayBoard);
        toolbar = findViewById(R.id.toolbar);
        androidMenu = new AndroidMenu(toolbar.getMenu(),map,model,this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (androidMenu==null) return false;
                androidMenu.optionSelected(item);
                return true;
            }
        });
        setMiniTitle("Pl: "+(model.player()+1));
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(100);
                        GameActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GameActivity.this.model.step();
//                                updateAllSquares();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //setActionBar(toolbar);
//        openOptionsMenu();
        display.invalidate();
    }

    @Override
    public void updateAllSquares() {
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
            board[y][x].invalidate();
        }
    }
    @Override
    public void updateGlobal() {
        updateTitle();
        androidMenu.updateItems();
        selector.updateItems();
    }
    public void setMiniTitle(String title) {
        miniTitle=title;
        updateTitle();
    }
    public String getMiniTitle() {
        return miniTitle;
    }
    private void updateTitle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (model.responsive())	toolbar.setTitle(miniTitle);
                else toolbar.setTitle(miniTitle+" (waiting)");
            }
        });
    }
    @Override
    public void updateEndStatus() {
        switch (model.getEndStatus()) {
            case WIN:
                updateRevelationDisplay();
                //win();
                break;
            case LOSS:
                updateRevelationDisplay();
                //loss();
                break;
            case BLOCKED:
                //blocked();
                break;
            case ONGOING:
                break;
            case OTHER_LEFT:
                break;
        }
    }
    private void updateRevelationDisplay() {
        RevelationDisplayGlobal global = new RevelationDisplayGlobal(model);
        for (RevelationDisplayLocal local: global.parts) {
            board[local.displayY()][local.displayX()].setRevelationDisplay(local);
        }
    }
}
