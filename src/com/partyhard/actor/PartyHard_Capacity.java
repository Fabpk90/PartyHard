package com.partyhard.actor;

public class PartyHard_Capacity {
	public String Name;
	public int percent;
	public boolean isHeal;
	public int amount;
	public int cost;
	public int id; //used for identifying the cap in xml
	public String description;
	
	public int job;
	/*
	 * 0 warrior
	 * 1 Clerk 
	 */
	
	public PartyHard_Capacity(String Name,String description, int percentOfSucceed, boolean isHeal, int amount, int manaCost, int job, int id)
	{
		this.Name = Name;
		percent = percentOfSucceed;
		this.isHeal = isHeal;
		this.amount = amount;
		this.cost = manaCost;
		this.job = job;
		this.id = id;
		this.description = description;
	}
}
