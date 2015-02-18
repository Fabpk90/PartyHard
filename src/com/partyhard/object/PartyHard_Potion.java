package com.partyhard.object;

public class PartyHard_Potion extends PartyHard_Consumable{
	private int amount;
	private int type;

	public PartyHard_Potion(String Name, String imagePath, int amount, int type) {
		super(Name, imagePath);
		this.amount = amount;
		this.type = type;

	}

}
