package Demo;

import java.awt.*;
import java.util.*;
import java.lang.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Superntural extends Game
{
	//static variables for descriptionthe window
	static int FRAMERATE = 20;
	static int SCREENWIDTH = 800;
	static int SCREENHEIGHT = 600;
	
	//some important data
	final int ENEMY = 10;
	final int BULLET_SPEED = 4;
	private static int ammo = 5;
	
	//sprite state
	final int STATE_NORMAL = 0;
	final int STATE_COLLIDED = 1;
	final int STATE_EXPLODING = 2;
	
	//sprite type
	final int PLAYER = 0;
	final int ZOMBIE = 1;
	final int MONSTER = 2;
	final int DEVIL = 3;
	 //unknowing
	 final int SPRITE_EXPLOSION = 200;
	 final int SPRITE_BULLET = 100;
	 final int SPRITE_Arc = 300;
	 //the total number of Enemies
	 static int ZOMBIES = 10;
	 static int MONSTERS = 5;
	 static int DEVILS = 1;
	 private int[] CurEnemies = new int[4];
	
	 //game state
	 final int GAME_MENU = 0;
	 final int GAME_RUNNING = 1;
	 final int GAME_OVER = 2;
	 
	 
	 //some helper
	  boolean showBounds = false;
	 boolean collisionTesting = true;
	 
	 
	 //init info for player
	 final int STARTX = 20;
	 final int STARTY = 20; 
	 //define the imaged used by the game
	 ImageEntity background;
	 ImageEntity GameOver;
	 ImageEntity[] model = new ImageEntity[3]; //the picture of player
	 ImageEntity bulletImage;
	 ImageEntity arcArea;
	 ImageEntity[] explosions = new ImageEntity[2];
	 ImageEntity[] Enemy = new ImageEntity[3];
	 ImageEntity AMMO ;
	 ImageEntity MENU;
	 ImageEntity HP;
	 /***************
	  * waiting to add
	  ****************/
	 
	 //game info
	 static int score = 0;
	 int gameState =GAME_MENU;
	 SoundClip shoot  = new SoundClip();
	 SoundClip chop = new SoundClip();
	 SoundClip BGM = new SoundClip();
	 SoundClip explosion = new SoundClip();
	 SoundClip MGM = new SoundClip();
	 SoundClip Chop = new SoundClip();
	 SoundClip Death = new SoundClip();
	 SoundClip UnderAttack = new SoundClip();
	 //create the random number
	 Random rand = new Random();
	 
	 //make player invulnerable temporarily
	 long collisionTimer = 0;
	 
	 //some key input tracking
	 boolean keyLeft,keyRight,keyUp,keyDown,keyFire,keyChop;
	 
	 
	 /**********************************
	  * constructor
	  ***********************************/
	 public Superntural()
	 {
		 super(FRAMERATE,SCREENWIDTH,SCREENHEIGHT);
	 }
	 
	 /************************************
	  * startup engine
	  *************************************/
	 void gameStartup()
	 {
		 //load the background
		 background = new ImageEntity(this,graphics());
		 background.load("background.png");
		 //load the player
		 model[0] = new ImageEntity(this,graphics());
		 model[0].load("character40x70x7.png");
		 model[1] = new ImageEntity(this,graphics());
		 model[1].load("shoot1.png");
		 model[2] = new ImageEntity(this, graphics());
		 model[2].load("choppos.png");
		 AMMO = new ImageEntity(this,graphics());
		 AMMO.load("ammo.png");
		 MENU = new ImageEntity(this, graphics());
		 MENU.load("MENU.png");
		 GameOver = new ImageEntity(this,graphics());
		 GameOver.load("END.png");
		 HP = new ImageEntity(this, graphics());
		 HP.load("hp.png");
		 
		//load the music
		 explosion.load("explode.wav");
		 shoot.load("shoot.wav");
		 BGM.load("BGM.wav");
		 MGM.load("MGM.wav");
		 Chop.load("chop.wav");
		 Death.load("Death.wav");
		 UnderAttack.load("betouched.wav");
		 
		 /*model[1] = new ImageEntity(this);
		 model[1].load(" ")*/
		 
		 //init info
		 CurEnemies[ZOMBIE] = 0;
		 CurEnemies[MONSTER] = 0;
		 CurEnemies[DEVIL] = 0;
		 //create the player
		 AnimatedSprite player = new AIAnimatedSprite(this, graphics());
		 player.settype(PLAYER);
		 player.setHp(100);
		 player.setAttack(0);
		 player.setAlive(true);
		 player.setCurrentstate(STATE_NORMAL);
		 player.setAniImage(model[0].getImage(),7,1,40,70);
		 player.settotFrame(1);
		 player.setfrDelay(5);
		 player.setX(STARTX);
		 player.setY(STARTY);
		 sprites().add(player);
		 
		 //load the bullet 
		 bulletImage = new ImageEntity(this,graphics());
		 bulletImage.load("bullet.png");
		 arcArea = new ImageEntity(this, graphics());
		 arcArea.load("Arc_area.png");
		 
		 //load the explosion sprite image
		 explosions[0] = new ImageEntity(this,graphics());
		 explosions[0].load("explosion.png");
		 //explosions[1] = new ImageEntity(this);
		 //explosions[1].load("");
		 
		 //load the zombie's image
		 Enemy[0] = new ImageEntity(this,graphics());
		 Enemy[0].load("skull40x70x2.png");
		 //load the monster's image
		 /*for(int i=0;i<ZOMBIES;i++)
		 {
			 Enemy1[i] = new ImageEntity(this);
			 Enemy1[1].load("monster.png");
		 }*/
		 //load the devils'image
		 /*for(int i=0;i<ZOMBIES;i++)
		 {
			 Enemy1[i] = new ImageEntity(this);
			 Enemy1[1].load("devil.png");
		 }*/
		 //start off  in pause mode
		 //pauseGame();
		 BGM.setLooping(true);
		 BGM.play();
		 
	 }
	 
	 //reset the Game
	 private void	resetGame()
	 {
		 //restart the music sound track
		 BGM.stop();
		 ammo = 5;
		 //wipe out sprites and  item
		 sprites().clear();
		 items().clear();
		 
		 //init player
		 AnimatedSprite player = new AIAnimatedSprite(this, graphics());
		 player.settype(PLAYER);
		 player.setHp(100);
		 player.setAttack(0);
		 player.setAlive(true);
		 player.setAniImage(model[0].getImage(),7,1,40,70);
		 player.settotFrame(1);
		 player.setfrDelay(5);
		 player.setCurrentstate(STATE_NORMAL);
		 collisionTimer = System.currentTimeMillis();
		 player.setX(STARTX);
		 player.setY(STARTY);
		 sprites().add(player);
		
		 //init info
		 score = 0;
		 gameState = GAME_RUNNING;
		 ZOMBIES = 10;
		 CurEnemies[ZOMBIE] = 0;
		 player.setCurrentstate(STATE_NORMAL);
		 gameRefreshScreen();
	 }
	 
	 /********************
	  * gameupdate engine
	  *******************/
	 void gameTimeUpdate()
	 {
		 checkInput();                          //need to describe
		 
		 AnimatedSprite player = (AnimatedSprite)sprites().get(0);
		 if(!gamePaused() && player.gettype() != PLAYER)
		 {
			 Death.play();
			 gameState = GAME_OVER;
		 }
	 }
	 
	 /******************************
	  * gameRefreshScreen event 
	  *****************************/
	 void gameRefreshScreen()
	 {
		 
		 Graphics2D g2d = graphics();
		 //MENU
		 if(gameState == GAME_MENU)
		 {
			 g2d.drawImage(MENU.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);
			 g2d.setFont(new Font("Verdana",Font.BOLD,36));
			 g2d.setColor(Color.BLACK);
			 g2d.drawString("Supernatural", 252, 202);
			 g2d.setColor(new Color(200,30,30));
			 g2d.drawString("Supernatural", 250, 200);
			 
			 int x = 270,y = 15;
			 g2d.setFont(new Font("宋体",Font.ITALIC | Font.BOLD,20));
			 g2d.setColor(Color.BLACK);
			 g2d.drawString("控制键:", x,++y*20);
			 g2d.drawString("上: w " , x + 20, ++y * 20);
			 g2d.drawString("下: s " , x + 20, ++y * 20);
			 g2d.drawString("右: d " , x + 20, ++y * 20);
			 g2d.drawString("左: a " , x + 20, ++y * 20);
			 g2d.drawString("开火: j" , x + 20, ++y * 20);
			 g2d.drawString("砍: k" , x + 20, ++y * 20);
			 g2d.drawString("游戏简介：Sam 和 Dean两兄弟在次踏上了猎鬼之旅，" , x - 50, ++y * 20);
			 g2d.drawString("这一次Dean不小心走散，误入魔窟，你的子弹有限，" , x - 50, ++y * 20);
			 g2d.drawString("避开怪物，尽可能多的消灭怪物！" , x - 50, ++y * 20);
			 
			 g2d.setFont(new Font("Ariel",Font.BOLD,24));
			 g2d.setColor(Color.CYAN);
			 g2d.drawString("press enter to hunting!" , 280, 570);
			 return;
		 }
		 if(gameState == GAME_RUNNING)
		 {
			 
			 AnimatedSprite player = (AnimatedSprite)sprites().get(0);
		 
			 ZOMBIES = 10 +score / 20;
			 //draw the background
			 g2d.drawImage(background.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);
		 
			 //print status info on the screen
			 g2d.setFont(new Font("宋体",Font.ITALIC | Font.BOLD,20));
			 g2d.setColor(Color.BLACK);
			 g2d.drawString("Dean生命值" + player.getHp(), 5, 15);
		 
			 g2d.drawString("当前得分 ： " + score, 5, 35);
			 
			 //show the position of mouse
			 g2d.drawString("帧率:"+frameRate(), 500, 15);
			 g2d.drawString("子弹数量 = " + ammo, 500, 35);
		 
		 
			 clearBody();
			 if(CurEnemies[ZOMBIE] < ZOMBIES)
			 createEnemy(ZOMBIE);
		 
			 //auto attack
			 for(int i =0 ;i<sprites().size();i++ )
			 {
				 AnimatedSprite spr = (AnimatedSprite)sprites().get(i);
				 if(spr.gettype() ==ZOMBIE || spr.gettype() ==MONSTER || spr.gettype()==DEVIL)
				 {
					 AIAnimatedSprite ene = (AIAnimatedSprite)sprites().get(i);
					 ene.auto(sprites());
				 }
			 }
			 return;
		}
		 if(gameState == GAME_OVER)
		 {
			 g2d.drawImage(GameOver.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);
			 g2d.setFont(new Font("Verdana",Font.BOLD,24));
			 g2d.setColor(new Color(100,30,30));
			 g2d.drawString("the road so far....", 260, 200);
			 
			 g2d.setFont(new Font("宋体",Font.CENTER_BASELINE,24));
			 g2d.setColor(Color.DARK_GRAY);
			 g2d.drawString("your score is " + score,260 , 350);
			 if(score < 50)
			 {
				 g2d.drawString("称号 :新手上路" ,260 , 400);
			 }
			if(score>=50 && score<200)
			 {
				 g2d.drawString("称号 :月光银刃" ,260 , 400);
			 }
			 if(score >= 200 && score < 500)
			 {
				 g2d.drawString("称号 :枪炮天使" ,260 , 400);
			 }
			 if(score >=500)
			 {
				 g2d.drawString("称号 :上帝之手" ,260 , 400);
			 }
			 g2d.drawString("Press r to Restart", 260, 500);
			 stop();
		 }
		 
		 
		 
	 }
	 
	 /*******************************************
	  * gameshutdown event pass by ganme engine
	  *****************************************/
	 void gameShutdown()
	 {
		 BGM.stop();
		 MGM.stop();
		 shoot.stop();
		 explosion.stop();
	 }
	 
	 /************************************************
	  * spriteUpdate event passed by game engine
	  ************************************************/
	 public void spriteUpdate(AnimatedSprite sprite)
	 {
		 switch(sprite.gettype())
		 {
		 case PLAYER:
		 case ZOMBIE:
		 case MONSTER:
		 case DEVIL:
		 case SPRITE_BULLET:
			 warp(sprite);               //need to describe
			 break;
		 case  SPRITE_EXPLOSION:
			 if(sprite.getcurrFrame() == sprite.gettotFrame() - 1)
			 {
				 sprite.setAlive(false);
			 }
			 break;
		 case SPRITE_Arc:
			 if(sprite.getcurrFrame() == sprite.gettotFrame() - 1)
			 {
				 sprite.setAlive(false);
			 }
			 break;
		 }
	 }
	 
	 /*****************************************************
	  * if collided
	  ***************************************************/
	 public void spriteDraw(AnimatedSprite sprite)
	 {
		 if(showBounds)
		 {
			 if(sprite.getCurrentstate() == STATE_COLLIDED)
				 sprite.drawBounds(Color.RED);
			else
				sprite.drawBounds(Color.BLUE);
		 }
	 }
	 
	 /*****************************
	  * sprite dying
	  ************************/
	 public void clearBody()
	 {
		 for(int i =0 ;i < sprites().size(); i++)
		 {
			 AnimatedSprite spr = (AnimatedSprite)sprites().get(i);
			 if(spr.gettype() == SPRITE_EXPLOSION)
				 return;
			 if(spr.getHp() <= 0)
			 {
				 spr.setAlive(false);
				 //sprites().remove(i);
				 if(spr.gettype() == ZOMBIE)
				 {
					 CurEnemies[spr.gettype()]--;
					 breakSprite(spr); 
					 int t = rand.nextInt(10);
					 if(t<3)
					 createAmmo(spr);
				 }
			 }
				 
		 }
	 }
	 public void spriteDying(AnimatedSprite sprite)
	 {
		 //no need now
	 }
	 
	 /**************************
	  * sprite collision event
	  ****************************/
	 public void spriteCollision(AnimatedSprite spr1,AnimatedSprite spr2)
	 {
		 //if turn off the test then return 
		 if(!collisionTesting) return;
		 //figure out what type of sprite collided
		 switch(spr1.gettype())
		 {
		 case SPRITE_BULLET:
		 		if(spr2.gettype() == ZOMBIE || spr2.gettype() == MONSTER ||spr2.gettype() == DEVIL)
		 		{
		 			spr1.setAlive(false);
		 			//spr2.setAlive(false);
		 			spr2.setHp(spr2.getHp() - spr1.attack());
		 		}
		 		break;
		 case	SPRITE_Arc:
		 		if(spr2.gettype() == ZOMBIE || spr2.gettype() == MONSTER ||spr2.gettype() == DEVIL)
		 		{
		 			spr2.setHp(spr2.getHp() - spr1.attack());
		 		}
		 		break;
		 case PLAYER:			
			 if(spr2.gettype() == ZOMBIE || spr2.gettype() == MONSTER ||spr2.gettype() == DEVIL)
			 {
				 if(spr1.getCurrentstate() == STATE_NORMAL)
				 {
					 UnderAttack.play();
					 spr1.setHp(spr1.getHp() - spr2.attack());
					 if(spr2.getfD() == 0)
						 spr1.setX(spr1.X() - 10);
					 else
						 spr1.setX(spr1.X() + 10);
					 //System.out.println(spr1.getHp());
					 collisionTimer = System.currentTimeMillis();
					 spr1.setCurrentstate(STATE_NORMAL);
				 }
				 else if(spr1.getCurrentstate() == STATE_COLLIDED)
				 {
					 if(collisionTimer + 3000 < System.currentTimeMillis())
					 {
						 spr1.setCurrentstate(STATE_NORMAL);
					 }
				 }
			 }
			 break;
		 }
	 }
	 
	 protected void addAmmo()
	{
			//test each sprites
			AnimatedSprite player = (AnimatedSprite) sprites().get(0);
				for(int j=0;j<items().size();j++)
				{
					ImageEntity ite = (ImageEntity) items().get(j);
					if(ite.isAlive())
					{
						if(player.isEat(ite))
						{
							ite.setAlive(false);
							ammo +=5;
							break;
						}
					}
				}
		}
	 
	 //keyevent
	 public void gameKeyDown(int keycode)
		{
		 	if(keycode != KeyEvent.VK_J)
		 	{
		 		AnimatedSprite spr =(AnimatedSprite) sprites().get(0);
		 		spr.settotFrame(7);
		 	}
		 	switch(keycode)
			{
				case KeyEvent.VK_A:
					//move left
					keyLeft = true;
					break;
				case KeyEvent.VK_D:
					//move right
					keyRight = true;
					break;
				case KeyEvent.VK_W:
					//accelerate
					keyUp = true;
					break;
				case KeyEvent.VK_S:
					//slow
					keyDown = true;
					break;
				case KeyEvent.VK_J:
					if(ammo > 0)
					fireBullet();            //need to describe
					//fire!
					keyFire = true;
					break;
				case KeyEvent.VK_K:
					con_chop();
					//chop!
					keyChop = true;
					break;
				case KeyEvent.VK_ENTER:
					if(gameState == GAME_MENU)
					{
						ammo = 5;
						resumeGame();
						MGM.setLooping(true);
						MGM.play();
						resetGame();
					}
						break;
				case KeyEvent.VK_ESCAPE:
					if(gameState == GAME_RUNNING)
					{
						pauseGame();
						gameState = GAME_OVER;
					}
					break;
					
				case KeyEvent.VK_R:
					if(gameState == GAME_OVER)
					{
						resumeGame();
						MGM.setLooping(true);
						MGM.play();
						start();
						resetGame();
					}
					break;
				case KeyEvent.VK_SPACE:
					showBounds=!showBounds;
					break;
				case KeyEvent.VK_Z:
					collisionTesting = !collisionTesting;
					break;
			}
		}
	 
	 public void gameKeyUp(int keycode)
		{
			AnimatedSprite spr =(AnimatedSprite) sprites().get(0);
			spr.settotFrame(1);
			switch(keycode)
			{
				case KeyEvent.VK_A:
					//move left
					keyLeft = false;
					break;
				case KeyEvent.VK_D:
					//move right
					keyRight = false;
					break;
				case KeyEvent.VK_W:
					//accelerate
					keyUp = false;
					break;
				case KeyEvent.VK_S:
					//slow
					keyDown = false;
					break;
				case KeyEvent.VK_J:
					//fire!
					keyFire = false;
					spr.setAniImage(model[0].getImage(),7,1,40,70);
					spr.settotFrame(7);
					spr.setfrDelay(5);
					break;
				case KeyEvent.VK_K:
					//chop!
					keyChop = false;
					spr.setAniImage(model[0].getImage(),7,1,40,70);
					spr.settotFrame(7);
					spr.setfrDelay(5);
					break;
			}
		}
	 
	 //no use key evnet
	 public void gameMouseDown(){}
	 public void gameMouseUp(){}
	 public void gameMouseMove(){}
	 
	 /**********************************
	  * create a sprite by its id
	  *********************************/
	 public void createEnemy(int enemy)
	 {
		 CurEnemies[enemy]++;
		 AIAnimatedSprite ene = new AIAnimatedSprite(this, graphics());
		 ene.settype(enemy);
		 ene.setHp(100);
		 ene.setAttack(10);
		 ene.setAniImage(Enemy[0].getImage(), 2, 1, 40,70);
		 ene.settotFrame(2);
		 ene.setfrDelay(20);
		 ene.setfrWidth(40);
		 ene.setfrHeight(70);
		 ene.setAlive(true);
		 ene.setCurrentstate(STATE_NORMAL);
		 //set to a random position
		 int x = rand.nextInt(SCREENWIDTH - Enemy[enemy - 1].width());
		 int y = rand.nextInt(SCREENHEIGHT - Enemy[enemy - 1].height());
		 ene.setX(x);
		 ene.setY(y);
		 
		 //add to the sprite list
		 sprites().add(ene);
	 }
	
	 public void createAmmo(AnimatedSprite spr)
	 {
		 double x = spr.getCenterX() - 8;
		 double y = spr.getCenterY() - 8;
		 ImageEntity ammo = new ImageEntity(this, graphics());
		 ammo.setAlive(true);
		 ammo.setImage(AMMO.getImage());
		 ammo.setx(x);
		 ammo.sety(y);
		 items().add(ammo);
	 }
	 /****************************************
	  * process keys that have been pressed
	  ****************************************/
	 public void checkInput()
	 {
		AnimatedSprite player = (AnimatedSprite)sprites().get(0);
	 	if(keyLeft)
	 	{
			player.setfD(180);
			player.setX(player.X()-3);
		}
		if(keyRight)
		{
			player.setfD(0);
			player.setX(player.X()+3);
		}
		if(keyUp)
		{
			player.setY(player.Y()-3);
		}
		if(keyDown)
		{
			player.setY(player.Y()+3);
		}
	 }
	 
	 /*************************
	  * fire a bullet event
	  ************************/
	 public void fireBullet()
	 {
		 //get the player info
		 ammo --;
		 shoot.play();
		 AnimatedSprite player = (AnimatedSprite)sprites().get(0);
		 player.setAniImage(model[1].getImage(),1,1,100,70);
		 player.settotFrame(1);
		 player.setfrDelay(5);
		 //create the new bullet sprite
		 AnimatedSprite bullet = new AnimatedSprite(this, graphics());	 
		 bullet.settype(SPRITE_BULLET);
		 bullet.setHp(5);
		 bullet.setAttack(80);
		 bullet.setAlive(true);
		 bullet.setAniImage(bulletImage.getImage(), 1, 1, 18, 18);
		
		 bullet.setfrDelay(2);
		 //bullet.setlifespan();
		 bullet.setfD(player.getfD());
		 
		 //set the start position
		 double x = player.X();
		 double y = player.Y();
		 double addX = 0.0;
		 if(bullet.getfD() == 0)
		 addX = 55;
		 else
			 addX =-15;
		 bullet.setX(x + addX);
		 bullet.setY(y);
		 
		 double svx = 0, svy =0;
		 if(bullet.getfD() == 180)
			 svx = -10;
		 if(bullet.getfD() == 0)
			 svx = 10;
		 bullet.setvelx(svx);
		 bullet.setvely(svy);
		 
		 //add bullet to the sprite list
		 sprites().add(bullet);
	 }
	 /************************
	  * normal attack event
	  ***********************/
	 public void con_chop()
	 {
		 //get the player info
		 Chop.play();
		 AnimatedSprite player = (AnimatedSprite)sprites().get(0);
		 player.setAniImage(model[2].getImage(),1,1,40,70);
		 player.settotFrame(1);
		 player.setfrDelay(2);
		 //create the new bullet sprite
		 AnimatedSprite Arc = new AnimatedSprite(this, graphics());	 
		 Arc.settype(SPRITE_Arc); 
		 Arc.setAlive(true);
		 Arc.setHp(60);
		 Arc.setAttack(30);
		 Arc.setAniImage(arcArea.getImage(), 2, 1, 30, 70);
		 Arc.setfrDelay(5);
		 //bullet.setlifespan();
		 Arc.setfD(player.getfD());
		 
		 //set the start position
		 double x = player.X();
		 double y = player.Y();
		 double addX = 0.0;
		 if(Arc.getfD() == 0)
		 addX = 40;
		 else
			 addX =-40;
		 Arc.setX(x + addX);
		 Arc.setY(y);
		 
		 //add bullet to the sprite list
		 sprites().add(Arc);
	 }
	 
	 public void breakSprite(AnimatedSprite spr)
	 {
		 //create a new explosion at passed location
		 addScore();
		 double x = spr.getCenterX() -48;
		 double y = spr.getCenterY() -48;
		 AnimatedSprite expl = new AnimatedSprite(this, graphics());
		 expl.settype(SPRITE_EXPLOSION);
		 expl.setHp(10);
		 expl.setAlive(true);
		 expl.setAniImage(explosions[0].getImage(),4,4,96,96);
		 expl.settotFrame(16);
		 expl.setfrDelay(2);
		 expl.setX(x);
		 expl.setY(y);
		 explosion.play();
		 sprites().add(expl);
	 }
	 
	 public void warp(AnimatedSprite spr)
	 {
		 //create the some shortcut variables
		 int w = spr.getfrWidth() - 1;
		 int h = spr.getfrHeight() -1;
		 //warp the sprites
		 if(spr.X()< 0 - w)
		 {
			 if(spr.gettype() == SPRITE_BULLET)
				 spr.setAlive(false);
			 else
				 spr.setX(SCREENWIDTH);
		 }
		 else if(spr.X() > SCREENWIDTH )
		 {
			 if(spr.gettype() == SPRITE_BULLET)
				 spr.setAlive(false);
			 else
				 spr.setX(0 - w);
		 }
		 if(spr.Y()< 0 -h)
		 {
			 if(spr.gettype() == SPRITE_BULLET)
				 spr.setAlive(false);
			 else
				 spr.setY(SCREENHEIGHT);
		 }
		 else if(spr.Y() > SCREENHEIGHT)
		 {
			 if(spr.gettype() == SPRITE_BULLET)
				 spr.setAlive(false);
			 else
				 spr.setY(0 - h);
		 }
	 }
	 
	 public void addScore()
	 {
		 score += 10;
	 }

}

