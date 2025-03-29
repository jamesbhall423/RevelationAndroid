package com.github.jamesbhall423.revelationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.jamesbhall423.revelationandroid.android.GameActivity;
import com.github.jamesbhall423.revelationandroid.android.mapmaker.MapMaker;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class FileViewer extends AppCompatActivity {
    public static final String FLAG_MAPMAKER = "FLAG_MAPMAKER";
    public static final String PATH_LOCATION = "PATH";
    private LinearLayout mainLayout;
    private String path;
    private File file;
    private boolean isMapmaker;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_viewer);
        Intent intent = getIntent();
        isMapmaker = intent.getBooleanExtra(FLAG_MAPMAKER,false);
        path = intent.getStringExtra(PATH_LOCATION);
        file = new File(path);
        mainLayout = findViewById(R.id.main_layout);
        String containingFolder = getFilesDir().getAbsolutePath();
        if (path.length()>containingFolder.length()) {
            Button back = new Button(this);
            back.setText("Back");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeDirectory(file.getParent());
                }
            });
            mainLayout.addView(back);
        }
        if (isMapmaker) {
            final EditText manualInput = new EditText(this);
            manualInput.setInputType(InputType.TYPE_CLASS_TEXT);
            manualInput.setText("File Name");
            mainLayout.addView(manualInput);
            Button create = new Button(this);
            create.setText("Create");
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileName = manualInput.getText().toString();
                    if (!fileName.contains(".") && !fileName.contains("/") && !fileName.contains("\\")) {
                        launchPipedActivity(fileName);
                    }
                }
            });
            mainLayout.addView(create);
            Button mkdir = new Button(this);
            mkdir.setText("Make Directory");
            mkdir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileName = path+"/"+manualInput.getText().toString();
                    new File(fileName).mkdir();
                    changeDirectory(fileName);
                }
            });
            mainLayout.addView(mkdir);
        }
        TextView folderDisplay = new TextView(this);
        folderDisplay.setText("Folders");
        mainLayout.addView(folderDisplay);
        File[] directories = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (directories!=null) for (final File nextDirectory: directories) {
            Button next = new Button(this);
            next.setText(nextDirectory.getName());
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeDirectory(nextDirectory.getAbsolutePath());
                }
            });
            mainLayout.addView(next);
        }
        TextView mapDisplay = new TextView(this);
        mapDisplay.setText("Maps");
        mainLayout.addView(mapDisplay);
        String[] maps = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(MainActivity.MAP_EXTENSION);
            }
        });
        if (maps!=null) for (final String map: maps) {
            Button next = new Button(this);
            final String name = map.substring(0,map.indexOf(MainActivity.MAP_EXTENSION));
            next.setText(name);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchPipedActivity(name);
                }
            });
            mainLayout.addView(next);
        }
    }
    private void changeDirectory(String newDirectory) {
        Intent next = new Intent();
        next.setClass(FileViewer.this,FileViewer.class);
        next.putExtra(FLAG_MAPMAKER,isMapmaker);
        next.putExtra(PATH_LOCATION,newDirectory);
        startActivity(next);
    }
    private void launchPipedActivity(String fileName) {
        Intent next = new Intent();
        next.setClass(this,isMapmaker ? MapMaker.class : GameActivity.class);
        String filepath = path+"/"+fileName+MainActivity.MAP_EXTENSION;
        next.putExtra(GameActivity.GAME_FILE,filepath);
        if (!isMapmaker) {
            next.putExtra(GameActivity.IP_REFERENCE,getIPAddress(true));
            next.putExtra(GameActivity.CONNECTION_DIRECTION,GameActivity.SERVER);
        }
        startActivity(next);
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
