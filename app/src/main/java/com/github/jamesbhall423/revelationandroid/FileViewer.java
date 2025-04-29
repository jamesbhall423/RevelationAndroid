package com.github.jamesbhall423.revelationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.jamesbhall423.revelationandroid.android.GameActivity;
import com.github.jamesbhall423.revelationandroid.android.mapmaker.MapMaker;
import com.github.jamesbhall423.revelationandroid.io.ShareClient;
import com.github.jamesbhall423.revelationandroid.io.ShareServer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class FileViewer extends AppCompatActivity implements ShareServer.Updater {
    public static final int TYPE_MAPMAKER = 0;
    public static final int TYPE_GAME = 1;
    public static final int TYPE_SEND = 2;
    public static final int TYPE_RECIEVE = 3;
    public static final String FLAG_TYPE = "FLAG_TYPE";
    public static final String PATH_LOCATION = "PATH";
    public static final String IP_REFERENCE = "IP_REFERENCE";
    public static final String TUTORIAL_LOCATION = "Tutorial";
    public static final int TYPE_TUTORIAL = 4;
    private LinearLayout mainLayout;
    private String ipOther;
    private String path;
    private File file;
    private int type;
    private ShareServer shareServer = null;
    private CheckBox shareCheckBox = null;
    private CheckBox deleteCheckBox = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_viewer);
        Intent intent = getIntent();
        type = intent.getIntExtra(FLAG_TYPE,TYPE_GAME);
        path = intent.getStringExtra(PATH_LOCATION);
        ipOther = intent.getStringExtra(IP_REFERENCE);
        System.out.println("Creating file viewer");
        System.out.println(type);
        System.out.println(path);
        System.out.println(ipOther);
        file = new File(path);
        mainLayout = findViewById(R.id.main_layout);
        String containingFolder = getFilesDir().getAbsolutePath();
        if (type==TYPE_TUTORIAL) containingFolder+="/"+TUTORIAL_LOCATION;
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
        if (type==TYPE_RECIEVE) {
            TextView label = new TextView(this);
            label.setText("Ready to receive?");
            mainLayout.addView(label);
            shareCheckBox = new CheckBox(this);
            shareCheckBox.setChecked(shareServer!=null);
            shareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked&&shareServer==null) {
                            shareServer = new ShareServer(path,FileViewer.this);
                        } else if (!isChecked&&shareServer!=null) {
                            shareServer.close();
                            shareServer = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            mainLayout.addView(shareCheckBox);
        }
        if (type==TYPE_MAPMAKER||type==TYPE_RECIEVE) {
            final EditText manualInput = new EditText(this);
            manualInput.setInputType(InputType.TYPE_CLASS_TEXT);
            manualInput.setText("File Name");
            mainLayout.addView(manualInput);
            if (type==TYPE_MAPMAKER) {
                TextView label = new TextView(this);
                label.setText("Delete?");
                mainLayout.addView(label);
                deleteCheckBox = new CheckBox(this);
                deleteCheckBox.setChecked(false);
                mainLayout.addView(deleteCheckBox);
                Button create = new Button(this);
                create.setText("Create");
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = manualInput.getText().toString();
                        if (!fileName.contains(".") && !fileName.contains("/") && !fileName.contains("\\")) {
                            doAction(fileName,v);
                        }
                    }
                });
                mainLayout.addView(create);
            }
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
        if (directories!=null) for (final File nextDirectory: directories) /*if (!nextDirectory.getName().equals(TUTORIAL_LOCATION))*/ {
            Button next = new Button(this);
            next.setText(nextDirectory.getName());
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteCheckBox!=null&&deleteCheckBox.isChecked()) deleteFileFromViewer(nextDirectory.getAbsolutePath(),v);
                    else changeDirectory(nextDirectory.getAbsolutePath());
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
            addMap(map);
        }
    }
    private void addMap(String map) {
        Button next = new Button(this);
        final String name = map.substring(0,map.indexOf(MainActivity.MAP_EXTENSION));
        next.setText(name);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction(name,v);
            }
        });
        mainLayout.addView(next);
    }
    private void changeDirectory(String newDirectory) {
        ensureShareServerClosed();
        Intent next = new Intent();
        next.setClass(FileViewer.this,FileViewer.class);
        next.putExtra(FLAG_TYPE,type);
        next.putExtra(PATH_LOCATION,newDirectory);
        if (type==TYPE_SEND) next.putExtra(IP_REFERENCE,ipOther);
        startActivity(next);
    }
    private void doAction(String fileName, View view) {
        System.out.println("Doing action");
        if (deleteCheckBox!=null&&deleteCheckBox.isChecked()) deleteFileFromViewer(path+"/"+fileName+".json",view);
        else if (type==TYPE_MAPMAKER||type==TYPE_GAME||type==TYPE_TUTORIAL) launchPipedActivity(fileName);
        else if (type==TYPE_SEND) {
            sendFile(fileName);
        }
    }
    private void deleteFileFromViewer(String fileName, View view) {
        if (new File(fileName).delete()) view.setVisibility(View.GONE);

    }
    private void sendFile(final String fileName) {
        System.out.println("IP OTHER -> "+ipOther);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShareClient.publish(ipOther,new File(path+"/"+fileName+MainActivity.MAP_EXTENSION),fileName);
            }
        }).start();
    }
    private void launchPipedActivity(String fileName) {
        System.out.println("launching activity");
        Intent next = new Intent();
        if (type==TYPE_MAPMAKER) next.setClass(this,MapMaker.class);
        else next.setClass(this,GameActivity.class);
        String filepath = path+"/"+fileName+MainActivity.MAP_EXTENSION;
        next.putExtra(GameActivity.GAME_FILE,filepath);
        if (type==TYPE_TUTORIAL) next.putExtra(GameActivity.TUTORIAL_REFERENCE, true);
        if (type==TYPE_GAME) {
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

    @Override
    public void update(final String map) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMap(map+MainActivity.MAP_EXTENSION);
            }
        });
    }
    @Override
    public void onBackPressed() {
        ensureShareServerClosed();
        super.onBackPressed();
    }
    public void ensureShareServerClosed() {
        if (shareCheckBox!=null) shareCheckBox.setChecked(false);
        if (shareServer!=null) {
            try {
                shareServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
