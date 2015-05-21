package com.partyhard.game;

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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_MapScreen implements Screen, InputProcessor{

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
	
	private ListStyle listStyle = new ListStyle();
	private TextButtonStyle buttonStyle = new TextButtonStyle();
	private LabelStyle labelStyle = new LabelStyle();
	
	//for randomly begin a fight
	private float fightTime = 0;	
	//probability of fight
	private int proba = 0;
	//array of monster name 
	private ArrayList<String> nameMonster = new ArrayList<String>();
	private Skin skin;
	
	
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
			//TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/arrows.pack")));
			
			Skin skin = new Skin(Gdx.files.internal("ui_button/arrows.pack"));		
			
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
			 
		 labelname.setPosition(mapNameTable.getWidth(), mapNameTable.getHeight()); 
		
		 mapNameTable.add(labelname);
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
			
			//preparing fighters
			ArrayList<PartyHard_Player_Fight> playerSquad = new ArrayList<PartyHard_Player_Fight>();
			
			playerSquad.add(new PartyHard_Player_Fight(0));
			playerSquad.add(new PartyHard_Player_Fight(1));
			 
		    fightScreen = new PartyHard_Fight(playerSquad, monster, this, (PartyHard_GameClass) mainGame, prop.get("Battle_Music", String.class));
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
	
	//used to toggle the menus
	private void toggleMenu()
	{
		//menu exist
		if(getTableIndex("tableMenu") != -1)
		{
			stage.getActors().removeIndex(getTableIndex("tableMenu"));
		}
		//if it doesn't
		else
		{			
			Table tableMenu = new Table();
			tableMenu.setName("tableMenu");
			tableMenu.center();
			
			List<TextButton> listMenu = new List<TextButton>(listStyle);
			listMenu.setHeight(100);
			listMenu.setWidth(80);
			
			
			TextButton btn;
			//adding the buttons to the menu
			for(int i= 0; i != 3;i++)
			{
				 btn = new TextButton(""+i, buttonStyle);
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
					 switch(((List<TextButton>) event.getTarget()).getSelectedIndex())
					 {
					 	case 0: //status
					 		//if the status has already been called
					 		if(getTableIndex("tableStatus") != -1)
					 			stage.getActors().removeIndex(getTableIndex("tableStatus"));
					 		
					 		Table tableStatus = new Table();
					 		tableStatus.setName("tableStatus");
					 		
					 		NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));
								
					 		NinePatchDrawable drawable = new NinePatchDrawable(patch);
					 		
					 		tableStatus.setBackground(drawable);					 							 		
					 		
					 		tableStatus.add();
					 		
					 		stage.getActors().add(tableStatus);
					 		
					 		break;
					 }
				
			}});
						
			//setting the pos of the list
			listMenu.setPosition(Gdx.graphics.getWidth() - listMenu.getWidth(), Gdx.graphics.getHeight() - listMenu.getHeight());
							
			tableMenu.addActor(listMenu);
			
			stage.getActors().add(tableMenu);
		}
	
		
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

}