package com.mygdx.actor;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.physics.box2d.Shape;
import com.partyhard.object.PartyHard_Object;

public class PartyHard_Player extends PartyHard_Actor implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private int Exp = 0;
	private int Level = 1;
	private int Money = 0;
	private  ArrayList<PartyHard_Object> bag;
	private int bagSpace = 0;
	
	public PartyHard_Player(String Name,String TexturePath, int Hp, int Atk, int Def, int bagSpace) {
		super(Name, Hp, Atk, Def);
		this.bagSpace = bagSpace;
		this.setSprite(TexturePath);
		
	}
	public void update(int displacementAmount, float delta)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
		{
			if((this.getX() + 32) + displacementAmount < Gdx.graphics.getWidth())
			moveToTheRight(displacementAmount, delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
		{
			if(this.getX() - displacementAmount > 0)
				moveToTheLeft(displacementAmount, delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
		{
			if(this.getY() - displacementAmount > 0)
			{
				moveBackward(displacementAmount, delta);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
		{
			if((this.getY() + 32) + displacementAmount < Gdx.graphics.getHeight() )
			{
				moveToward(displacementAmount, delta);
			}
		}
	}
	
	public void setSprite(String spritePath)
	{
		super.setSprite(spritePath);
	}
	
	public String getSprite()
	{
		return super.getSprite();
	}
	
	public void addExp(int amount)
	{
		Exp += amount;
	}
	
	public int getExp()
	{
		return Exp;
	}
	
	public int getMoney()
	{
		return Money;
	}
	
	public void setMoney(int amount)
	{
		Money = amount;
	}
	
	public void addMoney(int amount)
	{
		Money = amount;
	}
	
	public boolean pay(int amount)
	{
		if(Money - amount < 0)
			return false;
		else
		{
			Money -= amount;
			return true;
		}
		
	}
	
	public boolean addToBag(PartyHard_Object object)
	{
		if((bag.size() + 1) > bagSpace)
			return false;
		else
		{
			bag.add(object);
			return true;
		}
		
	}
	
}
