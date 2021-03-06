package com.partyhard.game;

import java.util.Random;

import utils.LabelAccessor;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_ScreenSplash implements Screen{
	
	private PartyHard_GameClass game;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	
	private Stage stage;
	
	private TweenManager tweenManager = new TweenManager();
	
	//the speed of the bouncy animation
	private int velocity = 2;
	
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
	
	private Music backgroundMusic;
	
	private ListStyle listStyle = new ListStyle();
	private TextButtonStyle buttonStyle = new TextButtonStyle();
	private LabelStyle labelStyle = new LabelStyle();

	private Skin skin;
	
	private float buttonSizeWidth = 0;
	private float buttonSizeHeight = 0;
	
	public PartyHard_ScreenSplash(PartyHard_GameClass gam)
	{
		game = gam;
	}
	

	@Override
	public void show() {
		
		TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
			
		skin = new Skin(tex);
		 
		NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));
			
		final NinePatchDrawable drawable = new NinePatchDrawable(patch);
		 
		listStyle.selection = drawable;
		listStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		//listStyle.font.getData().setScale(0.7f);
	    listStyle.fontColorSelected = Color.BLACK;
	    listStyle.fontColorUnselected = Color.WHITE;
	    listStyle.background = drawable;
	 
	    
	    
	    buttonStyle.up = skin.getDrawable("button.up");
		buttonStyle.down = skin.getDrawable("button.down");
		
		buttonStyle.pressedOffsetX = 1;
		buttonStyle.pressedOffsetY = -1;
		BitmapFont font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		buttonStyle.font = font;
		
		labelStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		labelStyle.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		labelStyle.fontColor = Color.WHITE;
		
		
		//used for knowing if there is saved games
		final FileHandle file = Gdx.files.internal("save/");	 
		
		
	//registering the accessors (used for modifying values with tween)
	Tween.registerAccessor(Label.class, new LabelAccessor());
		
	backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/map_sound_relax.mp3"));
	backgroundMusic.play();
	backgroundMusic.setLooping(true);
	
	camera = new OrthographicCamera();
	camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
	
	Random r = new Random();
	
	//choosing randomly the map to load
	switch(r.nextInt(3))
	{
		case 0:
			tiledmap = new TmxMapLoader().load("mainlevel.tmx");
			break;
		case 1:
			tiledmap = new TmxMapLoader().load("dungeonlv2.tmx");
			break;
		case 2:
			tiledmap = new TmxMapLoader().load("forest1.tmx");
			break;
	}
	
	
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
	
	buttonSizeHeight = stage.getHeight() / 4;
	buttonSizeWidth = stage.getWidth() / 4;
	
	Table buttonTable = new Table();
	buttonTable.setName("buttonTable");
	buttonTable.setSize(stage.getWidth(), stage.getHeight());
	
	/*
	 * creating 3 buttons one for play two for load three for quit
	 * TO DO  Options
	 */	

	 TextButton playButton = new TextButton("New Game", buttonStyle);
	
	playButton.setHeight(buttonSizeHeight);
	playButton.setWidth(buttonSizeWidth);
	playButton.setPosition((stage.getWidth() / 2) - playButton.getWidth() / 2, playButton.getHeight() * 2);
	
	playButton.addListener(new ClickListener(){
		  @Override 
          public void clicked(InputEvent event, float x, float y){
          	tiledmap.dispose();
          	backgroundMusic.stop();   		
          
          loadNewGame();
          }
	});	
	
	 TextButton quitButton = new TextButton("Quit", buttonStyle);
	
	quitButton.setHeight(playButton.getHeight());
	quitButton.setWidth(playButton.getWidth());
	quitButton.setPosition(stage.getWidth() / 2 - quitButton.getWidth() / 2 , 0);
	
	quitButton.addListener(new ClickListener(){
		  @Override 
          public void clicked(InputEvent event, float x, float y){          	
			  dispose();
			  Gdx.app.exit();    
          }
	});	
	
	//creating the options button
	
	TextButton optionsButton = new TextButton("Options", buttonStyle);
	
	optionsButton.setHeight(playButton.getHeight() / 2);
	optionsButton.setWidth(playButton.getWidth() / 2);
	
	optionsButton.bottom().right();
	
	optionsButton.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			toggleOptions();
		}
	});
	
	
	//if there is saved games
		if(file.list().length > 0)
		{
			 final TextButton loadButton = new TextButton("Load", buttonStyle);
				
				loadButton.setHeight(quitButton.getHeight());
				loadButton.setWidth(quitButton.getWidth());
				loadButton.setPosition(quitButton.getX(), quitButton.getY() + quitButton.getHeight());
				
				loadButton.addListener(new ClickListener(){
					

					@Override 
			          public void clicked(InputEvent event, float x, float y){      
						  if(getTableIndex("tableSave") != -1)
							  stage.getActors().removeIndex(getTableIndex("tableSave"));
						
						Table tableSave = new Table();
						  tableSave.setName("tableSave");
						  tableSave.setSize(stage.getWidth(), stage.getHeight());
						  tableSave.setPosition(loadButton.getX() + loadButton.getWidth() + 10, loadButton.getY());
						  						
						 
						 List<Label> listSave = new List<Label>(listStyle);
						 listSave.setPosition(0, 0);
						 listSave.setWidth(50);
						 
						 //populating the list with save game
						 for(int i = 0; i < file.list().length;i+=2)
						 {	
							 //cleaning up the name of the path
							 String name = file.list()[i].nameWithoutExtension();
							 name = name.replace("save/", "");
							 name = name.replace("Fight", "");
							 
							 Label save = new Label(""+name, labelStyle);
							 save.setName("Save n."+name);
							 
							 listSave.getItems().add(save);
						 }
						 
						 listSave.addListener(new ClickListener(){
							 @Override
							public void clicked(InputEvent event, float x,float y) {
								
								 List<Label> list = (List<Label>) event.getTarget();
								 
								 int idSave = Integer.parseInt(list.getSelected().getText().toString());
								 
								  PartyHard_MapScreen map = new PartyHard_MapScreen(game, new PartyHard_Player_Map(idSave), idSave);
								 					 
								 //disposing the map and the music
								 tiledmap.dispose();
						         backgroundMusic.stop();
						         
						         game.setScreen(map);
								 
							}
						 });
						 
						 ScrollPaneStyle style = new ScrollPaneStyle();
						 style.vScrollKnob = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/cursor.9.png")))));
						 ScrollPane scroll = new ScrollPane(listSave, style);
						 scroll.setForceScroll(false, true);
						 											 
						 scroll.setPosition(listSave.getX(), listSave.getY());
						 
						 Label chooseSave = new Label("Choose a save!", labelStyle);
						 chooseSave.setPosition(scroll.getX(), scroll.getY() + scroll.getHeight());
						 
						 tableSave.addActor(chooseSave);
						 tableSave.addActor(scroll);
						 
						 
						 stage.addActor(tableSave);
			          }
				});
				
		buttonTable.addActor(loadButton);
		}
	
	buttonTable.addActor(optionsButton);	
	buttonTable.addActor(playButton);
	buttonTable.addActor(quitButton);
	
	/*
	 * creating the title animation
	 */	
	
	Label labelTitle = new Label("Party Hard!",labelStyle);
	labelTitle.setFontScale(4, 2);
	
	labelTitle.setPosition(Gdx.graphics.getWidth() / 2 - (labelTitle.getWidth() * labelTitle.getFontScaleX()) / 2, (float) ((Gdx.graphics.getHeight() - (labelTitle.getHeight() * labelTitle.getFontScaleY())) ) );
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        tweenManager.update(delta);
        
        maprenderer.setView(camera);
        maprenderer.render();

		stage.act(delta);
		stage.draw();
		
			
		switch(movingX)
		{
			case 1: // +				
				if(camera.position.x + camera.viewportWidth / 2 < mapWidthPixel && camera.position.x + ((int) 1 + delta) < mapWidthPixel)
				{
					camera.position.x += ((int) velocity + delta);
				}
				else
					generateDirection();				
				break;
			case 2: // -
				if(camera.position.x - camera.viewportWidth / 2 > 0 && camera.position.x - ((int) 1 + delta) > 0)
				{
					camera.position.x -= ((int) velocity + delta);
				}
				else
					generateDirection();			
				break;
		}
		
		switch(movingY)
		{
		case 1: // +				
			if(camera.position.y + camera.viewportHeight / 2 < mapHeightPixel && camera.position.y + ((int) 1 + delta) < mapHeightPixel)
			{
				camera.position.y += ((int) velocity + delta);
			}
			else
				generateDirection();				
			break;
		case 2: // -
			if(camera.position.y - camera.viewportHeight / 2 > 0 && camera.position.y - ((int) 1 + delta) > 0)
			{
				camera.position.y -= ((int) velocity + delta);
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
	
	private void loadNewGame()
	{
		PartyHard_NewGame map = new PartyHard_NewGame(game,this );
       	game.setScreen(map);
	}
	
	private void toggleOptions()
	{
		if(getTableIndex("options") != -1)
			stage.getActors().removeIndex(getTableIndex("options"));
		else
		{
			Table tableOptions = new Table();
			tableOptions.setName("options");
			
			tableOptions.setSize(stage.getWidth(), stage.getHeight());
			
			SliderStyle style = new SliderStyle();
			style.background = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/blue_panel.9.png")))));
			style.knob = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/cursor.9.png")))));
			
			Slider slider = new Slider(0, 100, 0.5f, false, style);
			slider.setValue(game.masterVolume);
			
			//listener that updates the label
			slider.addListener(new ChangeListener()
			{

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					//refresh the master volume
					game.masterVolume = ((Slider)actor).getValue();
					
					//refresh the label option
					refreshVolumeOption(game.masterVolume);					
				}							
			}
			);
					
			slider.setPosition(0, buttonSizeHeight / 2 );
			
			Label lblVolume = new Label("Volume: "+slider.getValue(), labelStyle);
			lblVolume.setPosition(0, slider.getY() + slider.getHeight());			
			
			
			tableOptions.addActor(lblVolume);
			tableOptions.addActor(slider);
			stage.addActor(tableOptions);
		}
	}
	/**
	 * 
	 * @param value The master volume's value
	 */
	private void refreshVolumeOption(float value) {
		/*
		 * update the label in the option and the music
		 */
		Table tableOption = (Table) stage.getActors().get(getTableIndex("options"));
		
		//update label
		Label lblValue = (Label) tableOption.getChildren().get(0);
		lblValue.setText("Volume: "+value);
		
		//update table
		tableOption.getChildren().items[0] = lblValue;
		stage.getActors().items[getTableIndex("options")] = tableOption;
		
		//update sounds
		backgroundMusic.setVolume(value/100);
		
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
	
	/**
	 * @param name The name of the table
	 * 
	 * @return The index of the Table
	 */
	private int getTableIndex(String name)
	{
		for(int i = 0; i < stage.getActors().size; i++)
		{
			if(stage.getActors().get(i).getName() == name)
				return i;
		}	
		return -1;
	}
}
