package com.github.jamesbhall423.revelationandroid.graphics;

import com.github.jamesbhall423.revelationandroid.model.*;

import static com.github.jamesbhall423.revelationandroid.model.SquareClass.*;


public class SquarePainter {
	public static void paint(SquareClass model, SquareGraphics g,RevelationDisplayLocal endDisplay,boolean showTrueValue) {
		if (g==null) return;
		double height = g.getHeight();
		double width = g.getWidth();
		int color;
		switch (model.getType()) {
			case Empty:
			case Road:
			color=Colors.orange;
			break;
			case Mountain:
			color=Colors.gray;
			break;
			case Forest:
			color=Colors.green;
			break;
			default:
			throw new Error("Invalid type");
		}
		g.setColor(Colors.white);
		g.fillRectangle(0,0,(int)width,(int)height);
		if (model.getHighlight()) g.setColor(color);
		else g.setColor(Colors.darker(color));
		g.fillRectangle(1,1,(int)width-1,(int)height-1);
		if (model.getTeleporter()!=null) {
			g.setColor(Colors.blue);
			g.fillRectangle(1,1,(int)width/3,(int)height/3);
			g.setColor(Colors.black);
			g.setFontSize((int)(width/3.5));
			g.setFontBold();
			g.drawString(""+model.getTeleporter()[2],1,(int)(width/3.5));
		}
		drawRoads(model, g, endDisplay, width, height);
//		int pl = model.player();
//		if (model.flipDisplay()) pl=-pl;
//		if (pl==1) g.setColor(Colors.cyan);
//		else if (pl==-1) g.setColor(Colors.magenta);
//		if (model.player()!=0) g.fillRectangle(2*(int)width/3,2*(int)height/3,(int)width/3,(int)height/3);
		int player = model.flipDisplay() ? 1 : 0;
		int view = showTrueValue ? model.player() : model.getView(player);
		setPlayerColor(model,g, view);
		if (endDisplay!=null) drawRevelationDisplay(model,g,endDisplay.display(model),width,height);
		else if (view!=0) g.fillEllipse((int)width/3,(int)height/3,(int)width/3,(int)height/3);
		String turns = null;
		if (model.time()>1) turns = ""+model.time();
		if (turns!=null) {
			g.setColor(Colors.black);
			g.setFontSize((int)(width/3.5));
			g.setFontPlain();
			g.drawString(turns,(int) (2*width/3),(int) (0.9*height));
		}
	}
	private static void drawRevelationDisplay(SquareClass model, SquareGraphics g, String endDisplay, double width, double height) {
		g.setFontSize((int)width/2);
		g.setFontBold();
		int x = (int)width/3;
		if (endDisplay.length()>1) x-=(int) width/8;
		int y = (int)(2*height/3);
		g.drawString(endDisplay, x, y);
	}
	private static void setPlayerColor(SquareClass model, SquareGraphics graphics, int view) {
		if (!model.flipDisplay()&&view==1||model.flipDisplay()&&view==-1) graphics.setColor(Colors.cyan);
		else if (!model.flipDisplay()&&view==-1||model.flipDisplay()&&view==1) graphics.setColor(Colors.magenta);
	}
	private static void drawRoads(SquareClass model, SquareGraphics g, RevelationDisplayLocal endDisplay, double width, double height) {
		if (model.getType()==SquareType.Road) {
			if (endDisplay==null) g.setColor(Colors.yellow);
			else g.setColor(Colors.darker(Colors.yellow));
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(LEFT)) : model.isRoad(LEFT)) g.fillRectangle(0,(int)(2*height/5),(int)width/2,(int)(height/5));
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(RIGHT)) : model.isRoad(RIGHT)) g.fillRectangle((int)width/2,(int)(2*height/5),(int)width/2,(int)(height/5));
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(UP)) : model.isRoad(UP)) g.fillRectangle((int)(2*width/5),0,(int)width/5,(int)height/2);
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(DOWN)) : model.isRoad(DOWN)) g.fillRectangle((int)(2*width/5),(int)height/2,(int)width/5,(int)height/2);
		}
	}
}
