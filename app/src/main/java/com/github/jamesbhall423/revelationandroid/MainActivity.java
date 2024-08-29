package com.github.jamesbhall423.revelationandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.jamesbhall423.revelationandroid.android.GameActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        final EditText ip = findViewById(R.id.address);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivity = new Intent();
                gameActivity.setClass(MainActivity.this, GameActivity.class);
                gameActivity.putExtra(GameActivity.IP_OTHER,ip.getText().toString());

                startActivity(gameActivity);
            }
        });
    }
}
