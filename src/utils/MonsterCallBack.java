package utils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

public class MonsterCallBack implements TweenCallback {

	public int idMonster;
	
	public MonsterCallBack( int idMonster) {
		
		this.idMonster = idMonster;
	}

	@Override
	public void onEvent(int arg0, BaseTween<?> arg1) {
		// TODO Auto-generated method stub
		
	}

	

}
