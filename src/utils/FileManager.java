package utils;

import java.util.logging.FileHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager {

	private FileHandle file;
	private Base64Coder coder;
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
	
	public FileHandle readFile()
	{
		
	  FileHandle fileReturn = null;
	  fileReturn.writeString(coder.decodeString(file.readString()), false);
	  
	  return fileReturn;
	}
}
