package com.partyhard.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class PartyHard_Player_Map {
	
	
	private String imagePath;
	
	private Animation walk_backward;
	private Animation walk_left;
	private Animation walk_right;
	private Animation walk_toward;
	
	//used to know in which direction the player is going to
	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean movingTop = false;
	private boolean movingDown = false;
	
	private boolean collisionX = false;
	
	private boolean collisionY = false;
	
	private float x;
	private float y;
	
	private float Height = 0;
	private float Width = 0;
	
	//store the destination
	private Vector2 destination;
	
	private TiledMap collisionLayer;
	
	//the key word that makes a cell blocked
	private String blockedKey = "block";
	
	//used for movement smoothness
	private boolean moving = false;
	
	//normal constructor
	public PartyHard_Player_Map(int x, int y, String imagePath, TiledMap Map)
	{
		this.setX(x);
		this.setY(y);
		this.collisionLayer = Map;
		this.imagePath = imagePath;
		destination = new Vector2(x,y);
	}
	
	private void setX(float f) {
		this.x = f;
	}

	private void setY(float y) {
		this.y = y;
	}

	//special constructor without the tiledlayer
	/*
	 * @param imagePath The path of the image(Texture Atlas)
	 * @see TextureAtlas
	 */
	public PartyHard_Player_Map(int x, int y, String imagePath)
	{
		this.setX(x);
		this.setY(y);
	
		this.imagePath = imagePath;
	}
	
	
	public void setPosition(float x, float y)
	{
		setX(x);
		setY(y);
		
		destination.x = x;
		destination.y = y;
	}
	
	public void moveRight()
	{
		
			//stopping all previous movement before the new one
			stopMovement();
			moving = true;
			movingRight = true;	
			destination.x += 32;
		
	}
	
	public void moveLeft()
	{
		stopMovement();
		moving = true;
		movingLeft = true;
		//setting the position of the destination
		destination.x += - 32;
	}
	
	public void moveDown()
	{
		stopMovement();
		moving = true;
		movingDown = true;
		//setting the position of the destination
		destination.y +=  - 32;
	}
	
	public void moveTop()
	{
		stopMovement();
		moving = true;
		movingTop = true;
		//setting the position of the destination
		destination.y +=  + 32;
	}
	
	public void setCollisionLayer(TiledMap Map)
	{
		this.collisionLayer = Map;
	}
	
	public void moving(boolean ismoving)
	{
		this.moving = ismoving;
	}
	
	public boolean isMoving()
	{
		return moving;
	}

	
	public void update(float delta) {

		

	if(moving)
	{
		resetCollision();
		
		collisionX = collidesX();
		
		if(collisionX) 
		{
			stopMovement();
			destination.x = getX();
			destination.y = getY();
			resetCollision();
		}
		else
		{
			if(movingRight)
			{
				setX(getX() + (2+delta));
				if(getX() > destination.x)
				{
					stopMovement();
					setX(destination.x);
				}					
			}
			
			if(movingLeft)
			{
				setX(getX() + (-2 + -delta));
				if(getX() < destination.x)
				{					
					stopMovement();
					setX(destination.x);
				}
			}	
			
		}
			
		// move on y
		collisionY = collidesY();
			
		// react to y collision
		if(collisionY) 
		{
			stopMovement();
			destination.x = getX();
			destination.y = getY();
			resetCollision();
		}
		else
		{
			if(movingTop)
			{
				setY(getY() + (2+delta));
				if(getY() > destination.y)
				{
					stopMovement();
					setY(destination.y);
				}			
				
			}
				
			if(movingDown)
			{
				setY(getY() + (-2 + -delta));
				if(getY() < destination.y)
				{					
					stopMovement();
					setY(destination.y);
				}
			}	
		}
	}
	
	
		
	}
	
	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}
	
	public float getWidth() {	
		return Width;
	}

	public float getHeight() {		
		return Height;
	}
	
	public void setWidth(float Width)
	{
		this.Width = Width;
	}
	
	public void setHeight(float Height)
	{
		this.Height = Height;
	}

	private void resetCollision() {
		collisionX = false;
		
		collisionY = false;
	}

	public void stopMovement()
	{
		movingRight = false;
		movingLeft = false;
		movingTop = false;
		movingDown = false;
		moving = false;
	}

	public  void createPlayerAnimation()
	{
		Texture walkSheet = new Texture(Gdx.files.internal(getSprite())); 
		walkSheet.bind();
		
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/4, walkSheet.getHeight()/4);             
        
       TextureRegion[] walkFrames = new TextureRegion[4 * 4];
      
       setWidth(walkSheet.getWidth() / 4);
       setHeight(walkSheet.getHeight() / 8);
       
        for (int i = 0; i < 4; i++) 
        {      	
        	for(int x = 0; x < 4; x++)
        	{
        		// loading animation frame by frame
            	walkFrames[x] = tmp[i][x];
        	}           	
           
            //loading animation for each direction
            switch(i)
            {
            	case 0:
            		walk_backward = new Animation(0.25f, walkFrames);
            		walk_backward.setPlayMode(Animation.PlayMode.LOOP);
            		break;
            	case 1:
            		walk_left = new Animation(0.25f, walkFrames);
            		walk_left.setPlayMode(Animation.PlayMode.LOOP);
            		break;
            	case 2:
            		walk_right = new Animation(0.25f, walkFrames);
            		walk_right.setPlayMode(Animation.PlayMode.LOOP);              		
            		break;
            	case 3:
            		walk_toward = new Animation(0.25f, walkFrames);
            		walk_toward.setPlayMode(Animation.PlayMode.LOOP);
            		break;
            }     
           
            walkFrames = new TextureRegion[4 * 4];
        }
            
	}
	/**
	 * @param direction 0 for backward, 1 for left, 2 for right, 3 for toward
	 * @param frame The frame to find, using getKeyFrame()
	 */
	public TextureRegion getFrame(int direction, float frame)
	{
		switch(direction)
		{
			case 0:
				return walk_backward.getKeyFrame(frame,true);
			case 1:
				return walk_left.getKeyFrame(frame, true);
			case 2:
				return walk_right.getKeyFrame(frame, true);
			case 3:
				return walk_toward.getKeyFrame(frame, true);
			
		}
		return null;
	}
	
	public String getSprite()
	{
		return imagePath;
	}
	
	public void setSprite(String imagePath)
	{
		this.imagePath = imagePath;
	}
	
	private boolean isCellBlocked(float x, float y) {
		
		for(int i = 0; i < collisionLayer.getLayers().getCount(); i++ )
		{
			TiledMapTileLayer layer = (TiledMapTileLayer) collisionLayer.getLayers().get(i);			
			Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
			//check the cell for collisions
			if( cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey))
				return true;					
		}
		
		return false;
	}
	
	private boolean isCellTp(float x, float y)
	{
		for(int i = 0; i < collisionLayer.getLayers().getCount(); i++ )
		{
			TiledMapTileLayer layer = (TiledMapTileLayer) collisionLayer.getLayers().get(i);			
			Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
			//check the cell for collisions
			if( cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("x"))
				return true;					
		}
		return false;
	}
	
	private boolean collidesX() {
		for(float step = 0; step <= getWidth(); step +=  ((TiledMapTileLayer) collisionLayer.getLayers().get(0)).getTileWidth())
			if(isCellBlocked(destination.x, getY()))
				return true;
		return false;
	}

	private boolean collidesY() {
		//using destination for knowing where 
			if(isCellBlocked(getX(), destination.y))
				return true;
		return false;
	}

	public void dispose() {
		
		collisionLayer.dispose();
	}

	
}
