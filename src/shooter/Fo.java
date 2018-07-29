package shooter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class Fo {
	protected double x, y, width, height;
	//飞行物移动
	protected double step;
	//当前正在显示的图片
	//命
	protected int life;
	//飞行状态
	protected int state;
	
	public final static int ACTIVE = 0;
	public final static int DEAD = 1;
	public final static int REMOVE = 2;
	
	protected BufferedImage image;
    public Fo(double width,double height){
    	this();
    	this.width = width;
    	this.height = height;
    	x = Math.random()*(480-width);
    	y = -height;
    	step = Math.random()*3+0.8;
    }
    //留给子弹  英雄 天空等的无参构造器
    public Fo(){
    	life = 1;
    	state = ACTIVE;
    }
   
	public String toString(){
    	return x+","+y+","+width+","+height+","+life+","+state+","+image;
    }
	/*
	 * 在Fo中添加paint方法
	 * 利用画笔对象g绘制当前图片
	 * @param g传递来的画笔引用
	 */
	public void paint(Graphics g){
		g.drawImage(image,(int)x, (int)y,null);
	}
	public void move(){
		//重构move 实现播放销毁功能
		if(state == ACTIVE){
		    y += step;
		   if(y>852){
			   state=REMOVE;
		    }
		   return;
		}
		if(state == DEAD){
			//从子对象中获取下一组图片
			BufferedImage img = nextImage();
			if(img == null){
				state=REMOVE;
			}else{
				image = img;
			}
		}
	}
	//子类必须有的方法,返回下一个要播放的照片引用，如果返回null、表示没有课播放的照片
	protected abstract BufferedImage nextImage();
	
	//被打一下
	public void hit(){
		if(life>0){
			life--;
		}
		if(life==0){
			state=DEAD;
		}
	}
	//在Fo中添加碰撞检查方法
	public boolean duang(Fo obj){
		double x1,x2,y1,y2;
		x1 = this.x - obj.width;
		y1 = this.y - obj.height;
		x2 = this.x + this.width; 
		y2 = this.y + this.height;
		return x1 < obj.x && obj.x < x2 && y1< obj.y && obj.y < y2;
	}
	public boolean isActive(){
		return state == ACTIVE;
	}
	public boolean isDead(){
		return state == DEAD;
	}
	public boolean canRemove(){
		return state == REMOVE;
	}
	//飞行物添加“去死”方法
	public void goDead(){
		if(isActive()){
			state=DEAD;
		}
	}
}
