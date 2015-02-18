package com.mygdx.game;

import java.util.ArrayList;

import utils.SaveManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.actor.PartyHard_Monster;
@SuppressWarnings("unused")
public class PartyHard_GameClass extends Game {
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public SaveManager save;
	
	@Override
	public void create() {		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//PartyHard_MainScreen screensplash = new PartyHard_MainScreen(this,"mainlevel");
		//setScreen(screensplash);
		
		save = new SaveManager("monster.game");
		
		ArrayList<PartyHard_Monster> monsters = new ArrayList<PartyHard_Monster>();
		 try {
			monsters.add(save.loadMonster("goblin"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 ArrayList<PartyHard_Player>
		
		PartyHard_Fight fight = new PartyHard_Fight(null, monsters, null, this);
		setScreen(fight);
		
	}

	@Override
	public void dispose() {
		super.dispose();
		
	}

	@Override
	public void render() {	
		super.render();
	Gdx.gl.glClearColor(0, 0, 0, 1);
			
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
