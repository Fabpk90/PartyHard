package com.partyhard.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import utils.CallBackFight;
import utils.FileManager;
import utils.ImageAccessor;
import utils.LabelAccessor;
import utils.ObjectDatabase;
import utils.PartyHard_GameDialogue;
import utils.PartyHard_Shop;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.actor.PartyHard_Player_Map;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weareable;

public class PartyHard_MapScreen implements Screen, InputProcessor{

	public PartyHard_GameClass mainGame;
	private SpriteBatch spriteBatch;  
	
	private Stage stage;	
	
	public String Name;
	
	private PartyHard_Fight fightScreen;
	
	private ArrayList<PartyHard_Player_Fight> playerSquad = new ArrayList<PartyHard_Player_Fight>();
	
	//could be transfered into the player will see
	private int direction = 0;
	/*
	 * 0 back
	 * 1 left
	 * 2 right
	 * 3 toward
	 */
	
	private float animationTime = 0f;
	private float animationFightTime = 1.1f;
	
	private PartyHard_Player_Map playerMap;
	
	private Music mapSound;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	private MapProperties prop;
	
	private float mapPixelWidth;
	private float mapPixelHeight;
	
	private boolean isSafe = true;
	private float timeToFight = 0;
	
	private boolean blockedx = false;
	
	private ListStyle listStyle = new ListStyle();
	private TextButtonStyle buttonStyle = new TextButtonStyle();
	private LabelStyle labelStyle = new LabelStyle();
	private ListStyle listInventoryStyle = new ListStyle();
	
