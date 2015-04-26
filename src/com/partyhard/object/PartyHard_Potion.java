package com.partyhard.object;

public class PartyHard_Potion extends PartyHard_Consumable{
	private int amount;
	private int type;

	public PartyHard_Potion(String Name, String imagePath, String description, int amount, int type, int ObjectType) {
		super(Name, imagePath, description,ObjectType);
		this.setAmount(amount);
		this.setType(type);

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
