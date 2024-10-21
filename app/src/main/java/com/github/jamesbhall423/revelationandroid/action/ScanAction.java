package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class ScanAction extends CAction{
    private int centerX;
    private int centerY;
    private int displayX;
    private int displayY;
    public ScanAction(int player, int startTime, int x, int y) {
        super(player, 1, "scan", false);
        centerX = x;
        centerY = y;
        displayX = x;
        displayY = y;
        setStartTime(startTime);
    }
	@Override
	public String toString() {
		String out = LN+getName()+"- player: "+(player()+1)+" time: "+getStartTime()+" at "+SquareClass.coordinates(displayX,displayY);
		if (getResponse()!=null) out+=LN+"result: "+getResponse();
		out+=LN;
		return out;
	}

    @Override
    protected void process(BoxModel boxModel) {
		SquareModel square = boxModel.getModelSquare(centerX,centerY);
		displayX=square.displayX();
		displayY=square.displayY();
        int results[][] = new int[3][3];
        String out = "[";
        for (int y = displayY-1; y <= displayY+1; y++) if (boxModel.onDisplayModel(displayX,y)) {
            String nextPart = "[";
            for (int x = displayX-1; x <= displayX+1; x++) if (boxModel.onDisplayModel(x,y)) {
                SquareModel next = boxModel.getDisplaySquare(x,y);
                results[y-displayY+1][x-displayX+1] = next.player();
                next.setView(player(),next.player());
            }
            out+=nextPart+"]";
        } 
        setResponse(out+"]");
    }
    
}
