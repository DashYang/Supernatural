package Demo;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.System;
import javax.swing.*;
import java.util.*;

abstract class Game extends Applet implements Runnable,KeyListener,MouseListener,MouseMotionListener
{
	//the main loop
	private Thread gameloop;
	
	//internal list of sprites
	private LinkedList _sprites;
	private LinkedList _item;
	public LinkedList sprites(){return _sprites;}
	public LinkedList items(){return _item;}
	
	//screen and buffer related variables
	private BufferedImage backbuffer;
	private Graphics2D g2d;
	private int screenWidth,screenHeight;
	
	//keep track of mouse position and buttons
	private int mousePosX = 0;
	private int mousePosY = 0;
	private boolean mouseButton[] = new boolean[4];
	
	//frame rate counters and other timing variables
	private int _framecount = 0;
	private int _frameRate = 0;
	private int desireRate;
	private long startTime = System.currentTimeMillis();
	
	//local applet object
	public Applet applet(){return this;}
	
	//game pause state
	private boolean _gamePaused = false;
	public boolean gamePaused(){return _gamePaused;}
	public void pauseGame(){_gamePaused = true;}
	public void resumeGame(){_gamePaused = false;}
			
	//declare the game event methods that sub-class must implement
	abstract void gameStartup();
	abstract void gameTimeUpdate();
	abstract void gameRefreshScreen();
	abstract void gameShutdown();
	abstract void gameKeyDown(int keyCode);
	abstract void gameKeyUp(int keyCode);
	abstract void gameMouseDown();
	abstract void gameMouseUp();
	abstract void gameMouseMove();
	abstract void spriteUpdate(AnimatedSprite sprite);
	abstract void spriteDraw(AnimatedSprite sprite);
	abstract void spriteDying(AnimatedSprite sprite);
	abstract void spriteCollision(AnimatedSprite sp1,AnimatedSprite sp2);
	abstract void addAmmo();

	//constructor
	public Game(int frameRate,int width,int height)
	{
		desireRate = frameRate;
		screenWidth = width;
		screenHeight = height;
	}
	
	
	//return g2d object so sub-class can draw things
	public Graphics2D graphics(){return g2d;}
	
	//current frame rate
	public int frameRate(){ return _frameRate;}
	
	//mouse buttons and movement
	public boolean mouseButton(int btn){return mouseButton(btn);}
	public int mousePointX(){return mousePosX;}
	public int mousePointY(){return mousePosY;}
	
	
	//init event method
	public void init()
	{
		//create the back buffer and drawing surface
		resize(screenWidth,screenHeight);
		backbuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		
		//create the internal sprite list
		_sprites = new LinkedList<AnimatedSprite>();
		_item = new LinkedList<ImageEntity>();
		//start the input listener
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//this method implemented by sub-class
		gameStartup();
	}
	
	//applet update event method
	public void update(Graphics g)
	{
		//calculate the frame rate
		_framecount++;
		if(System.currentTimeMillis() > startTime + 1000 )
		{
			startTime = System.currentTimeMillis();
			_frameRate = _framecount;
			_framecount = 0;
			
			
			//clear the dead
			//purgeSprites();
		}
		//this method implemented by sub-class
		purgeSprites();
		gameRefreshScreen();
		purgeSprites();
		//draw the internal list of sprites 
		if(!gamePaused())
		{
			drawSprites();
			drawItem();
		}
		
		//redraw the screen
		paint(g);
	}
	
	
	//applet window paint event method
	public void paint(Graphics g)
	{
		g.drawImage(backbuffer, 0, 0, this);
	}
	
	// thread start 
	public void start()
	{
		gameloop = new Thread(this);
		gameloop.start();
	}
	
	//thread  run event (gameloop)
	public void run()
	{
		// acquite the current thread
		Thread t=Thread.currentThread();
		
		//process the main thread
		while(t==gameloop)
		{
			try
			{
				//set a consistent frame rate
				Thread.sleep(1000/desireRate);
			}catch(InterruptedException e){e.printStackTrace();}
		
			//update the internal list of sprites
			if(!gamePaused())
			{
				updateSprites();
				testCollisions();
				addAmmo();
				gameTimeUpdate();
			}
		
			//allow main game to update if needed
			
		
			//refresh the screen
			repaint();
		}
	}
	
	
	//thread stop event 
	public void stop()
	{
		//kill the loop
		gameloop = null;
		
		//method implemented by sub-class
		gameShutdown();
	}
	
