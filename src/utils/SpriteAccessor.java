package utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

	/*
	 * defining constant for indicating the properties that we want to change
	 */
	public static final int ALPHA = 0;
	public static final int Y = 1;
	public static final int X = 2;
	
	public SpriteAccessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getValues(Sprite sprite, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				returnValues[0] = sprite.getColor().a;
				return 1;
			case Y:
				returnValues[0] = sprite.getY();
				return 1;
			case X:
				returnValues[0] = sprite.getX();
				return 1;
		}
		//used an unknown tweentype
		return -1;
	}
	@Override
	public void setValues(Sprite sprite, int tweenType, float[] returnValues) {
		switch(tweenType)
		{
			case ALPHA:
				sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, returnValues[0]);
				break;
			case Y:
				sprite.setY(returnValues[0]);
				break;			
			case X:
				sprite.setX(returnValues[0]);
				break;
		}
		
	}
	
	
	

	




}
