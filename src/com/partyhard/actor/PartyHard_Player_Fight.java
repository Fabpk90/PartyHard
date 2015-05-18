package com.partyhard.actor;

import java.io.IOException;
import java.util.ArrayList;

import utils.PartyHard_Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.partyhard.object.PartyHard_Armor;
import com.partyhard.object.PartyHard_Object;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;

public class PartyHard_Player_Fight{
	
	public String Name;
	
	private int Class;
	private int Exp = 0;
	private int Level = 1;
	private int Money = 0;
	
	public boolean isLevelMax = false;
	public PartyHard_Level levelUp;
	
	public ArrayList<PartyHard_Object> bag;
	private int bagSpace = 0;
	
	private int objectUsed = -1;
	
	public ArrayList<PartyHard_Capacity> capacity;
	
	private int targetCapacity = -1;
	private int capacitySelected = -1;
	
	private int HpMax;
	
	private int Hp;
	private int Atk;
	private int Def;
	
	private boolean isDead = false;
	
	
	public int damageTaken = 0;
	public int healAmount = 0;
	
	public PartyHard_Player_Fight(int idplayer) 
	{
		XmlReader xml = new XmlReader();
		
		Element root;
		
		try
		{
			//FileManager fileManager = new FileManager("player_Fight.xml");
			
			root = xml.parse(Gdx.files.internal("player_Fight.xml"));
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
					
					 bag = new ArrayList<PartyHard_Object>();
					 capacity = new ArrayList<PartyHard_Capacity>();				
					 
					 Array<Element> items =	arrayOfPlayer.get(i).getChildByName("bag").getChildrenByName("item");
					 
					//populating the inventory
					for(int p = 0; p != bagSpace; p++)
					{
						switch(items.get(p).getInt("type"))
						{
							case 0: //weapon
								bag.add(new PartyHard_Weapon(items.get(p).getInt("id")));
								break;
							case 1: //Armor
								bag.add(new PartyHard_Armor(items.get(p).getInt("id")));
								break;
							case 2: //Potion
								bag.add(new PartyHard_Potion(items.get(p).getInt("id")));
								break;
						}
					}
										
					//getting the capacities
					
					Element rootCap = xml.parse(Gdx.files.local("capacity.xml"));
					
					Array<Element> arrayOfCap =	rootCap.getChildrenByName("capacity");
					Array<Element> arrayPlayerCap = arrayOfPlayer.get(i).getChildByName("capacity").getChildrenByName("cap");
					
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
					
					rootCap = xml.parse(Gdx.files.local("level.xml"));
					
					Array<Element> arrayOfLevel = rootCap.getChildrenByName("level");
					
					/*
					 * getting the info to level up
					 */
				
					for(int p = 0; p < arrayOfLevel.size; p++)
					{
						if(arrayOfLevel.get(p).getInt("class") == Class)
						{
							
							Array<Element> arrayOfPlayerLevel = arrayOfLevel.get(p).getChildrenByName("up");
							
							isLevelMax = false;
							//checking if the player is not level max  (+1 because the first level up is lvl 2)
							if(arrayOfPlayerLevel.size + 1 >= Level)
							{								
								System.out.println(arrayOfPlayerLevel.size);
								int expToUp = arrayOfPlayerLevel.get(Level - 1).getInt("exp");
								int hp = arrayOfPlayerLevel.get(Level - 1).getInt("hp");
								int atk = arrayOfPlayerLevel.get(Level - 1).getInt("atk");
								int def = arrayOfPlayerLevel.get(Level - 1).getInt("def");
								
								levelUp = new PartyHard_Level(this.Class, expToUp, hp, atk, def);										
								break;
							}
							else
							{
								isLevelMax = true;					
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
		if(amount >= Hp)
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
	
	public void refreshBag()
	{
		bagSpace = bag.size();
	}
	
	public void bagUsed()
	{
		bagSpace--;
	}
	
	public void setBagSpace(int amount)
	{
		bagSpace = amount;
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
	
	public void setExp(int amount)
	{
		Exp = amount;
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
		Money += amount;
	}
	
	public boolean canPay(int amount)
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
	
	public void LevelUp()
	{
		this.Level++;
		
		this.Atk += levelUp.atk;
		this.Def += levelUp.def;
		this.HpMax += levelUp.hp;
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
		HpMax += hpMax;
	}

	public int getDamageTaken() {
		return damageTaken;
	}

	public void setDamageTaken(int damageTaken) {
		int damage = damageTaken - Def;
		
		if(damage <0)
			damage = 1;
		
		this.damageTaken += damage;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount += healAmount;
	}

	public int getObjectUsed() {
		return objectUsed;
	}

	public void setObjectUsed(int objectUsed) {
		this.objectUsed = objectUsed;
	}	
}
