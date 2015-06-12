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
	
	private ArrayList<PartyHard_Object> merch = new ArrayList<PartyHard_Object>();
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
			root = xml.parse(Gdx.files.internal("data/Shop.xml"));
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
		switch(type)
		{
			case 0:
				merch.add(new PartyHard_Weapon(id));
				break;
			case 1:
				merch.add(new PartyHard_Armor(id));
				break;
			case 2:
				merch.add(new PartyHard_Potion(id));
				break;
		}
	}
	/**
	 * 
	 * @param index The index of the object in the shop
	 * @return The name of the object
	 */
	public String getObjectName(int index)
	{
		return merch.get(index).Name;
	}
	/**
	 * 
	 * @param index The index of the object in the shop
	 * @return The price of the given object
	 */
	public int getObjectPrice(int index)
	{
		return merch.get(index).price;
	}
	/**
	 * 
	 * @param type The type of the searched object
	 * @param index The index of the object in the shop
	 * @return -1 if not found
	 */
	public int getObjectAmount(int type, int index)
	{
		switch(type)
		{
			case 0:
				return ((PartyHard_Weapon) merch.get(index)).getAmount();
				
			case 1:
				return ((PartyHard_Armor) merch.get(index)).getAmount();
				
			case 2:
				return ((PartyHard_Potion) merch.get(index)).getAmount();
		}
		
		return -1;
	}
	/**
	 * 
	 * @param index The index of the object in the shop
	 * @return -1 if not found
	 */
	public int getObjectAmount(int index)
	{
		switch(getObjectType(index))
		{
			case 0:
				return ((PartyHard_Weapon) merch.get(index)).getAmount();
				
			case 1:
				return ((PartyHard_Armor) merch.get(index)).getAmount();
				
			case 2:
				return ((PartyHard_Potion) merch.get(index)).getAmount();
		}
		
		return -1;
	}
	/**
	 * @param index The index of the object in the shop
	 * @return ATTENTION, not catching any not found error!
	 */
	public int getObjectType(int index)
	{
		return merch.get(index).type;
	}
	/**
	 * @param index The index of the object in the shop
	 * @return ATTENTION, not catching any not found error!
	 */
	public String getObjectDescription(int index)
	{
		return merch.get(index).description;
	}
	
	public PartyHard_Object getObject(int index)
	{
		return merch.get(index);
	}
	
	/**
	 * @return The size()
	 */
	public int getShopSize()
	{
		return merch.size();
	}
	
}
