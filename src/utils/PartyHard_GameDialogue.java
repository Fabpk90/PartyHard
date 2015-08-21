package utils;

public class PartyHard_GameDialogue {
	public int id;
	public String message;
	public String pathImage = null;
	
	/**
	 * 
	 * @param id The id of the message
	 * @param message The content of the dialogue
	 */
	public PartyHard_GameDialogue(int id, String message)
	{
		this.id = id;
		this.message = message;
	}
	
	public PartyHard_GameDialogue(int id, String message, String pathImage)
	{
		this.id = id;
		this.message = message;
		this.pathImage = pathImage;
	}
}
