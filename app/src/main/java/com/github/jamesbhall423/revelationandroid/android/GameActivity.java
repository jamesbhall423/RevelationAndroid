package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.github.jamesbhall423.revelationandroid.FileViewer;
import com.github.jamesbhall423.revelationandroid.R;
import com.github.jamesbhall423.revelationandroid.ai.AIRunner;
import com.github.jamesbhall423.revelationandroid.ai.TurnCounter;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayGlobal;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements BoxViewUpdater {
    public static final int DISPLAY_MAIN = 0;
    public static final int DISPLAY_NOTIFICATIONS = 1;
    public static final int DISPLAY_INSTRUCTIONS = 2;
    public static final String PLAYER_REFERENCE = "PLAYER_REFERENCE";
    public static final String TUTORIAL_REFERENCE = "TUTORIAL_REFERENCE";
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
    private TextView instructionsDisplay;
    private ScrollView instructionsScroll;
    private int countNotifications = 0;
    private AndroidMenu androidMenu;
    private AndroidSelector selector;
    private Toolbar toolbar;
    private boolean host;
    private CMap map;
    private MainViewModel viewModel;
    private RadioGroup selectorView;
    private LinearLayout.LayoutParams listLayoutParams;
    private boolean isTutorial = false;
    private int tutorialNum = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.loading_screen);
        Intent intent = getIntent();
        final String ip_extra = intent.getStringExtra(IP_REFERENCE);
        int connection_type = intent.getIntExtra(CONNECTION_DIRECTION,SERVER);
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
        isTutorial = intent.getBooleanExtra(TUTORIAL_REFERENCE,false);
        if (isTutorial) {
            String fileName = game_file.substring((game_file.indexOf("Tutorial/")+"Tutorial/".length()));
            Scanner numLocator = new Scanner(fileName);
            tutorialNum = numLocator.nextInt();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (viewModel.boxModel() != null) setDetails(viewModel.boxModel());
        else {
            Thread loader = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (host) viewModel.loadGameFile(GameActivity.this,game_file,isTutorial);
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
    private void displayToastForTime(final int text, final int milis) {
        final GameActivity context = this;
        final int interval = 2000;
        final Toast[] toasts = new Toast[milis/interval];
        for (int i = 0; i < toasts.length; i++) toasts[i] = Toast.makeText(context,text,Toast.LENGTH_LONG);
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < toasts.length&&!context.isDestroyed(); i++) {
                    Toast toast = toasts[i];
                    toast.show();
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                    }
                    toast.cancel();
                }
            }
        }).start();
    }

    public void setDetails(BoxModel model) {

        setContentView(R.layout.game_activity);
        listLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        listLayoutParams.setMargins(30, 20, 30, 0);
        this.map = model.cmap();
        this.model = model;
        instructionsDisplay = new TextView(this);
        instructionsDisplay.setText(model.instructions());
        instructionsDisplay.setLayoutParams(listLayoutParams);
        instructionsScroll = new ScrollView(this);
        instructionsScroll.addView(instructionsDisplay);
        model.registerUpdater(viewModel.wrapBoxViewUpdater(this,this));
        selectorView = new RadioGroup(this);
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
        display.invalidate();
        switch (tutorialNum) {
            case 1:
                displayToastForTime(R.string.tutorial1,10000);
                break;
            case 2:
                displayToastForTime(R.string.tutorial2,10000);
                break;
            case 3:
                displayToastForTime(R.string.tutorial3,10000);
                break;
            case 4:
                displayToastForTime(R.string.tutorial4,10000);
                break;
        }
        if (isTutorial) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    GameActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!GameActivity.this.isDestroyed()) Toast.makeText(GameActivity.this,"The top-right menu contains more options",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            },15000);
        }
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
                break;
            case LOSS:
                updateRevelationDisplay();
                break;
            case BLOCKED:
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
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
            board[y][x].showTrueValue();
        }
    }

    public void showNotifications(List<CAction> notifications) {

        while (countNotifications<notifications.size()) {
            TextView next = new TextView(this);
            next.setText(notifications.get(countNotifications).toString());
            notificationList.addView(next, listLayoutParams);
            countNotifications++;
        }
        display.removeAllViews();
        display.addView(notificationDisplay);
    }

    public void showMain() {
        display.addView(selectorView);
        display.addView(displayBoard);
    }
    public void showDisplay(int displayNumber) {
        display.removeAllViews();
        switch (displayNumber) {
            case DISPLAY_INSTRUCTIONS:
                display.addView(instructionsScroll);
                break;
            case DISPLAY_NOTIFICATIONS:
                TurnCounter.printEdgeLengths(model);
                showNotifications(model.notifications());
                break;
            default:
                showMain();
        }
    }

    public void backToStart() {
        model.close();
        finish();
    }
    @Override
    public void onBackPressed() {
        System.out.println("On Back Pressed");
        if (model==null) super.onBackPressed();
        else showDisplay(DISPLAY_MAIN);
    }
}
