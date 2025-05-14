package com.github.jamesbhall423.revelationandroid.ai;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.BoxViewUpdater;
import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.ModelMenuItem;
import com.github.jamesbhall423.revelationandroid.model.SquareClick;
import com.github.jamesbhall423.revelationandroid.model.SquareClickType;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;
import com.github.jamesbhall423.revelationandroid.model.SquareType;
import com.github.jamesbhall423.revelationandroid.model.SquareViewUpdater;

import java.util.List;
import java.util.Random;

public class AIRunner implements Runnable, BoxViewUpdater, SquareViewUpdater {
    private SquareClickType place;
    private SquareClickType revert;
    private ModelMenuItem declare;
    private BoxModel model;
    private Random random = new Random();
    private int turnsToRevert;
    public AIRunner(BoxModel model) {
        this.model = model;
        loadActions();
        BoardEvaluator revertEvaluator = new BoardEvaluator(model,1);
        turnsToRevert = revertEvaluator.getTwiceTurnCount()/2;
    }

    private void loadActions() {
        model.registerUpdater(this);
        for (int y = 0; y < model.modelHeight(); y++) for (int x = 0; x < model.modelWidth(); x++) {
            model.getModelSquare(x,y).registerUpdater(this);
        }
        List<SquareClickType> types = SquareClickType.clickTypes(model);
        List<ModelMenuItem> menu = model.menu();
        for (SquareClickType type: types) {
            if (type.name().equals("place")) place=type;
            else if (type.name().equals("revert")) revert=type;
        }
        for (ModelMenuItem item: menu) {
            if (item.getAction()== CAction.DECLARE) declare = item;
        }
    }

    @Override
    public void run() {
        System.out.println("AI running");
        while (model.getEndStatus()== BoxModel.EndStatus.ONGOING) {
            System.out.println("Next");
            if (declare.enabled()) declare.doClick();
            else if (model.getTime()==turnsToRevert&&revert.numLeft()>0) {
                System.out.println("Revert attempt");
                boolean reverted = false;
                int maxCount = 200;
                for (int i = 0; i < maxCount&&!reverted; i++) {
                    int x = random.nextInt(model.modelWidth());
                    int y = random.nextInt(model.modelHeight());
                    SquareModel square = model.getModelSquare(x,y);
                    if (square.isPlayer(false)&&square.getView(0)==0) {
                        revert.doClick(x,y);
                        reverted=true;
                        if (square.getType()== SquareType.Road) {
                            if (!doRoads(x,y,true)) if (!doRoads(x,y,false)) throw new RuntimeException("Failure to do roads");
                        }
                    }
                }
                if (!reverted) turnsToRevert++;
            }
            else {
                BoardEvaluator evaluator = new BoardEvaluator(model,0);
                int x = evaluator.nextX();
                int y = evaluator.nextY();
                System.out.println("Evaluated: "+ x+": "+y);
                if (x>=0) {
                    place.doClick(x,y);
                    SquareModel square = model.getModelSquare(x,y);
                    if (square.getType()== SquareType.Road) {
                        if (!doRoads(x,y,true)) if (!doRoads(x,y,false)) throw new RuntimeException("Failure to do roads");
                    }
                } else {
                    place.doClick(random.nextInt(8),random.nextInt(8));
                }
            }
            while (model.getEndStatus()== BoxModel.EndStatus.ONGOING&&!model.responsive()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
                model.step();
                model.step();
            }
        }
        System.out.println("AI stopping");
    }
    private boolean doRoads(int x, int y, boolean testEmpty) {
        return doRoad(x+1,y,testEmpty)||doRoad(x,y+1,testEmpty)||doRoad(x,y-1,testEmpty)||doRoad(x-1,y,testEmpty);
    }
    private boolean doRoad(int x, int y, boolean testEmpty) {
        System.out.println("Entering do road: "+x+" "+y+" "+testEmpty);
        if (x<0||y<0||x>=model.modelWidth()||y>=model.modelHeight()) {
            System.out.println("Not in bounds");
            return false;
        }
        if (!model.getModelSquare(x,y).getHighlight()) {
            System.out.println("Not highlighted");
            return false;
        }
        if (testEmpty&&model.getModelSquare(x,y).getView(0)!=0) {
            System.out.println("Not empty");
            return false;
        }
        place.doClick(x,y);
        System.out.println("Road done");
        return true;
    }

    @Override
    public void updateAllSquares() {

    }

    @Override
    public void updateGlobal() {

    }

    @Override
    public void updateEndStatus() {

    }

    @Override
    public void update(int x, int y) {

    }
}
