package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.partyhard.game.PartyHard_GameClass;
import com.partyhard.game.PartyHard_ScreenSplash;

public class PartyHard_ExitDialog {

	public Dialog dialog;
	public WindowStyle windowStyle = new WindowStyle();
	public TextButtonStyle buttonStyle = new TextButtonStyle();
	private Screen screen;
	private PartyHard_GameClass mainGame;
	
	public PartyHard_ExitDialog(Screen actualScreen, PartyHard_GameClass maingame)
	{		
		this.screen = actualScreen;
		this.mainGame = maingame;
		
		windowStyle.titleFont = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		buttonStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		dialog = new Dialog("Confirm Exit?",windowStyle);
		
		TextButton yes = new TextButton("Yes", buttonStyle);
		yes.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.dispose();
				
				PartyHard_ScreenSplash splash = new PartyHard_ScreenSplash(mainGame);
				mainGame.setScreen(splash);
				
			}
		});
		
		TextButton no = new TextButton("No", buttonStyle);
		
		dialog.button(yes);
		dialog.button(no);
	}
	
	public PartyHard_ExitDialog(PartyHard_GameClass maingame)
	{		
		this.mainGame = maingame;
		
		windowStyle.titleFont = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		buttonStyle.font = new BitmapFont(Gdx.files.internal("font/White.fnt"));
		
		dialog = new Dialog("Confirm Exit?",windowStyle);
		
		TextButton yes = new TextButton("Yes", buttonStyle);
		yes.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {	
				PartyHard_ScreenSplash splash = new PartyHard_ScreenSplash(mainGame);
				mainGame.setScreen(splash);
				
			}
		});
		
		TextButton no = new TextButton("No", buttonStyle);
		
		dialog.button(yes);
		dialog.button(no);
	}
	
}
