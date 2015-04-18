package com.partyhard.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ExitDialog extends Dialog {
	private int response = -1;

	public ExitDialog(String title, Skin skin) {
		super(title, skin);
		// TODO Auto-generated constructor stub
	}

	public ExitDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		// TODO Auto-generated constructor stub
	}

	public ExitDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
		// TODO Auto-generated constructor stub
	}
	
	//called regardless which constructor has been call
	{
		text("Are you sure you want to exit");
		
		button("No",1);
		
	}

	@Override
	protected void result(Object object) {
		// TODO Auto-generated method stub
		response = Integer.parseInt((String) object);
		
	}
	
	public int getResponse()
	{
		return response;
	}

}
