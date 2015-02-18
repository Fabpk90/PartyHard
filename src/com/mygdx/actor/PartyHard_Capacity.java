package com.mygdx.actor;

public class PartyHard_Capacity {
	public String Name;
	public int percent;
	public boolean isHeal;
	public int amount;
	public int cost;
	
	public int job;
	/*
	 * 0 warrior
	 * 1 Clerk 
	 */
	
	public PartyHard_Capacity(String Name, int percentOfSucceed, boolean isHeal, int amount, int manaCost, int job)
	{
		this.Name = Name;
		percent = percentOfSucceed;
		this.isHeal = isHeal;
		this.amount = amount;
		this.cost = manaCost;
		this.job = job;
	}
}
