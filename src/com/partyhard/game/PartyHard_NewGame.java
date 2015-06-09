package com.partyhard.game;

import java.io.OutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.actor.PartyHard_Player_Map;

public class PartyHard_NewGame implements Screen {	
	private Stage stage = new Stage();
	
	private PartyHard_GameClass game;
	private PartyHard_ScreenSplash screenSplash;
	
	private ArrayList<PartyHard_Player_Fight> playerFight = new ArrayList<PartyHard_Player_Fight>();
	
	private NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/menu.9.png"))));	
	private NinePatchDrawable drawable = new NinePatchDrawable(patch);
	
	private LabelStyle labelStyle = new LabelStyle();
	private TextFieldStyle textStyle = new TextFieldStyle();
	private ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
	private SelectBoxStyle selectStyle = new SelectBoxStyle();
	private ListStyle listStyle = new ListStyle();	
	private TextButtonStyle buttonStyle = new TextButtonStyle();

	private Image characterImage = new Image();
	
	private int numberOfCharacter = -1;
	private int indexOfCharacter = 0;
	private boolean isMale = true;
	
	private int numberOfPlayer = 0;
	private int numberMaxOfPlayer = 3;

	private Skin skin;
	
	public PartyHard_NewGame(PartyHard_GameClass game, PartyHard_ScreenSplash screenSplash)
	{
		this.game = game;
		this.screenSplash = screenSplash;
		
		labelStyle.font = new BitmapFont();
		
		textStyle.font = new BitmapFont();
		textStyle.fontColor = Color.BLACK;		
			
		scrollPaneStyle.background = drawable;
		
		listStyle.font = new BitmapFont();
		listStyle.fontColorSelected = Color.BLACK;
		listStyle.fontColorUnselected = Color.WHITE;
		listStyle.selection = drawable;
		
		selectStyle.font = new BitmapFont();
		selectStyle.fontColor = Color.BLUE;
		selectStyle.scrollStyle = scrollPaneStyle;
		selectStyle.listStyle = listStyle;
		
		TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
		skin = new Skin(tex);
		
		buttonStyle.up = skin.getDrawable("button.up");
		buttonStyle.down = skin.getDrawable("button.down");
		
		buttonStyle.pressedOffsetX = 1;
		buttonStyle.pressedOffsetY = -1;		
		buttonStyle.font = new BitmapFont();
		
	}
	
	
	@Override
	public void show() {
	Gdx.input.setInputProcessor(stage);
	
	Image background = new Image(new Texture(Gdx.files.internal("battleground.png")));
	
	background.setPosition(0, 0);
	background.setFillParent(true);
	
	Label welcome = new Label("Welcome to Party Hard!", labelStyle);
	welcome.setFontScale(2);
	welcome.setWidth(welcome.getWidth() * 2);
	welcome.setPosition(stage.getWidth() / 2 - welcome.getWidth() / 2, stage.getHeight() - welcome.getHeight());
	
	Table newPlayer = new Table();
	newPlayer.setName("newPlayer");
	newPlayer.center();
	
	newPlayer.setBackground(drawable);
	
	//used for store the image of the character, and update it easily
	Table imgCharacter = new Table();
	imgCharacter.setName("imgCharacter");
	
	//used for the label playerNumber
	Table numberPlayer = new Table();
	numberPlayer.setName("numberPlayer");
	
	Label numPlayer = new Label("Number of Player created: "+numberOfPlayer, labelStyle);
	numPlayer.setPosition(welcome.getX() , welcome.getY() - 100);
	
	/*
	 * TO DO create the player (asking the name) TextBox center screen, maybe do an introduction
	 */
	
	//load the number of image for the character
	loadNumberOfCharacter();
	
	
	TextField newName = new TextField("", textStyle);
	newName.setPosition(stage.getWidth() / 2 - newName.getWidth() / 2, stage.getHeight() / 2);
	
	
	newName.setMessageText("Enter a Name");
	
	newName.setMaxLength(10);
	newName.setBlinkTime(1f);
	
	Label lblName = new Label("Name:", labelStyle);
	lblName.setPosition(newName.getX() - lblName.getWidth(), newName.getY());
	
	
	SelectBox<Label> newClass = new SelectBox<Label>(selectStyle);
	
	//adding the classes manually, maybe make the loading dynamic 	
	Label warrior = new Label("0", labelStyle);
	warrior.setName("Warrior");
	
	Label clerck = new Label("1", labelStyle);
	clerck.setName("Clerck");
	
	newClass.getItems().add(warrior);
	newClass.getItems().add(clerck);
		
	
	newClass.setPosition(newName.getX(), newName.getY() - newClass.getHeight());
	newClass.setWidth(100);
	newClass.setSelectedIndex(0);
	
	Label lblClass = new Label("Choose a class:", labelStyle);
	lblClass.setPosition(newClass.getX() - lblClass.getWidth(), newClass.getY());
	
	//load the first character (Male)
	loadImage(0,isMale);
	characterImage.setPosition(newName.getX() + newName.getWidth() + 30, newName.getY());
	
	
	Image leftArrow = new Image(new Texture("ui_button/arrowLeft.png"));
	leftArrow.setPosition(characterImage.getX() - 20, characterImage.getY());
	
	leftArrow.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			//if there is a way to go backward
			if(indexOfCharacter != 0)
			{
				indexOfCharacter--;
				loadImage(indexOfCharacter, isMale);
			}
				
		}
	});
	
	Image rightArrow = new Image(new Texture("ui_button/arrowRight.png"));
	rightArrow.setPosition(characterImage.getX() + characterImage.getWidth(), characterImage.getY());
	
	rightArrow.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {				
			
			//if there is a way to go backward
			if(indexOfCharacter != numberOfCharacter)
			{
				indexOfCharacter++;
				loadImage(indexOfCharacter, isMale);
			}
				
		}
	});
	
	Label male = new Label("Male", labelStyle);
	male.setPosition(characterImage.getX(), characterImage.getY() - male.getHeight());
	
	male.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			isMale = true;	
			loadImage(indexOfCharacter, isMale);
		}
	});
	
	Label female = new Label("Female", labelStyle);
	female.setPosition(characterImage.getX() + female.getWidth(), characterImage.getY() - female.getHeight());
	
	female.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			isMale = false;		
			loadImage(indexOfCharacter, isMale);
		}
	});
	
	
	TextButton back = new TextButton("Back", buttonStyle);
	back.setWidth(stage.getWidth() / 3);
	back.setPosition(0, 0);
	
	back.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			game.setScreen(screenSplash);
		}
	});
	
	TextButton next = new TextButton("Next", buttonStyle);
	next.setWidth(back.getWidth());
	next.setPosition(back.getX() + next.getWidth(), back.getY());
	
	next.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(numberOfPlayer != numberMaxOfPlayer)
			{
				TextField Name = (TextField) ((Table) stage.getActors().items[getTableIndex("newPlayer")]).getChildren().get(6);
				if(Name.getText() != "" && !Name.getText().isEmpty())
				{
					SelectBox<Label> Class = (SelectBox<Label>) ((Table) stage.getActors().items[getTableIndex("newPlayer")]).getChildren().get(7);							
					
					if(isMale)
						playerFight.add(new PartyHard_Player_Fight(Name.getText(), Class.getSelectedIndex(),"player/"+indexOfCharacter+"m.png"));
					else
						playerFight.add(new PartyHard_Player_Fight(Name.getText(), Class.getSelectedIndex(),"player/"+indexOfCharacter+"f.png"));
					
					indexOfCharacter = 0;
					loadImage(indexOfCharacter, isMale);
					numberOfPlayer++;
					updateNumberOfPlayer();
				}
				
				if(numberOfPlayer > 1)
				{
					//if the button confirm doesn't exists
					if(getTableIndex("tableConfirm") == -1)
					{
						Table tableConfirm = new Table();
						tableConfirm.setName("tableConfirm");
						
						TextButton confirm = new TextButton("Confirm", buttonStyle);
						confirm.setWidth(stage.getWidth() / 3);
						confirm.setPosition(stage.getWidth() - confirm.getWidth(), 0);
						
						//when click: first save and then go
						confirm.addListener(new ClickListener(){
							@Override
							public void clicked(InputEvent event, float x, float y) {
								//getting the id for the save
								int id = getNextSaveId();
								
								//creating the files
								FileHandle file = Gdx.files.local("save/"+id+"Fight.xml");								
								file.write(false);
								file = Gdx.files.local("save/"+id+"Map.xml");	
								file.write(true);
								
								//prepare the file for saving
								playerFight.get(0).setIdSave(id);
								playerFight.get(0).prepareForSave();
								
								for(int i = 0; i < playerFight.size(); i++)
								{						
									playerFight.get(i).setIdSave(id);
									playerFight.get(i).save(i);
								}
								
								//close the xml
								playerFight.get(0).finishFile();
								
								//creating the playermap and saving it
								PartyHard_Player_Map playerMap = new PartyHard_Player_Map(192, 192, playerFight.get(0).imagePath, id);
								playerMap.setMap("mainlevel", true);
								playerMap.setMoney(500);
								
								playerMap.save();
								
								PartyHard_MapScreen map = new PartyHard_MapScreen(game, "mainlevel", playerMap, id);							
								game.setScreen(map);
							
							}
						});
						
						
						tableConfirm.addActor(confirm);
						
						stage.addActor(tableConfirm);
					}
				}
				
			}
			
		}		
	});
	
	
	TextButton delete = new TextButton("Delete Character", buttonStyle);
	delete.setPosition(lblClass.getX(), lblClass.getY() - delete.getHeight());
	
	delete.addListener(new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(numberOfPlayer != 0)
			{
				playerFight.remove(numberOfPlayer - 1);
				numberOfPlayer--;
				updateNumberOfPlayer();				
			}
		}
	});
	
	
	newPlayer.addActor(male);
	newPlayer.addActor(female);
	newPlayer.addActor(rightArrow);
	newPlayer.addActor(leftArrow);
	newPlayer.addActor(lblClass);
	newPlayer.addActor(lblName);
	newPlayer.addActor(newName);
	newPlayer.addActor(newClass);
	newPlayer.addActor(back);
	newPlayer.addActor(next);
	newPlayer.addActor(delete);
	
	imgCharacter.addActor(characterImage);
	
	numberPlayer.addActor(numPlayer);
	
	stage.addActor(background);
	stage.addActor(newPlayer);
	stage.addActor(welcome);	
	stage.addActor(imgCharacter);
	stage.addActor(numberPlayer);
	
	stage.setDebugAll(true);

	}
	/**
	 * 
	 * @param id The id of the player image 
	 */
	public void loadImage(int id, boolean isMale)
	{
		 Texture walkSheet = null;
		if(isMale)
		  walkSheet = new Texture(Gdx.files.internal("player/"+id+"m.png")); 
		else
		  walkSheet = new Texture(Gdx.files.internal("player/"+id+"f.png")); 
		
		 TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/4, walkSheet.getHeight()/4);  	
			 
		 
		 //storing the pos for updating after constructor
		 float x = characterImage.getX();
		 float y = characterImage.getY();
		 
		 characterImage = new Image(tmp[0][0]);
		 characterImage.setPosition(x, y);
		 
		 //updating the imgCharacter
		 if(getTableIndex("imgCharacter") != -1)
		 {
			 Table newTable = (Table) stage.getActors().get(getTableIndex("imgCharacter"));
			 newTable.getChildren().clear();
			 
			 newTable.addActor(characterImage);
			 
			 stage.getActors().items[getTableIndex("imgCharacter")] = newTable;
		 }
		
	}
	
	public void loadNumberOfCharacter()
	{
		//because libgdx store file in the android project, so the directory needed to be indicate like this
		FileHandle file = Gdx.files.internal("./bin/player/");			
		
		for(int i = 0; i < file.list().length; i+=2)
		{							
			numberOfCharacter++;			
		}	
	}

	private void updateNumberOfPlayer() {
		
		if(getTableIndex("numberPlayer") != -1)
		{
			Table newTable = (Table) stage.getActors().items[getTableIndex("numberPlayer")];			
			((Label) newTable.getChildren().get(0)).setText("Number of Player created: "+numberOfPlayer);
			
			stage.getActors().items[getTableIndex("numberPlayer")] = newTable;
			
			//checking if there is enough player to go, if not delete the confirm button
			if(numberOfPlayer < 2)
			{
				if(getTableIndex("tableConfirm") != -1)
				{
					stage.getActors().removeIndex(getTableIndex("tableConfirm"));
				}
			}
			
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);        
        
        
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
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
	}
	
	/**
	 * @param name The name of the table
	 * 
	 * @return The index of the Table
	 */
	private int getTableIndex(String name)
	{
		for(int i = 0; i < stage.getActors().size; i++)
		{
			if(stage.getActors().get(i).getName() == name)
				return i;
		}	
		return -1;
	}
	
	private int getNextSaveId()
	{
		FileHandle save = Gdx.files.internal("save/");
		
		System.out.println("asdasd"+save.list().length/2);
		
		return save.list().length/2;
	}
}
