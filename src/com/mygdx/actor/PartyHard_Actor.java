package com.mygdx.actor;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PartyHard_Actor extends Actor{
	private int Health;
	private String Name;
	private int Attack;
	private int Def;
	private String sprite;
	
	public PartyHard_Actor(String Name, int Hp, int Atk, int Def)
	{
		this.Name = Name;
		Health = Hp;
		Attack = Atk;
		this.Def = Def;
		
	}
	
	public void directDamage(int amount)
	{
		Health = Health - amount;
	}
	
	public void damage(int amount)
	{
		if(amount > Def)
			Health = Health - (amount - Def);
	}
	
	public void moveToTheRight(int amount, float delta)
	{
		 this.setX((this.getX() + (amount * delta)));
	}
	
	public void moveToTheLeft(int amount, float delta)
	{
		this.setX((this.getX() - (amount * delta)));
	}
	
	public void moveToward(int amount, float delta)
	{
		this.setY(this.getY() + (amount * delta));
	}
	
	public void moveBackward(int amount, float delta)
	{
		this.setY((this.getY() - (amount * delta)));
	}
	
	public void setSprite(String spritePath)
	{
		sprite = spritePath;
	}
	
	public String getSprite()
	{
		return sprite;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public int getAtk()
	{
		return Attack;
	}
	
	public int getLife()
	{
		return Health;
	}

}
