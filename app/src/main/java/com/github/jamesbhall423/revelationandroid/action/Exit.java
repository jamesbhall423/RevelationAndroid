package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;
import static com.github.jamesbhall423.revelationandroid.model.BoxModel.EndStatus;


public class Exit extends CAction {
	public static final Exit EXIT = new Exit();
	private Exit() {
		super(0,0,"exit",true);
	}
	@Override
	protected void process(BoxModel boxModel) {
		if (boxModel.getEndStatus()==EndStatus.ONGOING) boxModel.setEndStatus(EndStatus.OTHER_LEFT);
	}
	@Override
	public int typeVal() {
		return VAL_MIN;
	}
}
