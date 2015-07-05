package utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PartyHard_Capacity {
	public String Name;
	public int percent;
	public boolean isHeal;
	public int amount;
	public int cost;
	public int id; //used for identifying the cap in xml
	public String description;
	
	public int job;
	/*
	 * 0 warrior
	 * 1 Clerk 
	 */
	
	public static int PUNCH = 0;
	public static int HEAL = 1;
	public static int DOUBLEKICK = 2;
	
	public PartyHard_Capacity(String Name,String description, int percentOfSucceed, boolean isHeal, int amount, int manaCost, int job, int id)
	{
		
		this.Name = Name;
		percent = percentOfSucceed;
		this.isHeal = isHeal;
		this.amount = amount;
		this.cost = manaCost;
		this.job = job;
		this.id = id;
		this.description = description;
	}

	public PartyHard_Capacity(int id) {
	
		this.id = id;
			
		XmlReader xml = new XmlReader();
		
		Element root = null;
		
		try {
			root = xml.parse(Gdx.files.internal("data/Capacity.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//loading the root
		
		Array<Element> arrayOfCap = root.getChildrenByName("capacity");	
		for(int i = 0; i < arrayOfCap.size; i++)
		{
			//cap found
			if(arrayOfCap.get(i).getInt("id") == id)
			{
				
				
				Name = arrayOfCap.get(i).getChildByName("name").getAttribute("value");
				percent = arrayOfCap.get(i).getChildByName("percent").getIntAttribute("value");
				isHeal = arrayOfCap.get(i).getChildByName("isheal").getBooleanAttribute("value");
				amount = arrayOfCap.get(i).getChildByName("power").getIntAttribute("value");
				cost = arrayOfCap.get(i).getChildByName("mana").getIntAttribute("value");
				job = arrayOfCap.get(i).getChildByName("job").getIntAttribute("value");
				description = arrayOfCap.get(i).getChildByName("description").getAttribute("value");										
				//stop here, the right cap has been found, no need to go further
				break;
				
			}
		}	
		
	}
}
