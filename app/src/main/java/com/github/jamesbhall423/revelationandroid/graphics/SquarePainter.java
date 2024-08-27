package com.github.jamesbhall423.revelationandroid.graphics;

import com.github.jamesbhall423.revelationandroid.model.*;

import static com.github.jamesbhall423.revelationandroid.model.SquareClass.*;


public class SquarePainter {
	public static void paint(SquareClass model, SquareGraphics g) {
		paint(model,g,null);
	}
	public static void paint(SquareClass model, SquareGraphics g,RevelationDisplayLocal endDisplay) {
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
		if (model.getHighlight()) g.setColor(color);
		else g.setColor(Colors.darker(color));
		g.fillRectangle(0,0,(int)width,(int)height);
		if (model.getTeleporter()!=null) {
			g.setColor(Colors.blue);
			g.fillRectangle(0,0,12,12);
			g.setColor(Colors.black);
			g.setFontSize(11);
			g.setFontBold();
			g.drawString(""+model.getTeleporter()[2],0,11);
		}
		drawRoads(model, g, endDisplay, width, height);
		setPlayerColor(model,g);
		if (endDisplay!=null) drawRevelationDisplay(model,g,endDisplay.display(model),width,height);
		else if (model.getView()!=0) g.fillEllipse((int)width/2-5,(int)height/2-5,10,10);
	}
	private static void drawRevelationDisplay(SquareClass model, SquareGraphics g, String endDisplay, double width, double height) {
		g.setFontSize(18);
		g.setFontBold();
		int x = (int)width/2-6;
		if (endDisplay.length()>1) x-=4;
		int y = (int)height/2+6;
		g.drawString(endDisplay, x, y);
	}
	private static void setPlayerColor(SquareClass model, SquareGraphics graphics) {
		if (!model.flipDisplay()&&model.getView()==1||model.flipDisplay()&&model.getView()==-1) graphics.setColor(Colors.cyan);
		else if (!model.flipDisplay()&&model.getView()==-1||model.flipDisplay()&&model.getView()==1) graphics.setColor(Colors.magenta);
	}
	private static void drawRoads(SquareClass model, SquareGraphics g, RevelationDisplayLocal endDisplay, double width, double height) {
		if (model.getType()==SquareType.Road) {
			if (endDisplay==null) g.setColor(Colors.yellow);
			else g.setColor(Colors.darker(Colors.yellow));
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(LEFT)) : model.isRoad(LEFT)) g.fillRectangle(0,(int)height/2-3,(int)width/2,6);
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(RIGHT)) : model.isRoad(RIGHT)) g.fillRectangle((int)width/2,(int)height/2-3,(int)width/2,6);
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(UP)) : model.isRoad(UP)) g.fillRectangle((int)width/2-3,0,6,(int)height/2);
			if (model.flipDisplay() ? model.isRoad(flipSingleDir(DOWN)) : model.isRoad(DOWN)) g.fillRectangle((int)width/2-3,(int)height/2,6,(int)height/2);
		}
	}
}
