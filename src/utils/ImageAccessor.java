package utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageAccessor implements TweenAccessor<Image> {
	/*
	 * defining constant for indicating the properties that we want to change
	 */
	public static final int ALPHA = 0;
	public static final int Y = 1;
	public static final int X = 2;
	
	public ImageAccessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getValues(Image image, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				returnValues[0] = image.getColor().a;
				return 1;
			case Y:
				returnValues[0] = image.getY();
				return 1;
			case X:
				returnValues[0] = image.getX();
				return 1;
		}
		
		return -1;
	}

	@Override
	public void setValues(Image image, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				image.setColor(image.getColor().r, image.getColor().g, image.getColor().b, returnValues[0]);
				break;
			case Y:
				image.setY(returnValues[0]);
				break;
			case X:
				image.setX(returnValues[0]);
				break;
		}
		
	}

}
