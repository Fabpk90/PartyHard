package com.partyhard.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
@SuppressWarnings("unused")
public class PartyHard_GameClass extends Game {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	@Override
	public void create() {		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		/*
		 * loading the fancy blue cursor only if the client is on android
		 */
		if(Gdx.app.getType() == ApplicationType.Desktop)
		{
			Pixmap pm = new Pixmap(Gdx.files.internal("ui/cursorHand.png"));
			Gdx.input.setCursorImage(pm, 0, 0);
			pm.dispose();
		}
			
		PartyHard_ScreenSplash screensplash = new PartyHard_ScreenSplash(this);
		this.setScreen(screensplash);	
		
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.exit();
		
	}

	@Override
	public void render() {	
		super.render();
	Gdx.gl.glClearColor(0, 0, 0, 1);
			
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