	//keylistener event
	public void keyTyped(KeyEvent k){}
	public void keyPressed(KeyEvent k)
	{
		gameKeyDown(k.getKeyCode());
	}
	public void keyReleased(KeyEvent k)
	{
		gameKeyUp(k.getKeyCode());
	}
	
	//checkButtons stores the state of the mouse buttons
	private void checkButtons(MouseEvent e)
	{
		switch(e.getButton())
		{
		case MouseEvent.BUTTON1:
			mouseButton[1] = true;
			mouseButton[2] = false;
			mouseButton[3] = false;
			break;
		case MouseEvent.BUTTON2:
			mouseButton[1] = false;
			mouseButton[2] = true;
			mouseButton[3] = false;
			break;
		case MouseEvent.BUTTON3:
			mouseButton[1] = false;
			mouseButton[2] = false;
			mouseButton[3] = true;
			break;
		}
	}
	
	//mouse listener event
	public void mousePressed(MouseEvent e)
	{
		checkButtons(e);
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseDown();
	}
	
	public void mouseReleased(MouseEvent e)
	{
		checkButtons(e);
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseUp();
	}
	
	public void mouseMoved(MouseEvent e)
	{
		checkButtons(e);
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseDown();
	}
	
	public void mouseDragged(MouseEvent e)
	{
		checkButtons(e);
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseDown();
		gameMouseMove();
	}
	
	public void mouseEntered(MouseEvent e)
	{
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseMove();
	}
	
	public void mouseExited(MouseEvent e)
	{
		mousePosX=e.getX();
		mousePosY=e.getY();
		gameMouseMove();
	}
	
	public  void mouseClicked(MouseEvent e){}   //no use
	
	
	//update the sprite list from the game loop thread
	
	protected void updateSprites()
	{
		for(int i=0;i<_sprites.size();i++)
		{
			AnimatedSprite spr = (AnimatedSprite) _sprites.get(i);
			if(spr.isAlive())
			{
				spr.updatePosition();
				spr.updateAnimation();
				spriteUpdate(spr);
			}
			else
			{
				spriteDying(spr);
			}
		}
	}
	
	//peroform collision testing of all active sprites
	protected void testCollisions()
	{
		//test each sprites
		for(int i =0;i<_sprites.size();i++)
		{
			AnimatedSprite spr1 = (AnimatedSprite) _sprites.get(i);
			if(spr1.isAlive())
			{
				for(int j=0;j<_sprites.size();j++)
				{
					AnimatedSprite spr2 = (AnimatedSprite) _sprites.get(j);
					if(spr2.isAlive() && i!=j)
					{
						if(spr1.collidesWith(spr2))
						{
							//System.out.println("knock");
							spriteCollision(spr1, spr2);
							break;
						}
						else
						{
							//spr1.setCollided(false);
						}
					}
				}
			}
		}
	}
	

	//draw all active sprites
	//sprite lower in the list are drawn on top
	protected void drawSprites()
	{
		if(gamePaused() == true)
			return;
		//draw sprites in order
		for(int i=0;i<_sprites.size();i++)
		{
			AnimatedSprite spr = (AnimatedSprite) _sprites.get(i);
			if(spr.isAlive())
			{
				spr.updateAnimation();
				spr.draw();
				spriteDraw(spr);      //show bounds
			}
		}
	}
	
	protected void drawItem()
	{
		for(int i = 0;i<_item.size();i++)
		{
			ImageEntity ite = (ImageEntity) _item.get(i);
			if(ite.isAlive())
			{
				ite.draw();
			}
		}
	}
	//clear the dead
	private void purgeSprites()
	{
		for(int i=0;i<_sprites.size();i++)
		{
			AnimatedSprite spr = (AnimatedSprite) _sprites.get(i);
			if(!spr.isAlive())
			_sprites.remove(i);
		}
		for(int i=0;i<_item.size();i++)
		{
			ImageEntity ite = (ImageEntity) _item.get(i);
			if(!ite.isAlive())
			_item.remove(i);
		}
	}
}
