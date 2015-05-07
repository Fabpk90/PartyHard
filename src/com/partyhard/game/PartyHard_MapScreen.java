package com.partyhard.game;

import java.util.ArrayList;
import java.util.Random;

import utils.FileManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_MapScreen implements Screen{

	public Game mainGame;
	private SpriteBatch spriteBatch;  
	
	private Stage stage;	
	
	public String Name;
	
	private PartyHard_Fight fightScreen;
	
	//could be transfered into the player will see
	private int direction = 0;
	/*
	 * 0 back
	 * 1 left
	 * 2 right
	 * 3 toward
	 */
	
	private float animationTime = 0f;
	
	private PartyHard_Player_Map playerMap;
	
	private Sound mapSound;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	private MapProperties prop;
	
	private float mapPixelWidth;
	private float mapPixelHeight;
	
	private boolean isSafe;
	
	private boolean blockedx = false;
	
	private float scale = 2f;
	
	//for randomly begin a fight
	private float fightTime = 0;	
	//probability of fight
	private int proba = 0;
	//array of monster name 
	private ArrayList<String> nameMonster = new ArrayList<String>();
	
	
	public PartyHard_MapScreen(Game gameToKeep, String mapName, PartyHard_Player_Map playerMap)
	{	
		mainGame = gameToKeep;
		this.playerMap = playerMap;
		tiledmap = new TmxMapLoader().load(mapName+".tmx");
		this.playerMap.setMap(mapName);
		this.playerMap.setCollisionLayer(tiledmap);
		
	}			
	
	@Override
	public void render(float delta) {
			
		animationTime += delta;
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        maprenderer.setView(camera);
        maprenderer.render();
        
        stage.act(delta);
       
        
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();      
        
       
        playerMap.update(delta);
        
     
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
            	if(fightTime >=2)
            	{
            		Random r = new Random();
            		
            		//if pseudo-randomly the fight is happening
            		if(r.nextInt(101) < proba)
            		{
            			playerMap.stopMovement();
            		        		
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
	   		   			camera.update();
	   		   		}		   				   		   	
        	}
        	
        	
        	if(playerMap.isMovingOnY())
        	{
	        		//same as above but for the height
	   		   	 if(playerMap.getY() - (camera.viewportHeight / 2 )  > 0 && playerMap.getY() + (camera.viewportHeight / 2) < mapPixelHeight)
	   		   	 {
	   		   		 //if the player cannot go on the x axe then move only on the y		   		
	   		   		 camera.position.set(camera.position.x, playerMap.getY(), 0);	   		   		
	   		   	     camera.update(); 		         
	   		   	 }
        	}
		   	       
        /*
	     * Movement
	     */             
        	 //for desktop
        if(Gdx.app.getType() == Gdx.app.getType().Desktop)
        {
        	scale = 4f;
        	  if(!playerMap.isMoving())
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
		      
		        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		        {
		        	this.dispose();
		        	mainGame.dispose();
		        	Gdx.app.exit();	
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
	        	
	    		playerMap.setMap(Name);
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
		
	camera = new OrthographicCamera();
	camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
	
	//camera.viewportHeight = camera.viewportHeight / 1.2f;
	//camera.viewportWidth = camera.viewportWidth / 1.2f;
	
	camera.position.set(playerMap.getX(), playerMap.getY(), 0);
		
	camera.update();
	
	
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
		 
		 LabelStyle style1 = new LabelStyle();		 
		 style1.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		 
		 //used for printing the map name
		 Label labelname = new Label(Name, style1);	
		
		 // setting position of the label
		 mapNameTable.center().top();
		 mapNameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 mapNameTable.setName("name");
			 
		 labelname.setPosition(mapNameTable.getWidth(), mapNameTable.getHeight()); 
		
		 mapNameTable.add(labelname);
		 stage.addActor(mapNameTable);
	
		
		Gdx.input.setInputProcessor(stage);
		
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
		String safe = prop.get("Safe", String.class);
		
		if(safe.equals("true"))
			isSafe = true;
		else
			isSafe = false;
		 
		 //getting ifght info only if the map is not safe
		 if(!isSafe)
		 {
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
		 	 	 
		 //getting the dimension of the map
		 mapPixelWidth = mapWidth * tilePixelWidth;
		 mapPixelHeight = mapHeight * tilePixelHeight;
		 
		 //loading the maprenderer
		 maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
		 
		 //updating the label
		 Label labelName = (Label) searchTable("name").getChildren().get(0);	
		 labelName.scaleBy(5);
		 
		 if(isSafe)
			 labelName.setText(Name + " (Safe)");
		 else
			 labelName.setText(Name);	
		 
		 Table name = searchTable("name");
		 name.getChildren().items[0] = labelName;
		 
		 stage.getActors().items[getTableIndex("name")] = name;
		 
		 //loading player anim
		 playerMap.createPlayerAnimation();
		 
		 //updating camera	 	 
		 camera.position.x = camera.viewportWidth / 2;
		 camera.position.y = camera.viewportHeight / 2;
		 
		 camera.update();	
		 
		 //loading bg music
		 mapSound = Gdx.audio.newSound(Gdx.files.internal("sound/"+ prop.get("Music", String.class)+".mp3"));		
		 mapSound.loop();
		 
		//FileManager fileManager = new FileManager("monster.xml");
		// fileManager.saveFile(false, Gdx.files.internal("monster.xml"));
		
	}

	@Override
	public void dispose() {
		tiledmap.dispose();
		mapSound.dispose();
		spriteBatch.dispose();	
		stage.dispose();
		playerMap.dispose();
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
			
			//preparing fighters
			ArrayList<PartyHard_Player_Fight> playerSquad = new ArrayList<PartyHard_Player_Fight>();
			
			playerSquad.add(new PartyHard_Player_Fight(0));
			playerSquad.add(new PartyHard_Player_Fight(1));
			 
		    fightScreen = new PartyHard_Fight(playerSquad, monster, this, (PartyHard_GameClass) mainGame);
			mainGame.setScreen(fightScreen);
			
			mapSound.dispose();
			
	}

	public void moveRight()
	{	
        		playerMap.stopMovement();
        		direction = 2;
        		playerMap.moveRight();
	}
	
	public void moveLeft()
	{
				playerMap.stopMovement();
				direction = 1;
				playerMap.moveLeft();
	}
	
	public void moveTop()
	{
			playerMap.stopMovement();
    		direction = 3;
    		playerMap.moveTop();
	}
	
	public void moveDown()
	{
			playerMap.stopMovement();
    		direction = 0;
    		playerMap.moveDown();
	}
	
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
	
	/*
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