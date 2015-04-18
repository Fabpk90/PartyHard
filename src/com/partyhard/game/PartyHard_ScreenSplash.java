package com.partyhard.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_ScreenSplash implements Screen{
	
	private Game game;
	private Texture backgroundImg;
	
	private Stage stage;
	private Table buttonTable;
	
	public PartyHard_ScreenSplash(Game gam)
	{
		game = gam;
	}
	

	@Override
	public void show() {
	stage = new Stage();
	buttonTable = new Table();
	
	/*
	 * creating 2 button one for play two for quit
	 * TO DO  Options
	 */
	
	TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
	
	Skin skin = new Skin(tex);

	
	TextButtonStyle buttonStyle = new TextButtonStyle();
	
	buttonStyle.up = skin.getDrawable("button.up");
	buttonStyle.down = skin.getDrawable("button.down");
	
	buttonStyle.pressedOffsetX = 1;
	buttonStyle.pressedOffsetY = -1;
	
	BitmapFont font = new BitmapFont();
	font.setColor(Color.BLUE);
	font.setScale(1.5f);	
	
	buttonStyle.font = font; 
	
	 TextButton playButton = new TextButton("Play", buttonStyle);
	
	playButton.pad(10);
	
	playButton.setHeight(Gdx.graphics.getHeight() / 2);
	playButton.setWidth(Gdx.graphics.getWidth() / 4);
	playButton.setPosition((Gdx.graphics.getWidth() / 2) - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	
	playButton.addListener(new ClickListener(){
		  @Override 
          public void clicked(InputEvent event, float x, float y){
          	
            PartyHard_MapScreen map = new PartyHard_MapScreen(game,"mainlevel", new PartyHard_Player_Map(100,100, "player/test_player.png"));
            game.setScreen(map);
          }
	});
	
	 TextButton quitButton = new TextButton("Quit", buttonStyle);
	
	quitButton.setHeight(playButton.getHeight() - quitButton.getHeight());
	quitButton.setWidth(playButton.getWidth());
	quitButton.setPosition(playButton.getX(), playButton.getY() - quitButton.getHeight() - 50);
	
	quitButton.addListener(new ClickListener(){
		  @Override 
          public void clicked(InputEvent event, float x, float y){
          	
			  dispose();
			  Gdx.app.exit();
            
          }
	});
	
	quitButton.pad(10);
	
	buttonTable.addActor(playButton);
	buttonTable.addActor(quitButton);
	
	stage.addActor(buttonTable);
	
	//telling that stage can manage input
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
		// TODO Auto-generated method stub
		
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
