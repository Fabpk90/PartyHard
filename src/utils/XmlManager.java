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
/*
 * <root>
	<player_Fight num="0">
		<name value="Pavel"/>
		<hp value="9995"/>
		<def value="200"/>
		<atk value="50"/>
		<exp value="60"/>
		<money value="20"/>
		<level value="10"/>
		<bag space="5">
			<item class="class com.partyhard.object.PartyHard_Object" object="com.partyhard.object.PartyHard_Object@7233bb11"/>
			<item class="class com.partyhard.object.PartyHard_Object" object="com.partyhard.object.PartyHard_Object@223e93aa"/>
			<item class="class com.partyhard.object.PartyHard_Object" object="com.partyhard.object.PartyHard_Object@3a508f76"/>
			<item class="class com.partyhard.object.PartyHard_Object" object="com.partyhard.object.PartyHard_Object@7efaa9e2"/>
			<item class="class com.partyhard.object.PartyHard_Object" object="com.partyhard.object.PartyHard_Object@7aa7758e"/>
		</bag>
	</player_Fight>
</root>

 */

