package com.partyhard.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PartyHard_EndScreen implements Screen {

	private LabelStyle labelStyle = new LabelStyle();
	private Stage stage = new Stage();
	private PartyHard_GameClass game;
	
	
	public PartyHard_EndScreen(PartyHard_GameClass game)
	{
		this.game = game;
	}
	
	@Override
	public void show() {
		 labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		 
		 Table tableFinal = new Table();
		 
		 Label lblThanks = new Label("Thanks for playing this Demo!", labelStyle);
		 lblThanks.setFontScale(2f);
		 lblThanks.setPosition(stage.getWidth() / 2 - lblThanks.getWidth(), stage.getHeight() / 2);		 
		 
		 lblThanks.setColor(Color.WHITE);
		 
		 Label lblExit = new Label("Exit", labelStyle);
		 lblExit.setPosition(stage.getWidth() / 2 - lblExit.getWidth(), 0);
		 
		 lblExit.addListener(new ClickListener(){
		 @Override
		public void clicked(InputEvent event, float x, float y) {
			
			 PartyHard_ScreenSplash screen = new PartyHard_ScreenSplash(game);
			 game.setScreen(screen);
			 
		}
		 });
		 
		 tableFinal.addActor(lblThanks);
		 tableFinal.addActor(lblExit);
		 
		 stage.addActor(tableFinal);
		 
		 Gdx.input.setInputProcessor(stage);
		 
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();

	}

}
