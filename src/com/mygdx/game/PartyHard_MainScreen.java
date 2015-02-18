package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.actor.PartyHard_Player;

public class PartyHard_MainScreen implements Screen, InputProcessor{

	private static final float TIMESTEP = 1 / 60f;
	private static final int VELOCITYITERATIONS = 8;
	private static final int POSITIONITERATIONS = 3;
	public Game mainGame;
	private SpriteBatch spriteBatch;   
	
	private Stage stage;
	
	/*
	 * Player Movement
	 */
	
	private Animation walk_right;
	
	private Animation walk_left;
	
	private Animation walk_toward;
	
	private Animation walk_backward;
	
	private float animationTime = 0f;
	
	private int direction = -1;
	/*
	 * 0 toward
	 * 1 right
	 * 2 backward
	 * 3 left
	 */
	
	private PartyHard_Player player;
	
	private Sound mapSound;
	
	private OrthographicCamera camera;
	private TiledMap tiledmap;
	private TiledMapRenderer maprenderer;
	private MapProperties prop;
	
	private int mapPixelWidth;
	private int mapPixelHeight;
	
	private TextureRegion currentFrame;
	private Object PartyHard_Fight; 
	
	public PartyHard_MainScreen(Game gameToKeep, String mapPath)
	{	
		mainGame = gameToKeep;
		
		tiledmap = new TmxMapLoader().load(mapPath+".tmx");
	}
	
	public  void createPlayerAnimation()
	{
		Texture walkSheet = new Texture(Gdx.files.internal(player.getSprite())); 
		walkSheet.bind();
		
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/4, walkSheet.getHeight()/4);
        
