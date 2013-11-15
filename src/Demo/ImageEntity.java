package Demo;

import java.awt.*;
import java.awt.geom.*;
import java.applet.*;
import java.net.*;

public class ImageEntity 
{
	//the base physics info
	private boolean alive;
	private double x,y;          //position
	private double velx,vely;    //velocity
	private double faceDirection;//    
	
	//the base image element
	private Image image;
	private Applet applet;
	private Graphics2D g2d;
	
	//the get method
	public boolean isAlive(){return alive;}
	public double X(){return x;}
	public double Y(){return y;}
	public double getVx(){return velx;}
	public double getVy(){return vely;}
	public double getfD(){return faceDirection;}
	public Image getImage(){return image;}
	public Applet getApp(){return applet;}
	public Graphics2D getG2d(){return g2d;}
	
	//the set method
	public void setAlive(boolean alive){this.alive=alive;}
	public void setx(double x){this.x=x;}
	public void sety(double y){this.y=y;}
	public void setvelx(double x){this.velx=x;}
	public void setvely(double y){this.vely=y;}
	public void setfD(double angle){this.faceDirection = angle;}
	public void setImage(Image image){this.image=image;}
	public void setApp(Applet a){this.applet = a;}
	public void setG2d(Graphics2D g2d){this.g2d = g2d;}
	
	//other method to support
	public int width()
	{
		return getImage().getWidth(applet);
	}
	public int height()
	{
		return getImage().getHeight(applet);
	}
	public double getCenterX()
	{
		return X()+width()/2;
	}
	public double getCenterY()
	{
		return Y()+height()/2;
	}
	private URL getURL(String filename)
	{
		URL url=null;
		try{
			url=this.getClass().getResource(filename);
		}catch(Exception e){}
		return url;
	}
	public void load(String filename)
	{
		setImage(applet.getImage(getURL(filename)));
	}
	
	public void draw()
	{
		int x = (int)X();
		int y = (int)Y();
		if(getfD() == 0)
			g2d.drawImage(getImage(),x, y,width(),height(),applet);
		else
			g2d.drawImage(getImage(),x+width(), y,-width(),height(),applet);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)X(),(int)Y(),width(),height());
	}
	//constructor
	public ImageEntity(Applet a,Graphics2D g2d)
	{
		//physic ele
		setAlive(false);
		setx(0);
		sety(0);
		setvelx(0.0);
		setvely(0.0);
		setfD(0);
		
		//image
		applet = a;
		this.g2d = g2d;
		setImage(null);
	}
	
}
