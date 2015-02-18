package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.actor.PartyHard_Actor;
import com.mygdx.actor.PartyHard_Monster;

public class PartyHard_Fight implements Screen{
	
	private Screen mapscreen;
	
	private SpriteBatch batch; 
    private Stage stage;
	private Skin skin;
	
	private Table tableFirst;
	
	
	private Sound backgroundmusic;
	
	private OrthographicCamera camera;

	private ArrayList<PartyHard_Actor> playerSquad;
	private ArrayList<PartyHard_Monster> enemySquad;
	
	private int showlife = 10;
	
	public PartyHard_GameClass game;
	
	public PartyHard_Fight(ArrayList<PartyHard_Actor> playerSquad, ArrayList<PartyHard_Monster> enemySquad, PartyHard_MainScreen mainScreen, PartyHard_GameClass game)
	{
	
		this.playerSquad = playerSquad;
		this.enemySquad = enemySquad;
		
		System.out.println(enemySquad.get(0).Name);
		
		mapscreen = mainScreen;
		
		this.game = game;
		/*
		 * Saving monsters
		 */
		
	}


	@Override
	public void show() {
		
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		tableFirst = new Table();
		stage = new Stage();
		
		Image background = new Image(new Texture(Gdx.files.internal("battleground.png")));
		
		background.setPosition(0, 0);
		background.setFillParent(true);
		
		TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
		
		skin = new Skin(tex);
		
		LabelStyle labelstyle = new LabelStyle();
		
		labelstyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"),Gdx.files.internal("font/font_0.png"), false);
		
		//creating table to put inside the label life of characters
		Table tableLife = new Table();
		
		
		//loading labels and setting a proper name to update life through function
		Label labelLifeFirstPlayer = new Label(""+playerSquad.get(0).getLife(), labelstyle);
		labelLifeFirstPlayer.setName(playerSquad.get(0).getName());
		
		Label labelLifeSecondPlayer = new Label(""+playerSquad.get(0).getLife(), labelstyle);
		labelLifeSecondPlayer.setName(playerSquad.get(1).getName());
	
		tableLife.addActor(labelLifeFirstPlayer);
		tableLife.addActor(labelLifeSecondPlayer);
		
		
		/*
		 * button style 
		 */
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		
		buttonStyle.up = skin.getDrawable("button.up");
		buttonStyle.down = skin.getDrawable("button.down");
		
		buttonStyle.pressedOffsetX = 1;
		buttonStyle.pressedOffsetY = -1;
		
		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLUE);
		font.setScale(1.5f);	
		
		buttonStyle.font = font; 
		
		/*
		 * list of actions
		 */
		
		final TextButton atk = new TextButton("atk!", buttonStyle);
		
			atk.pad(20);		
			atk.setSize(Gdx.graphics.getWidth()/2, 100);
			atk.setHeight(100);	

		
		 Gdx.audio.newSound(Gdx.files.internal("sound/battle_sound.mp3")).loop(.50f);
		

		
		final TextButton buttonFlee = new TextButton("Flee!", buttonStyle);
		
		buttonFlee.pad(20);
		
		buttonFlee.setSize(Gdx.graphics.getWidth()/2, 100);
		
		buttonFlee.setHeight(100);	
		

       /*
        * Creating button + table for the 2 state: 1 normal(before fight) 2 the fight
        */
		
		
      final TextButton buttonFight = new TextButton("Fight", buttonStyle);
      
      	buttonFight.pad(20);		
		buttonFight.setWidth(Gdx.graphics.getWidth()/2);
		buttonFight.setHeight(100);
		
		 buttonFight.addListener(new ClickListener(){
	            @Override 
	            public void clicked(InputEvent event, float x, float y){
	            	
	               stage.getActors().get(1).setVisible(true);
	               stage.getActors().get(2).setVisible(false);
	            }
	        });
		
       tableFirst.add(buttonFight).width(Gdx.graphics.getWidth() / 2);
       tableFirst.add(buttonFlee).width(Gdx.graphics.getWidth() / 2);
      
       
       tableFirst.right().bottom();
       tableFirst.setHeight(100);
       tableFirst.setWidth(Gdx.graphics.getWidth());
       
       
       
       final Table tableFight = new Table();       
       
       final TextButton buttonAtk = new TextButton("Atk!", buttonStyle);
       	buttonAtk.pad(20);		
		buttonAtk.setWidth(Gdx.graphics.getWidth()/2);
		buttonAtk.setHeight(100);
		
		final TextButton buttonBack = new TextButton("Back", buttonStyle);
		
		buttonBack.pad(20);		
		buttonBack.setWidth(Gdx.graphics.getWidth()/2);
		buttonBack.setHeight(100);
		
		buttonBack.addListener(new ClickListener(){
	            @Override 
	            public void clicked(InputEvent event, float x, float y){
	            	
	               stage.getActors().get(1).setVisible(false);
	               stage.getActors().get(2).setVisible(true);
	            }
	        });
	       
		tableFight.add(buttonAtk).width(Gdx.graphics.getWidth() / 2);
	    tableFight.add(buttonBack).width(Gdx.graphics.getWidth() / 2);
		
       tableFight.setVisible(false);
       tableFight.right().bottom();
       
       tableFight.setHeight(100);
       tableFight.setWidth(Gdx.graphics.getWidth());
       
 
   	labelLifeFirstPlayer.setPosition(tableFirst.getWidth(), tableFirst.getY());
   	
       
       stage.addActor(background);
       stage.addActor(tableFight);
       stage.addActor(tableFirst);
       stage.addActor(labelLifeFirstPlayer);
   
        Gdx.input.setInputProcessor(stage);		
        
	}


	@Override
	public void render(float delta) {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
	        batch.begin();	      
	        
	        stage.act(delta);
	        stage.draw();
	        
	      
	        
	        batch.end();
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		backgroundmusic.dispose();
	}
	
	
	private void loadMonsters()
	{
		FileHandle file = new FileHandle("monster/monster.json");// loading the monster file
		JsonReader jsonRead = new JsonReader();// object that helps to transform json into object
		Json json = new Json();
		
		for(int i = 0; i < enemySquad.size(); i++)
		{
			if(enemySquad.get(i).Name == jsonRead.parse(file).name)
			{
				//removing the old monster that had only a name by a new complete one
				enemySquad.remove(i);
				enemySquad.add(i, json.fromJson(PartyHard_Monster.class, file));
			}
			
		}
		
		/*
		 * TODO: generating random life atk def exp for monster by getting the range from the object
		 */
		
	}
	
}
