package Demo;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class SoundClip {
	
	//source for audio data
	private AudioInputStream sample;
	
	//looping property is read-only here
	private Clip clip;
	public Clip getClip(){return clip;}
	
	//repeat property for continuous playback
	private boolean looping = false;
	public void setLooping(boolean _looping){looping=_looping;}
	
	//repeat property used to play sound multiple times
	private int repeat=0;
	public void setRepeat(int _repeat){repeat =_repeat;}
	public int getRepaet(){return repeat;}
	
	//filename property
	private String filename="";
	public void setFilename(String _filename){filename=_filename;}
	public String getFilename(){return filename;}
	
	//property to verify when sample is ready
	public boolean isLoaded()
	{
		return (boolean)(sample!=null);
	}
	
	//constructor
	public SoundClip()
	{
		try{
			//create a sound buffer
			clip=AudioSystem.getClip();
		}catch (LineUnavailableException e){}
	}
	
	//load sound file
	public boolean load(String audiofile)
	{
		try
		{
			//prepare for inputstream
			setFilename(audiofile);
			//set audio stream source
			sample = AudioSystem.getAudioInputStream(getURL(filename));
			clip.open(sample);
			return true;
		}catch(IOException e){return false;}
		catch(UnsupportedAudioFileException e){return false;}
		catch(LineUnavailableException e){return false;}
	}
	
	private URL getURL(String filename)
	{
		URL url=null;
		try{
			url = this.getClass().getResource(filename);
		}catch(Exception e){}
		return url;
	}
	//this overloaded constructor takes a sound file as a patameter
	public SoundClip(String audiofile)
	{
		this();     //call default constructor
		load(audiofile); //
	}
	
	public void play()
	{
		//exit if the sample hasn't been loaded
		if(!isLoaded()) return;
		
		//reset
		clip.setFramePosition(0);
		//play sample with optional looping
		if(looping)
			clip.loop(clip.LOOP_CONTINUOUSLY);
		else
			clip.loop(repeat);
	}
	public void stop()
	{
		clip.stop();
	}
}

