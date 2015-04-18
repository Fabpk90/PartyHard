package utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelAccessor implements TweenAccessor<Label> {

	/*
	 * defining constant for indicating the properties that we want to change
	 */
	public static final int ALPHA = 0;
	public static final int Y = 1;
	public static final int X = 2;
	
	
	public LabelAccessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getValues(Label label, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				returnValues[0] = label.getColor().a;
				return 1;
			case Y:
				returnValues[0] = label.getY();
				return 1;
			case X:
				returnValues[0] = label.getX();
				return 1;
		}
		//used an unknown tweentype
		return -1;
	}

	@Override
	public void setValues(Label label, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				label.setColor(label.getColor().r, label.getColor().g, label.getColor().b, returnValues[0]);
				break;
			case Y:
				label.setY(returnValues[0]);
				break;
				
			case X:
				label.setX(returnValues[0]);
				break;
		}
	}

	

}
