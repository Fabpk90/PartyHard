package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager {

	private FileHandle file;
	private FileHandle fileToReturn;
	public FileManager(String filePath) {
		
		file = Gdx.files.local(filePath);
		
	}

	public void saveFile(boolean encoded, FileHandle fileToSave)
	{
		if(encoded)
		{								
			file.writeString(Base64Coder.encodeString(fileToSave.readString()), false);
		}
		else
			file.writeString(fileToSave.readString(), false);	
	}
	
	public void saveFile(boolean encoded, String fileToSave)
	{
		if(encoded)
		{								
			file.writeString(Base64Coder.encodeString(fileToSave), false);
		}
		else
			file.writeString(fileToSave, false);		
	}
	
	public FileHandle readFile()
	{	
	  fileToReturn.writeString(Base64Coder.decodeString(file.readString()), false);
	  
	  return fileToReturn;
	}
}