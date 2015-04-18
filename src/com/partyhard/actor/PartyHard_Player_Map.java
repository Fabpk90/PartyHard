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

public class PartyHard_Player_Map extends Sprite{
	
	
	private String imagePath;
	
	private Animation walk_backward;
	private Animation walk_left;
	private Animation walk_right;
	private Animation walk_toward;
	
	//used to know in which direction the player is going
	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean movingTop = false;
	private boolean movingDown = false;
	
	private boolean collisionXRight = false;
	private boolean collisionXLeft = false;
	
	private boolean collisionYDown = false;
	private boolean collisionYTop = false;
	
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
	
	//special constructor without the tiledlayer
	public PartyHard_Player_Map(int x, int y, String imagePath)
	{
		this.setX(x);
		this.setY(y);
	
		this.imagePath = imagePath;
	}
	
	
	@Override 
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		destination.x = x;
		destination.y = y;
	}
	
	public void moveRight()
	{
		
			//stopping all previous movement before the new one
			stopMovement();
			moving = true;
			movingRight = true;
			//setting the position of the destination
			destination.x += + 32;
		
		
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

		resetCollision();

	if(moving)
	{
		if(movingLeft) // going left
			collisionXLeft = collidesLeft();
		if(collisionXLeft)// checking left collision if exist going 32 pixel before
			setX(destination.x + 32);
		 if(movingRight) // going right
			collisionXRight = collidesRight();
		 if(collisionXRight)
				setX(destination.x - 32);

		// react to x collision
		if(collisionXRight || collisionXLeft ) 
		{
			stopMovement();
			destination.x = getX();
			destination.y = getY();
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
		if(movingDown) // going down
			collisionYDown = collidesBottom();
		if(collisionYDown)
			setY(destination.y + 32);
		if(movingTop) // going up
			collisionYTop = collidesTop();
		if(collisionYTop)
			setY(destination.y - 32);

		// react to y collision
		if(collisionYTop || collisionYDown) {
			stopMovement();
			destination.x = getX();
			destination.y = getY();
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
	
	private void resetCollision() {
		collisionXRight = false;
		 collisionXLeft = false;
		
		 collisionYDown = false;
		 collisionYTop = false;
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
	 * @param frame the frame to find using getKeyFrame
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
		boolean ok = false;
		for(int i = 0; i < collisionLayer.getLayers().getCount(); i++ )
		{
			TiledMapTileLayer layer = (TiledMapTileLayer) collisionLayer.getLayers().get(i);
			Cell cell =  layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
			//check the cell for collisions
			if( cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey))
				ok = true;					
		}
		
		return ok;
	}
	
	private float getRightCell()
	{
		//getting a layer for getting the width 
		TiledMapTileLayer layer = (TiledMapTileLayer) collisionLayer.getLayers().get(0);
		return (this.getX() + layer.getTileWidth());
	}

	private boolean collidesRight() {
		for(float step = 0; step <= getHeight(); step +=  ((TiledMapTileLayer) collisionLayer.getLayers().get(0)).getTileWidth())
			if(isCellBlocked(getX() + getWidth(), getY()))
				return true;
		return false;
	}

	private boolean collidesLeft() {
		for(float step = 0; step <= getHeight(); step += ((TiledMapTileLayer) collisionLayer.getLayers().get(0)).getTileWidth())
			if(isCellBlocked(getX() - step, getY()))
				return true;
		return false;
	}

	private boolean collidesTop() {
		for(float step = 0; step <= getWidth(); step += ((TiledMapTileLayer) collisionLayer.getLayers().get(0)).getTileWidth())
			if(isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	private boolean collidesBottom() {
		for(float step = 0; step <= getWidth(); step += ((TiledMapTileLayer) collisionLayer.getLayers().get(0)).getTileWidth())
			if(isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}

	
}
