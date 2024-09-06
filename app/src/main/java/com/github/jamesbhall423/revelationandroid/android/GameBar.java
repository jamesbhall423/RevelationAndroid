package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;

public class GameBar extends Toolbar {
    public GameBar(Context context, CMap map, BoxModel model) {
        super(context);
//        final AndroidMenu androidMenu = new AndroidMenu(getMenu(),map,model);
//        setOnMenuItemClickListener(new OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (androidMenu==null) return false;
//                androidMenu.optionSelected(item);
//                return true;
//            }
//        });
    }

}
