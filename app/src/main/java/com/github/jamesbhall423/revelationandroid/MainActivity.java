package com.github.jamesbhall423.revelationandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jamesbhall423.revelationandroid.android.GameActivity;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String MAP_FOLDER = "/";
    private static final String MAP_EXTENSION = ".cmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInitializer.run(this);
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
//        System.out.println("Files Directory = " + getFilesDir());
//        AssetManager manager = getAssets();
//        try {
//            String[] paths = manager.list("maps/");
//            System.out.println(paths.length);
//            for (String path: paths) {
//                System.out.println(path);
//            }
//        } catch (IOException e) {
//
//        }

//        LinearLayout mainLayout = new LinearLayout(this);
//        mainLayout.setOrientation(LinearLayout.VERTICAL);
//        Button button = new Button(this);
//        button.setText("Hello");
//        GridLayout layout = new GridLayout(this);
//        layout.setOrientation(GridLayout.HORIZONTAL);
//        layout.setColumnCount(2);
//        for (int i = 0; i < 4; i++) {
//            SquareState state = new SquareState();
//            switch (i) {
//                case 0:
//                    state.type = SquareType.Empty;
//                    break;
//                case 1:
//                    state.type = SquareType.Forest;
//                    break;
//                case 2:
//                    state.type = SquareType.Mountain;
//                    break;
//                case 3:
//                    state.type = SquareType.Road;
//                    state.road = SquareModel.UP+SquareModel.DOWN;
//            }
//            SquareClass square = new SquareClass(i%2,i/2,state);
//            AndroidSquare next = new AndroidSquare(this,square,null,2);
//            layout.addView(next);
//        }
//        mainLayout.addView(button);
//        mainLayout.addView(layout);
////        SquareClass square = new SquareClass(0,0,new SquareState());
////        AndroidSquare next = new AndroidSquare(this,square,null);
////        ImageView next = new ImageView(this);
////        next.setImageDrawable(new SquareDrawable(square));
//        setContentView(mainLayout);
//        setContentView(R.layout.circle_main);
        setContentView(R.layout.activity_main);
        Button join = findViewById(R.id.join);
        final EditText ip = findViewById(R.id.address);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent();
                gameActivity.setClass(MainActivity.this, GameActivity.class);
                gameActivity.putExtra(GameActivity.IP_REFERENCE,ip.getText().toString());
                gameActivity.putExtra(GameActivity.CONNECTION_DIRECTION,GameActivity.CLIENT);
                startActivity(gameActivity);
            }
        });
        Button hoster = findViewById(R.id.host);
        final TextView displayIP = findViewById(R.id.thisIP);
        final String IP_ADDRESS = getIPAddress(true);
        final EditText fileName = findViewById(R.id.fileName);
        displayIP.setText(IP_ADDRESS);
        File fileDir = getFilesDir();
        final String dataFolderName = fileDir.getPath();
        hoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent();
                gameActivity.setClass(MainActivity.this, GameActivity.class);
                gameActivity.putExtra(GameActivity.IP_REFERENCE,IP_ADDRESS);
                gameActivity.putExtra(GameActivity.GAME_FILE,dataFolderName+MAP_FOLDER+fileName.getText().toString()+MAP_EXTENSION);
                gameActivity.putExtra(GameActivity.CONNECTION_DIRECTION,GameActivity.SERVER);
                startActivity(gameActivity);
            }
        });
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
