package utils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MonsterCallBackTween implements TweenCallback {

	public int monsterIndex;
	public int monsterIndexTable;
	public Table monsterTable;
	public Table monsterName;
	
	public MonsterCallBackTween(int monsterIndex, int monsterIndexTable, Table monsterTable, Table monsterName) {
		
		this.monsterIndex = monsterIndex;
		this.monsterIndexTable = monsterIndexTable;
		this.monsterTable = monsterTable;
		this.monsterName = monsterName;
	}

	@Override
	public void onEvent(int arg0, BaseTween<?> arg1) {
		// TODO Auto-generated method stub
		
	}

	

}
