package shooter;

import java.awt.Graphics;

public  class BigPlaneAward extends BigPlane implements Award{

	public void paint(Graphics g){
		super.paint(g);
		g.drawRect((int)x,(int) y,(int) width,(int) height);//´ò·É»úÍâ²à¿ò
		
	}
	public int getAward(){
		return DOUBLE_FIRE;
	}

}