	private NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));						
    private NinePatchDrawable drawable = new NinePatchDrawable(patch);
	
	//for randomly begin a fight
	private float fightTime = 0;	
	//probability of fight
	private int proba = 0;
	//array of monster name 
	private ArrayList<String> nameMonster = new ArrayList<String>();
	private Skin skin;
	
	
	/*
	 * Inventory var
	 */
	
	//in which inventory we are?
	private int playerSelected = -1;
	private int itemIndex = -1;
	private int playerSelectedForObject = -1;
	
	private boolean enterEquip = false;
	
	//used for knowing which item has been selected for buy
	private int itemShopSelected = -1;
	private PartyHard_Shop Shop;
	//used for knowing in which inventory the buy item will goes
	private int playerInventory = -1;
	
	private int idSave;
	private boolean isBoss;
	
	private FileManager fileManager = new FileManager();	
	private TweenManager tweenManager = new TweenManager();
	
	private ArrayList<PartyHard_GameDialogue> dialogueSpeech = new ArrayList<PartyHard_GameDialogue>();
	
	public PartyHard_MapScreen(PartyHard_GameClass gameToKeep, String mapName, PartyHard_Player_Map playerMap, int idSave)
	{	
		mainGame = gameToKeep;
		this.playerMap = playerMap;
		tiledmap = new TmxMapLoader().load(mapName+".tmx");
		this.playerMap.setCollisionLayer(tiledmap);	
		this.idSave = idSave;
		
		this.Name = mapName;
	}
	
	public PartyHard_MapScreen(PartyHard_GameClass gameToKeep, PartyHard_Player_Map playerMap, int idSave)
	{
		mainGame = gameToKeep;
		this.playerMap = playerMap;
		tiledmap = new TmxMapLoader().load(playerMap.getMap()+".tmx");
		this.playerMap.setCollisionLayer(tiledmap);	
		this.idSave = idSave;
		
		this.Name = playerMap.getMap();
	}
	
	@Override
	public void render(float delta) {
			
		animationTime += delta;
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        maprenderer.setView(camera);
        maprenderer.render();
        
        stage.act(delta);
       
        tweenManager.update(delta);
        
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();      
        
       if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) )
        playerMap.update(delta + 2);
       else
    	   playerMap.update(delta);
        
     //updating the player's animation
       
        if(animationTime + delta >= 1)
        	animationTime = 0;
        
        if(playerMap.isMoving())
        {
        	spriteBatch.draw(playerMap.getFrame(direction, animationTime), playerMap.getX(), playerMap.getY());
        	
        	//if the map is not safe
        	
        	if(!isSafe)
        	{        	        	
        		fightTime += delta;
            	//begin fight
            	if(fightTime >= timeToFight)
            	{
            		Random r = new Random();
            		
            		//if pseudo-randomly the fight is happening
            		if(r.nextInt(101) < proba)
            		{
            			playerMap.stopMovement(true);
            			playerMap.isFighting = true;
            		        		
            			loadFight();
            		}
            		fightTime = 0;
            	}
        	}    	
        }
        	
        else
        	spriteBatch.draw(playerMap.getFrame(direction, 0), playerMap.getX(), playerMap.getY());//idle animation
        
        spriteBatch.end();
        /*
         * TO DO:  camera, maybe move the algo into functions
         */
        //used for knowing if the player can go on the x axe
        
        blockedx = false;
        
        	 if(playerMap.getX() - (camera.viewportWidth / 2 )  < 0  || playerMap.getX() + (camera.viewportWidth / 2) > mapPixelWidth)
   		   	 {
        		 blockedx = true;          		 
   		   	 }

        	if(playerMap.isMovingOnX())
        	{
	        		//if the camera is not going outside on the width
	   		   		if(!blockedx)
	   		   		{
	   		   			camera.position.set(playerMap.getX(), camera.position.y, 0);	   		   			
	   		   		}		   				   		   	
        	}
        	
        	
        	if(playerMap.isMovingOnY())
        	{
	        		//same as above but for the height
	   		   	 if(playerMap.getY() - (camera.viewportHeight / 2 )  > 0 && playerMap.getY() + (camera.viewportHeight / 2) < mapPixelHeight)
	   		   	 {
	   		   		 //if the player cannot go on the x axe then move only on the y		   		
	   		   		 camera.position.set(camera.position.x, playerMap.getY(), 0);	   		   		   		   	    	         
	   		   	 }
        	}
        	
        	 camera.update(); 	
		   	       
        /*
	     * Movement
	     */             
        	 //for desktop
        if(Gdx.app.getType() == Gdx.app.getType().Desktop)
        {
        	  if(!playerMap.isMoving() && !playerMap.isShopping && !playerMap.isFighting && !playerMap.isTalking)
          {	    
		    	if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		    	{
		    		playerMap.moving(true);	    		
		    		moveLeft();
		    	}
		    				    			
		        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		        {
		        	playerMap.moving(true);		    
		        	moveRight();
		        }
		        		
		        if(Gdx.input.isKeyPressed(Input.Keys.UP))
		        {
		        	playerMap.moving(true);
		        	moveTop();
		        }
		        		
		        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
		        {
		        	playerMap.moving(true);
		        	moveDown();
		        }
		        
		        
          }
        }     
	        stage.draw();
	        
	        //check if the player is tping
	        if(playerMap.isTp)
	        {	 	        		        	
	        	//setting the new pos thanks to the tp (multiply by the width of a tile to have the same position as tiled)
	        	playerMap.setPosition( playerMap.Tp().destination.x * 32,  playerMap.Tp().destination.y * 32);
	        	
	        	//changing the name of the map
	        	Name =  playerMap.Tp().NameMap;      		
	        	
	        	//reloading
	        	tiledmap = new TmxMapLoader().load(Name+".tmx");	   
	        	
	    		playerMap.setMap(Name, isSafe);
	    		playerMap.setCollisionLayer(tiledmap);
	    			    	
	    		playerMap.isTp = false;
	    		
	    		mapSound.dispose();
	    		
	    		load();
	    		
	        }
	        
	}

	@Override
	public void resize(int width, int height) {
		 stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		
	playerMap.isFighting = false;
	loadSquad();
		
	camera = new OrthographicCamera();
	camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
	
	camera.viewportHeight = camera.viewportHeight / 1.2f;
	camera.viewportWidth = camera.viewportWidth / 1.2f;
	
	camera.position.set(playerMap.getX(), playerMap.getY(), 0);		
	camera.update();
	
	Tween.registerAccessor(Image.class, new ImageAccessor());
	
	Table mapNameTable = new Table();
	stage = new Stage();

		spriteBatch = new SpriteBatch();
		
		/*
		 * Android Button initialization
		 */
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			/*
			 * Loading the skin which contains the arrows info
			 */						
			TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/arrows.pack")));
			
			Skin skin = new Skin(tex);		
			
			/*
			 * Button style that change at each button need a new one each time too apparently
			 */
			
			ButtonStyle styleRight = new ButtonStyle();
			styleRight.up = skin.getDrawable("arrowRight");
			styleRight.down = skin.getDrawable("arrowRight");			
			
			/*
			 * TODO: listener
			 */
			Button rightArrow = new Button(styleRight);
			rightArrow.setHeight((float) (camera.viewportHeight *0.055));
			rightArrow.setWidth((float) (camera.viewportWidth *0.055));
			rightArrow.setPosition(Gdx.graphics.getWidth() - rightArrow.getWidth(), rightArrow.getHeight());
			
			rightArrow.addListener(new InputListener() {
		        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	               if(!playerMap.isMoving())
	               {
	            	   playerMap.moving(true);
	            	   moveRight();
	               }
	                return false;
	        }});
			
			ButtonStyle styledown = new ButtonStyle();
			styledown.up = skin.getDrawable("arrowDown");
			styledown.down = skin.getDrawable("arrowDown");	
			
			Button downArrow = new Button(styledown);
			
			downArrow.setHeight((float) (camera.viewportHeight *0.055));
			downArrow.setWidth((float) (camera.viewportWidth *0.055));
			downArrow.setPosition(rightArrow.getX() - downArrow.getWidth(), 0);
			
			downArrow.addListener(new ClickListener()
			{
	            @Override 
	            public void clicked(InputEvent event, float x, float y)
	            {
	            	if(!playerMap.isMoving())
	            	{
		            	playerMap.moving(true);		    
			        	moveDown();  
	            	}
	            	          
	            }
	        });
			
			ButtonStyle styleleft = new ButtonStyle();
			styleleft.up = skin.getDrawable("arrowLeft");
			styleleft.down = skin.getDrawable("arrowLeft");	
			
			Button leftArrow = new Button();
			leftArrow.setStyle(styleleft);
			
			leftArrow.setHeight((float) (camera.viewportHeight *0.055));
			leftArrow.setWidth((float) (camera.viewportWidth *0.055));
			leftArrow.setPosition(rightArrow.getX() - (leftArrow.getWidth() * 2), leftArrow.getHeight() );
			
			leftArrow.addListener(new ClickListener()
			{
	            @Override 
	            public void clicked(InputEvent event, float x, float y)
	            {
	            	if(!playerMap.isMoving())
	            	{
		            	playerMap.moving(true);		    
			        	moveLeft();
	            	}
	            	            
	            }
	        });
			
			
			ButtonStyle styleright = new ButtonStyle();
			styleright.up = skin.getDrawable("arrowUp");
			styleright.down = skin.getDrawable("arrowUp");	
			
			Button topArrow = new Button();
			topArrow.setStyle(styleright);
			
			topArrow.setHeight((float) (camera.viewportHeight *0.055));
			topArrow.setWidth((float) (camera.viewportWidth *0.055));
			topArrow.setPosition(downArrow.getX(), topArrow.getHeight() * 2);
			
			topArrow.addListener(new ClickListener()
			{
	            @Override 
	            public void clicked(InputEvent event, float x, float y)
	            {
	            	if(!playerMap.isMoving())
	            	{
	            		playerMap.moving(true);		    
	            		moveTop();
	            	}
	            	            
	            }
	        });
			
			Table androidarrow = new Table();
			androidarrow.setName("androidArrow");//not used but settled for further uses
			
			androidarrow.addActor(topArrow);
			androidarrow.addActor(leftArrow);
			androidarrow.addActor(downArrow);
			androidarrow.addActor(rightArrow);
			
			stage.addActor(androidarrow);

		}
		
		playerMap.createPlayerAnimation();
		
		
		 prop = tiledmap.getProperties();		 
		 Name = prop.get("Name", String.class);
		 
		 		 
		 labelStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		 
		 //used for printing the map name
		 Label labelname = new Label(Name, labelStyle);			
		
		 // setting position of the label
		 mapNameTable.center().top();
		 mapNameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 mapNameTable.setName("name");
			 
		 labelname.setPosition(mapNameTable.getWidth() / 2 - labelname.getWidth(), mapNameTable.getHeight() - 30); 
		
		 mapNameTable.addActor(labelname);
		 stage.addActor(mapNameTable);
	
		 //setting processor for input
		 
		 InputMultiplexer inputMultiplexer = new InputMultiplexer();
		 inputMultiplexer.addProcessor(stage);
		 inputMultiplexer.addProcessor(this);
		 
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//stage.setDebugAll(true);
		load();
	}

	@Override
	public void hide() {
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
	
	public void load()
	{
		 prop = tiledmap.getProperties();
		
		//calculating width and height of the map		
		 int mapWidth = prop.get("width", Integer.class);
		 int mapHeight = prop.get("height", Integer.class);
		 int tilePixelWidth = prop.get("tilewidth", Integer.class);
		 int tilePixelHeight = prop.get("tileheight", Integer.class);
		 
		Name = prop.get("Name", String.class);	
		
		 //getting if the map is safe or not
		 isSafe = Boolean.valueOf(prop.get("Safe", String.class));
	     isBoss = Boolean.valueOf(prop.get("Boss", String.class));		
		 
		 //getting ifght info only if the map is not safe
		 if(!isSafe)
		 {
			 if(!isBoss)
			 {
				 //time for checking fight
				 timeToFight = Integer.parseInt(prop.get("Time", String.class));
				 //probability for a fight
				 proba = Integer.parseInt(prop.get("Proba", String.class));
				 
				 //getting the name of the monsters then splitting them(they are stored with a comma)
				 String monsterNameRaw = prop.get("Monsters", String.class);
				 String name[] = monsterNameRaw.split(",");
				 
				 //making sure that the array is clean
				 nameMonster = new ArrayList<String>();
				 
				 for(int i = 0; i < name.length; i++)
				 {
					 nameMonster.add(name[i]);
				 }
			 }
			
			 			 
			
		 }
		 	 	 
		 //getting the dimension of the map
		 mapPixelWidth = mapWidth * tilePixelWidth;
		 mapPixelHeight = mapHeight * tilePixelHeight;
		 
		 //loading the maprenderer
		 maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
		 
		 //updating the label
		 Label labelName = (Label) searchTable("name").getChildren().get(0);
		 
		 if(isSafe && !isBoss)
			 labelName.setText(Name + " (Safe)");
		 else
			 labelName.setText(Name);	
		 
		 if(isBoss)
			 labelName.setColor(Color.CYAN);
		 
		 //recentering the label
		 labelName.setPosition(stage.getWidth() / 2 - labelName.getWidth() / 2, stage.getHeight() - labelName.getHeight());
		 				 
		 //updating the table where the label is
		 Table name = searchTable("name");
		 name.getChildren().items[0] = labelName;
		 
		 stage.getActors().items[getTableIndex("name")] = name;
		 
		 //loading player anim
		 playerMap.createPlayerAnimation();
		 
		 camera.position.y = playerMap.getY();
		 camera.position.x = playerMap.getX();
		 camera.update();
		 
		 //checking if the camera is outside the map		 		 
		
		 	//checking y -
			 if(playerMap.getY() < camera.viewportHeight / 2)
			 {				 
				 camera.position.y =  camera.viewportHeight / 2;
			 }
			 
			 //checking y +
			 if(playerMap.getY() + camera.viewportHeight / 2 > mapPixelHeight)
			 {
				camera.position.y = mapPixelHeight - camera.viewportHeight / 2; 
			 }
			 
			 //checking x -
			 if(playerMap.getX() < camera.viewportWidth / 2)
			 {
				 camera.position.x = camera.viewportWidth / 2;
			 }
			 
			 //checking x +
			 if(playerMap.getX() + camera.viewportWidth / 2 > mapPixelWidth)
			 {
				 camera.position.x = mapPixelWidth - camera.viewportWidth / 2; 
			 }
			
			 camera.update();
 
		 //loading bg music
		 mapSound = Gdx.audio.newMusic(Gdx.files.internal("sound/"+ prop.get("Music", String.class)+".mp3"));
		 mapSound.play();
		 mapSound.setLooping(true);
		 mapSound.setVolume(mainGame.masterVolume/100);
		 
		 
		 /*
		  * loading the styles
		  */
		 
		 TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
			
			skin = new Skin(tex);
			
			//tex.dispose();
		 
		 NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));
			
		NinePatchDrawable drawable = new NinePatchDrawable(patch);
		 
		listStyle.selection = drawable;
		listStyle.font = new BitmapFont();
	    listStyle.fontColorSelected = Color.BLACK;
	    listStyle.fontColorUnselected = Color.WHITE;
	    listStyle.background = drawable;
	    
	    
	    buttonStyle.up = skin.getDrawable("button.up");
		buttonStyle.down = skin.getDrawable("button.down");
		
		buttonStyle.pressedOffsetX = 1;
		buttonStyle.pressedOffsetY = -1;		
		
		buttonStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		//updating the map of the playermap object
		playerMap.setMap(playerMap.getMap(), isSafe);
		
		//saving if is not the boss map
		if(!isBoss)
			save();	
		
		
		//load the dialogue for the map and show them if there is at least one
		loadDialogue();
		showDialogue();
	}

	@Override
	public void dispose() {
		save();
		tiledmap.dispose();
		mapSound.dispose();
		spriteBatch.dispose();	
		stage.dispose();
		playerMap.dispose();				
	}
	
	private void loadSquad() {
		XmlReader xml = new XmlReader();
		
		fileManager.loadFile("save/"+idSave+"Fight.xml");
		
		FileHandle file = fileManager.getDecodedFile();	
		
		playerSquad = new ArrayList<PartyHard_Player_Fight>();
		
		try {
			Element root = xml.parse(file);
			
			Array<Element> players = root.getChildrenByName("player_Fight");
			
			for(int i = 0; i < players.size; i++)
			{
				playerSquad.add(new PartyHard_Player_Fight(i, idSave));
			}
			
			fileManager.saveFile(true, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadFight()
	{
		Random r = new Random();
			//loading the monsters
			ArrayList<PartyHard_Monster> monster = new ArrayList<PartyHard_Monster>();
			
			for(int i = 0; i < nameMonster.size(); i++)
			{
				//used for knowing how much monster of each type is fighting
				int numberOfMonster = r.nextInt(3) + 1;
				for(int p = 0; p < numberOfMonster; p++)
				{
					monster.add(new PartyHard_Monster(nameMonster.get(i)));
				}
			}
			
			/*
			 * TO DO: maybe make the loading dynamic
			 */		
			animFight(monster, prop.get("Battle_Music").toString(), false);
	}
	
	private void loadBossFight()
	{
		animFight(playerMap.Boss.bossSquad, playerMap.Boss.musicPath, true);
	}
	
	private void loadDialogue()
	{
		
		//resetting the dialogue array
		dialogueSpeech.clear();
		
		XmlReader xml = new XmlReader();
		
		try {
			Element root = xml.parse(Gdx.files.internal("data/Dialog.xml"));
			
			Array<Element> arrayOfDialogue = root.getChildrenByName("dialog");
			
			int id;
			String message;
			
			for(int i = 0; i != arrayOfDialogue.size; i++)
			{
				//if the dialogue is for this map
				if(arrayOfDialogue.get(i).get("map").toString().equals(Name))
				{
					id = arrayOfDialogue.get(i).getInt("id");
					message = arrayOfDialogue.get(i).get("message");
					
					//if there is an image, load it
					if(!arrayOfDialogue.get(i).get("imagePath").isEmpty())
					{
						String imagePath = arrayOfDialogue.get(i).get("imagePath");
						
						dialogueSpeech.add(new PartyHard_GameDialogue(id, message, imagePath));
					}
					else
						dialogueSpeech.add(new PartyHard_GameDialogue(id, message));
				}				
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showDialogue()
	{
		
		if(getTableIndex("dialog") != -1)
			stage.getActors().removeIndex(getTableIndex("dialog"));
		
		
		//checking if the player has seen all the dialogues or if the map doesn't contains any dialogue
		if(dialogueSpeech.size() != 0 && playerMap.getIdDialogue() - 1 < dialogueSpeech.get(dialogueSpeech.size() - 1).id)
		{
			//disable player movement input
			playerMap.isTalking = true;
			
			Table tableDialog = new Table();
			tableDialog.setName("dialog");
			
			//setting the table 1/4 of the screen
			tableDialog.setWidth(stage.getWidth());
			tableDialog.setHeight(stage.getHeight() / 4);	
			
			TextButton dialogueHolder = new TextButton(dialogueSpeech.get(getPosIdDialogue(playerMap.getIdDialogue())).message, buttonStyle);
			dialogueHolder.setSize(tableDialog.getWidth(), tableDialog.getHeight());
			
			//setting the textbutton's label settings
			dialogueHolder.getLabelCell().width(dialogueHolder.getWidth());
			dialogueHolder.getLabelCell().pad(10f);
			dialogueHolder.getLabel().setWrap(true);
			dialogueHolder.invalidate();
			
			//checking if the dialogue need an image
			if(dialogueSpeech.get(playerMap.getIdDialogue()).pathImage != null)
			{
				//loading the image and set it to the right of the dialogue
				Image imageDialogue = new Image(new Texture(dialogueSpeech.get(playerMap.getIdDialogue()).pathImage));
				imageDialogue.setPosition(0, dialogueHolder.getHeight());
				
				tableDialog.addActor(imageDialogue);
			}
			
			//when the player click on the dialogue the next dialogue shows up or not
			dialogueHolder.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y) {
					playerMap.isTalking = false;
					playerMap.dialogueRed();
					showDialogue();
				}
			});
			
			tableDialog.addActor(dialogueHolder);
			
			stage.addActor(tableDialog);			
		}
		
	}
	
	/**
	 * 
	 * @param id the id of the dialogue you are looking for
	 * @return -1 if not found or the index of the dialogue in the arraylist
	 */
	private int getPosIdDialogue(int id)
	{
		for(int i = 0; i < dialogueSpeech.size(); i++)
		{
			if(id == dialogueSpeech.get(i).id)
				return i;
		}
		
		return -1;
	}
	
	private void animFight(ArrayList<PartyHard_Monster> monsters, String musicPath, boolean isBoss) 
	{	
		//making sure that the player will not move during the animation
		playerMap.isFighting = true;
		
		//loading the image for the animation
		Image animationFight = new Image(new Texture(Gdx.files.internal("animation/transe.png")));
		animationFight.setWidth(stage.getWidth());
		animationFight.setHeight(stage.getHeight());
		animationFight.setOrigin(animationFight.getWidth() / 2, animationFight.getHeight()/ 2);
		
		//setting the image completely transparent
		animationFight.setColor(animationFight.getColor().g, animationFight.getColor().g, animationFight.getColor().b, 0);
		
		Tween.set(animationFight, ImageAccessor.ALPHA).target(0).start(tweenManager);
		
		Tween.to(animationFight, ImageAccessor.ALPHA, animationFightTime).target(1).start(tweenManager).setCallback(new CallBackFight(playerSquad, monsters, this, mainGame, musicPath, mapSound, isBoss));
		
		stage.addActor(animationFight);
	}


	private void loadShop(final int id)
	{	
		//loading the background for the table		 		 		
		NinePatchDrawable drawable = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/Shop.9.png")))));		
		
		//for disabling movements
		playerMap.isShopping = true;
		playerMap.stopMovement(false);
		
		Table tableShop = new Table();
		tableShop.setName("tableShop");
		
		//setting bg
		tableShop.setBackground(drawable);
		
		Table tableMoney = new Table();
		tableMoney.setName("tableMoney");			
		
		//fill up the entire screen
		tableShop.setWidth(stage.getWidth() / 2);
		tableShop.setHeight(stage.getHeight() / 2);
		tableShop.setPosition(0, stage.getHeight() / 2);
		
		Label description = new Label(playerMap.getShopDescription(id), labelStyle);
		description.setHeight(100);
		description.setPosition(tableShop.getWidth() / 2 - description.getWidth() / 2, tableShop.getHeight() - description.getHeight());		

		Label moneyAmount = new Label("Money: "+playerMap.getMoney(),labelStyle);
		moneyAmount.setPosition(description.getX() + description.getWidth() / 2 - moneyAmount.getWidth() / 2 , description.getY() - moneyAmount.getHeight() + stage.getHeight() / 2);
		
		Label Buy = new Label("Buy", labelStyle);
		Buy.setPosition(tableShop.getWidth() / 3 - Buy.getWidth() - 100, tableShop.getHeight() - 100);
		
		Buy.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//cleaning the shop from old menus
				toggleSubShopMenu();
				
					Table tableBuy = new Table();
					tableBuy.setName("tableBuy");					
					tableBuy.setSize(stage.getWidth() - 40, stage.getHeight() - 120);
					tableBuy.setPosition(20, stage.getHeight() / 2);
					
					List<Label> listBuy = new List<Label>(listStyle);
					listBuy.setSize(tableBuy.getWidth() / 6 , tableBuy.getHeight() / 4);
					
					 Shop = playerMap.getShop(id);
					//populating the list with the item
					for(int i = 0;i < Shop.getShopSize(); i++)
					{
						Label item = new Label(""+i, labelStyle);
						item.setName(Shop.getObjectName(i)+"  Price:" + Shop.getObjectPrice(i));
						
						listBuy.getItems().add(item);
						
					}
					
					
					
					listBuy.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							
								if(getTableIndex("tableBuyChoice") != -1)
									stage.getActors().removeIndex(getTableIndex("tableBuyChoice"));
							
								Table tableBuyChoice = new Table();
								tableBuyChoice.setName("tableBuyChoice");
								
								tableBuyChoice.setSize(stage.getWidth(), stage.getHeight());
								
								//getting the id of the item selected for showing up some info, and display the list choice																	
								List<Label> listItem = (List<Label>) event.getTarget();
								itemShopSelected = listItem.getSelectedIndex();
								
								
								Label itemDescription = new Label(""+Shop.getObjectDescription(itemShopSelected)+" +"+Shop.getObjectAmount(itemShopSelected), labelStyle);
								itemDescription.setFontScale(.8f);
								
								switch(Shop.getObjectType(itemShopSelected))
								{
									case ObjectDatabase.WEAPON:
										itemDescription.setText(itemDescription.getText() + "Atk");
										break;
										
									case ObjectDatabase.ARMOR:
										itemDescription.setText(itemDescription.getText() + "Def");
										break;
								}
								
								itemDescription.setPosition(listItem.getX() + listItem.getWidth(), listItem.getY() + stage.getHeight() / 2);
								itemDescription.setWrap(true);
								
								itemDescription.setWidth(100);
								itemDescription.setHeight(listItem.getHeight());
								
								Label Buy = new Label("Buy", labelStyle);
								Buy.setName(""+Integer.parseInt(listItem.getSelected().getText().toString()));
								Buy.setPosition(itemDescription.getX() + itemDescription.getWidth(), itemDescription.getY());
								
								Buy.addListener(new ClickListener()
								{
									@Override
									public void clicked(InputEvent event, float x, float y) 
									{
									   //player has been selected and can pay
										if(playerMap.canPay(Shop.getObjectPrice(itemShopSelected)) && playerInventory != -1)
										{										
											if(playerSquad.get(playerInventory).getBagSpace() != playerSquad.get(playerInventory).bag.size())
											{
												playerMap.pay(Shop.getObjectPrice(itemShopSelected));
												playerSquad.get(playerInventory).bag.add(Shop.getObject(itemShopSelected));
												
												refreshMoneyShop();	
												playerInventory = -1;											
											}										
										}
										
									}
								});
								
								
								List<Label> listPlayer = new List<Label>(listStyle);
																
								for(int i = 0; i < playerSquad.size(); i++)
								{
									Label player = new Label(""+i, labelStyle);
									player.setName(playerSquad.get(i).Name);
									
									listPlayer.getItems().add(player);
								}
								
								//getting the player's inventory selected for the bought goods
								listPlayer.addListener(new ClickListener()
								{
									@Override
									public void clicked(InputEvent event, float x, float y) 
									{
										playerInventory = ((List<Label>)event.getTarget()).getSelectedIndex();
									}
								});
								
								listPlayer.setSize(100, 100);
								
								listPlayer.setPosition(Buy.getX() + Buy.getWidth(), Buy.getY());
								
								Label lblPlayer = new Label("Select a player", labelStyle);
								lblPlayer.setPosition(listPlayer.getX(), listPlayer.getY() + listPlayer.getHeight());
								
								
								tableBuyChoice.addActor(lblPlayer);
								tableBuyChoice.addActor(itemDescription);
								tableBuyChoice.addActor(Buy);
								tableBuyChoice.addActor(listPlayer);
								
								stage.addActor(tableBuyChoice);							
							
						}
					});
					
					ScrollPaneStyle style = new ScrollPaneStyle();
					ScrollPane scroll = new ScrollPane(listBuy, style);
					scroll.setSize(listBuy.getWidth(), listBuy.getHeight());
					
					tableBuy.addActor(scroll);				
					
					stage.addActor(tableBuy);
												
			}
		});
		
		
		Label Sell = new Label("Sell", labelStyle);
		Sell.setPosition(tableShop.getWidth() / 1.5f - Sell.getWidth() - 100, tableShop.getHeight() - 100);
		
		Sell.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				toggleSubShopMenu();
				
				toggleSellInventory();
				
			}
		});
		
		
		Label Quit = new Label("Quit", labelStyle);	
		Quit.setPosition(tableShop.getWidth() - (Quit.getWidth() + 20)  - 100, tableShop.getHeight() - 100);		
		
		Quit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//quitting the shop: deleting the shop table and the money table
				stage.getActors().removeIndex(getTableIndex("tableShop"));
				stage.getActors().removeIndex(getTableIndex("tableMoney"));
				
				toggleSubShopMenu();
				
				playerMap.isShopping = false;
			}
		});
		
		
		tableShop.addActor(description);
		tableShop.addActor(Buy);
		tableShop.addActor(Sell);
		tableShop.addActor(Quit);
		
		tableMoney.addActor(moneyAmount);
		
		stage.addActor(tableShop);
		stage.addActor(tableMoney);
		
	}

	
	private void refreshMoneyShop()
	{
		if(getTableIndex("tableMoney") != -1)
		{
			Table tableMoney = (Table) stage.getActors().get(getTableIndex("tableMoney"));
			
			Label moneyAmount = (Label) tableMoney.getChildren().get(0);
			moneyAmount.setText("Money: "+playerMap.getMoney());
			
			tableMoney.getChildren().items[0] = moneyAmount;
			stage.getActors().items[getTableIndex("tableMoney")] = tableMoney;
			
			//removing the list of character if exists
			if(getTableIndex("tableBuyChoice") != -1)
				((Table) stage.getActors().items[getTableIndex("tableBuyChoice")]).getChildren().removeIndex(3);
		}
	}
	
	public void moveRight()
	{	
        		playerMap.stopMovement(false);
        		direction = 2;
        		playerMap.moveRight();
	}
	
	public void moveLeft()
	{
				playerMap.stopMovement(false);
				direction = 1;
				playerMap.moveLeft();
	}
	
	public void moveTop()
	{
			playerMap.stopMovement(false);
    		direction = 3;
    		playerMap.moveTop();
	}
	
	public void moveDown()
	{
			playerMap.stopMovement(false);
    		direction = 0;
    		playerMap.moveDown();
	}
	
	/**
	 * @param name the name of the table (setName)
	 * 
	 * @return The table or null
	 */
	private Table searchTable(String name)
	{
		for(int i = 0; i < stage.getActors().size; i++)
		{
			if(stage.getActors().get(i).getName() == name)
				return (Table) stage.getActors().get(i);
		}
		
		//hopefully will never be 
		return null;
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
	
	//used to toggle the menus
	private void toggleMenu()
	{
		//menu exist
		if(getTableIndex("tableMenu") != -1)
		{
			stage.getActors().removeIndex(getTableIndex("tableMenu"));	
			
			toggleSubMenu();
		}
		//if it doesn't
		else
		{			
			Table tableMenu = new Table();
			tableMenu.setName("tableMenu");
			tableMenu.center();
			
			List<Label> listMenu = new List<Label>(listStyle);
			listMenu.setHeight(100);
			listMenu.setWidth(80);	
			
			Label btn;
			//adding the buttons to the menu
			for(int i= 0; i != 3;i++)
			{
				 btn = new Label("", labelStyle);
				switch(i)
				{
					case 0://Status of the player
						btn.setName("Status");						
					break;
					
					case 1://Inventory of the player
						btn.setName("Inventory");						
					break;
					
					case 2://Exit game
						btn.setName("Quit");																	
					break;
				}
				
				listMenu.getItems().add(btn);
			}	
			
			listMenu.addListener(new ClickListener(){
				 @Override 
		            public void clicked(InputEvent event, float x, float y){
					 //loading the drawable for the tables background
					 
					 
					 switch(((List<TextButton>) event.getTarget()).getSelectedIndex())
					 {
					 	case 0: //status
					 		//if the status has already been called
					 		toggleSubMenu();
					 		
					 		//creating table and setting the pos
					 		Table tableStatus = new Table();
					 		tableStatus.setName("tableStatus");					 				 		
					 		
					 		tableStatus.setBackground(drawable);					 							 							 							 		
					 		
					 		tableStatus.setHeight(stage.getHeight() / 2);
					 		tableStatus.setWidth(stage.getWidth() / 2);					 							 		
					 		
					 		tableStatus.setPosition(stage.getWidth() /2 - tableStatus.getWidth(), stage.getHeight() / 2);
					 		
					 		//creating the cat labels
					 		
					 		Label Name = new Label("Name", labelStyle);
					 		Name.setPosition(10, tableStatus.getHeight() - 40);	
					 		
					 		Label Hp = new Label("Hp", labelStyle);
					 		Hp.setPosition(110, Name.getY());
					 		
					 		Label Atk = new Label("Atk", labelStyle);
					 		Atk.setPosition(210, Name.getY());
					 		
					 		Label Def = new Label("Def", labelStyle);
					 		Def.setPosition(310, Name.getY());
					 		
					 		Label Lvl = new Label("Level", labelStyle);
					 		Lvl.setPosition(410, Name.getY());
					 		
					 		Label Exp = new Label("Exp", labelStyle);
					 		Exp.setPosition(510, Name.getY());
					 							 	
					 		//populating the table with the player info
					 		Label labelPlayer = new Label("", labelStyle);
					 		for(int i = 0; i < playerSquad.size(); i++)
					 		{
					 			for(int p = 0; p < 6; p++)
					 			{
					 				switch(p)
						 			{
						 				case 0: //Name
						 					labelPlayer = new Label(playerSquad.get(i).Name, labelStyle);						 					
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );
						 					labelPlayer.setName(""+i);
						 					//adding the equipment show
						 					labelPlayer.addListener(new InputListener()
						 					{
						 						//the mouse is over the name?
						 						@Override
						 						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
						 						{
						 							 playerSelected = Integer.parseInt(event.getTarget().getName()); 
						 							
						 							//checking if the table exist
						 							if(getTableIndex("tableEquip") != -1)
						 								stage.getActors().removeIndex(getTableIndex("tableEquip"));
						 							
						 							enterEquip = true;						 												 							
						 							
						 							//creating the table
						 							Table tableEquip = new Table();
						 							tableEquip.setName("tableEquip");
						 							tableEquip.setBackground(drawable);
						 							
						 							/*
						 							 * size of the list
						 							 */
						 							
						 							tableEquip.setHeight(stage.getHeight() / 4);
											 		tableEquip.setWidth(stage.getWidth() / 2);	
											 		
											 		
											 		//creating the label of equipment, need to check 2 var, 1 for wep 2 for arm
											 		Label labelWep = new Label("Weapon", labelStyle);
											 		labelWep.setPosition(tableEquip.getWidth() / 4 - labelWep.getWidth(), tableEquip.getHeight() - labelWep.getHeight());
											 		
											 		Label labelArmor = new Label("Armor", labelStyle);
											 		labelArmor.setPosition(tableEquip.getWidth() / 2 - labelArmor.getWidth(), tableEquip.getHeight() - labelArmor.getHeight());
											 		
											 			// if wep is equip
											 			if(playerSquad.get(playerSelected).weaponEquipped != -1)
											 			{
											 				Label Wep = new Label(playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).weaponEquipped)).Name, labelStyle);
											 				Wep.setPosition(labelWep.getX(), labelWep.getY()  - Wep.getHeight());
											 				
											 				Label amountWep = new Label("+"+((PartyHard_Weareable)playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).weaponEquipped))).getAmount()+" Atk", labelStyle);
											 				amountWep.setPosition(Wep.getX(), Wep.getY() - amountWep.getHeight());
											 				
											 				tableEquip.addActor(amountWep);
											 				tableEquip.addActor(Wep);
											 			}
											 			
											 			if(playerSquad.get(playerSelected).armorEquipped != -1)
											 			{
											 				Label Arm = new Label(playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped)).Name, labelStyle);
											 				Arm.setPosition(labelArmor.getX(), labelArmor.getY()  - Arm.getHeight());
											 				
											 				Label amountArm = new Label("+"+((PartyHard_Weareable)playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped))).getAmount()+" Def", labelStyle);
											 				amountArm.setPosition(Arm.getX(), Arm.getY() - amountArm.getHeight());
											 				
											 				tableEquip.addActor(Arm);
											 				tableEquip.addActor(amountArm);
											 			}
											 			
											 		tableEquip.addActor(labelArmor);
											 		tableEquip.addActor(labelWep);	
											 		
											 													 		
											 		tableEquip.setPosition(x, y + stage.getHeight() / 2);							
											 		stage.addActor(tableEquip);
						 							
						 						}
						 						
						 						//exiting the name
						 						@Override
						 						public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
						 						{
						 							enterEquip = false;
						 							//deleting the table
						 							if(getTableIndex("tableEquip") != -1)
						 								stage.getActors().removeIndex(getTableIndex("tableEquip"));
						 						}
						 						
						 						//following the mouse
						 						public boolean mouseMoved(InputEvent event, float x, float y)
						 						{
						 							//update only if the player has the mouse on the name
						 							if(enterEquip)
						 								stage.getActors().items[getTableIndex("tableEquip")].setPosition(x, y + stage.getHeight() / 2);
						 							return false;
						 						}					 												 						
						 					});
						 					
						 					labelPlayer.addListener(new ClickListener()
						 					{
							 					@Override
							 				    public void clicked(InputEvent event, float x, float y) 
							 					{
							 						playerMap.setSprite(playerSquad.get(playerSelected).imagePath);
							 						playerMap.createPlayerAnimation();
							 					}
						 					});				 								 					
						 				break;
						 				
						 				case 1: //Hp
						 					labelPlayer = new Label(""+playerSquad.get(i).getLife(), labelStyle);
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );
						 				break;
						 				
						 				case 2: //Atk
						 					labelPlayer = new Label(""+playerSquad.get(i).getAtk(), labelStyle);
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );
						 				break;
						 				
						 				case 3: //Def
						 					labelPlayer = new Label(""+playerSquad.get(i).getDef(), labelStyle);
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );
						 				break;
						 				
						 				case 4: //Lvl
						 					labelPlayer = new Label(""+playerSquad.get(i).getLevel(), labelStyle);
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );						 					
						 				break;
						 				
						 				case 5: // Exp
						 					labelPlayer = new Label(""+playerSquad.get(i).getExp(), labelStyle);
						 					labelPlayer.setPosition(10 + p * 100, Name.getY() - 100 - (i * 100) );
					 					break;
						 			}
					 				tableStatus.addActor(labelPlayer);
					 			}
					 			
					 		}
					 		
					 		tableStatus.addActor(Name);
					 		tableStatus.addActor(Hp);
					 		tableStatus.addActor(Atk);
					 		tableStatus.addActor(Def);
					 		tableStatus.addActor(Lvl);
					 		tableStatus.addActor(Exp);	
					 		
					 		
					 		stage.addActor(tableStatus);
					 		
					 		break;
					 		
					 	case 1: //inventory					 		
					 		//deleting the old menu			 			
					 			toggleSubMenu();
					 		toggleInventory();
					 			break;
					 		
					 	case 2://Quit
					 		save();
					 		
					 		PartyHard_ScreenSplash screen = new PartyHard_ScreenSplash(mainGame);					 		
					 		mainGame.setScreen(screen);
					 		
					 		dispose();					 							 		
					 		break;
					 }
				
			}});
						
			//setting the pos of the list
			listMenu.setPosition(stage.getWidth() - listMenu.getWidth(), stage.getHeight() - listMenu.getHeight());
			
			Label Money = new Label("Money: "+playerMap.getMoney(), labelStyle);
			Money.setPosition(stage.getWidth()  - Money.getWidth(), listMenu.getY() - Money.getHeight());
				
			tableMenu.addActor(Money);
			tableMenu.addActor(listMenu);
			
			stage.addActor(tableMenu);
		}
	
		
	}
	
	private void toggleSellInventory()
	{
		if(getTableIndex("tableSell") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSell"));
		
		if(getTableIndex("tableSellObject") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSellObject"));
		
		Table tableSell = new Table();
		tableSell.setName("tableSell");
		
		tableSell.setPosition(10, stage.getHeight() / 2);
		tableSell.setSize(stage.getWidth() / 2, stage.getHeight() / 4);
		
		final float dimension = tableSell.getWidth() / playerSquad.size();
		
		//populating the table with player's inventory		
		for(int i = 0; i < playerSquad.size(); i++)
		{
			List<Label> listInventory = new List<Label>(listStyle);
			listInventory.setName(""+i);
			listInventory.setPosition(i * dimension, 10);
			
			//populating the list
			for(int x = 0; x < playerSquad.get(i).bag.size(); x++)
			{
				Label item = new Label(""+x, labelStyle);
				item.setName(playerSquad.get(i).bag.get(x).Name+" Price:" +playerSquad.get(i).bag.get(x).price / 2);
				
				listInventory.getItems().add(item);
			}
			
			//no item added, so adding a warning
			if(listInventory.getItems().size == 0)
			{
				Label lblNoItem = new Label ("", labelStyle);
				lblNoItem.setName("No Item left !");
				
				listInventory.getItems().add(lblNoItem);
			}
			
			listInventory.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(getTableIndex("tableSellObject") != -1)
						stage.getActors().removeIndex(getTableIndex("tableSellObject"));
					
					playerInventory = Integer.parseInt(event.getTarget().getName());
					if(playerSquad.get(playerInventory).bag.size() != 0)
					{
						Table tableSellObject = new Table();
						tableSellObject.setName("tableSellObject");
						
						tableSellObject.setPosition(10, stage.getHeight() / 2);
						tableSellObject.setSize(stage.getWidth() / 2, stage.getHeight() / 4);										
						
						Label Sell = new Label("Sell", labelStyle);
						Sell.setPosition(playerInventory * dimension + event.getTarget().getWidth(), event.getTarget().getY() + Sell.getHeight() / 2);				
						
						Sell.setName(""+((List<Label>)event.getTarget()).getSelectedIndex());
						
						Sell.addListener(new ClickListener(){
							@Override
							public void clicked(InputEvent event, float x, float y) {
								//adding the money, then removing it and setting back to default the var													
								playerMap.addMoney(playerSquad.get(playerInventory).bag.get(Integer.parseInt(event.getTarget().getName())).price / 2);
								
								playerSquad.get(playerInventory).removeObjectFromInventory(Integer.parseInt(event.getTarget().getName()));
								
								playerInventory = -1;
								toggleSellInventory();
								refreshMoneyShop();
							}
						});
						
						tableSellObject.addActor(Sell);
						
						stage.addActor(tableSellObject);
					}
					
				}});
					
					
			
			listInventory.setSize(150, 100);
			
			ScrollPane scroll = new ScrollPane(listInventory, new ScrollPaneStyle());
			scroll.setPosition(listInventory.getX(), listInventory.getY());
			
			scroll.setHeight(listInventory.getHeight());
			
			Label lblName = new Label(playerSquad.get(i).Name, labelStyle);
			lblName.setPosition(scroll.getX() + scroll.getWidth() / 2 - lblName.getWidth() / 2, scroll.getY() + scroll.getHeight());
			
			tableSell.addActor(lblName);
			tableSell.addActor(scroll);
			
		}
		
		Label lblDescription = new Label("Select an item to sell", labelStyle);
		lblDescription.setPosition(tableSell.getWidth() / 2 - lblDescription.getWidth() / 2, tableSell.getHeight() - lblDescription.getHeight());
		
		tableSell.addActor(lblDescription);
		
		stage.addActor(tableSell);
	}
	
	private void toggleInventory()
	{
		if(getTableIndex("tableInventory") != -1)
			stage.getActors().removeIndex(getTableIndex("tableInventory"));
		if(getTableIndex("tableSpace") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSpace"));
		
		//creating the table that will holds the inventory
 		Table inventory = new Table();
 		inventory.setName("tableInventory");
 		inventory.setBackground(drawable);
 				
 		inventory.setSize(stage.getWidth() / 2, stage.getHeight() / 2);
 		inventory.setPosition(stage.getWidth()/2 - inventory.getWidth(), stage.getHeight() / 2);
 		
 		//creating the title label (centered)
 		Label lblinventory = new Label("Inventory", labelStyle);
 		lblinventory.setPosition(inventory.getWidth() / 2 - lblinventory.getWidth() / 2  , inventory.getHeight() - 40);
 		
 		//showing each player -> click then show up the inventory of the player choose
 		float dimension = inventory.getWidth() / (playerSquad.size() + 1);					 							 		
 		
 		Table tableInventorySpace = new Table();
 		tableInventorySpace.setName("tableSpace");
 		
 		tableInventorySpace.setSize(stage.getWidth() / 2, stage.getHeight() / 2);
 		tableInventorySpace.setPosition(stage.getWidth()/2 - inventory.getWidth(), stage.getHeight() / 2);
 		
 		for(int i = 0; i < playerSquad.size(); i++)
 		{
 			//creating label for each player
 			Label labelNamePlayer = new Label(playerSquad.get(i).Name, labelStyle);
 			labelNamePlayer.setName(""+i);
 			
 			labelNamePlayer.setPosition(10 + ((dimension + 100) * i )  , inventory.getHeight() / 2);
 			
 			//displaying the bagSpace
 			Label bagSpace = new Label("Bag space: "+playerSquad.get(i).bag.size()+"/"+playerSquad.get(i).getBagSpace(), labelStyle);
 			bagSpace.setPosition(labelNamePlayer.getX(), labelNamePlayer.getY() + bagSpace.getHeight());
 			
 			//creating the inventory
 			List<TextButton> listItem = new List<TextButton>(listStyle);
 			listItem.setHeight(100);
 			listItem.setPosition(labelNamePlayer.getX(), labelNamePlayer.getY() - 40);
 			listItem.setName(""+i);//used for knowing of who the inventory is
 				
 			
 			for(int p = 0; p < playerSquad.get(i).bag.size(); p++)
 			{						 				
 				TextButton Item = new TextButton(""+playerSquad.get(i).bag.get(p).getItemId(), buttonStyle);		 				
 				
 				Item.setName(playerSquad.get(i).bag.get(p).Name);
 				Item.pad(5f);					 									 									 				
 				
 				listItem.getItems().add(Item);
 			}
 			
 			//adding at least one textbutton for telling that the invenotry is empty
 			if(listItem.getItems().size == 0)
 			{
 				TextButton Item = new TextButton("", buttonStyle);		 				
 				
 				Item.setName("No item left!");
 				Item.pad(5f);					 									 									 				
 				
 				listItem.getItems().add(Item);
 			}

 			//managing the click on items
 			
 			listItem.addListener(new ClickListener(){
 				
				@Override 
 				public void clicked(InputEvent event, float x, float y){					 					
					if( ((List<TextButton>) event.getTarget()).getSelected().getName() != "No item left!")
						{
						//the index of the player
	 					playerSelected = Integer.parseInt(event.getTarget().getName());	
	 						
	 					//index of the selected item
	 					itemIndex = playerSquad.get(playerSelected).getItemIndex(Integer.parseInt(((List<TextButton>) event.getTarget()).getSelected().getText().toString()));					 						 				
	 					
	 					//if the item is found in the inventory
	 					if(itemIndex != -1)
	 					{					 						
	 						//deleting the old table if exists
		 					if(getTableIndex("tableChoice") != -1)
		 						stage.getActors().removeIndex(getTableIndex("tableChoice"));
		 					
		 					Table tableChoice = new Table();
		 					tableChoice.setName("tableChoice");
		 					tableChoice.setSize(stage.getWidth() / 2, stage.getHeight() / 2);
					 		tableChoice.setPosition(stage.getWidth()/2 - tableChoice.getWidth(), stage.getHeight() / 2);
		 					
					 		/*
	 						 * 0 - drop
	 						 * 1 = equip
	 						 * 2 - use 						 
	 						 */
					 		
	 						List<Label> listChoice = new List<Label>(listStyle);	
	 						
	 						listChoice.setWidth(50);
	 						listChoice.setHeight(50);
	 						
	 						//getting the pos of the itemList
	 						Table tableItem = searchTable("tableInventory");					 							 									
	 						listChoice.setPosition(tableItem.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getX() + 100, tableItem.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getY());
	 						
	 						switch(playerSquad.get(playerSelected).bag.get(itemIndex).type)
			 				{
			 					case 0: //weapon
			 						//itemEquipped
			 						//unequip			 									 						 						
			 						if(playerSquad.get(playerSelected).weaponEquipped != -1 &&  playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).weaponEquipped) == itemIndex )
			 						{
			 							Label unequip = new Label("3", labelStyle);
			 							unequip.setName("Unequip");
			 							
			 							listChoice.getItems().add(unequip);
			 						}
			 						else//equip
			 						{
			 							Label equipWep = new Label("1",labelStyle);
				 						equipWep.setName("Equip");						 						
				 						
				 						listChoice.getItems().add(equipWep);
			 						}		 						
			 						break;
			 					case 1: //armor
			 						if(playerSquad.get(playerSelected).armorEquipped != -1 &&  playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped) == itemIndex)
			 						{
			 							Label unequip = new Label("3", labelStyle);
			 							unequip.setName("Unequip");
			 							
			 							listChoice.getItems().add(unequip);
			 						}
			 						else
			 						{
			 							Label equipArm = new Label("1",labelStyle);
				 						equipArm.setName("Equip");						 						
				 						
				 						listChoice.getItems().add(equipArm);
			 						}			 										 					
			 						break;
			 					case 2: //potion
			 					Label use = new Label("2", labelStyle);
			 					use.setName("Use");
			 						
			 					listChoice.getItems().add(use);
			 					
			 					List<Label> listChara = new List<Label>(listStyle);
		 						
		 						for(int i = 0; i < playerSquad.size(); i++)
		 						{
		 							Label lblName = new Label("",labelStyle);
		 							lblName.setName(playerSquad.get(i).Name);
		 							
		 							listChara.getItems().add(lblName);
		 						}
		 						
		 						listChara.addListener(new ClickListener(){
		 							@Override
		 							public void clicked(InputEvent event, float x, float y) {
		 								playerSelectedForObject = ((List<Label>) event.getTarget()).getSelectedIndex();	 								
		 							}
		 						});
		 						
		 						listChara.setWidth(100);
		 						listChara.setHeight(100);
		 						listChara.setPosition(listChoice.getX() + listChoice.getWidth(), listChoice.getY());
		 						
		 						LabelStyle style = new LabelStyle();
		 						style.font = new BitmapFont();
		 						
		 						Label lblChara = new Label("Which Player?", style);
		 						lblChara.setPosition(listChara.getX() , listChara.getY() + listChara.getHeight());
		 								 						
		 						
		 						tableChoice.addActor(lblChara);
		 						tableChoice.addActor(listChara);
			 						break;
			 				}
	 						
	 						//adding default behavior 
	 						Label labelDrop = new Label("0", labelStyle);
	 						labelDrop.setName("Drop");
	 						
	 						listChoice.getItems().add(labelDrop);					 						
	 						
	 						listChoice.addListener(new ClickListener(){					 						
	 						public void clicked(InputEvent event, float x, float y){					 							
	 							
	 								int choice = Integer.parseInt(((List<Label>) event.getTarget()).getSelected().getText().toString());					 										 								 							
	 								
	 								//checking for the choice, deleting the tableChoice only if the action has been done(the conditions have been checked)
	 								
		 							switch(choice)
		 							{
		 								case 0://drop
		 									//Removing from inventory
		 									if(playerSelected != -1 && itemIndex != -1 )
		 									{
		 										playerSquad.get(playerSelected).removeObjectFromInventory(itemIndex);	
		 										
		 										stage.getActors().removeIndex(getTableIndex("tableInventory"));
		 										toggleInventory();
		 										
		 										refreshBagSpace(playerSelected);
		 										
		 										itemIndex = -1;
		 										playerSelected = -1;
		 										
		 										stage.getActors().removeIndex(getTableIndex("tableChoice"));		 										
		 									}		 																					 																											 							
		 									
		 									break;
		 								case 1: //equip, then erase the choices
		 									if(playerSelected != -1 && itemIndex != -1)
		 									{
		 										playerSquad.get(playerSelected).setEquipSlot(itemIndex);	
		 										toggleInventory();
		 										
		 										itemIndex = -1;
		 										
		 										stage.getActors().removeIndex(getTableIndex("tableChoice"));
		 									}
		 												 																 							
		 									break;
		 								case 2: //use	
		 									if(playerSelected != -1 && itemIndex != -1 && playerSelectedForObject != -1)
		 									{		 										
		 											playerSquad.get(playerSelected).setLife(((PartyHard_Potion)playerSquad.get(playerSelected).bag.get(itemIndex)).getAmount());
			 										playerSquad.get(playerSelected).bag.remove(itemIndex);
			 									
			 										stage.getActors().removeIndex(getTableIndex("tableInventory"));
			 										toggleInventory();	
			 										refreshBagSpace(playerSelected);
			 										
			 										itemIndex = -1;
			 										playerSelectedForObject = -1;
			 										
			 										stage.getActors().removeIndex(getTableIndex("tableChoice"));
		 									}		 									
		 									break;
		 									
		 								case 3: //unequip
		 									if(playerSelected != -1 && itemIndex != -1)
		 									{
		 										playerSquad.get(playerSelected).unequipItem(itemIndex);		
		 										
		 										itemIndex = -1;
		 										
		 										stage.getActors().removeIndex(getTableIndex("tableChoice"));
		 									}
		 													 															 									
		 									break;
		 							}
		 							//erasing the choices table
		 							
	 						}}); 						
	 						tableChoice.addActor(listChoice);
	 						
	 						stage.addActor(tableChoice);
	 						
	 					}
	 										 					
	 				}
	 			}});			 												 							 				
 			
 			//creating the scroll
 			ScrollPaneStyle style = new ScrollPaneStyle();					 			
 			ScrollPane scroll = new ScrollPane(listItem, style);	
 			scroll.setForceScroll(false, true);
 			
 			scroll.setPosition(listItem.getX(), listItem.getY() - listItem.getHeight() - 10);
 			scroll.setWidth(listItem.getHeight());
 			
 			inventory.addActor(labelNamePlayer);			
			inventory.addActor(scroll);	
			
			tableInventorySpace.addActor(bagSpace);
 		}
 		
 		inventory.addActor(lblinventory);	
 		stage.addActor(inventory);
 		stage.addActor(tableInventorySpace);
	}

	
	private void toggleSubMenu()
	{
		//deleting the other sub menu if created
		
		if(getTableIndex("tableStatus") != -1)		
			stage.getActors().removeIndex(getTableIndex("tableStatus"));		
		
		if(getTableIndex("tableInventory") != -1)
			stage.getActors().removeIndex(getTableIndex("tableInventory"));
		
		if(getTableIndex("tableSpace") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSpace"));
		
		if(getTableIndex("tableChoice") != -1)
			stage.getActors().removeIndex(getTableIndex("tableChoice"));
			
		
	}
	
	private void toggleSubShopMenu()
	{
		//resetting the var for the shop
		itemIndex = -1;
		playerSelected = -1;
		
		if(getTableIndex("tableBuy") != -1)
		{
			stage.getActors().removeIndex(getTableIndex("tableBuy"));
			itemShopSelected = -1;
		}
			
		if(getTableIndex("tableBuyChoice") != -1)
			stage.getActors().removeIndex(getTableIndex("tableBuyChoice"));
		
		if(getTableIndex("tableSell") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSell"));
		
		if(getTableIndex("tableSellObject") != -1)
			stage.getActors().removeIndex(getTableIndex("tableSellObject"));
	
	}
	
	private void refreshBagSpace(int id)
	{
		/*
		 * getting the table updating the label and putting back the table into the stage
		 */
		
		 Table tableSpace = (Table) stage.getActors().get(getTableIndex("tableSpace"));
		 
		 Label lblSpace = (Label) tableSpace.getChildren().items[id];
		 lblSpace.setText("Bag space: "+playerSquad.get(id).bag.size()+"/"+playerSquad.get(id).getBagSpace());
		 
		 tableSpace.getChildren().items[id] = lblSpace;
		 stage.getActors().items[getTableIndex("tableSpace")] =tableSpace;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode)
		{
			case Input.Keys.ESCAPE:
				toggleMenu();
			return true;
			
			case Input.Keys.ENTER:
				//if the player has the boss in front of him
				if(playerMap.isCellBoss())
					loadBossFight();
				else//or a shop
					if(!playerMap.isShopping)
					{
			        	if(playerMap.getCellShop(direction) != -1)
			        	{		        		
			        		loadShop(playerMap.getCellShop(direction));
			        	}
					}
					else
						System.out.println("yo");
			return true;
			
			case Input.Keys.N:
				SUPERAWESOMECHEAT();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void save() {

		//first save the fighters
		playerSquad.get(0).prepareForSave();
		for(int i = 0; i < playerSquad.size(); i++)
		{
			playerSquad.get(i).save(i);
		}
				
		playerSquad.get(0).finishFile();
		//the the player map		   			 
		playerMap.save();				
	}
	
	private void SUPERAWESOMECHEAT()
	{
		//creating the label for letting know the super mean user that the cheat has been activated
		Label lblCheat = new Label("Cheat activated! You cheater!", labelStyle);
		
		//centering the label
		lblCheat.setPosition(stage.getWidth() / 2 - lblCheat.getWidth() / 2, stage.getHeight() / 2 - 100);
		
		//animating
		Tween.set(lblCheat, LabelAccessor.Y).target(lblCheat.getY()).start(tweenManager);
		Tween.set(lblCheat, LabelAccessor.ALPHA).target(1).start(tweenManager);
		
		Tween.to(lblCheat, LabelAccessor.Y, 1f).target(lblCheat.getY() + 100).start(tweenManager);
		Tween.to(lblCheat, LabelAccessor.ALPHA, 1f).delay(1f).target(0).start(tweenManager);
		
		stage.addActor(lblCheat);
		
		//adding atk and life to the squad
		for(int i = 0; i < playerSquad.size(); i++)
		{
			playerSquad.get(i).addAtk(100);
			playerSquad.get(i).addLife(100);
		}
	}
}