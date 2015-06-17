package utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.partyhard.actor.PartyHard_Monster;

public class PartyHard_Boss {

	public ArrayList<PartyHard_Monster> bossSquad = new ArrayList<PartyHard_Monster>();
	public Vector2 position = new Vector2();
	public String musicPath;
	
	public PartyHard_Boss(String[] name, int x, int y ,String musicPath)
	{
		for(int i = 0; i < name.length; i++)
		{
			bossSquad.add(new PartyHard_Monster(name[i]));
		}
		
		position.x = x;
		position.y = y;
		
		this.musicPath = musicPath;
	}
	
	public PartyHard_Boss(String name, int x, int y, String musicPath)
	{
		bossSquad.add(new PartyHard_Monster(name));
		
		position.x = x;
		position.y = y;
		
		this.musicPath = musicPath;
	}
	
	public PartyHard_Boss()
	{
		
	}
}
