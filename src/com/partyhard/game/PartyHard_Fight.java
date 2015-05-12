package com.partyhard.game;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;

import utils.ImageAccessor;
import utils.LabelAccessor;
import utils.MonsterCallBackTween;
import utils.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.XmlWriter;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Fight;
import com.partyhard.object.PartyHard_Weapon;

public class PartyHard_Fight implements Screen {
	
	private PartyHard_MapScreen mapscreen;
	
	private SpriteBatch batch; 
    private Stage stage;
	private Skin skin;
	
	private Table tableFirst;
	
   	//creating table to put inside the label life of characters
	private  Table tableLife;
	
	private Table tableTurn;
		
	private Sound backgroundMusic;
	
	private ArrayList<PartyHard_Player_Fight> playerSquad;
	private ArrayList<PartyHard_Monster> enemySquad;
	
	private int turn = 0;
	
	private int playerAlive = 0;
	
	private int expgained = 0;
	
	private float scale = 1f;
	
	//used for printing what the player win (exp)
	private Dialog winDialog;
	
	private TextButtonStyle buttonStyle;
	private ListStyle listStyle;
	
	private LabelStyle labelstyle = new LabelStyle();
	
	private boolean win = false;
	private boolean loose = false;
	
	//used for animation
	private TweenManager tweenManager = new TweenManager();
	
	public PartyHard_GameClass game;
	
	private String musicPath;

	
	public PartyHard_Fight(ArrayList<PartyHard_Player_Fight> playerSquad, ArrayList<PartyHard_Monster> enemySquad, PartyHard_MapScreen mainScreen, PartyHard_GameClass game, String backgroundMusicPath)
	{
	
		this.playerSquad = playerSquad;
		this.enemySquad = enemySquad;
		
		mapscreen = mainScreen;
		
		this.game = game;
		TweenManager w = new TweenManager();
	
		for(int i = 0; i < playerSquad.size(); i++)
		{
			if(!playerSquad.get(i).isDead())
					playerAlive++;
		}
		
		musicPath = backgroundMusicPath;
		
	}


	@Override
	public void show() {		
		tableLife = new Table();
		tableLife.setName("tableLife");
		
		tableTurn = new Table();
		tableTurn.setName("tableTurn");
	
		batch = new SpriteBatch();
		tableFirst = new Table();
		stage = new Stage();
		
		//registering the accessors (used for modifying values with tween)
		Tween.registerAccessor(Label.class, new LabelAccessor());
		Tween.registerAccessor(Image.class, new ImageAccessor());
		
		//scaling for android devices
		if(Gdx.app.getType() == Gdx.app.getType().Android)
			scale = 3f;
		
		Image background = new Image(new Texture(Gdx.files.internal("battleground.png")));
		
		background.setPosition(0, 0);
		background.setFillParent(true);
		
		TextureAtlas tex = new TextureAtlas((Gdx.files.internal("ui_button/button.pack")));
		
		skin = new Skin(tex);		
		
		/*
		 * button style 
		 */
		
		buttonStyle = new TextButtonStyle();
		
		buttonStyle.up = skin.getDrawable("button.up");
		buttonStyle.down = skin.getDrawable("button.down");
		
		buttonStyle.pressedOffsetX = 1;
		buttonStyle.pressedOffsetY = -1;
		
		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLUE);
		if(Gdx.app.getType() == Gdx.app.getType().Android)
			font.setScale(scale);	
		else
			font.setScale(2f);
		buttonStyle.font = font;
		//smoothing the font
		buttonStyle.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		/*
		 * label style
		 */
		listStyle = new ListStyle();
    	
    	//the drawable requires to be processed i'm using a ninepatch because it's the simplest
    	NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/blue_panel.9.png"))));
		
		NinePatchDrawable drawable = new NinePatchDrawable(patch);
    	listStyle.selection = drawable;
    	
    	listStyle.font = new BitmapFont();
    	listStyle.fontColorSelected = Color.BLACK;
    	listStyle.fontColorUnselected = Color.WHITE;
    	listStyle.background = drawable;
    	
