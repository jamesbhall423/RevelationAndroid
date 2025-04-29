package com.github.jamesbhall423.revelationandroid.android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.jamesbhall423.revelationandroid.ai.AIRunner;
import com.github.jamesbhall423.revelationandroid.io.ConnectionCreator;
import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.PointInt2D;
import com.github.jamesbhall423.revelationandroid.model.SelectionItemUpdater;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;
import com.github.jamesbhall423.revelationandroid.model.SquareViewUpdater;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

import java.io.IOException;

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

    private BoxModel hostAI(String game_file) throws IOException {
        CMap map = CMap.read(game_file, JSONSerializer.REVELATION_SERIALIZER);
        SelfBuffer[] bs = new SelfBuffer[map.players.length];
        for (int i = 0; i < bs.length; i++) bs[i]=new SelfBuffer();
        SelfBuffer.setLinks(bs);
        BoxModel[] models = new BoxModel[map.players.length];
        for (int i = 0; i < models.length; i++) models[i]=new BoxModel((CMap)map.clone(),i,bs[i]);
        new Thread(new AIRunner(models[0])).start();
        return models[1];
    }
    public void loadGameFile(final GameActivity activity, String game_file, boolean tutorial) {
        try {
            if (tutorial) model = hostAI(game_file);
            else model = ConnectionCreator.createHost(game_file);
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
