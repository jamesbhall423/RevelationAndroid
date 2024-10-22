package com.github.jamesbhall423.revelationandroid.android;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.jamesbhall423.revelationandroid.io.ConnectionCreator;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.PointInt2D;
import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;
import com.github.jamesbhall423.revelationandroid.model.SquareViewUpdater;

public class MainViewModel extends ViewModel {
    private BoxModel model;
    public MainViewModel() {
        System.out.println("creating Main view Model");
    }

    public void loadIP(final GameActivity activity, String ip_extra, int player) {
        try {
            model = ConnectionCreator.createClient(ip_extra, player);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setDetails(model);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void loadGameFile(final GameActivity activity, String game_file) {
        try {
            model = ConnectionCreator.createHost(game_file);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setDetails(model);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public BoxModel boxModel() {
        return model;
    }
    public BoxViewUpdater wrapBoxViewUpdater(final AppCompatActivity activity,final BoxViewUpdater toWrap) {
        final MutableLiveData<Boolean> squaresFlag = new MutableLiveData<>(false);
        final MutableLiveData<Boolean> endFlag = new MutableLiveData<>(false);
        final MutableLiveData<Boolean> globalFlag = new MutableLiveData<>(false);
        squaresFlag.observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toWrap.updateAllSquares();
            }
        });
        endFlag.observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toWrap.updateEndStatus();
            }
        });
        globalFlag.observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toWrap.updateGlobal();
            }
        });
        return new BoxViewUpdater() {
            @Override
            public void updateAllSquares() {
                updateDataFlap(squaresFlag);
            }

            @Override
            public void updateGlobal() {
                updateDataFlap(globalFlag);
            }

            @Override
            public void updateEndStatus() {
                updateDataFlap(endFlag);
            }
        };
    }
    public SelectionItemUpdater wrapSelectionItemUpdater(final AppCompatActivity activity,final SelectionItemUpdater toWrap) {
        final MutableLiveData<Boolean> flag = new MutableLiveData<>(false);
        flag.observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toWrap.update();
            }
        });
        return new SelectionItemUpdater() {
            @Override
            public void update() {
                updateDataFlap(flag);
            }
        };
    }
    public SquareViewUpdater wrapSquareViewUpdater(final AppCompatActivity activity, final SquareViewUpdater toWrap, int modelX, int modelY) {
        final MutableLiveData<PointInt2D> reference = new MutableLiveData<>(new PointInt2D(modelX,modelY));
        reference.observe(activity, new Observer<PointInt2D>() {
            @Override
            public void onChanged(PointInt2D point) {
                toWrap.update(point.x,point.y);
            }
        });
        return new SquareViewUpdater() {
            @Override
            public void update(int x, int y) {
                reference.postValue(new PointInt2D(x,y));
            }
        };
    }
    private void updateDataFlap(MutableLiveData<Boolean> flag) {
        flag.postValue(!flag.getValue());
    }
}
