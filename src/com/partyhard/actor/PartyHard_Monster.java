package com.partyhard.actor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;



public class PartyHard_Monster extends Sprite {

	private static int id = 0;
	
	public String Name;
	public String imagePath;
	
	public int monsterId;
	
	public int actualHp;
	
	public int actualAtk;
	
	public int actualDef;
	
	public int actualExp;
	
	private int idTarget;
	
	private int damage = 0;
	
	private boolean Dead = false;
	
	public PartyHard_Monster(String monster) {
		
		//the id of the monster (used for the fight)
		monsterId = id;
		id++;
		
		//used for the name and searching the right monster(primary key = name)
		Name = monster;
		//used for randomizing the stats
		Random r = new Random();
		
		XmlReader xml = new XmlReader();
		
		Element root;
		try {
			root = xml.parse(Gdx.files.internal("monster.xml"));//loading the root
			//loading the monster
			Element monsterRoot = root.getChildByName(monster);
			
			imagePath = monsterRoot.getChildByName("imagepath").get("path");
			
			super.set(new Sprite(new Texture(Gdx.files.internal(imagePath))));
			super.scale(2f);
			
			//calculating all the stats from the xml base stats
			
			actualHp = r.nextInt(monsterRoot.getChildByName("Hp").getInt("hpMax") - monsterRoot.getChildByName("Hp").getInt("hpMin") + monsterRoot.getChildByName("Hp").getInt("hpMin"));
			
			actualAtk =  r.nextInt(monsterRoot.getChildByName("Atk").getInt("atkMax") - monsterRoot.getChildByName("Atk").getInt("atkMin") + monsterRoot.getChildByName("Atk").getInt("atkMin"));
			
			actualDef =  r.nextInt(monsterRoot.getChildByName("Def").getInt("defMax") - monsterRoot.getChildByName("Def").getInt("defMin") + monsterRoot.getChildByName("Def").getInt("defMin"));
			
			actualExp =  r.nextInt(monsterRoot.getChildByName("Exp").getInt("expMax") - monsterRoot.getChildByName("Exp").getInt("expMin") +  monsterRoot.getChildByName("Exp").getInt("expMin"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	/**
	 * @return the idTarget
	 */
	public int getIdTarget() {
		return idTarget;
	}

	/**
	 * @param idTarget the idTarget to set
	 */
	public void setIdTarget(int idTarget) {
		this.idTarget = idTarget;
	}

	public int getDamage() {
		return damage;
	}
	/*
	 * @param damage The damages that the monster takes
	 * @note Remember to check the state after dealing damages
	 */

	public void setDamage(int damage) {
		this.damage += damage;
		if(damage >= actualHp)
		{
			setDead(true);
			actualHp = 0;
		}
	}

	public boolean isDead() {
		return Dead;
	}

	public void setDead(boolean dead) {
		Dead = dead;
	}
}
