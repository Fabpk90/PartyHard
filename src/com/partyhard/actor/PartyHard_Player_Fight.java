package com.partyhard.actor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import utils.FileManager;
import utils.PartyHard_Capacity;
import utils.PartyHard_Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.partyhard.object.PartyHard_Armor;
import com.partyhard.object.PartyHard_Object;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;
import com.partyhard.object.PartyHard_Weareable;

public class PartyHard_Player_Fight{
	
	public String Name;
	public String imagePath;
	
	private int Class;
	private int Exp = 0;
	private int Level = 1;

	
	public int armorEquipped = -1;
	
	public int weaponEquipped = -1;
	
	public boolean isLevelMax = false;
	public PartyHard_Level levelUp;
	
	public ArrayList<PartyHard_Object> bag = new ArrayList<PartyHard_Object>();
	private int bagSpace = 0;
	
	private int objectUsed = -1;
	
	public ArrayList<PartyHard_Capacity> capacity = new ArrayList<PartyHard_Capacity>();
	
	private int targetCapacity = -1;
	private int capacitySelected = -1;
	
	private int HpMax;
	
	private int Hp;
	private int Atk;
	private int Def;
	
	private boolean isDead = false;
	
	public int damageTaken = 0;
	public int healAmount = 0;
	
	private int idSave;
	
	private FileManager fileManager = new FileManager();
	
