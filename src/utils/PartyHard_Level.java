package utils;


public class PartyHard_Level {

	public int clazz;
	public int expToUp;
	public int hp;
	public int atk;
	public int def;
	public PartyHard_Capacity cap = null;
	
	public PartyHard_Level(int clazz, int expToUp, int hp, int atk, int def,PartyHard_Capacity cap) {
		this.clazz = clazz;
		this.expToUp = expToUp;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.cap = cap;
	}
	
	public PartyHard_Level(int clazz, int expToUp, int hp, int atk, int def) {
		this.clazz = clazz;
		this.expToUp = expToUp;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
	}

}
