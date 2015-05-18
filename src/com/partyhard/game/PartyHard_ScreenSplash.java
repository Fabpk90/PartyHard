package com.partyhard.game;

import java.util.Random;

import utils.LabelAccessor;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_ScreenSplash implements Screen{
	
	private Game game;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	
	private Stage stage;
	
	private TweenManager tweenManager = new TweenManager();
	
	private int movingX = 0;
	private int movingY = 0;
	/*
	 * 0 not moving
	 * 1 +
	 * 2 -
	 */
	
	private int mapWidthPixel;
	private int mapHeightPixel;
	
	private Random r = new Random();
	
	public PartyHard_ScreenSplash(Game gam)
	{
		game = gam;
	}
	

	@Override
	public void show() {
	//registering the accessors (used for modifying values with tween)
	Tween.registerAccessor(Label.class, new LabelAccessor());
		
	camera = new OrthographicCamera();
	camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
	
	
	tiledmap = new TmxMapLoader().load("mainlevel.tmx");
	//setting the camera on the middle of the screen
	
	maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
	
	MapProperties prop = tiledmap.getProperties();
	
	//calculating width and height of the map		
	 int mapWidth = prop.get("width", Integer.class);
	 int mapHeight = prop.get("height", Integer.class);
	 int tilePixelWidth = prop.get("tilewidth", Integer.class);
	 int tilePixelHeight = prop.get("tileheight", Integer.class);
	 
	 mapWidthPixel = mapWidth * tilePixelWidth;
	 mapHeightPixel = mapHeight * tilePixelHeight; 
	
	 //setting the camera at the middle of the map
	camera.position.set(mapWidthPixel / 2, mapHeightPixel / 2, 0);
		
	stage = new Stage();
	Table buttonTable = new Table();
	
	/*
	 * creating 2 button one for play two for quit
	 * TO DO  Options
	 */
	
	TextureAtlas tex = new TextureAtlas(Gdx.files.internal("ui_button/button.pack"));
	
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
	
	playButton.setHeight(Gdx.graphics.getHeight() / 4);
	playButton.setWidth(Gdx.graphics.getWidth() / 4);
	playButton.setPosition((Gdx.graphics.getWidth() / 2) - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 4);
	
	playButton.addListener(new ClickListener(){
		  @Override 
          public void clicked(InputEvent event, float x, float y){
          	tiledmap.dispose();
            PartyHard_MapScreen map = new PartyHard_MapScreen(game,"mainlevel", new PartyHard_Player_Map(256,256, "player/test_player.png"));
            game.setScreen(map);
          }
	});
	playButton.pad(5);
	
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
	
	quitButton.pad(5);
	
	buttonTable.addActor(playButton);
	buttonTable.addActor(quitButton);
	
	/*
	 * creating the title animation
	 */
		
	LabelStyle labelStyle = new LabelStyle();
	labelStyle.font = new BitmapFont();
	
	Label labelTitle = new Label("Party Hard!",labelStyle);
	labelTitle.setFontScale(6, 4);
	
	labelTitle.setPosition(Gdx.graphics.getWidth() / 2 - (labelTitle.getWidth() * labelTitle.getFontScaleX()) / 2, (float) ((Gdx.graphics.getHeight()* 2) / 3));
	/*
	 * first adding all in the stage to access to the label in Tween
	 */
	
	stage.addActor(labelTitle);
	stage.addActor(buttonTable);
	
	//creating the sequence for the animation
	
	Timeline.createSequence()
	.push(Tween.set(labelTitle, LabelAccessor.ScaleFont).target(labelTitle.getFontScaleX(), labelTitle.getFontScaleY()))
	.push(Tween.to(labelTitle, LabelAccessor.ScaleFont, 2f).target(labelTitle.getFontScaleX() + 1, labelTitle.getFontScaleY() + 2))
	.repeatYoyo(-1, 0f)
	.start(tweenManager);
	
	
	//telling that stage can manage input
	 Gdx.input.setInputProcessor(stage);
	 
	 //updating camera
	 camera.update();
	 
	 generateDirection();
	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        tweenManager.update(delta);
        
        maprenderer.setView(camera);
        maprenderer.render();

		stage.act(delta);
		stage.draw();
		
			
		switch(movingX)
		{
			case 1: // +				
				if(camera.position.x + camera.viewportWidth / 2 < mapWidthPixel)
				{
					camera.position.x += (int) 2 + delta;
				}
				else
					generateDirection();				
				break;
			case 2: // -
				if(camera.position.x - camera.viewportWidth / 2 > 0)
				{
					camera.position.x -= (int) 2 + delta;
				}
				else
					generateDirection();			
				break;
		}
		
		switch(movingY)
		{
		case 1: // +				
			if(camera.position.y + camera.viewportHeight / 2 < mapHeightPixel)
			{
				camera.position.y += (int) 2 + delta;
			}
			else
				generateDirection();				
			break;
		case 2: // -
			if(camera.position.y - camera.viewportHeight / 2 > 0)
			{
				camera.position.y -= (int) 2 + delta;
			}
			else
				generateDirection();			
			break;
		}
		
		camera.update();
		
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
		game.dispose();
		tiledmap.dispose();
	}
	
	private void generateDirection()
	{
			//random number to see where will move
				movingX = r.nextInt(3);
				movingY = r.nextInt(3);
				
				//if the camera is not moving then recall the function to make it move
				if(movingY == 0 && movingX == 0)
				{
					generateDirection();
				}
	}
}
