package utils;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.partyhard.object.PartyHard_Armor;
import com.partyhard.object.PartyHard_Object;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;

public class PartyHard_Shop {
	public String Name;
	public String description;
	
	public int x;
	public int y;
	
	ArrayList<PartyHard_Object> merch = new ArrayList<PartyHard_Object>();
	/**
	 * 
	 * @param id The id of the Shop
	 * @param Map The map where the shop is
	 */
	public PartyHard_Shop(int id, String Map)
	{
		/*
		 * TO DO load from xml, think about load all the items for easier adding
		 */
		
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		try {
			root = xml.parse(Gdx.files.internal("Shop.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		//getting the shop element
		Element arrayOfShop = root.getChildByName(Map).getChildByName("shop");
		
		//getting pos of the shop
		x = arrayOfShop.getInt("x");
		y = arrayOfShop.getInt("y");
		
		description = arrayOfShop.get("description");
		
		Array<Element> items = arrayOfShop.getChildrenByName("item");
		
		//populating the shop
		for(int i = 0; i < items.size; i++)
		{						
			switch(items.get(i).getInt("type"))
			{
				case 0: //wep
					merch.add(new PartyHard_Weapon(items.get(i).getInt("id")));
					break;
					
				case 1: //armor
					merch.add(new PartyHard_Armor(items.get(i).getInt("id")));
					break;
					
				case 2: //potion
					merch.add(new PartyHard_Potion(items.get(i).getInt("id")));
					break;
			}
		}
	}
	/**
	 * 
	 * @param type The type of the item, 1 wep, 2 armor, 3 potion
	 * @param id The id of the object
	 */
	public void addItem(int type, int id)
	{
		
	}
	
	/**
	 * 
	 * @param index Index of the item
	 * @return -1 if not found
	 */
	
	public int getItemIndex(int index)
	{
		return -1;
	}
	
}
