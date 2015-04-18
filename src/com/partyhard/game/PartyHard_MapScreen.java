package com.partyhard.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_MapScreen implements Screen{

	public Game mainGame;
	private SpriteBatch spriteBatch;  
	
	private Stage stage;
	
	
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
	
	private int mapPixelWidth;
	private int mapPixelHeight;
	
	public PartyHard_MapScreen(Game gameToKeep, String mapPath, PartyHard_Player_Map playerMap)
	{	
		mainGame = gameToKeep;
		this.playerMap = playerMap;
		tiledmap = new TmxMapLoader().load(mapPath+".tmx");
	}
	
	
	
	
	@Override
	public void render(float delta) {
			
		animationTime += delta;
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        maprenderer.setView(camera);
        maprenderer.render();
        
        stage.act(delta);
       
        
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.enableBlending();        
        
       
        playerMap.update(delta);
        
        if(animationTime + delta >= 1)
        	animationTime = 0;
        if(playerMap.isMoving())
        	spriteBatch.draw(playerMap.getFrame(direction, animationTime), playerMap.getX(), playerMap.getY());
        else
        	spriteBatch.draw(playerMap.getFrame(direction, 0), playerMap.getX(), playerMap.getY());
        
        spriteBatch.end();
        /*
         * TO DO:  camera 
         */
        
        
        /*
	     * Movement
	     */
        
      
        	 //for desktop
        if(Gdx.app.getType() == Gdx.app.getType().Desktop)
        {
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
	}

	@Override
	public void resize(int width, int height) {
		 stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		
	camera = new OrthographicCamera();
	camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
	
	camera.viewportHeight = camera.viewportHeight / 1.2f;
	camera.viewportWidth = camera.viewportWidth / 1.2f;
		
	camera.update();
	
	Table mapNameTable = new Table();
	stage = new Stage();
	

		spriteBatch = new SpriteBatch();
		
		/*
		 * Android Button initialization
		 */
		if(Gdx.app.getType() == Gdx.app.getType().Android)
		{
			/*
			 * Loading the skin wich contains the arrows info
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
			androidarrow.addActor(topArrow);
			androidarrow.addActor(leftArrow);
			androidarrow.addActor(downArrow);
			androidarrow.addActor(rightArrow);
			
			stage.addActor(androidarrow);
			Gdx.input.setInputProcessor(stage);

		}
		
		
		playerMap = new PartyHard_Player_Map(100,100, "player/test_player.png", tiledmap );
		playerMap.createPlayerAnimation();
		
		mapSound = Gdx.audio.newSound(Gdx.files.internal("sound/map_sound_normal.mp3"));
		mapSound.setPitch(mapSound.loop(), 2.3f);

		 prop = tiledmap.getProperties();
		 int mapWidth = prop.get("width", Integer.class);
		 int mapHeight = prop.get("height", Integer.class);
		 int tilePixelWidth = prop.get("tilewidth", Integer.class);
		 int tilePixelHeight = prop.get("tileheight", Integer.class);
		 
		 String mapName = prop.get("Name", String.class);
		 
		 LabelStyle style1 = new LabelStyle();
		 
		 style1.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		 
		 //used for printing the map name
		 Label labelname = new Label(mapName, style1);
		
		 	 
		 // setting position of the label
		 mapNameTable.center();
		 mapNameTable.top();
		 
		 mapNameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			 
		 labelname.setPosition(mapNameTable.getWidth(), mapNameTable.getHeight()); 
		
		 mapNameTable.add(labelname);
		 stage.addActor(mapNameTable);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		
		maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
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

	@Override
	public void dispose() {
		tiledmap.dispose();
		mapSound.dispose();
		spriteBatch.dispose();	
		stage.dispose();
	}

	public void moveRight()
	{
		//if(playerMap.getX() + 32 < mapPixelWidth)
        	//if((camera.position.x  + 632) + 32 < mapPixelWidth)
        	{
        		playerMap.stopMovement();
        		direction = 2;
        		playerMap.moveRight();
        		//camera.translate(32,0);
        	}
	}
	
	public void moveLeft()
	{
		//if(camera.position.x - 32 >= Gdx.graphics.getWidth() / 2  )
		//{
				playerMap.stopMovement();
				direction = 1;
				playerMap.moveLeft();
				//camera.translate(-32,0);
		//}
	}
	
	public void moveTop()
	{
		//if((camera.position.y + 392) < mapPixelHeight)
    	//{
    		//camera.translate(0,32);
			playerMap.stopMovement();
    		direction = 3;
    		playerMap.moveTop();
    	//}
	}
	
	public void moveDown()
	{
		//if((camera.position.y - 328) - 32 > 0 )
    	//{
    		//camera.translate(0,-32);
			playerMap.stopMovement();
    		direction = 0;
    		playerMap.moveDown();
    	//}	
	}
}