package com.partyhard.object;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


public class PartyHard_Weapon extends PartyHard_Object implements PartyHard_Weareable{
	
	private int weaponType;
	public boolean equip = false;
	private int id;

	/*
	 * 0 sword
	 */
	private int amount = 0; //amount of atk if weapon or def if armor
	
	public PartyHard_Weapon(int id)
	{
		super("", "", "", 0, 0);

		
		this.setId(id);
			
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		try {
			root = xml.parse(Gdx.files.internal("data/Weapon.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		Array<Element> arrayOfItem = root.getChildrenByName("weapon");	
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
				super.price = Integer.parseInt(arrayOfItem.get(i).getAttribute("price"));
				
				/*
				 * load info for the weapon
				 */
							
				weaponType = Integer.parseInt(arrayOfItem.get(i).getAttribute("type"));
				amount = Integer.parseInt(arrayOfItem.get(i).getAttribute("amount"));
				
				//stop here the right weapon has been found, no nedd to go further
				break;
				
			}
		}			
	}


	public int getWeaponType() {
		return weaponType;
	}


	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public boolean isEquip() {
		return equip;
	}


	public void setEquip(boolean equip) {
		this.equip = equip;
	}


	@Override
	public boolean isEquiped(){
		return equip;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
}
