package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.partyhard.actor.PartyHard_Monster;
import com.partyhard.actor.PartyHard_Player_Map;

public class SaveManager {
	 private FileHandle file;
	
	 public SaveManager(String filename)
	 {
			 file = Gdx.files.internal(filename);
			 
	 }
	 
	 public void SaveMonster(PartyHard_Monster objectToSave, boolean append)
	 {
		 //loading the file 
		 FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file.file(), append);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			// used to write the object
			try {
				ObjectOutputStream oos= new ObjectOutputStream(fos);
				
				oos.writeObject(objectToSave);
				
				oos.close();
				fos.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	 }
	 
	 public PartyHard_Monster loadMonster(String monsterName) throws ClassNotFoundException
	 { 
		 PartyHard_Monster monster = null;
		 
		 FileInputStream fis = null;
		try {
			fis = new FileInputStream(file.file());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			// creating the object stored in the file
			try {
				ObjectInputStream ois= new ObjectInputStream(fis);
		
				
				boolean finded = false;
				do
				{
					monster = (PartyHard_Monster) ois.readObject();
					if(monster.Name == monsterName)
						finded = true;
					
				}while(finded || ois.available() > 0);
				
				fis.close();
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		return monster;
		 
	 }
	 
	 public void SavePlayer_Map(PartyHard_Player_Map player, boolean append)
	 {
		 FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file.file(), append);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

				// used to write the object
				try {
					ObjectOutputStream oos= new ObjectOutputStream(fos);
					
					oos.writeObject(player);
					
					oos.close();
					fos.close();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 }
}
	    
	   