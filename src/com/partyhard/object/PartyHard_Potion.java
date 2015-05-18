package com.partyhard.object;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PartyHard_Potion extends PartyHard_Consumable{
	private int amount;
	private int potionType;
	/*
	 * 0 Health
	 */
	private int id;
	private int price;

	public PartyHard_Potion(int id) {
super("", "", "", 0);

		
		this.setId(id);
			
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		try {
			root = xml.parse(Gdx.files.internal("Potion.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		Array<Element> arrayOfItem = root.getChildrenByName("potion");	
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
				super.type = 2;
				
				/*
				 * load info for the weapon
				 */
				
				setPrice(Integer.parseInt(arrayOfItem.get(i).getAttribute("price")));
				setPotionType(Integer.parseInt(arrayOfItem.get(i).getAttribute("type")));
				amount = Integer.parseInt(arrayOfItem.get(i).getAttribute("amount"));
				
				//stop here the right weapon has been found, no nedd to go further
				break;
				
			}
		}	

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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

	public int getPotionType() {
		return potionType;
	}

	public void setPotionType(int potionType) {
		this.potionType = potionType;
	}

}
