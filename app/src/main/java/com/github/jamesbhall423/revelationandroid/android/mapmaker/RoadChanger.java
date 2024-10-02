package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;
import com.github.jamesbhall423.revelationandroid.model.SquareType;

public class RoadChanger implements SquareChanger {
    private int road;
    private int antiRoad;
    private BoxModel model;
    public RoadChanger(BoxModel model, int road) {
        this.road = road;
        antiRoad = reverseDirection(road);
        this.model = model;
    }
    public static int reverseDirection(int road) {
        switch (road) {
            case SquareModel.DOWN:
                return SquareModel.UP;
            case SquareModel.UP:
                return SquareModel.DOWN;
            case SquareModel.LEFT:
                return SquareModel.RIGHT;
            case SquareModel.RIGHT:
                return SquareModel.LEFT;
            default:
                return 0;
        }
    }

    @Override
    public void alterSquare(SquareClass square) {
        alterRoad(road,square);
        try {
            SquareClass other = null;
            switch (road) {
                case SquareModel.LEFT:
                    other = model.getModelSquare(square.getX()-1,square.getY());
                    break;
                case SquareModel.RIGHT:
                    other = model.getModelSquare(square.getX()+1,square.getY());
                    break;
                case SquareModel.UP:
                    other = model.getModelSquare(square.getX(),square.getY()-1);
                    break;
                case SquareModel.DOWN:
                    other = model.getModelSquare(square.getX(),square.getY()+1);
                    break;
            }
            alterRoad(antiRoad,other);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
    public static void alterRoad(int road,SquareClass square) {
        int priorRoad = square.getRoad();
        int newRoad = priorRoad^road;
        if (newRoad==0) square.setType(SquareType.Empty);
        else square.setType(SquareType.Road);
        square.setRoad(newRoad);
    }

    @Override
    public void deactivate() {

    }
}
