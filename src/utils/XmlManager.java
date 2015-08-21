package utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.partyhard.object.PartyHard_Consumable;
import com.partyhard.object.PartyHard_Object;
import com.partyhard.object.PartyHard_Potion;
import com.partyhard.object.PartyHard_Weapon;


public class XmlManager {

	public FileHandle file;
	public XmlManager(String fileName) {
		file = Gdx.files.local(fileName);
	}
		public  void writeObject(PartyHard_Potion object) throws Exception{
	        XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	            		  file.write(true)));
	        encoder.writeObject(object);
	        encoder.close();
	    }
		
		public  void writeObject(PartyHard_Weapon object) throws Exception{
	        XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	            		  file.write(true)));
	        encoder.writeObject(object);
	        encoder.close();
	    }
		
		public  void writeObject(PartyHard_Consumable object) throws Exception{
	        XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	            		  file.write(true)));
	        encoder.writeObject(object);
	        encoder.close();
	    }
		public  void writeObject(PartyHard_Capacity object) throws Exception{
	        XMLEncoder encoder =
	           new XMLEncoder(
	              new BufferedOutputStream(
	            		  file.write(true)));
	        encoder.writeObject(object);
	        encoder.close();
	    }

	    public PartyHard_Object readObject(String objectName) throws Exception {
	        XMLDecoder decoder =
	            new XMLDecoder(new BufferedInputStream(
	                file.read()));
	        
	        PartyHard_Object o = null;
	        
	        while(decoder.readObject() != null)
	        {
	        	 o = (PartyHard_Object)decoder.readObject();
	        	 if(o.Name == objectName)
	        	 {
	        		 break;
	        	 }
	        }
	        
	        decoder.close();
	        return o;
	    }
	    
	    public PartyHard_Object readObject() throws Exception {
	        XMLDecoder decoder =
	            new XMLDecoder(new BufferedInputStream(
	                file.read()));
	        
	        PartyHard_Object o = (PartyHard_Object)decoder.readObject();        	
	        	        
	        decoder.close();
	        return o;
	    }
	}