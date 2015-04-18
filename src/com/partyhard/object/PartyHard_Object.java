package com.partyhard.object;

public class PartyHard_Object {
	public String Name;
	public String imagePath;
	public String description;
	public int type;
	
	/*
	 * type 
	 * 0 weapon
	 * 1 armor
	 * 2 potion
	 */
	
	public PartyHard_Object(String Name, String imagePath, String description, int type)
	{
		this.Name = Name;
		this.imagePath = imagePath;
		this.description = description;
		this.type = type;
	}

}
