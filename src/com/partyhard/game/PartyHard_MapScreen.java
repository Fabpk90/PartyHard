package com.partyhard.game;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
import com.badlogic.gdx.utils.XmlWriter;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.actor.PartyHard_Player_Map;
import com.partyhard.object.PartyHard_Armor;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;
import com.partyhard.object.PartyHard_Weareable;

public class PartyHard_MapScreen implements Screen, InputProcessor{

	public Game mainGame;
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
	
	private PartyHard_Player_Map playerMap;
	
	private Sound mapSound;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	private MapProperties prop;
	
	private float mapPixelWidth;
	private float mapPixelHeight;
	
	private boolean isSafe;
	private float timeToFight = 0;
	
	private boolean blockedx = false;
	
	private float scale = 2f;
	
	private ListStyle listStyle = new ListStyle();
	private TextButtonStyle buttonStyle = new TextButtonStyle();
	private LabelStyle labelStyle = new LabelStyle();
	private ListStyle listInventoryStyle = new ListStyle();
	
	//for randomly begin a fight
	private float fightTime = 0;	
	//probability of fight
	private int proba = 0;
	//array of monster name 
	private ArrayList<String> nameMonster = new ArrayList<String>();
	private Skin skin;
	
	/*
	 * Invotry var
	 */
	
	//of which inventory we are?
	private int playerSelected;
	private int itemIndex;
	
	private boolean enterEquip = false;
	private boolean isShopping;
	
