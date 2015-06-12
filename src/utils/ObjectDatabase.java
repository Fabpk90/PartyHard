package utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.partyhard.object.PartyHard_Armor;
import com.partyhard.object.PartyHard_Object;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;

public class ObjectDatabase {

	/*
	 * TO DO: load all the items (wep, arm , potion)
	 */
	
	public static final int WEAPON = 0;
	public static final int ARMOR = 1;
	public static final int POTION = 2;
	
	Array<PartyHard_Object> Objects = new Array<PartyHard_Object>();
	
	public ObjectDatabase()
	{
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		//first fetching armors		
		try {
			root = xml.parse(Gdx.files.internal("data/Armor.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		Array<Element> arrayOfItem = root.getChildrenByName("armor");	
		for(int i = 0; i < arrayOfItem.size; i++)
		{
			Objects.add(new PartyHard_Armor(i));
		}
		
		try {
			root = xml.parse(Gdx.files.internal("data/Weapon.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 arrayOfItem = root.getChildrenByName("weapon");	
		for(int i = 0; i < arrayOfItem.size; i++)
		{
			Objects.add(new PartyHard_Weapon(i));
		}
		
		try {
			root = xml.parse(Gdx.files.internal("data/Potion.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 arrayOfItem = root.getChildrenByName("potion");	
		for(int i = 0; i < arrayOfItem.size; i++)
		{
			Objects.add(new PartyHard_Potion(i));
		}
		
	}
	/**
	 * 
	 * @param type Type of the item
	 * @param id The id of the item
	 * @return -1 if not found
	 */
	public int getAmount(int type, int id)
	{
		for(int i = 0; i < Objects.size; i++)
		{
			if(Objects.get(i).type == type)
			{
				switch(type)
				{
					case WEAPON:
						return ((PartyHard_Weapon) Objects.get(i)).getAmount();
						
					case ARMOR:
						return ((PartyHard_Armor) Objects.get(i)).getAmount();
						
					case POTION:
						return ((PartyHard_Potion) Objects.get(i)).getAmount();
				}
			}
		}
		
		return -1;
	}
	/**
	 * 
	 * @param type Type of the item
	 * @param id The id of the item 
	 * @return null if not found
	 */
	public String getName(int type, int id)
	{
		for(int i = 0; i < Objects.size; i++)
		{
			if(Objects.get(i).type == type)
			{
				switch(type)
				{
					case WEAPON:
						return ((PartyHard_Weapon) Objects.get(i)).Name;
						
					case ARMOR:
						return ((PartyHard_Armor) Objects.get(i)).Name;
						
					case POTION:
						return ((PartyHard_Potion) Objects.get(i)).Name;
				}
			}
		}
		
		return null;
	}
	/**
	 * 
	 * @param type The type of the item
	 * @param id The id of the item
	 * @return -1 if not found
	 */
	public int getPrice(int type, int id)
	{
		for(int i = 0; i < Objects.size; i++)
		{
			if(Objects.get(i).type == type)
			{
				switch(type)
				{
					case WEAPON:
						return ((PartyHard_Weapon) Objects.get(i)).price;
						
					case ARMOR:
						return ((PartyHard_Armor) Objects.get(i)).getPrice();
						
					case POTION:
						return ((PartyHard_Potion) Objects.get(i)).getPrice();
				}
			}
		}
		
		return -1;
	}
	/**
	 * 
	 * @param type The type of the object
	 * @param id The id of the object
	 * @return The object or null if not found
	 */
	
	public PartyHard_Object  getItem(int type, int id)
	{
		for(int i = 0; i < Objects.size; i++)
		{
			if(Objects.get(i).type == type)
			{
				switch(type)
				{
					case WEAPON:
						return (PartyHard_Weapon)Objects.get(i);
						
					case ARMOR:
						return (PartyHard_Armor)Objects.get(i);
						
					case POTION:
						return (PartyHard_Potion)Objects.get(i);
				}
			}
		}
		
		return null;
	}
		
}
