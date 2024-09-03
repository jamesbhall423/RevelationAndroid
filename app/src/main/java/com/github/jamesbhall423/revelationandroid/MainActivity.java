package com.github.jamesbhall423.revelationandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.jamesbhall423.revelationandroid.android.AndroidSquare;
import com.github.jamesbhall423.revelationandroid.android.GameActivity;
import com.github.jamesbhall423.revelationandroid.android.SquareDrawable;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;
import com.github.jamesbhall423.revelationandroid.model.SquareState;
import com.github.jamesbhall423.revelationandroid.model.SquareType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
