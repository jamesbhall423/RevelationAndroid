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
        setContentView(R.layout.activity_main);
        Button join = findViewById(R.id.join);
        final EditText ip = findViewById(R.id.address);
        final EditText playerInput = findViewById(R.id.playerInput);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent();
                gameActivity.setClass(MainActivity.this, GameActivity.class);
                gameActivity.putExtra(GameActivity.IP_REFERENCE,ip.getText().toString());
                gameActivity.putExtra(GameActivity.PLAYER_REFERENCE,Integer.parseInt(playerInput.getText().toString()));
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
