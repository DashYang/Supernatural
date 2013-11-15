package Demo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.applet.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

public class AnimatedSprite 
{
	//info for image
	private ImageEntity entity;
	private Image animimage;
	private int currFrame,totFrames;
	private int animDir;
	private int frCount,frDelay;
	private int frWidth,frHeight;
	private int cols;
    BufferedImage tempImage;
	Graphics2D tempSurface;
	//info for the sprite
	private int Hp;    
	private int type;       
	private int currentState;
	private int attack;
	
	//get method
	public double X(){return entity.X();}
	public double Y(){return entity.Y();}
	public int attack(){return attack;}
	public Image getAniImage(){return animimage;}
	public boolean isAlive(){return entity.isAlive();}
	public int getcurrFrame(){return currFrame;}
	public int gettotFrame(){return totFrames;}
	public int getanimDir(){return animDir;}
	public int getfrCount(){return frCount;}
	public int getfrDelay(){return frDelay;}
	public int getfrWidth(){return frWidth;}
	public int getfrHeight(){return frHeight;}
	public int getcols(){return cols;}
	public int getHp(){return Hp;}
	public int gettype(){return type;}
	public int getCurrentstate(){return currentState;}
	public Rectangle getBound(){return entity.getBounds();}
	public double getfD(){return entity.getfD();}
	public double getVx(){return entity.getVx();}
	public double getVy(){return entity.getVy();}
	public ImageEntity getImageEntity(){ return entity;}
	public double getCenterX()
	{
		return entity.getCenterX();
	}
	public double getCenterY()
	{
		return entity.getCenterY();
	}
	
	//set mehtod
	public void setX(double x){entity.setx(x);}
	public void setY(double y){entity.sety(y);}
	public void setAttack(int a){this.attack = a;}
	
	
	public void setAlive(boolean alive){entity.setAlive(alive);}
	public void setAniImage(Image ima,int columns,int rows,int width,int height)
	{
		this.animimage = ima;
		setcols(columns); 
		settotFrame(columns * rows);
		setfrWidth(width);
		setfrHeight(height);
		tempImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		tempSurface = tempImage.createGraphics();
		entity.setImage(tempImage);
	}
	public void setImage(Image ima){entity.setImage(ima);}
	public void setcurrFrame(int t){this.currFrame = t;}
	public void settotFrame(int t){this.totFrames = t;}
	public void setanimDir(int t){this.animDir = t;}
	public void setfrCount(int t){this.frCount = t;}
	public void setfrDelay(int t){this.frDelay = t;}
	public void setfrWidth(int t){this.frWidth = t;}
	public void setfrHeight(int t){this.frHeight = t ;}
	public void setcols(int t){this.cols = t;}
	public void setHp(int t){this.Hp = t;}
	public void settype(int t){this.type = t;}
	public void setCurrentstate(int t){this.currentState = t;}
	public void setfD(double angle){entity.setfD(angle); }
	public void setvelx(double x){entity.setvelx(x);}
	public void setvely(double y){entity.setvely(y);}
	
	//supprot method
	private URL getURL(String filename)
	{
		URL url = null;
		try{
			url= this.getClass().getResource(filename);
		}
		catch(Exception e){}
		return url;
	}
	
	public void load(String filename,int columns,int rows,int width,int height)
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		animimage = tk.getImage(getURL(filename));
		setcols(columns); 
		settotFrame(columns * rows);
		setfrWidth(width);
		setfrHeight(height);
		tempImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		tempSurface = tempImage.createGraphics();
		entity.setImage(tempImage);
	}
	
	//constructor
	AnimatedSprite(Applet applet,Graphics2D g2d)
	{
		entity = new ImageEntity(applet,g2d);
		tempSurface = g2d;
		currFrame = 0;
		totFrames = 0;
		animDir = 1;
		frCount = 0;
		frDelay = 0;
		frWidth = 0;
		frHeight = 0;
		cols = 1;
		
		Hp = 0;
		type = 0;
		currentState = 0;
	}
	public void updateAnimation()
	{
		frCount++;
		if(getfrCount()>getfrDelay())
		{
			setfrCount(0);
			//update the animation
			setcurrFrame(getcurrFrame()+getanimDir());
			if(getcurrFrame()>gettotFrame() - 1)
			{
				setcurrFrame(0);
			}
			else if(getcurrFrame() < 0)
			{
				setcurrFrame(gettotFrame() - 1);
			}
		}
	}
	
	public void draw()
	{
		int fx = (getcurrFrame()%getcols())*getfrWidth();
		int fy = (getcurrFrame()/getcols())*getfrHeight();
		
		//copy the frame onto the temp image
		tempImage.flush();
		tempImage = new BufferedImage(getfrWidth(),getfrHeight(),BufferedImage.TYPE_INT_ARGB);
		tempSurface = tempImage.createGraphics();
		tempSurface.drawImage(animimage, 0 ,0,getfrWidth() , getfrHeight() , fx, fy, fx+getfrWidth(), fy+getfrHeight(), entity.getApp());
		//pass the temp image on to the parent class
		entity.setImage(tempImage);
		entity.draw();
	}
	
	public void updatePosition()
	{
		double x = entity.X();
		double y = entity.Y();
		double svx = entity.getVx();
		double svy = entity.getVy();
		entity.setx(x + svx);
		entity.sety( svy + y);
	}
	
	public boolean collidesWith(AnimatedSprite spr)
	{
		return (entity.getBounds().intersects(spr.getBound()));
	}
	
	public boolean isEat(ImageEntity ite)
	{
		return (entity.getBounds().intersects(ite.getBounds()));
	}
	
	public void drawBounds(Color c)
	{
		entity.getG2d().setColor(c);
		entity.getG2d().draw(getBound());
	}
}
