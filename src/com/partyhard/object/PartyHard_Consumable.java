package com.partyhard.object;

public abstract class PartyHard_Consumable extends PartyHard_Object{

	public PartyHard_Consumable(String Name, String imagePath, String description, int type, int price) {
		super(Name, imagePath, description, type, price);
	}
	
	public abstract int getType();
	public abstract void setType(int type);
	public abstract int getAmount();
	public abstract void setAmount(int amount);
	
}
