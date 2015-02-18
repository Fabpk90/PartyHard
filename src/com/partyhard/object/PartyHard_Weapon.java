package com.partyhard.object;


public class PartyHard_Weapon {
	public String Name;
	public int price;
	private String imagePath;
	private int weaponType;
	/*
	 * TO DO: set the types
	 * 
	 */

	
	public PartyHard_Weapon(String Name, String imagePath, int price)
	{
		this.Name = Name;
		this.imagePath = imagePath;
		this.price = price;
	}
}
