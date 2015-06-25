package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PartyHard_Fight_Animation {

	private Animation animation;
	
	public PartyHard_Fight_Animation(String pathAnimation)
	{
		Texture animationSheet = new Texture(Gdx.files.internal("aniamtion/"+pathAnimation+".png"));
	}
	

	
}
