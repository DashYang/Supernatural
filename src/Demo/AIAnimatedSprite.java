package Demo;

import java.applet.Applet;
import java.awt.Graphics2D;
import java.util.*;
import java.math.*;

public class AIAnimatedSprite extends AnimatedSprite 
{
	
	//sprite type
	final int ZOMBIE = 1;
	final int MONSTER = 2;
	final int DEVIL = 3;
	 //NO AI
	final int PLAYER = 0;
	final int SPRITE_EXPLOSION = 200;
	final int SPRITE_BULLET = 100;
	AIAnimatedSprite(Applet applet,Graphics2D g2d)
	{
		super(applet,g2d);
	}
	
	public void auto(LinkedList sprites)    //auto to attack
	{
		if(gettype() == PLAYER ||gettype() == SPRITE_EXPLOSION || gettype() == SPRITE_BULLET)
			return ;
		AnimatedSprite tar = (AnimatedSprite)sprites.get(0);  //player or the target;
		int[][] move ={{-1,0},{0,1},{1,0},{0,-1}};
		int index = -1;
		double dis = 500000000.0;
		for(int i =0;i < 4; i++)
		{
			double x = this.X() + move[i][0];
			double y = this.Y() + move[i][1];
			double tdis = Math.abs(x-tar.X()) + Math.abs(y - tar.Y());
			if(dis > tdis )
			{
				dis = tdis;
				index = i;
			}
		}
		
		double nx = this.X();
		double ny = this.Y();
		if(index >= 0)
		{
			this.setX(nx + move[index][0]);
			this.setY(ny + move[index][1]);
		}	
		if(tar.X()>nx)
			this.setfD(180);
		else
			this.setfD(0);
	}
}
