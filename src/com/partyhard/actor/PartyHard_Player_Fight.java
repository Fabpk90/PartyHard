package com.partyhard.actor;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.partyhard.object.PartyHard_Object;

public class PartyHard_Player_Fight{

	
	private String Name;
	
	private int Class;
	private int Exp = 0;
	private int Level = 1;
	private int Money = 0;
	
	public ArrayList<PartyHard_Object> bag;
	public int bagSpace = 0;
	
	public ArrayList<PartyHard_Capacity> capacity;
	
	private int HpMax;
	
	private int Hp;
	private int Atk;
	private int Def;
	
	private boolean isDead = false;
	
	private int targetCapacity = -1;
	private int capacitySelected = -1;
	
	public PartyHard_Player_Fight(int idplayer) 
	{
		XmlReader xml = new XmlReader();
		
		Element root;
		
		try
		{
			root = xml.parse(Gdx.files.local("player_Fight.xml"));
			Array<Element> arrayOfPlayer =	root.getChildrenByName("player_Fight");	
			for(int i = 0; i < arrayOfPlayer.size; i++)
			{
				//the player has been found
				if(arrayOfPlayer.get(i).getInt("num") == idplayer)
				{
					Name = arrayOfPlayer.get(i).getChildByName("name").getAttribute("value");
					setHpMax(Integer.parseInt(arrayOfPlayer.get(i).getChildByName("hpmax").getAttribute("value")));
					Hp = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("hp").getAttribute("value"));
					Def = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("def").getAttribute("value"));
					Atk = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("atk").getAttribute("value"));
					Exp = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("exp").getAttribute("value"));
					Money = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("money").getAttribute("value"));
					Level = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("level").getAttribute("value"));
					bagSpace = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("bag").getAttribute("space"));
					Class = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("class").getAttribute("value"));
					int Capacities = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("capacity").getAttribute("value"));
					
					 bag = new ArrayList<PartyHard_Object>();
					 capacity = new ArrayList<PartyHard_Capacity>();
					
					//populating the inventory
					for(int p = 0; p != bagSpace; p++)
					{
						/*
						PartyHard_Weapon weapon = new PartyHard_Weapon("Test", "", 0);
						bag.add(weapon);
						/*
						 * TO DO checking the attribute class to determine which object is 
						 */
					}
					
					/*
					 * TODO get all the id from the player and check them by capacity if equal add
					 */
					
					//getting the capacities
					
					Element rootCap = xml.parse(Gdx.files.local("capacity.xml"));
					
					Array<Element> arrayOfCap =	rootCap.getChildrenByName("capacity");
					Array<Element> arrayPlayerCap = root.getChildByName("player_Fight").getChildByName("capacity").getChildrenByName("cap");
					
					//looking first in capacity and then checking if it exist in playerxml					
					for(int y = 0; y < arrayOfCap.size; y++)
					{
											
						for(int m = 0; m < arrayPlayerCap.size; m++)
						{
							
							if(y == arrayPlayerCap.get(m).getInt("id"))
							{
								String capName = arrayOfCap.get(y).getChildByName("name").getAttribute("value");
								String capDescription = arrayOfCap.get(y).getChildByName("description").getAttribute("value");
								
								int job = Integer.parseInt(arrayOfCap.get(y).getChildByName("job").getAttribute("value"));
								int mana = Integer.parseInt(arrayOfCap.get(y).getChildByName("mana").getAttribute("value"));
								boolean isHeal = arrayOfCap.get(y).getChildByName("isheal").getBoolean("value");
								int atk = Integer.parseInt(arrayOfCap.get(y).getChildByName("power").getAttribute("value"));
								int percent = Integer.parseInt(arrayOfCap.get(y).getChildByName("percent").getAttribute("value"));
								
								capacity.add(new PartyHard_Capacity(capName, capDescription, percent, isHeal, atk, mana, this.Class,y)); 
							
							}
													
						}
						
					}															
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//checking if the chara is dead
		if(Hp <= 0)
			isDead = true;
		
		
		
	}
	
	public void Damage(int amount)
	{
		if(amount > Hp)
		{
			Hp = 0;
			isDead = true;
		}
		else
			Hp -= amount;
			
	}
	
	public int getclass()
	{
		return Class;
	}
	
	public void setclass(int Class)
	{
		this.Class = Class;
	}
	
	public int getBagSpace()
	{
		return bagSpace;
	}
	
	public void setAtk(int amount)
	{
		this.Atk = amount;
	}
	
	public int getAtk()
	{
		return Atk;
	}
	
	public void setTarget(int idTarget)
	{
		this.targetCapacity = idTarget;
	}
	
	public int getTarget()
	{
		return targetCapacity;
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
	
	/*
	 * @ param object An object deriving from Object
	 */	
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
	
	public void setDef(int amount)
	{
		this.Def = amount;
	}
	
	public int getDef()
	{
		return Def;
	}
	
	public void setLevel(int newLevel)
	{
		this.Level = newLevel;
	}
	
	public int getLevel()
	{
		return Level;
	}
	
	public void setLife(int amount)
	{
		//if the settled life is > than the max hp
		if(amount + Hp > HpMax)
			Hp = HpMax;
		Hp += amount;
	}
	
	public int getLife()
	{
		return Hp;
	}
	
	public String getName()
	{
		return Name;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getCapacitySelected() {
		return capacitySelected;
	}

	public void setCapacitySelected(int capacitySelected) {
		this.capacitySelected = capacitySelected;
	}

	public int getHpMax() {
		return HpMax;
	}

	public void setHpMax(int hpMax) {
		HpMax = hpMax;
	}
	
}