	public PartyHard_MapScreen(Game gameToKeep, String mapName, PartyHard_Player_Map playerMap)
	{	
		mainGame = gameToKeep;
		this.playerMap = playerMap;
		tiledmap = new TmxMapLoader().load(mapName+".tmx");
		this.playerMap.setMap(mapName);
		this.playerMap.setCollisionLayer(tiledmap);	
		
		//loading the playerSquad
		playerSquad.add(new PartyHard_Player_Fight(0));
		playerSquad.add(new PartyHard_Player_Fight(1));
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
        
       if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) )
        playerMap.update(delta + 1);
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
		        
		        if(Gdx.input.isKeyPressed(Input.Keys.ENTER))
		        {
		        	if(!isShopping)
		        	if(playerMap.getCellShop() != -1)
		        	{
		        		loadShop(playerMap.getCellShop());
		        	}
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
		 
		 		 
		 labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		 
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
		 	 	 
		 //getting the dimension of the map
		 mapPixelWidth = mapWidth * tilePixelWidth;
		 mapPixelHeight = mapHeight * tilePixelHeight;
		 
		 //loading the maprenderer
		 maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
		 
		 //updating the label
		 Label labelName = (Label) searchTable("name").getChildren().get(0);	
		// labelName.scaleBy(5);
		 
		 if(isSafe)
			 labelName.setText(Name + " (Safe)");
		 else
			 labelName.setText(Name);	
		 		
		 
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
		 mapSound = Gdx.audio.newSound(Gdx.files.internal("sound/"+ prop.get("Music", String.class)+".mp3"));		
		 mapSound.loop();
		 
		//FileManager fileManager = new FileManager("monster.xml");
		// fileManager.saveFile(false, Gdx.files.internal("monster.xml"));
		 
		 /*
		  * loading the styles
		  */
		 
		 TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
			
			skin = new Skin(tex);
			
			tex.dispose();
		 
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
		BitmapFont font = new BitmapFont();
		
		buttonStyle.font = font;
		buttonStyle.font.getRegion().getTexture().setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		
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
			 
		    fightScreen = new PartyHard_Fight(playerSquad, monster, this, (PartyHard_GameClass) mainGame, prop.get("Battle_Music", String.class));
			mainGame.setScreen(fightScreen);
			
			mapSound.dispose();
			
	}

	private void loadShop(int id)
	{
		
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
					 NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));						
				 	 final NinePatchDrawable drawable = new NinePatchDrawable(patch);
					 
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
						 						//the is over the name?
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
											 				Label Wep = new Label(playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).weaponEquipped).Name, labelStyle);
											 				Wep.setPosition(labelWep.getX(), labelWep.getY()  - Wep.getHeight());
											 				
											 				Label amountWep = new Label(""+((PartyHard_Weareable) playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).weaponEquipped)).getAmount()+" Atk", labelStyle);
											 				amountWep.setPosition(Wep.getX(), Wep.getY() - amountWep.getHeight());
											 				
											 				tableEquip.addActor(amountWep);
											 				tableEquip.addActor(Wep);
											 			}
											 			
											 			if(playerSquad.get(playerSelected).armorEquipped != -1)
											 			{
											 				Label Arm = new Label(playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped)).Name, labelStyle);
											 				Arm.setPosition(labelArmor.getX(), labelArmor.getY()  - Arm.getHeight());
											 				
											 				Label amountArm = new Label(""+((PartyHard_Weareable) playerSquad.get(playerSelected).bag.get(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped))).getAmount()+" Def", labelStyle);
											 				amountArm.setPosition(Arm.getX(), Arm.getY() - amountArm.getHeight());
											 				
											 				tableEquip.addActor(Arm);
											 				tableEquip.addActor(amountArm);
											 			}
											 			
											 		tableEquip.addActor(labelArmor);
											 		tableEquip.addActor(labelWep);	
											 		
											 													 		
											 		tableEquip.setPosition(x, y + stage.getHeight() / 2);
											 	//	stage.setDebugAll(true);
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
					 		
					 			//creating the table that will holds the inventory
					 		Table inventory = new Table();
					 		inventory.setName("tableInventory");
					 		inventory.setBackground(drawable);
					 		
					 		
					 		inventory.setSize(stage.getWidth() / 2, stage.getHeight() / 2);
					 		inventory.setPosition(stage.getWidth()/2 - inventory.getWidth(), stage.getHeight() / 2);
					 		
					 		//creating the title label (centered)
					 		Label lblinventory = new Label("Inventory", labelStyle);
					 		lblinventory.setPosition(inventory.getWidth() / 2 - (lblinventory.getText().length * 10) / 2 , inventory.getHeight() - 40);
					 							 							 		
					 		//showing each player -> click then show up the inventory of the player choose
					 		float dimension = inventory.getWidth() / (playerSquad.size() + 1);					 							 		
					 		
					 		
					 		for(int i = 0; i < playerSquad.size(); i++)
					 		{
					 			//creating label for each player
					 			Label labelNamePlayer = new Label(playerSquad.get(i).Name, labelStyle);
					 			labelNamePlayer.setName(""+i);
					 			
					 			labelNamePlayer.setPosition(dimension + (dimension * i) , inventory.getHeight() / 2);				 								 			
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
						 						
						 						switch(playerSquad.get(playerSelected).bag.get(itemIndex).type)
								 				{
								 					case 0: //weapon
								 						//itemEquipped
								 						//unequip
								 						if(playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).weaponEquipped) == itemIndex )
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
								 						if( playerSquad.get(playerSelected).getItemIndex(playerSquad.get(playerSelected).armorEquipped) == itemIndex )
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
								 						break;
								 				}
						 						
						 						//adding default behavior 
						 						Label labelDrop = new Label(""+itemIndex, labelStyle);
						 						labelDrop.setName("Drop");
						 						
						 						listChoice.getItems().add(labelDrop);					 						
						 						
						 						listChoice.addListener(new ClickListener(){					 						
						 						public void clicked(InputEvent event, float x, float y){					 							
						 							
						 								int choice = Integer.parseInt(((List<Label>) event.getTarget()).getSelected().getText().toString());					 								
							 							
							 							switch(choice)
							 							{
							 								case 0://drop
							 									//Removing from inventory
							 									playerSquad.get(playerSelected).removeObjectFromInventory(itemIndex);													
							 									
							 									//updating the inventory table
							 									Table Inv = searchTable("tableInventory");
							 									List<TextButton> newInv = (List<TextButton>) ((ScrollPane) Inv.getChildren().get(playerSelected + 1 + (1 * playerSelected))).getWidget();
							 									
							 									//removing item dropped 
							 									newInv.getItems().removeIndex(itemIndex);
							 									
							 									//checking if ther is some item left
							 									if(newInv.getItems().size == 0)
							 									{
							 										TextButton noItem = new TextButton("", buttonStyle);
							 										noItem.setName("No item left!");
							 										newInv.getItems().add(noItem);
							 									}
							 									
							 									//re creating the scroll
							 									ScrollPaneStyle style = new ScrollPaneStyle();					 			
							 						 			ScrollPane scroll = new ScrollPane(newInv, style);	
							 						 			scroll.setForceScroll(false, true);
							 						 			
							 						 			scroll.setPosition(Inv.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getX(), Inv.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getY());
							 						 			scroll.setWidth(newInv.getWidth());
							 						 			
							 						 			//updating the list
							 						 			Inv.getChildren().items[playerSelected + 1 + (1 * playerSelected)] = scroll;
							 									
							 									//updating the table										 			
							 						 			stage.getActors().removeIndex(getTableIndex("tableInventory"));
							 									stage.addActor(Inv);
																 							
							 									
							 									
							 									break;
							 								case 1: //equip, then erase the choices
							 									playerSquad.get(playerSelected).setEquipSlot(itemIndex);
							 									
							 							
							 									break;
							 								case 2: //use
							 									
							 									break;
							 									
							 								case 3: //unequip
							 									playerSquad.get(playerSelected).unequipItem(itemIndex);
							 															 									
							 									break;
							 							}
							 							//erasing the choices table
							 							stage.getActors().removeIndex(getTableIndex("tableChoice"));
						 						}});
						 									 						 												
						 						//getting the pos of the itemList
						 						Table tableItem = searchTable("tableInventory");					 						
						 							listChoice.setWidth(100);
						 							listChoice.setHeight(100);
						 						listChoice.setPosition(tableItem.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getX() + 100, tableItem.getChildren().get(playerSelected + 1 + (1 * playerSelected)).getY());
						 											 				 						
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
					 		}	 		
					 		inventory.addActor(lblinventory);
					 		
					 		stage.addActor(inventory);
					 		break;
					 		
					 	case 2://Quit
					 		save();
					 		dispose();
					 		Gdx.app.exit();
					 		break;
					 }
				
			}});
						
			//setting the pos of the list
			listMenu.setPosition(stage.getWidth() - listMenu.getWidth(), stage.getHeight() - listMenu.getHeight());
							
			tableMenu.addActor(listMenu);
			
			stage.addActor(tableMenu);
		}
	
		
	}

	private void toggleSubMenu()
	{
		//deleting the other sub menu if called
		
		if(getTableIndex("tableStatus") != -1)		
			stage.getActors().removeIndex(getTableIndex("tableStatus"));		
		
		if(getTableIndex("tableInventory") != -1)
			stage.getActors().removeIndex(getTableIndex("tableInventory"));
		
		if(getTableIndex("tableChoice") != -1)
			stage.getActors().removeIndex(getTableIndex("tableChoice"));		
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode)
		{
			case Input.Keys.ESCAPE:
				toggleMenu();
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

		try {
		//FileManager file = new FileManager("player_Fight.xml");
			FileHandle file = Gdx.files.local("player_Fight.xml");
				
			 StringWriter stringwriter = new StringWriter();
			 XmlWriter xml = new XmlWriter(stringwriter);
		 
				xml.element("root");	
				for(int i = 0;i < playerSquad.size(); i++)
				{
					xml.element("player_Fight").attribute("num", i)	       
	                .element("name").attribute("value", playerSquad.get(i).Name).pop()
	                .element("class").attribute("value", playerSquad.get(i).getclass()).pop()
	                .element("hpmax").attribute("value", playerSquad.get(i).getHpMax()).pop()
	                .element("hp").attribute("value", playerSquad.get(i).getLife()).pop()
	                .element("def").attribute("value", playerSquad.get(i).getDef()).pop()
	                .element("atk").attribute("value", playerSquad.get(i).getAtk()).pop()
	                .element("exp").attribute("value", playerSquad.get(i).getExp()).pop()
	                .element("money").attribute("value", playerSquad.get(i).getMoney()).pop()
	                .element("level").attribute("value", playerSquad.get(i).getLevel()).pop()
	                .element("weaponEquip").attribute("value", playerSquad.get(i).weaponEquipped).pop()
	                .element("armorEquip").attribute("value", playerSquad.get(i).armorEquipped).pop()
	                .element("bag").attribute("space", playerSquad.get(i).getBagSpace());
					
					//populating the bag
					for(int p = 0; p < playerSquad.get(i).bag.size(); p++)
					{
						xml.element("item").attribute("type", playerSquad.get(i).bag.get(p).type);	
						
						switch(playerSquad.get(i).bag.get(p).type)
		        		{
		        			case 0: //weapon
		        				PartyHard_Weapon wep = (PartyHard_Weapon) playerSquad.get(i).bag.get(p);
		        				xml.attribute("id", wep.getId()).pop();
		        			break;
		        			case 1: //armor
		        				PartyHard_Armor armor = (PartyHard_Armor) playerSquad.get(i).bag.get(p);
		        				xml.attribute("id", armor.getId()).pop();
		        			break;
		        			case 2: //potion
		        				PartyHard_Potion pot = (PartyHard_Potion) playerSquad.get(i).bag.get(p);
		        				xml.attribute("id", pot.getId()).pop();
		        			break;
		        		}
					}
					xml.pop();//closing the bag
					xml.element("capacity").attribute("value", playerSquad.get(i).capacity.size());
					
					for(int p = 0; p < playerSquad.get(i).capacity.size(); p++)
					{
						xml.element("cap").attribute("id", playerSquad.get(i).capacity.get(p).id).pop();				
					}
					//closing cap and then the player
					xml.pop();
					xml.pop();
			
					/*
					 * updating the inventory with (class, object)
					 */
					             
				}
				      //to be sure that all the element has been close  
				   xml.close();                   
			
				   file.writeString(stringwriter.toString(), false);
				   			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
	}


}