       TextureRegion[] walkFrames = new TextureRegion[4 * 4];
     
       
        for (int i = 0; i < 4; i++) 
        {      	
        	
        		// loading animation frame by frame
            	walkFrames[0] = tmp[i][0];
            	walkFrames[1] = tmp[i][1];
            	walkFrames[2] = tmp[i][2];
            	walkFrames[3] = tmp[1][3];
           
            //loading animation for each direction
            switch(i)
            {
            	case 0:
            		walk_backward = new Animation(0.25f, walkFrames);
            		walk_backward.setPlayMode(Animation.PlayMode.NORMAL);
            		break;
            	case 1:
            		walk_left = new Animation(0.25f, walkFrames);
            		walk_left.setPlayMode(Animation.PlayMode.NORMAL);
            		break;
            	case 2:
            		walk_right = new Animation(0.25f, walkFrames);
            		walk_right.setPlayMode(Animation.PlayMode.NORMAL);              		
            		break;
            	case 3:
            		walk_toward = new Animation(0.25f, walkFrames);
            		walk_toward.setPlayMode(Animation.PlayMode.NORMAL);
            		break;
            }     
            walkFrames = new TextureRegion[4 * 4];
        }
        System.out.println(walk_toward.getPlayMode());
            
	}
	
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        maprenderer.setView(camera);
        maprenderer.render();
        
        stage.act(delta);
        
        
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.enableBlending();        
        
       
        switch(direction)
        {
        	case 0:       		  
        		spriteBatch.draw(walk_toward.getKeyFrame(animationTime, true), player.getX(), player.getY());
        		break;
        	case 1:
        		spriteBatch.draw(walk_right.getKeyFrame(animationTime, true), player.getX(), player.getY());
        		break;
        	case 2:
        		spriteBatch.draw(walk_backward.getKeyFrame(animationTime, true), player.getX(), player.getY());
        		break;
        	case 3:
        		spriteBatch.draw(walk_left.getKeyFrame(animationTime, true), player.getX(), player.getY());
        		break;
        }
         
        /*
         * TO DO:  camera 
         */
        
        
        /*
	     * Movement
	     */
	    
	    	if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
	    		if(camera.position.x - 32 >= Gdx.graphics.getWidth() / 2  )
	    		{
	    				if((animationTime += Gdx.graphics.getDeltaTime()) >= 1)
	    					animationTime = 0;
	    				camera.translate(-32,0);
	    				direction = 3;
	    		}
	    			
	        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
	        	if(player.getX() + 32 < mapPixelWidth)
	        	if((camera.position.x  + 632) + 32 < mapPixelWidth)
	        	{
    				if((animationTime += Gdx.graphics.getDeltaTime()) >= 1)
    					animationTime = 0;
	        		camera.translate(32,0);
	        		direction = 1;
	        	}
	        		
	        if(Gdx.input.isKeyPressed(Input.Keys.UP))
	        	if((camera.position.y + 392) < mapPixelHeight)
	        	{
	        		if((animationTime += Gdx.graphics.getDeltaTime()) >= 1)
    					animationTime = 0;
	        		camera.translate(0,32);
	        		direction = 0;
	        	}
	        		
	        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
	        	if((camera.position.y - 328) - 32 > 0 )
	        	{
	        		if((animationTime += Gdx.graphics.getDeltaTime()) >= 1)
    					animationTime = 0;
	        		camera.translate(0,-32);
	        		direction = 2;
	        	}
	        if(Gdx.input.isKeyPressed(Input.Keys.F))
	        {
	        	PartyHard_Fight fight = new PartyHard_Fight(null,null,this, (PartyHard_GameClass) mainGame);
	        }
	        	
	        spriteBatch.end();
	        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	Table mapNameTable = new Table();
	stage = new Stage();
		
		spriteBatch = new SpriteBatch();
		
		/*
		 * Android Button initialization
		 */
		
		
		
			/*
			 * Loading the skin wich contains the arrows info
			 */
			
			Table androidButtonTable = new Table();
			androidButtonTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			androidButtonTable.bottom().right();
			
			
			TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/androidButton.pack")));
			
			Skin skin = new Skin(tex);
			
			
			/*
			 * Button style that change at each button
			 */
			
			ButtonStyle style = new ButtonStyle();
			style.up = skin.getDrawable("android_up");
			style.down = skin.getDrawable("android_up");
			
			
			
			
			/*
			 * TODO: listener
			 */
			Button upArrow = new Button(style);
			upArrow.setHeight((float) (Gdx.graphics.getHeight()*0.055));
			upArrow.setWidth((float) (Gdx.graphics.getWidth()*0.055));
			upArrow.setPosition(Gdx.graphics.getWidth() - upArrow.getWidth(), 50);
			
			
			style.up = skin.getDrawable("android_down");
			style.down = skin.getDrawable("android_down");
			
			Button downArrow = new Button(style);
			
			
			style.up = skin.getDrawable("android_left");
			style.down = skin.getDrawable("android_left");
			
			Button leftArrow = new Button();
			leftArrow.setStyle(style);
			
			style.up = skin.getDrawable("android_right");
			style.down = skin.getDrawable("android_right");
			
			Button rightArrow = new Button();
			rightArrow.setStyle(style);
			
			androidButtonTable.addActor(rightArrow);
			androidButtonTable.addActor(leftArrow);
			androidButtonTable.addActor(downArrow);
			androidButtonTable.addActor(upArrow);
			stage.addActor(androidButtonTable);
		
		
		
		/*
		 * End initialization
		 */
		
		player = new PartyHard_Player("lol","player/test_player.png", 0, 0, 0, 25);
		player.setPosition(500, 500);
		
		createPlayerAnimation();
		
		mapSound = Gdx.audio.newSound(Gdx.files.internal("sound/map_sound_normal.mp3"));
		mapSound.setPitch(mapSound.loop(), 2.5f);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
		
		camera.update();
		
		 prop = tiledmap.getProperties();
		 int mapWidth = prop.get("width", Integer.class);
		 int mapHeight = prop.get("height", Integer.class);
		 int tilePixelWidth = prop.get("tilewidth", Integer.class);
		 int tilePixelHeight = prop.get("tileheight", Integer.class);
		 
		 String mapName = prop.get("Name", String.class);
		 
		 LabelStyle style1 = new LabelStyle();
		 
		 style1.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		 
		 System.out.println(mapName);
		 
		 Label labelname = new Label(mapName, style1);
		
		 	 
		 mapNameTable.center();
		 
		 mapNameTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 mapNameTable.top();
		 
		 labelname.setPosition(mapNameTable.getWidth(), mapNameTable.getHeight()); 
		
		 mapNameTable.add(labelname);
		 stage.addActor(mapNameTable);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		
		maprenderer = new OrthogonalTiledMapRenderer(tiledmap);
	    Gdx.input.setInputProcessor(this);	
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
	}

	@Override
	public boolean keyDown(int keycode) {
		
		 
	        if(keycode == Input.Keys.ESCAPE)
	        {
	        	this.dispose();
	        	mainGame.dispose();
	        	Gdx.app.exit();	
	        }
	        	
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
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