    	/*
    	 * checking the music to play
    	 */
    	backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("sound/"+musicPath+".mp3"));
		
		//searching if it's a boss fight
			for(int i = 0; i < enemySquad.size(); i++)
			{
				if(enemySquad.get(i).isBoss())
				{
					backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("sound/battle_boss.mp3"));
					break;
				}					 				
			}
		
		/*
		 * list of actions
		 */
			
		final TextButton buttonFlee = new TextButton("Flee!", buttonStyle);
		
		buttonFlee.pad(20);		
		buttonFlee.setSize(Gdx.graphics.getWidth()/2, 100);		
		buttonFlee.setHeight(100);	
		
		buttonFlee.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	
              Random r = new Random();
              
              if(r.nextInt(101) > 5)
              {       	          	 
            	  game.setScreen(mapscreen);
            	  backToMap();
              }
             
              
            }
        });

       /*
        * Creating button + table for the 2 state: 1 normal(before fight) 2 the fight
        */
		
		
      final TextButton buttonFight = new TextButton("Fight", buttonStyle);
      
      	buttonFight.pad(20);		
		buttonFight.setWidth(Gdx.graphics.getWidth()/2);
		buttonFight.setHeight(100);
		
		/*
		 * clicking on it will activate the second phase of the fight
		 */
		
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
       	buttonAtk.pad(10);		
       	buttonAtk.setWidth(Gdx.graphics.getWidth()/3);
		buttonAtk.setHeight(100);		
       	
		buttonAtk.setPosition(0, 0);		
		
		Table tableCap = new Table();
	 	tableCap.setName("tableCap");
		
		buttonAtk.addListener(new ClickListener(){
			 @Override 
	            public void clicked(InputEvent event, float x, float y){			 
				    /*
		             * Creating the list of abilities that can be used against the monster
		             */		            				 		
				 	Table tableCap = null;
					
				 	//tableCap already exist
					if(searchTable("tableCap") != null)
					{
						tableCap = searchTable("tableCap");
					}
					else
						tableCap = new Table();
						tableCap.setName("tableCap");
					
				 		List<TextButton> list = new List<TextButton>(listStyle);
			            list.setName("capacity");				           
			            
			            /*
			             * this set the selected cap and show up the list of chara if needed (if heal)
			             */
			            
			            list.addListener(new ClickListener(){
			             public void clicked(InputEvent event, float x, float y){
			            	 
			            	 List<TextButton> list = (List<TextButton>) event.getTarget();            	
			            		 playerSquad.get(getAlivePlayer()).setCapacitySelected(Integer.parseInt(list.getSelected().getText().toString()));
		            	 
			            	//if the user is switch cap from healing to normal
            				 if(getTableIndex("tableListChara") != -1)
            					 stage.getActors().removeIndex(getTableIndex("tableListChara"));
			            	 
			            	 if(playerSquad.get(getAlivePlayer()).capacity.get(playerSquad.get(getAlivePlayer()).getCapacitySelected()).isHeal)
			            	 {
			            		 Table tableListChara = new Table();
			            		 tableListChara.setName("tableListChara");
			            		 
			            		 tableListChara.center();
			            		 
			            		 List<TextButton> listChara = new List<TextButton>(listStyle);
			            		 	
			            		 //the list display the name of the textbutton not the text
			            		 for(int l = 0; l < playerSquad.size(); l++)
			            		 {
			            			 TextButton chara = new TextButton(""+l, buttonStyle);
			            			 chara.setName(playerSquad.get(l).Name);
			            			 
			            			 listChara.getItems().add(chara);
			            		 }
			            		 
			            		 
			            		 //setting the listener that set the Target of the player cap
			            		 listChara.addListener(new ClickListener(){
			            			 public void clicked(InputEvent event, float x, float y){
			            				 
			            				 List<TextButton> list = (List<TextButton>) event.getTarget();
			            				 playerSquad.get(getAlivePlayer()).setTarget(Integer.parseInt(list.getSelected().getText().toString()));			            				 			            				
			            			 }
			             
			            		 });
			            		             		 
			            		 /*
			            		  * Displaying the list of chara
			            		  */
									Table cap = searchTable("tableCap");								
									
									ScrollPaneStyle scrollStyle = new ScrollPaneStyle();		            	
					            	ScrollPane scroll = new ScrollPane(listChara, scrollStyle);
					            	
					            	scroll.setPosition(cap.getChildren().get(0).getX() + cap.getChildren().get(0).getWidth() + 50, cap.getChildren().get(0).getY());
									
									tableListChara.addActor(scroll);
									stage.addActor(tableListChara);									
			            	 }
			            	 
			            	}
			            });		            	
			            
		            	for(int u = 0; u != playerSquad.get(getAlivePlayer()).capacity.size(); u++)
			            	{
		            			//the list use the name to display, so i used the text to know which cap has been selected
			            		TextButton cap = new TextButton(""+u, buttonStyle);
			            		cap.setName(playerSquad.get(getAlivePlayer()).capacity.get(u).Name);
			            			            		
			            		list.getItems().add(cap);
			            	}		            			            	
		            	
		            		tableCap.center();	            		
		            		            				            		
							Table label = searchTable("tableTurn");							                    		
		            		
		            	 	list.setPosition(label.getChildren().get(0).getX(), label.getChildren().get(0).getY() + list.getHeight() + 40);
		            	
		            		ScrollPaneStyle scrollStyle = new ScrollPaneStyle();		            		
		            	    ScrollPane scroll = new ScrollPane(list, scrollStyle);
		            	  
		  	            	scroll.setPosition(list.getX() - list.getWidth(), list.getY()); 	
		  	            			  	            	
		  	            	tableCap.addActor(scroll);
		  	            	
		  	            	tableCap.setVisible(true);
		  	            	
		  	            	//if the label was not already created
		  	            	if(getTableIndex("tableLabelTarget") == -1)
		  	            	{
		  	            		Table tableTarget = new Table();
			  	            	tableTarget.setName("tableLabelTarget");
			  	            	tableTarget.center();
			  	      		
			  	            	Label label1 = new Label("Select a capacity", labelstyle);
			  	      		
			  	            	label1.setPosition(scroll.getX(), scroll.getY() + scroll.getHeight() );
			  	            	
			  	            	tableTarget.addActor(label1);
			  	            	
			  	            	stage.getActors().add(tableTarget);
		  	            	}		
		  	            		if(getTableIndex("tableCap") != -1)	  	            	
		  	            			stage.getActors().removeIndex(getTableIndex("tableCap"));	
		  	            		stage.getActors().add(tableCap);
				 	
				 				 			 
				 //used to manage the fight	making sure that the player has not loose win or has selected a capacity	
				 if(!win && !loose && playerSquad.get(getAlivePlayer()).getCapacitySelected() != -1 && playerSquad.get(getAlivePlayer()).getTarget() != -1)
				 {
					 fight();
				 }
					
			 }
		});	
		
		final TextButton buttonItem = new TextButton("Item", buttonStyle);
	      
      	buttonItem.pad(10);		
		buttonItem.setSize(buttonAtk.getWidth(), 100);
		
		buttonItem.setPosition(buttonAtk.getX() + buttonAtk.getWidth(), 0);	
		
		
		final TextButton buttonBack = new TextButton("Back", buttonStyle);
		
		buttonBack.pad(10);		
		buttonBack.setSize(buttonItem.getWidth(), 100);
	
		buttonBack.setPosition(buttonItem.getX() + buttonItem.getWidth(), 0);
		
		buttonBack.addListener(new ClickListener(){
	            @Override 
	            public void clicked(InputEvent event, float x, float y){	            	
	            	/*
	            	 * removing the menus only if they exist
	            	 */
	            	
	            	//disabling the menus(if exist)
	            	if(getTableIndex("tableCap") != -1)
	            		stage.getActors().get(getTableIndex("tableCap")).setVisible(false);;
	    			//if a cap healing selected we need to erase the list of chara from the screen
	    			if(getTableIndex("tableListChara") != -1)
	    				stage.getActors().removeIndex(getTableIndex("tableListChara"));
	    			if(getTableIndex("tableLabelTarget") != -1)
	    				stage.getActors().removeIndex(getTableIndex("tableLabelTarget"));
	            	
	               stage.getActors().get(1).setVisible(false);
	               stage.getActors().get(2).setVisible(true);
	            }
	        });

		
		tableFight.add(buttonAtk).width(Gdx.graphics.getWidth() / 3);
		tableFight.add(buttonItem).width(Gdx.graphics.getWidth() / 3);
	    tableFight.add(buttonBack).width(Gdx.graphics.getWidth() / 3);
		
       tableFight.setVisible(false);
       tableFight.setSize(Gdx.graphics.getWidth(), 100);
       tableFight.right().bottom();
       tableFight.setPosition(0, 0);
       
       /*
        * Creating the label that show the life of the characters
        */	
		
		labelstyle.font = new BitmapFont();
		labelstyle.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
       

		Table tableLabelName = new Table();
		tableLabelName.setName("tableName");
		
		//loading labels and setting a proper name to update life through function
		for(int i = 0; i < playerSquad.size(); i++)
		{
			Label labelLife = new Label(""+playerSquad.get(i).getLife(), labelstyle);
			labelLife.setName(playerSquad.get(i).Name);
			labelLife.setFontScale(2f);
			labelLife.setName(""+i);
			
			Label labelName = new Label(playerSquad.get(i).Name, labelstyle);
			labelName.setName(playerSquad.get(i).Name+"name");
			labelName.setFontScale(2f);
				
			/*
			 * setting the position( dividing the screen width by the number of player then multiply by i)
			 */
			
			labelLife.setPosition((Gdx.graphics.getWidth() / playerSquad.size()) * i + 100, 0);
			labelName.setPosition(labelLife.getX() - labelLife.getWidth(), 30);
			
			
			labelLife.setWidth(100f);
			labelLife.setHeight(20f);
			
			tableLife.addActor(labelLife);
			tableLabelName.addActor(labelName);
		}
		
		tableLife.setHeight(100);
		tableLife.setWidth(Gdx.graphics.getWidth());
		tableLife.setPosition(0, 100);
		
		tableLabelName.setHeight(100);
		tableLabelName.setWidth(Gdx.graphics.getWidth());
		tableLabelName.setPosition(0, 100);
		
		tableLabelName.center();
		tableLife.center();
		
		/*
		 * Creating the monsters
		 */
		Table monsterTable = new Table();
		monsterTable.setName("monsterTable");
		monsterTable.center();
		
		//store the label
		Table monsterTableName = new Table();
		monsterTableName.setName("monsterTableName");
		monsterTableName.center();
		
		for(int i = 0; i != enemySquad.size(); i++)
		{
			Label monsterName = new Label(enemySquad.get(i).Name, labelstyle);
			Image monsterImage = new Image(new Texture(Gdx.files.internal(enemySquad.get(i).imagePath)));
			
			//scaling
			monsterImage.setSize(monsterImage.getWidth() * scale, monsterImage.getHeight() * scale);
			
			
			//used to know what enemy has been choose by the player to be attacked
			monsterImage.setName(""+ (enemySquad.get(i).monsterId));
			monsterName.setName(""+ (enemySquad.get(i).monsterId));

			
			monsterImage.addListener(new ClickListener(){
	            @Override 
	            public void clicked(InputEvent event, float x, float y){
         
	            int monsterSelected = 0;
	            	
	            //cap selected 
	            if(playerSquad.get(getAlivePlayer()).getCapacitySelected() != -1)
	            {
	            	if(!playerSquad.get(getAlivePlayer()).capacity.get(playerSquad.get(getAlivePlayer()).getCapacitySelected()).isHeal)
	            	{
	            		//used for knowing which monster has been selected
		            	Table monsterTable = null;		            	            	
		            	
						try {
							monsterTable = searchTable("monsterTable");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	for(int i = 0; i != monsterTable.getChildren().size; i++)
		            	{
		            		if(monsterTable.getChildren().get(i).getName() == event.getTarget().getName())
		            		{
		            			monsterSelected = i;
		            		}
		            	}
		            	
		            	//setting the arrow for showing which monster has been select
		            	for(int i = 0; i < stage.getActors().size; i++)
		            	{
		            		//table found
		            		if(stage.getActors().get(i).getName() == "monsterArrow")
		            		{            			            			
		            			
		            			Image monster = (Image) monsterTable.getChildren().get(monsterSelected);
		            			
		            			//getting the index of the table
		            			int index = getTableIndex("monsterArrow");	          
		            			
		            			//setting the pos of the arrow
		            			Table monsterArrow = null;
								try {
									monsterArrow = searchTable("monsterArrow");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		            			
		            			monsterArrow.getChildren().get(0).setPosition(monster.getX() + (monster.getWidth() / 2) - monsterArrow.getWidth(), monster.getY() + (monster.getHeight() + 20));
		            			monsterArrow.setVisible(true);	
		            			monsterArrow.setSize(monsterArrow.getWidth() * scale, monsterArrow.getHeight() * scale);
		            			
		            			//inserting the arrow	     			            			            			
		            			stage.getActors().insert(index, monsterArrow);      
		            			stage.getActors().removeIndex(index + 1 );	
		            			
		            		}
		            	}
		            	//id of the selected monster
		            	playerSquad.get(getAlivePlayer()).setTarget(Integer.parseInt(event.getTarget().getName()));
		            	
	            	}//end if not heal
		          }//end if not cap
	            else
	            {
	            	/*
	            	 * ERROR: cap not selected            	
	            	 */
	            }
		        }});
				
				monsterImage.setPosition(monsterImage.getWidth()  + (i * 100), Gdx.graphics.getHeight() / 2);
				monsterName.setPosition(monsterImage.getX(), monsterImage.getY() + monsterImage.getHeight() + 10);
				
				monsterTable.addActor(monsterImage);
				monsterTableName.addActor(monsterName);
	    }	       		
	
		//creating the label that indicates who need to attack with the first alive character
		Label labelTurn = null;
		for(int y = 0; y < playerSquad.size(); y++)
		{
			if(!playerSquad.get(y).isDead())
			{
				labelTurn = new Label(playerSquad.get(y).Name+"'s turn", labelstyle);
				break;
			}
		}
		
		
		labelTurn.setName("labelTurn");
		
		labelTurn.setFontScale(2f);
		labelTurn.setPosition(Gdx.graphics.getWidth() / 2 - labelTurn.getWidth(), 150);//setting at the middle
		
		tableTurn.center();
		tableTurn.addActor(labelTurn);
		
		/*
		 * Creating the arrow that show which monster has been selected
		 */
		
		Table tableMonsterSelected = new Table();
		tableMonsterSelected.center();
		tableMonsterSelected.setName("monsterArrow");
		
		TextureAtlas text = new TextureAtlas((Gdx.files.internal("ui_button/arrows.pack")));
		
		Skin skin = new Skin(text);	

		ButtonStyle styleRight = new ButtonStyle();
		styleRight.up = skin.getDrawable("arrowDown");
		styleRight.down = skin.getDrawable("arrowDown");	
		
		Button monsterchoose = new Button(styleRight);
		
		tableMonsterSelected.setVisible(false);
		tableMonsterSelected.addActor(monsterchoose);	
		
       stage.addActor(background);
       stage.addActor(tableFight);
       stage.addActor(tableFirst);
       stage.addActor(tableLife);      
       stage.addActor(tableTurn);
       stage.addActor(monsterTableName);
       stage.addActor(monsterTable);
       stage.addActor(tableMonsterSelected);
       stage.addActor(tableLabelName);
       stage.addActor(tableCap);
       
        Gdx.input.setInputProcessor(stage);		
        
        backgroundMusic.setVolume(backgroundMusic.loop(), 0.5f);
        
        /*
         * Fade in
         */
        //set alpha to 0 and then go to 1
              
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));    
        
        /*
         * Applying the stuff that the player has equipped
         */
        
        for(int i = 0; i < playerSquad.size(); i++)
        {
        	for(int p = 0; p != playerSquad.get(i).bagSpace; p++)
        	{
        		switch(playerSquad.get(i).bag.get(p).type)
        		{
        			case 0: //weapon
        				PartyHard_Weapon weapon =  (PartyHard_Weapon) playerSquad.get(i).bag.get(p);
        				playerSquad.get(i).setAtk(playerSquad.get(i).getAtk() + weapon.getAmount());
        			break;
        		}
        	}
        }
	}

	/*
	 * Dispose all resources that are not needed for the map
	 */
	private void backToMap() {
		
		stage.dispose();
		backgroundMusic.dispose();
		
		stage = new Stage();
	}


	@Override
	public void render(float delta) {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
	        batch.begin();	      
	        
	        
	        
	        tweenManager.update(delta);
	        
	        stage.act(delta);
	        stage.draw();
	            
	        batch.end();
	}

	private void fight()
	{
		
		if(playerSquad.get(getAlivePlayer()).getCapacitySelected() > -1)
		{						
			//disabling the menus
			stage.getActors().get(getTableIndex("tableCap")).setVisible(false);
			//if a cap healing selected we need to erase the list of chara from the screen
			if(playerSquad.get(getAlivePlayer()).capacity.get(playerSquad.get(getAlivePlayer()).getCapacitySelected()).isHeal)
				stage.getActors().removeIndex(getTableIndex("tableListChara"));
			//if not heal then need to remove the arrowMonster
			else
				stage.getActors().get(getTableIndex("monsterArrow")).setVisible(false);
				
			stage.getActors().removeIndex(getTableIndex("tableLabelTarget"));
								
			//turning off the arrow
			int index = getTableIndex("monsterArrow");
			
			stage.getActors().get(index).setVisible(false);
				
			/*
			 * FIGHT!!
			 */
			if(turn == playerAlive - 1)
			{
				/*
				 *Calculating which player monsters have to attack randomly
				 */
				
				for(int p = 0; p < enemySquad.size();)
				{
					Random r = new Random();
					
					int random = r.nextInt(playerSquad.size());
					
					if(!playerSquad.get(random).isDead())
					{
						playerSquad.get(random).setDamageTaken(enemySquad.get(p).actualAtk);
						p++;
					}				
				}				
				/*
				 * TO DO calculate type damages
				 */				
				for(int i = 0; i < playerSquad.size(); i++)
				{
					if(!playerSquad.get(i).isDead())
					{
						//if the cap doesn't heals 
						if(!playerSquad.get(i).capacity.get(playerSquad.get(i).getCapacitySelected()).isHeal)
						{																									
							//getting the index in the table of the monster and the index in the array									
							int monsterIndex = -1;
							
							for(int y = 0; y != enemySquad.size(); y++)
							{
								if(enemySquad.get(y).monsterId == playerSquad.get(i).getTarget())
									monsterIndex = y;
							}				
							
							//if there is still monster to kill 
								if(monsterIndex != -1)
								{
									int damage = playerSquad.get(i).getAtk() - enemySquad.get(monsterIndex).actualDef;
									if(damage <= 0)						
										damage = 1;
																				
									enemySquad.get(monsterIndex).setDamageTaken(damage);																				
								}																												
							}//closing if not heal
						else
						{					
							playerSquad.get(playerSquad.get(i).getTarget()).setHealAmount((playerSquad.get(i).capacity.get(playerSquad.get(i).getCapacitySelected()).amount));														
						}
						
						//setting to default damage taken 
						//playerSquad.get(enemySquad.get(i).getIdTarget()).damageTaken = 0;
				}
					/*
					 * Monster turn
					 */				
					for(int o = 0; o < enemySquad.size(); o++)
					{			
						playerSquad.get(enemySquad.get(o).getIdTarget()).setDamageTaken(enemySquad.get(o).actualAtk);						
					}					
			}
				
				/*
				 * showing damage and dealing them
				 */
				
				//first player
				
				for(int i = 0; i < playerSquad.size(); i++)
				{
					if(!playerSquad.get(i).isDead())
					{
						if(!playerSquad.get(i).capacity.get(playerSquad.get(i).getCapacitySelected()).isHeal)
						{
							//searching the monster
							Table monsterTable = searchTable("monsterTable");
							
							//used for finding the monster in the table
							int monsterIndexTable = -1;
							//finding the monster in the array
							int monsterIndex = -1;
							
							//searching the right monster				
								for(int x = 0; x != monsterTable.getChildren().size; x++)
								{
									if(Integer.parseInt(monsterTable.getChildren().get(x).getName()) == playerSquad.get(i).getTarget())
									{
										monsterIndexTable = x;
									}
								}																			
										//getting the index in the table of the monster and the index in the array
										
										for(int y = 0; y != enemySquad.size(); y++)
										{
											if(enemySquad.get(y).monsterId == playerSquad.get(i).getTarget())
												monsterIndex = y;
										}
										
										
										//if the dealt damages has been shown
										if(enemySquad.get(monsterIndex).getDamage() != 0)
										{
											/*
											 * Animating the damage
											 */
											Label labelDamage = new Label(""+enemySquad.get(monsterIndex).getDamage(), labelstyle);
											labelDamage.setFontScale(scale * 2);
											labelDamage.setColor(Color.RED);
											
											//setting the value before animation
											Image monsterimage = (Image) monsterTable.getChildren().get(monsterIndexTable);		
											labelDamage.setPosition(monsterimage.getX() + (monsterimage.getWidth() / 2), monsterimage.getY() + monsterimage.getHeight());
											
											stage.addActor(labelDamage);
											
											/*
											 * Animations work like that: value SET to value TO in x seconds
											 */
											
											Tween.set(labelDamage, LabelAccessor.Y).target(labelDamage.getY()).start(tweenManager);
											Tween.set(labelDamage, LabelAccessor.ALPHA).target(1).start(tweenManager);
											
											Tween.to(labelDamage, LabelAccessor.Y, 2f).target(monsterimage.getY() + monsterimage.getHeight() + 100).start(tweenManager);
											Tween.to(labelDamage, LabelAccessor.ALPHA, 2).target(0).delay(1).start(tweenManager);
											
											//dealing damage and setting back to default the damage taken
											enemySquad.get(monsterIndex).actualHp -= enemySquad.get(monsterIndex).getDamage();
											enemySquad.get(monsterIndex).damage = 0;
											
											//the monster is dead
											if(enemySquad.get(monsterIndex).actualHp <= 0)
											{																										
												expgained += enemySquad.get(monsterIndex).actualExp; 																																
												
												Table monsterName = searchTable("monsterTableName");									
												
												/*
												 * fading out the monster that has been kill
												 */
												
												Tween.set((Image) monsterTable.getChildren().get(monsterIndexTable), SpriteAccessor.ALPHA).target(1).start(tweenManager);
												
												Tween.to((Image) monsterTable.getChildren().get(monsterIndexTable), SpriteAccessor.ALPHA, 1).target(0).setCallback(new MonsterCallBackTween(enemySquad.get(monsterIndex).monsterId){										
													@Override
													public void onEvent(int callbackType, BaseTween<?> callback) {	

														/*
														 * recalculating indexes
														 */
														
														Table monsterTable = searchTable("monsterTable");
														Table monsterName = searchTable("monsterTableName");
														
														//used for finding the monster in the table
														int monsterIndexTable = -1;
														//finding the monster in the array
														int monsterIndex = -1;
														
														//searching the right monster				
															for(int x = 0; x != monsterTable.getChildren().size; x++)
															{
																if(Integer.parseInt(monsterTable.getChildren().get(x).getName()) == idMonster)
																{
																	monsterIndexTable = x;
																}
															}
															
															//getting the index in the table of the monster and the index in the array
															
															for(int y = 0; y != enemySquad.size(); y++)
															{
																if(enemySquad.get(y).monsterId == idMonster)
																	monsterIndex = y;
															}
															
															//checking if the monster is not already dead
														if(monsterIndexTable != -1 || monsterIndex != 1 )
														{
															switch(callbackType)
															{
																//animation finishes
																case TweenCallback.COMPLETE:
																	//removing the killed monster
																	monsterTable.getChildren().removeIndex(monsterIndexTable);
																	monsterName.getChildren().removeIndex(monsterIndex);
																			
																	//used for the update
																 int index = getTableIndex("monsterTableName");
																		
																	//updating the table
																	stage.getActors().removeIndex(index);								
																	stage.getActors().removeIndex(index);
																			
																	stage.getActors().insert(index, monsterTable);
																	stage.getActors().insert(index, monsterName);
																		
																	//deleting the killed monster from the array
																	enemySquad.remove(monsterIndex);		
																			
																	//win
																	if(enemySquad.size() == 0)
																	{
																		win();
																	}								
																	break;											
															}
														}
														
													}							
												}).start(tweenManager);
																																							
											}
											
											enemySquad.get(monsterIndex).damage = 0;
										}
										
										
						}
						//cap heal
						else
						{
							//if the player is really getting healed
							if(playerSquad.get(i).getHealAmount() > 0)
							{
								//creating label with the heal amount
								Label labelHealth = new Label(""+playerSquad.get(i).getHealAmount(), labelstyle);
								labelHealth.setFontScale(scale * 2);
								labelHealth.setColor(Color.GREEN);
								
								//healing the player and set back to default the heal amount
								playerSquad.get(i).setLife(playerSquad.get(i).healAmount);
								playerSquad.get(i).healAmount = 0;
								
								/*
								 * getting the coordinate of the player's label healed
								 */
								
								Table tableName = searchTable("tableName");
								
								Label labelName = (Label) tableName.getChildren().get(i);
								
								labelHealth.setPosition(labelName.getX(), labelName.getY() + 150);
								
								Tween.set(labelHealth, LabelAccessor.Y).target(labelHealth.getY()).start(tweenManager);
								Tween.set(labelHealth, LabelAccessor.ALPHA).target(1).start(tweenManager);
								
								Tween.to(labelHealth, LabelAccessor.Y, 2f).target(labelHealth.getY() + 100).start(tweenManager);
								Tween.to(labelHealth, LabelAccessor.ALPHA, 2).target(0).delay(1).start(tweenManager);
								
								stage.addActor(labelHealth);
							}
							
						}							
									//setting the fight setting back to default
									playerSquad.get(i).setCapacitySelected(-1);	
									playerSquad.get(i).setTarget(-1);
					}
					
				}
				
				/*
				 * monster turn to show damage
				 */
				
				for(int i = 0; i < playerSquad.size(); i++)
				{
					//if the player is not dead and have received damages
					if(!playerSquad.get(i).isDead() && playerSquad.get(i).getDamageTaken() > 0)
					{						
						//getting the damage dealt to the player
						int damageToPlayer = playerSquad.get(i).getDamageTaken();
						
						//dealing damage and setting them back to 0
						playerSquad.get(i).Damage(damageToPlayer);
						playerSquad.get(i).damageTaken = 0;
						
						
						Label labelDamage = new Label(""+damageToPlayer, labelstyle);
						labelDamage.setFontScale(scale * 2);
						labelDamage.setColor(Color.RED);
						
						/*
						 * animating the damage
						 */
						
						Table tableName = searchTable("tableName");
						
						Label labelName = (Label) tableName.getChildren().get(i);
						
						labelDamage.setPosition(labelName.getX(), labelName.getY() + 150);
						
						Tween.set(labelDamage, LabelAccessor.Y).target(labelDamage.getY()).delay(1).start(tweenManager);
						Tween.set(labelDamage, LabelAccessor.ALPHA).target(1).delay(2).start(tweenManager);
						
						Tween.to(labelDamage, LabelAccessor.Y, 2f).target(labelDamage.getY() + 100).delay(2).start(tweenManager);
						Tween.to(labelDamage, LabelAccessor.ALPHA, 2).target(0).delay(3).start(tweenManager);
						
						stage.addActor(labelDamage);
						
						//controlling if the player is not already dead						
							if(playerSquad.get(i).getLife() <= 0)
							{												
								playerSquad.get(i).setDead(true);
								playerAlive--;
								
								if(playerAlive == 0)
								{
									gameOver();
									loose = true;
								}							
							}								
					}
								
				}
				
				/*
				 * Updating the label that show the life
				 */
				
				Table tableLife = searchTable("tableLife");

				//update table
				/*
				 * getting the label, updating and setting back to the array
				 */
				for(int p = 0; p < playerSquad.size(); p++)
				{
					Label labelLife = (Label) tableLife.getChildren().get(p);
					labelLife.setText(""+playerSquad.get(p).getLife());
				 
					tableLife.getChildren().items[p] = labelLife;
				}							
				
				//update stage
				stage.getActors().removeIndex(getTableIndex("tableLife"));
				stage.addActor(tableLife);							
							
			turn = -1;			
		}
			//next turn
			turn++;
			
			Label labelTurn = (Label) tableTurn.getChildren().get(0);
			
			//if the character that should have atk is dead then look for another alive character
			if(playerSquad.get(turn).isDead())
			{
				for(int y = 0; y < playerSquad.size(); y++)
				{
					if(!playerSquad.get(y).isDead())
					{					
						labelTurn.setText(playerSquad.get(y).Name+"'s turn");					
						break;
					}
				}	
			}
			else
				labelTurn.setText(playerSquad.get(turn).Name+"'s turn");	
				
			//updating the table without the function addActor(object,index) the delete/insert is needed
		
			//because the array doesn't let me remove the last actor
			if(turn == tableTurn.getChildren().size)
			{
				tableTurn.getChildren().removeIndex(turn - 1);
				tableTurn.getChildren().insert(turn - 1, labelTurn);
			}
				
			else
			{
				tableTurn.getChildren().removeIndex(turn);
				tableTurn.getChildren().insert(turn, labelTurn);				
			}
			
		}
	}
	
	private void fleeFail()
	{
		
	}
	
	private void gameOver() {
		
		//Sound gameOver = Gdx.audio.newSound()
		WindowStyle style = new WindowStyle();
		
		NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/blue_panel.9.png"))));
		
		NinePatchDrawable drawable = new NinePatchDrawable(patch);
		style.background =  drawable;
		style.titleFont = new BitmapFont();
		style.titleFontColor = Color.BLACK;
		
		Dialog win = new Dialog("", style);
		
		//exit the fight when pressed
		TextButton btnWin = new TextButton("Ok",buttonStyle);
		btnWin.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	save();
            	
            	dispose();
            	Gdx.app.exit();
            }
        });
		win.button(btnWin);
		
		//set res and pos
		
		win.setPosition(Gdx.graphics.getWidth() / 2 - (win.getWidth() / 2), Gdx.graphics.getHeight() / 2  - (win.getHeight() / 2));
		win.setResizable(true);
		/*
		 * Creating the text with the exp gained
		 */
		LabelStyle labelstyle = new LabelStyle();
		
		labelstyle.font = new BitmapFont();
		labelstyle.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
       
		win.text("Game Over!", labelstyle);
		
		winDialog = win;
		stage.addActor(winDialog);
		
	}

