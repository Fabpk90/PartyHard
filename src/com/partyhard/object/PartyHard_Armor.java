package com.partyhard.object;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PartyHard_Armor extends PartyHard_Object implements PartyHard_Weareable {

	/*
	 * 0 - chest
	 * 1 - head
	 * 2 - foot
	 * 3 - arms
	 */
	
	private int amount;
	private boolean equip = false;
	private int price;
	private int armorType;
	private int id;
	public PartyHard_Armor(int id) {
super("", "", "", 0);

		
		this.setId(id);
			
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		try {
			root = xml.parse(Gdx.files.internal("Armor.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		Array<Element> arrayOfItem = root.getChildrenByName("armor");	
		for(int i = 0; i < arrayOfItem.size; i++)
		{
			//weapon found
			if(arrayOfItem.get(i).getInt("id") == id)
			{
				String Name = arrayOfItem.get(i).getAttribute("name");
				String imagePath = arrayOfItem.get(i).getAttribute("imagePath");
				String description = arrayOfItem.get(i).getAttribute("description");
				
				/*
				 * Act like the super constructor
				 */			
				super.Name = Name;
				super.imagePath = imagePath;
				super.description = description;
				super.type = 0;
				
				/*
				 * load info for the weapon
				 */
				
				setPrice(Integer.parseInt(arrayOfItem.get(i).getAttribute("price")));
				armorType = Integer.parseInt(arrayOfItem.get(i).getAttribute("type"));
				amount = Integer.parseInt(arrayOfItem.get(i).getAttribute("amount"));
				
				//stop here the right weapon has been found, no need to go further
				break;
				
			}
		}	
	}

	@Override
	public boolean isEquiped() {
		return equip;
	}

	@Override
	public void setEquip(boolean equip) {
		this.equip = equip;

	}

	@Override
	public int getAmount() {
		return amount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
