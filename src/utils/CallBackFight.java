package utils;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.audio.Music;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.game.PartyHard_Fight;
import com.partyhard.game.PartyHard_GameClass;
import com.partyhard.game.PartyHard_MapScreen;


public class CallBackFight implements TweenCallback{

	private String battleMusic;
	private PartyHard_GameClass mainGame;
	private PartyHard_MapScreen map;
	private ArrayList<PartyHard_Monster> monsters;
	private ArrayList<PartyHard_Player_Fight> players;
	private Music mapMusic;
	private boolean isBoss = false;
	
	
	public CallBackFight(ArrayList<PartyHard_Player_Fight>players, ArrayList<PartyHard_Monster> monsters,PartyHard_MapScreen map, PartyHard_GameClass mainGame, String battleMusic, Music mapMusic, boolean isBoss)
	{
		this.players = players;
		this.monsters = monsters;
		this.map = map;
		this.mainGame = mainGame;
		this.battleMusic = battleMusic;
		this.mapMusic = mapMusic;
		this.isBoss  = isBoss;
	}
	
	@Override
	public void onEvent(int event, BaseTween<?> arg1) {
		
		switch(event)
		{
			case TweenCallback.COMPLETE:
				mapMusic.dispose();
				PartyHard_Fight fight = new PartyHard_Fight(players, monsters, map, mainGame, battleMusic, isBoss);
				mainGame.setScreen(fight);
				break;
		}
	}

}
