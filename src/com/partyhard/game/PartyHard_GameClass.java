package com.partyhard.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlWriter;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
@SuppressWarnings("unused")
public class PartyHard_GameClass extends Game {
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	@Override
	public void create() {		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		/*
		 * loading the fancy blue cursor
		 */
		Pixmap pm = new Pixmap(Gdx.files.internal("ui/cursorHand.png"));
		Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		/*
		ArrayList<PartyHard_Monster> monsters = new ArrayList<PartyHard_Monster>();
		 monsters.add(new PartyHard_Monster("blob"));
		 monsters.add(new PartyHard_Monster("bear"));
		 monsters.add(new PartyHard_Monster("champ"));
		 
		 ArrayList<PartyHard_Player_Fight> playerSquad = new ArrayList<PartyHard_Player_Fight>();
		 
		 playerSquad.add(new PartyHard_Player_Fight(0));
		 playerSquad.add(new PartyHard_Player_Fight(1));
		 
		 
		
		PartyHard_Fight fight = new PartyHard_Fight(playerSquad, monsters, null, this);
		setScreen(fight);
	
		*/
		
		PartyHard_ScreenSplash screensplash = new PartyHard_ScreenSplash(this);
		this.setScreen(screensplash);
		
		
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.exit();
		
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
