package com.github.jamesbhall423.revelationandroid.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.github.jamesbhall423.revelationandroid.R;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayGlobal;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.io.ConnectionCreator;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CMap;

import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class GameActivity extends AppCompatActivity implements BoxViewUpdater {
    public static final String PLAYER_REFERENCE = "PLAYER_REFERENCE";
    private static final int IN_PORT=4111;
    public static final String CONNECTION_DIRECTION = "CONNECTION_DIRECTION";
    public static final String IP_REFERENCE = "IP_REFERENCE";
    public static final String GAME_FILE = "GAME_FILE";
    public static final int CLIENT = 0;
    public static final int SERVER = 1;
    private boolean running = true;
    private AndroidSquare[][] board;
    private BoxModel model;
    private GridLayout displayBoard;
    private LinearLayout display;
    private ScrollView notificationDisplay;
    private LinearLayout notificationList;
    private int countNotifications = 0;
    private AndroidMenu androidMenu;
    private AndroidSelector selector;
    private Toolbar toolbar;
    private boolean host;
    private CMap map;
    private String miniTitle;
    private MainViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.loading_screen);
        Intent intent = getIntent();
        final String ip_extra = intent.getStringExtra(IP_REFERENCE);
        int connection_type = intent.getIntExtra(CONNECTION_DIRECTION,CLIENT);
        host = (connection_type==SERVER);
        final String game_file;
        if (host) game_file =  intent.getStringExtra(GAME_FILE);
        else game_file = null;
        final int player = intent.getIntExtra(PLAYER_REFERENCE,0)-1;
        TextView typeView = findViewById(R.id.Description);
        TextView ipView = findViewById(R.id.IP);
        if (host) typeView.setText("Hosting");
        else typeView.setText("Joining");
        ipView.setText(ip_extra);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (viewModel.boxModel() != null) setDetails(viewModel.boxModel());
        else {
            Thread loader = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Creating network connection");
                    if (host) viewModel.loadGameFile(GameActivity.this,game_file);
                    else viewModel.loadIP(GameActivity.this,ip_extra,player);
                }
            });
            loader.start();
        }
    }
    public MainViewModel viewModel() {
        return viewModel;
    }
    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
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

    public void loadIP(String ip_extra, int player) {
        try {
            final BoxModel model = ConnectionCreator.createClient(ip_extra, player);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDetails(model);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void loadGameFile(String game_file) {
        try {
            final BoxModel model = ConnectionCreator.createHost(game_file);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDetails(model);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setDetails(BoxModel model) {

        setContentView(R.layout.game_activity);
        this.map = model.cmap();
        this.model = model;
        model.registerUpdater(viewModel.wrapBoxViewUpdater(this,this));
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
            board[y][x] = new AndroidSquare(this,viewModel,model.getDisplaySquare(x,y),selector,model.displayHeight());
            displayBoard.addView(board[y][x]);
        }
        display.addView(selectorView);
        display.addView(displayBoard);
        toolbar = findViewById(R.id.toolbar);
        androidMenu = new AndroidMenu(toolbar.getMenu(), model,this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (androidMenu==null) return false;
                androidMenu.optionSelected(item);
                return true;
            }
        });
        notificationDisplay = new ScrollView(this);
        notificationList = new LinearLayout(this);
        notificationList.setOrientation(LinearLayout.VERTICAL);
        notificationDisplay.addView(notificationList);
        setMiniTitle("Pl: "+(model.player()+1));
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (running) {
                        GameActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (running) GameActivity.this.model.step();
                            }
                        });
                        if (running) Thread.sleep(100);
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
        if (board==null) return;
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
            board[y][x].invalidate();
        }
    }
    @Override
    public void updateGlobal() {
        updateTitle();
        if (androidMenu!=null) androidMenu.updateItems();
        if (selector!=null) selector.updateItems();
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
                if(toolbar!=null) toolbar.setTitle(model.defaultTitle());
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

    public void showNotifications(List<CAction> notifications) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);
        while (countNotifications<notifications.size()) {
            TextView next = new TextView(this);
            next.setText(notifications.get(countNotifications).toString());
            notificationList.addView(next, layoutParams);
            countNotifications++;
        }
        display.removeView(displayBoard);
        display.addView(notificationDisplay);
    }

    public void hideNotifications() {
        display.removeView(notificationDisplay);
        display.addView(displayBoard);
    }
}
