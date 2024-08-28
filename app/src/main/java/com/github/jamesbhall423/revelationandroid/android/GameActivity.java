package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.InetBuffer;
import com.github.jamesbhall423.revelationandroid.model.InetReciever;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameActivity extends Activity {
    private static final int IN_PORT=4111;
    public static final String IP_OTHER = "IP_OTHER";
    private AndroidSquare[][] board;
    private BoxModel model;
    private GridView displayBoard;
    private AndroidMenu androidMenu;
    private AndroidSelector selector;
    private boolean host;
    private CMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String ip_extra = intent.getStringExtra(IP_OTHER);
        loadIP(ip_extra);
        ListView selectorView = new ListView(this);
        selector = new AndroidSelector(selectorView,model);
        displayBoard = new GridView(this);
        displayBoard.setNumColumns(model.displayWidth());
        displayBoard.setHorizontalSpacing(1);
        displayBoard.setVerticalSpacing(1);
        for (int y = 0; y < model.displayHeight(); y++) for (int x = 0; x < model.displayWidth(); x++) {
            board[y][x] = new AndroidSquare(this,model.getDisplaySquare(x,y),selector);
            displayBoard.addView(board[y][x]);
        }
        addContentView(selectorView,null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        androidMenu = new AndroidMenu(menu,map,model);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        androidMenu.optionSelected(item);
        return true;
    }

    private void loadIP(String ip_extra) {
        try {
            int player=1;
            int port = 6666;
            Socket outSocket = new Socket(ip_extra,port);
            ServerSocket welcomeSocket = new ServerSocket(IN_PORT);
            Socket inSocket = welcomeSocket.accept();
            ObjectInputStream istream = new ObjectInputStream(inSocket.getInputStream());
            CMap map=(CMap)istream.readObject();
            SelfBuffer[] bs = new SelfBuffer[map.players.length];
            bs[player]=new SelfBuffer();
            bs[1-player]=new InetBuffer(outSocket);
            SelfBuffer.setLinks(bs);
            new Thread(new InetReciever(inSocket,bs[player])).start();
            welcomeSocket.close();
            setDetails((CMap)map.clone(),player,player==0,bs[player]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setDetails(CMap map, int playerNum, boolean host, SelfBuffer buffer) {
        this.host = host;
        model = new BoxModel(map, playerNum, buffer);
        this.map = map;
    }
}
