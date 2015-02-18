package com.mygdx.actor;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



public class PartyHard_Monster extends Sprite implements Serializable{
	


	private static final long serialVersionUID = 1L;
	
	public String Name;
	public String imagePath;
	
	/*
	 * TO DO: range of propreties e.g.: max atk min atk
	 */
	
	public int MaxHp;
	public int MinHp;
	public int actualHp;
	
	public int MaxAtk;
	public int MinAtk;
	public int actualAtk;
	
	public int MaxDef;
	public int MinDef;
	public int actualDef;
	
	public int MaxExp;
	public int MinExp;
	public int actualExp;
	
	
	public PartyHard_Monster(String monster, String pathImage, int Maxhp, int Minhp, int Maxatk,int Minatk, int Maxdef, int Mindef, int Maxexp, int Minexp) {
		Name = monster;
		imagePath = pathImage;
		
		super.set(new Sprite(new Texture(Gdx.files.internal(pathImage))));
		
		MaxHp = Maxhp;
		MinHp = Minhp;
		
		MaxAtk = Maxatk;
		MinAtk = Minatk;
		
		MaxDef = Maxdef;
		MinDef= Mindef;
		
		MaxExp = Maxexp;
		MinExp = Minexp;
	}
	
	public PartyHard_Monster(String nameOfTheMonsterThatWillBeLoaded)
	{
		this.Name = nameOfTheMonsterThatWillBeLoaded;
	}

}
