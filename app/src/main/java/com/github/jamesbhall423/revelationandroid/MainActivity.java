package com.github.jamesbhall423.revelationandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.jamesbhall423.revelationandroid.android.GameActivity;

public class MainActivity extends AppCompatActivity {
    public static final String MAP_EXTENSION = ".cmap";
    private static final String IP_REFERENCE = "LAST_IP";
    private static final String DEFAULT_IP = "IP Address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        AppInitializer.run(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Revelation");
        EditText ip = findViewById(R.id.address);
        ip.setText(sharedPref.getString(IP_REFERENCE,DEFAULT_IP));
        Button joinP1 = findViewById(R.id.joinP1);
        joinP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinIP(1);
            }
        });
        Button joinP2 = findViewById(R.id.joinP2);
        joinP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinIP(2);
            }
        });
        Button hoster = findViewById(R.id.host);
        hoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(false);
            }
        });
        Button mapmakerButton = findViewById(R.id.mapmaker);
        mapmakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(true);
            }
        });
    }
    public void joinIP(int player) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor ipEdit = sharedPref.edit();
        EditText ip = findViewById(R.id.address);
        ipEdit.putString(IP_REFERENCE,ip.getText().toString());
        ipEdit.apply();
        Intent gameActivity = new Intent();
        gameActivity.setClass(MainActivity.this, GameActivity.class);
        gameActivity.putExtra(GameActivity.IP_REFERENCE,ip.getText().toString());
        gameActivity.putExtra(GameActivity.PLAYER_REFERENCE,player);
        gameActivity.putExtra(GameActivity.CONNECTION_DIRECTION,GameActivity.CLIENT);
        startActivity(gameActivity);
    }


    private void openFile(boolean isMapmaker) {
        Intent intent = new Intent();
        intent.setClass(this,FileViewer.class);
        intent.putExtra(FileViewer.FLAG_MAPMAKER,isMapmaker);
        intent.putExtra(FileViewer.PATH_LOCATION,getFilesDir().getAbsolutePath());
        startActivity(intent);
    }
}
