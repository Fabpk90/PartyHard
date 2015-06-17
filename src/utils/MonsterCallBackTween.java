package utils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

public class MonsterCallBackTween implements TweenCallback {

	public int idMonster;
	
	public MonsterCallBackTween( int idMonster) {
		
		this.idMonster = idMonster;
	}

	@Override
	public void onEvent(int arg0, BaseTween<?> arg1) {
		// TODO Auto-generated method stub
		
	}

	

}