/*
 * @param name the name of the table (setName)
 * 
 * @return The table or an exception if null
 */
	private Table searchTable(String name)
	{
		for(int i = 0; i < stage.getActors().size; i++)
		{
			if(stage.getActors().get(i).getName() == name)
				return (Table) stage.getActors().get(i);
		}
		
		//hopefully will never be 
		return null;
	}
/*
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
	
	private void win()
	{
		//adding exp to player and checking for level up
		for(int i = 0; i < playerSquad.size(); i++)
		{
			//if the player is not dead give him some exp
			if(!playerSquad.get(i).isDead())
			{
				playerSquad.get(i).addExp(expgained);
				
				//level up?
				if(playerSquad.get(i).getExp() >= playerSquad.get(i).levelUp.expToUp)
				{
					playerSquad.get(i).LevelUp();
					playerSquad.get(i).setExp(0);					
					
					/*
					 * Creating animated label that shows above the player name
					 */					
					Label labelLevelUp = new Label(playerSquad.get(i).Name+" has leveled up!", labelstyle);				
					Table tableName = searchTable("tableLife");
					
					stage.addActor(labelLevelUp);
															
					labelLevelUp.setPosition(tableName.getChildren().get(i).getX(), tableName.getChildren().get(i).getY()+ 150);
					
					Tween.set(labelLevelUp, LabelAccessor.Y).target(labelLevelUp.getY()).start(tweenManager);
					Tween.to(labelLevelUp,  LabelAccessor.Y, 1f).target(labelLevelUp.getY() + 100).start(tweenManager);
					
				}
			}
			
		}
		
		
		win = true;
		
		Sound soundWin = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
		soundWin.play();
		/*
		 * Prepares the dialog with the vars
		 */
		WindowStyle style = new WindowStyle();
		
		NinePatch patch = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("ui/blue_panel.9.png"))));
		
		NinePatchDrawable drawable = new NinePatchDrawable(patch);
		style.background =  drawable;
		style.titleFont = new BitmapFont();
		style.titleFontColor = Color.BLACK;
		
		Dialog win = new Dialog("", style);
		
		//exit the fight when pressed
		TextButton btnWin = new TextButton("Ok",buttonStyle);
		btnWin.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	save();
            	
            	 game.setScreen(mapscreen);
           	  	backToMap();
            }
        });
		win.button(btnWin);
		
		//set res and pos
		
		win.setPosition(Gdx.graphics.getWidth() / 2 - (win.getWidth() / 2), Gdx.graphics.getHeight() / 2  - (win.getHeight() / 2));
		win.setResizable(true);
		/*
		 * Creating the text with the exp gained
		 */
		LabelStyle labelstyle = new LabelStyle();
		
		labelstyle.font = new BitmapFont();
		labelstyle.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
       
		win.text("EXP win: "+expgained, labelstyle);
		
		winDialog = win;
		stage.addActor(winDialog);
	}
	
	private void save() {
	
		/*
		 * resetting the stats back before the items buff was applies
		 */
		 for(int i = 0; i < playerSquad.size(); i++)
	        {
	        	for(int p = 0; p != playerSquad.get(i).bagSpace; p++)
	        	{
	        		switch(playerSquad.get(i).bag.get(p).type)
	        		{
	        		/*
	        			case 0: //weapon
	        				PartyHard_Weapon weapon =  (PartyHard_Weapon) playerSquad.get(i).bag.get(p);
	        				playerSquad.get(i).setAtk(playerSquad.get(i).getAtk() - weapon.getAmount());
	        			break;
	        			*/
	        		}
	        	}
	        }
		
		try {
		//FileManager file = new FileManager("player_Fight.xml");
			FileHandle file = Gdx.files.local("player_Fight.xml");
				
			 StringWriter stringwriter = new StringWriter();
			 XmlWriter xml = new XmlWriter(stringwriter);
		 
				xml.element("root");	
				for(int i = 0;i < playerSquad.size(); i++)
				{
					xml.element("player_Fight").attribute("num", i)	       
	                .element("name").attribute("value", playerSquad.get(i).Name).pop()
	                .element("class").attribute("value", playerSquad.get(i).getclass()).pop()
	                .element("hpmax").attribute("value", playerSquad.get(i).getHpMax()).pop()
	                .element("hp").attribute("value", playerSquad.get(i).getLife()).pop()
	                .element("def").attribute("value", playerSquad.get(i).getDef()).pop()
	                .element("atk").attribute("value", playerSquad.get(i).getAtk()).pop()
	                .element("exp").attribute("value", playerSquad.get(i).getExp()).pop()
	                .element("money").attribute("value", playerSquad.get(i).getMoney()).pop()
	                .element("level").attribute("value", playerSquad.get(i).getLevel()).pop()
	                .element("bag").attribute("space", playerSquad.get(i).getBagSpace());
					
					//populating the bag
					for(int p = 0; p < playerSquad.get(i).bagSpace; p++)
					{
						xml.element("item").attribute("type", playerSquad.get(i).bag.get(p).type);	
						
						switch(playerSquad.get(i).bag.get(p).type)
		        		{
		        			case 0: //weapon
		        				PartyHard_Weapon wep = (PartyHard_Weapon) playerSquad.get(i).bag.get(p);
		        				xml.attribute("id", wep.id).pop();
		        			break;
		        		}
					}
					xml.pop();//closing the bag
					xml.element("capacity").attribute("value", playerSquad.get(i).capacity.size());
					
					for(int p = 0; p < playerSquad.get(i).capacity.size(); p++)
					{
						xml.element("cap").attribute("id", playerSquad.get(i).capacity.get(p).id).pop();				
					}
					//closing cap and then the player
					xml.pop();
					xml.pop();
			
					/*
					 * updating the inventory with (class, object)
					 */
					             
				}
				      //to be sure that all the element has been close  
				   xml.close();                   
			
				   file.writeString(stringwriter.toString(), false);
				   			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
	}

	private int getAlivePlayer()
	{
		if(playerSquad.get(turn).isDead())
		{
			for(int i = 0; i < playerSquad.size(); i++)
			{
				if(!playerSquad.get(i).isDead())
				{
					return i;
				}
			}
		}		
			return turn;
	}
	
	@Override
	public void resize(int width, int height) {
		 stage.getViewport().update(width, height, true);			
	}

	@Override
	public void pause() {
		save();
		
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
		backgroundMusic.dispose();
		game.dispose();		
	}	
}