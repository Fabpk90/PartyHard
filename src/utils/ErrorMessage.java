package utils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ErrorMessage {
	
	//used for find the table each id = to a table for managing more errors
	private static int id = 0;
	
	public ErrorMessage() {
	
		
		id++;
		
	}
	
	/*
	 * @param Text The Text that will be show as the error
	 * @param color The text color
	 * @param duration The duration of the animation
	 */
	
	public static void createError(String Text, Color color, LabelStyle labelStyle, float duration, Stage stage, TweenManager tweenManager)
	{
		Table tableError = new Table();
		
		tableError.setName(""+id);
		tableError.center();
		
		Label label = new Label(Text, labelStyle);
		label.setColor(color);
		
		label.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		
		stage.addActor(tableError);

    	System.out.println("asd");
    	
		Tween.set(label, LabelAccessor.Y).target(label.getY()).start(tweenManager);
		Tween.set(label, LabelAccessor.ALPHA).target(1).start(tweenManager);
		
		Tween.to(label, LabelAccessor.Y, 5f).target(Gdx.graphics.getHeight()).start(tweenManager);
		Tween.to(label, LabelAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);	
	
	}

}
