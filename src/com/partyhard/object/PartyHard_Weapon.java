package com.partyhard.object;


public class PartyHard_Weapon extends PartyHard_Object{
	
	public int price;
	private int weaponType;
	private int amount = 0; //amount of atk if weapon or def if armor
	/*
	 * TO DO: set the types
	 * 
	 */

	
	public PartyHard_Weapon(String Name, String imagePath, String description, int price, int type, int amount)
	{
		/*
		 * TO DO load weapon from xml
		 */
		//sending 0 because it's a weapon
		super(Name, imagePath, description, 0);
		this.price = price;
		this.weaponType = type;
		this.amount = amount;
	}


	public int getWeaponType() {
		return weaponType;
	}


	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}
}
