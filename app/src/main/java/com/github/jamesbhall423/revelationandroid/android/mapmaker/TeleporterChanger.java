package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import android.app.Activity;
import android.widget.Toast;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;

public class TeleporterChanger implements SquareChanger {
    private SquareClass lastSquare = null;
    private boolean[] teleporterNumbersUsed = new boolean[10];
    private BoxModel model;
    private int teleporterNumber = 0;
    private Activity context;
    public TeleporterChanger(Activity context, BoxModel model) {
        this.model = model;
        int width = model.modelWidth();
        int height = model.modelHeight();
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            SquareModel square = model.getModelSquare(x,y);
            if (square.getTeleporter()!=null) teleporterNumbersUsed[square.getTeleporter()[2]]=true;
        }
        this.context = context;
    }
    @Override
    public void alterSquare(SquareClass square) {
        int[] teleporter = square.getTeleporter();
        if (teleporter!=null) {
            SquareClass other = model.getModelSquare(teleporter[0],teleporter[1]);
            square.setTeleporter(null);
            other.setTeleporter(null);
            teleporterNumbersUsed[teleporter[2]] = false;
            deactivate();
        } else if (lastSquare==null)  {
            teleporterNumber = 0;
            try {
                while (teleporterNumbersUsed[teleporterNumber]) teleporterNumber++;
                lastSquare = square;
                lastSquare.setHighlight(true);
            } catch (IndexOutOfBoundsException e) {
                Toast.makeText(context,"Maximum number of teleporters reached",Toast.LENGTH_SHORT).show();
            }
        } else {
            if (square!=lastSquare) {
                teleporterNumbersUsed[teleporterNumber] = true;
                lastSquare.setTeleporter(new int[]{square.X,square.Y,teleporterNumber});
                square.setTeleporter(new int[]{lastSquare.X,lastSquare.Y,teleporterNumber});
            }
            deactivate();
        }
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        if (lastSquare!=null) lastSquare.setHighlight(false);
        lastSquare = null;
    }
}
