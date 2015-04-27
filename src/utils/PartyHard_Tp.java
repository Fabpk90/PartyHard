package utils;

import com.badlogic.gdx.math.Vector2;

public class PartyHard_Tp {

	public Vector2 position = new Vector2();
	public Vector2 destination = new Vector2();

	public String NameMap;
	
	public PartyHard_Tp(float xPos, float yPos, String nameNewMap, float xDestination, float yDestination) {
		
		position.x = xPos;
		position.y = yPos;
		
		NameMap = nameNewMap;
		destination.x = xDestination;
		destination.y = yDestination;		
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}

	public String getNameMap() {
		return NameMap;
	}

	public void setNameMap(String nameMap) {
		NameMap = nameMap;
	}
	
	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}
	
	public void setDestination(float x, float y)
	{
		destination.x = x;
		destination.y = y;
	}

}
