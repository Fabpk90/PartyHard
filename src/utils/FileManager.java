package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

public class FileManager {

	private FileHandle file;
	public FileManager(String filePath) 
	{		
		file = Gdx.files.local(filePath);		
	}
	
	public FileManager()
	{
		
	}
	
	public void loadFile(String path)
	{
		file = Gdx.files.local(path);
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

	/**
	 * Attention need to encode the file after this
	 * @return the decoded file
	 */
	public FileHandle getDecodedFile()
	{	
		file.writeString(Base64Coder.decodeString(file.readString()), false);
		
		return file;
	}
	
	public FileHandle getFile()
	{
		return file;
	}
}