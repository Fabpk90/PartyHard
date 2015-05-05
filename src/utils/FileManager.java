package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager {

	private FileHandle file;
	private Base64Coder coder;
	private FileHandle fileToReturn;
	public FileManager(String filePath) {
		
		file = Gdx.files.local(filePath);
		
	}

	public void saveFile(boolean encoded, FileHandle fileToSave)
	{
		if(encoded)
		{								
			file.writeString(coder.encodeString(fileToSave.readString()), false);
		}
		else
			file.writeString(fileToSave.readString(), false);		
	}
	
	public void saveFile(boolean encoded, String fileToSave)
	{
		if(encoded)
		{								
			file.writeString(coder.encodeString(fileToSave), false);
		}
		else
			file.writeString(fileToSave, false);		
	}
	
	public FileHandle readFile()
	{	
	  fileToReturn.writeString(coder.decodeString(file.readString()), false);
	  
	  return fileToReturn;
	}
}