package com.partyhard.actor;

import java.util.Random;

import utils.FileManager;

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
	
	public int damage = 0;
	
	private boolean Dead = false;
	
	private boolean isBoss = false;
	
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
		
			//loading the encrypted file
			FileManager fileManager = new FileManager("monster.xml");
			
			root = xml.parse(fileManager.readFile().readString());//loading the root
			//loading the monster
			Element monsterRoot = root.getChildByName(monster);
			
			imagePath = monsterRoot.getChildByName("imagepath").get("path");
			
			super.set(new Sprite(new Texture(Gdx.files.internal(imagePath))));
			super.scale(2f);
			
			//calculating all the stats from the xml base stats
			
			actualHp = r.nextInt(monsterRoot.getChildByName("Hp").getInt("hpMax")) + monsterRoot.getChildByName("Hp").getInt("hpMin");
			
			actualAtk =  r.nextInt(monsterRoot.getChildByName("Atk").getInt("atkMax")) + monsterRoot.getChildByName("Atk").getInt("atkMin");
			
			actualDef =  r.nextInt(monsterRoot.getChildByName("Def").getInt("defMax")) + monsterRoot.getChildByName("Def").getInt("defMin");
			
			actualExp =  r.nextInt(monsterRoot.getChildByName("Exp").getInt("expMax")) +  monsterRoot.getChildByName("Exp").getInt("expMin");
			
			isBoss = monsterRoot.getChildByName("isBoss").getBoolean("value");				
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

	public void setDamageTaken(int damage) {
		int dmg = damage - actualDef;
		
		if(dmg < 0)
			dmg = 1;
		
		this.damage += damage;
	}

	public boolean isDead() {
		return Dead;
	}

	public void setDead(boolean dead) {
		Dead = dead;
	}

	public boolean isBoss() {
		return isBoss;
	}

	public void setBoss(boolean isBoss) {
		this.isBoss = isBoss;
	}
	
	
}