	public PartyHard_Player_Fight(int idplayer, int idSave) 
	{
		this.idSave = idSave;
		XmlReader xml = new XmlReader();
		
		Element root;
		
		try
		{
			fileManager.loadFile("save/"+idSave+"Fight.xml");
			
			root = xml.parse(fileManager.getFile());
			
			Array<Element> arrayOfPlayer =	root.getChildrenByName("player_Fight");	
			for(int i = 0; i < arrayOfPlayer.size; i++)
			{
				//the player has been found
				if(arrayOfPlayer.get(i).getInt("num") == idplayer)
				{
					Name = arrayOfPlayer.get(i).getChildByName("name").getAttribute("value");
					setHpMax(Integer.parseInt(arrayOfPlayer.get(i).getChildByName("hpmax").getAttribute("value")));
					imagePath = arrayOfPlayer.get(i).getChildByName("imagePath").getAttribute("value");
					Hp = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("hp").getAttribute("value"));
					Def = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("def").getAttribute("value"));
					Atk = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("atk").getAttribute("value"));
					Exp = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("exp").getAttribute("value"));					
					Level = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("level").getAttribute("value"));
					bagSpace = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("bag").getAttribute("space"));
					Class = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("class").getAttribute("value"));
					
					weaponEquipped = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("weaponEquip").getAttribute("value"));
					armorEquipped = Integer.parseInt(arrayOfPlayer.get(i).getChildByName("armorEquip").getAttribute("value"));
					
					 bag = new ArrayList<PartyHard_Object>();
					 capacity = new ArrayList<PartyHard_Capacity>();				
					 
					 Array<Element> items =	arrayOfPlayer.get(i).getChildByName("bag").getChildrenByName("item");
					 
					 //if there is items
					 if(items.size > 0)					 
					//populating the inventory
					for(int p = 0; p != items.size; p++)
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
					
					Element rootCap = xml.parse(Gdx.files.local("data/Capacity.xml"));
					
					Array<Element> arrayOfCap =	rootCap.getChildrenByName("capacity");
					Array<Element> arrayPlayerCap = arrayOfPlayer.get(i).getChildByName("capacity").getChildrenByName("cap");
					
					//looking first in capacity and then checking if it exist in playerxml					
					for(int y = 0; y < arrayOfCap.size; y++)
					{
											
						for(int m = 0; m < arrayPlayerCap.size; m++)
						{
							
							if(y == arrayPlayerCap.get(m).getInt("id"))
							{																
								capacity.add(new PartyHard_Capacity(y)); 							
							}
													
						}
						
					}
					
					rootCap = xml.parse(Gdx.files.local("data/Level.xml"));
					
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
								int expToUp = arrayOfPlayerLevel.get(Level - 1).getInt("exp");
								int hp = arrayOfPlayerLevel.get(Level - 1).getInt("hp");
								int atk = arrayOfPlayerLevel.get(Level - 1).getInt("atk");
								int def = arrayOfPlayerLevel.get(Level - 1).getInt("def");
								
								//the character gain a cap when he level up
								if(arrayOfPlayerLevel.get(Level - 1).getInt("cap") != -1)								
									levelUp = new PartyHard_Level(this.Class, expToUp, hp, atk, def, new PartyHard_Capacity(arrayOfPlayerLevel.get(Level - 1).getInt("cap")));	
								else
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

	public PartyHard_Player_Fight(String Name, int Class, String imagePath)
	{
		this.Name = Name;
		this.Class = Class;
		this.imagePath = imagePath;
		
		switch(Class)
		{
			case 0://warrior
				HpMax = 50;
				Hp = 50;
				Atk = 10;
				Def = 10;
				break;
				
			case 1://Clerk
				HpMax = 40;
				Hp = 40;
				Atk = 5;
				Def = 10;			
				break;
		}
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
	
	public void addAtk(int amount)
	{
		this.Atk += amount;
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
	
	public void addLife(int amount)
	{
		Hp += amount;
		HpMax += amount;
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
	
	public void removeObjectFromInventory(int index)
	{
			
		//checkig if the object was equipped, if equipped setting back to default the slot, - 1 because of size()
		if(index == getItemIndex(weaponEquipped) )
		{
			weaponEquipped = -1;
		}
			
		else if(index == getItemIndex(armorEquipped))
			armorEquipped = -1;
		
		bag.remove(index);				
	}
	
	public void useObject()
	{
		bag.remove(objectUsed);
		
	}
	/**
	 * @param type  0 - Wep, 1 - Armor
	 * @return True if item equipped on this slot
	 */
	public boolean isEquipSlot(int type)
	{
		switch(type)
		{
			case 0: // weapon
				if(weaponEquipped != -1)
					return true;
				else
					return false;				
			case 1: // armor
				if(armorEquipped != -1)
					return true;
				else
					return false;				
				 	
		}
		//nothing has been found
		return false;
	}
	
	public void unequipItem(int itemindex)
	{
		switch(getItemType(itemindex))
		{
			case 0://wep				
				Atk -=	((PartyHard_Weareable) bag.get(getItemIndex(weaponEquipped))).getAmount();				
				weaponEquipped = -1;
				break;
			case 1: //armor
				Def -= ((PartyHard_Weareable) bag.get(getItemIndex(armorEquipped))).getAmount();
				armorEquipped = -1;
				break;				
		}
	}
	
	/**
	 * @param type The type of the item
	 * @param itemIndex Index of the item in the bag
	 */
	public void setEquipSlot(int type, int itemIndex)
	{
		switch(type)
		{
			case 0: // weapon
				weaponEquipped = itemIndex;					
				//applying the bonus
				Atk += ((PartyHard_Weareable) bag.get(weaponEquipped)).getAmount();
				break;					
			case 1: // armor
				armorEquipped = itemIndex;
				Def += ((PartyHard_Weareable) bag.get(armorEquipped)).getAmount();
				break;				 	
		}
	}
	
	/**
	 * @param itemIndex Index of the item in the bag
	 */
	public void setEquipSlot(int itemIndex)
	{
		switch(getItemType(itemIndex))
		{
			case 0: // weapon
				weaponEquipped = bag.get(itemIndex).getItemId();	
				Atk += ((PartyHard_Weareable) bag.get(itemIndex)).getAmount();
				break;					
			case 1: // armor
				armorEquipped = bag.get(itemIndex).getItemId();
				Def += ((PartyHard_Weareable) bag.get(itemIndex)).getAmount();
				break;					
		}
	}
	/**
	 * @param id The id of the item
	 * @return -1 if not found 
	 */
	public int getItemIndex(int id)
	{
		for(int i = 0; i < bag.size(); i++)
		{						
			if(bag.get(i).getItemId() == id)
				return i;
		}
		
		return -1;
	}
	
	public int getItemType(int itemIndex)
	{
		System.out.println(bag.get(itemIndex).Name+"name");
		
		return bag.get(itemIndex).type;
	}
	
	private void loadCapacity(int id)
	{
		XmlReader xml = new XmlReader();
		Element rootCap = null;
		try {
			rootCap = xml.parse(Gdx.files.internal("data/Capacity.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Array<Element> arrayOfCap =	rootCap.getChildrenByName("capacity");
		
		//looking first in capacity and then checking if it exist in playerxml					
		for(int y = 0; y < arrayOfCap.size; y++)
		{												
				if(y == id)
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
	
	public int getIdSave() {
		return idSave;
	}

	public void setIdSave(int idSave) {
		this.idSave = idSave;
	}

	public void save(int id)
	{
		try {
			/*
			 * Saving the player 
			 */		
			
			fileManager.loadFile("save/"+idSave+"Fight.xml");
			FileHandle file = fileManager.getDecodedFile();
				
			 StringWriter stringwriter = new StringWriter();
			 XmlWriter xml = new XmlWriter(stringwriter);	
				
					xml.element("player_Fight").attribute("num", id)	       
	                .element("name").attribute("value",  Name).pop()
	                .element("imagePath").attribute("value",  imagePath).pop()
	                .element("class").attribute("value",  getclass()).pop()
	                .element("hpmax").attribute("value",  getHpMax()).pop()
	                .element("hp").attribute("value",  getLife()).pop()
	                .element("def").attribute("value",  getDef()).pop()
	                .element("atk").attribute("value",  getAtk()).pop()
	                .element("exp").attribute("value",  getExp()).pop()
	                .element("level").attribute("value",  getLevel()).pop()
	                .element("weaponEquip").attribute("value",  weaponEquipped).pop()
	                .element("armorEquip").attribute("value",  armorEquipped).pop()
	                .element("bag").attribute("space",  getBagSpace());
					
					//populating the bag
					for(int p = 0; p <  bag.size(); p++)
					{
						xml.element("item").attribute("type",  bag.get(p).type);	
						
						switch(bag.get(p).type)
		        		{
		        			case 0: //weapon
		        				PartyHard_Weapon wep = (PartyHard_Weapon) bag.get(p);
		        				xml.attribute("id", wep.getId()).pop();
		        			break;
		        			case 1: //armor
		        				PartyHard_Armor armor = (PartyHard_Armor) bag.get(p);
		        				xml.attribute("id", armor.getId()).pop();
		        			break;
		        			case 2: //potion
		        				PartyHard_Potion pot = (PartyHard_Potion) bag.get(p);
		        				xml.attribute("id", pot.getId()).pop();
		        			break;
		        		}
					}
					xml.pop();//closing the bag
					xml.element("capacity").attribute("value", capacity.size());
					
					for(int p = 0; p < capacity.size(); p++)
					{
						xml.element("cap").attribute("id", capacity.get(p).id).pop();				
					}
					//closing cap and then the player
					xml.pop();
					xml.pop();
					         
					xml.close();
			
				   file.writeString(stringwriter.toString(), true);
				   
				   fileManager.saveFile(true, file);
	}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	
	}
	
	public void prepareForSave()
	{					
		fileManager.loadFile("save/"+idSave+"Fight.xml");
		
		FileHandle file = fileManager.getDecodedFile();

		file.delete();
		
		file.writeString("<root>", true);
		
		fileManager.saveFile(true, file);
	}
	
	public void finishFile()
	{
		fileManager.loadFile("save/"+idSave+"Fight.xml");
		 
		FileHandle file = fileManager.getDecodedFile();
		
		 file.writeString("</root>", true);
		 
		 fileManager.saveFile(true, file);
	}
	
}
